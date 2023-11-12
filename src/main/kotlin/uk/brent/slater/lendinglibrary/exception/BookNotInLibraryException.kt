package uk.brent.slater.lendinglibrary.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class BookNotInLibraryException(message: String) : Exception(message)
