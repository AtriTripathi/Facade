package com.atritripathi.facade.api

import com.atritripathi.facade.data.UnsplashPhoto

data class UnsplashResponse(
    val results: List<UnsplashPhoto>
)