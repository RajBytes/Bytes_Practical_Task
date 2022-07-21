package com.bytes.app.utils

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * This is the Kotlin Extension function for SearchView to check for the Search Query
 */
fun androidx.appcompat.widget.SearchView.getQueryTextChangeStateFlow(): MutableStateFlow<String> {
    val searchedQuery = MutableStateFlow("")
    setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            searchedQuery.value = query
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            searchedQuery.value = newText
            return true
        }
    })
    return searchedQuery
}