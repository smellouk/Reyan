package com.islamversity.navigation

import com.islamversity.navigation.model.SearchLocalModel
import com.islamversity.navigation.model.SearchLocalModel.Sinker.EXTRA_SEARCH
import com.islamversity.navigation.model.SurahLocalModel
import com.islamversity.navigation.model.SurahLocalModel.Sinker.EXTRA_SURAH_DETAIL

sealed class Screens(
    val name: String,
    val extras: Pair<String, ByteArray>? = null,
    val pushAnimation: NavigationAnimation? = null,
    val popAnimation: NavigationAnimation? = null
) {

    /**
     * Screens supporting dynamic loading (Dynamic Feature Module) should implement this interface
     */
    interface Dynamic {
        val module: String
    }

    internal object Test : Screens("com.islamversity.navigation.TestController")

    object Home : Screens("com.islamversity.quran_home.view.HomeView")

    class Search(
        model: SearchLocalModel,
        pushAnimation: NavigationAnimation? = null,
        popAnimation: NavigationAnimation? = null
    ) : Screens(
        "com.islamversity.search.view.SearchView",
        EXTRA_SEARCH to SearchLocalModel.toByteArray(model),
        pushAnimation,
        popAnimation
    )

    class SurahDetail(
        model: SurahLocalModel,
        pushAnimation: NavigationAnimation? = null,
        popAnimation: NavigationAnimation? = null
    ) : Screens(
        "com.islamversity.surah.view.SurahView",
        EXTRA_SURAH_DETAIL to SurahLocalModel.toByteArray(model),
        pushAnimation,
        popAnimation
    )

    class Settings(
    ) : Screens(
        "com.islamversity.settings.SettingsView"
    )
}
