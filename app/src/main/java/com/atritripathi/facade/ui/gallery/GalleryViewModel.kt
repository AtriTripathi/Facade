package com.atritripathi.facade.ui.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.atritripathi.facade.data.UnsplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: UnsplashRepository
) : ViewModel() {

    companion object {
        private const val DEFAULT_QUERY = "cats"
    }

    private val currentQuery = MutableLiveData(DEFAULT_QUERY)

    val photos = currentQuery.switchMap { queryString ->
        repository.getSearchResult(queryString).cachedIn(viewModelScope)
    }

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }

}