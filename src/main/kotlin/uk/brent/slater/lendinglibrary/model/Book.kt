package uk.brent.slater.lendinglibrary.model

data class Book (
    val isbn: String,
    val title: String,
    val author: String,
    var isBorrowed: Boolean=false,
    val isReference: Boolean=false,
)