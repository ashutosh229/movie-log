package com.example.movielog.core.navigation

object Routes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val HOME = "home"
    const val SEARCH = "search"
    const val LIBRARY = "library"
    const val PROFILE = "profile"
    const val ANALYTICS = "analytics"

    const val ARG_CONTENT_TYPE = "contentType"
    const val ARG_CONTENT_ID = "contentId"
    const val DETAIL = "detail/{$ARG_CONTENT_TYPE}/{$ARG_CONTENT_ID}"

    fun detail(contentType: String, contentId: String): String {
        return "detail/$contentType/$contentId"
    }
}