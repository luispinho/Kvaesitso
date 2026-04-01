package de.mm20.launcher2.preferences.search

import de.mm20.launcher2.preferences.LauncherDataStore
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class AppSearchSettings internal constructor(
    private val dataStore: LauncherDataStore,
) {
    val fuzzySearch
        get() = dataStore.data.map { it.appSearchFuzzy }.distinctUntilChanged()

    fun setFuzzySearch(fuzzySearch: Boolean) {
        dataStore.update {
            it.copy(appSearchFuzzy = fuzzySearch)
        }
    }
}
