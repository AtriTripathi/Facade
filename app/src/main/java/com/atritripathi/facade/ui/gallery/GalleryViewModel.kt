package com.atritripathi.facade.ui.gallery

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.atritripathi.facade.data.UnsplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: UnsplashRepository,
    state: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_CURRENT_QUERY = "current_query_key"
        private const val DEFAULT_QUERY = "cats"
    }

    private val currentQuery = state.getLiveData(KEY_CURRENT_QUERY, DEFAULT_QUERY)

    val photos = currentQuery.switchMap { queryString ->
        repository.getSearchResult(queryString)
            .asLiveData()
            .cachedIn(viewModelScope)
    }

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }
}