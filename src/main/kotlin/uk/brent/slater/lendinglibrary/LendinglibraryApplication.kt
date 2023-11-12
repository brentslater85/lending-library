package uk.brent.slater.lendinglibrary

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LendinglibraryApplication

fun main(args: Array<String>) {
	runApplication<LendinglibraryApplication>(*args)
}
