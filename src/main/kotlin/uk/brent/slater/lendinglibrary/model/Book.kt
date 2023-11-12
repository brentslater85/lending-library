package uk.brent.slater.lendinglibrary.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.UniqueConstraint

@Entity
class Book (
    @Id
    val isbn: String,
    val title: String,
    val author: String,
    var isBorrowed: Boolean=false,
    val isReference: Boolean=false,
)