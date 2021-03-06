package com.islamversity.surah

import com.islamversity.core.mvi.BaseState
import com.islamversity.core.mvi.BaseViewState
import com.islamversity.surah.model.AyaUIModel

data class SurahState(
    override val base: BaseState,
    val ayas : List<AyaUIModel>,
    val mainAyaFontSize : Int,
    val startFrom : Int
) : BaseViewState {
    companion object {
        fun idle() =
            SurahState(
                base = BaseState.stable(),
                ayas = emptyList(),
                mainAyaFontSize = 0,
                startFrom = 0
            )
    }
}