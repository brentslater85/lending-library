package uk.brent.slater.lendinglibrary.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class BookAlreadyInLibraryException(message: String) : Exception(message)
