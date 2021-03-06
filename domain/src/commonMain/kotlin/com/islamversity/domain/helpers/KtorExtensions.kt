package com.islamversity.domain.helpers

import com.islamversity.core.*
import com.islamversity.domain.model.servermodels.error.ErrorServerModel
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Response<*>.getErrorRepoModel(): ErrorHolder =
    (this as Response.Error)
        .runCatching {
            Json.decodeFromString(ErrorServerModel.serializer(), body)
        }
        .getOrNull()
        ?.let {
            ErrorHolder.Message(it.message, it.code)
        }
        ?: ErrorHolder.Message(this.body, this.status, throwable)

fun <T> Flow<Response<T>>.errorHandler(times: Long = 3): Flow<Response<T>> =
    retry(times) { throwable ->
        throwable is IOException
    }
        .catch { t: Throwable ->
            println(t.message)
            emit(createThrowableErrorResponse(t))
        }

fun <T> createThrowableErrorResponse(throwable: Throwable): Response<T> {
    val status = getServerErrorStatusCode(throwable)
    val errorMessage = getThrowableErrorMessage(throwable)
    return Response.Error(status, errorMessage, throwable) as Response<T>
}

fun getThrowableErrorMessage(throwable: Throwable): String =
    when (throwable) {
        is IOException -> "Failed To read"
        is NoSuchElementException -> "No Element Found"
        is IllegalArgumentException -> "Wrong Argument"
        else -> "Error"
    }

fun <T, U> Flow<Response<T>>.eitherError(map: (T) -> U): Flow<Either<ErrorHolder, U>> =
    listMerge(
        {
            filter { it.isNotSuccessful }
                .map { it.getErrorRepoModel().left() }
        },
        {
            filter { it.isSuccessful }
                .map {
                    it as Response.Success<T>
                    map(it.data).right()
                }
        }
    )
