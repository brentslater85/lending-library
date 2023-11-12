package uk.brent.slater.lendinglibrary.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class UnableToBorrowException(message: String) : Exception(message)
