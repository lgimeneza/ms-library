package io.demo.mslibrary.domain

data class Isbn(val value: String) {
    fun value() = value.lowercase()
}
