package nl.maxbevelander.telemax

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TelemaxApplication

fun main(args: Array<String>) {
	runApplication<TelemaxApplication>(*args)
}
