package com.zest.toyproject

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ToyProjectApplication

fun main(args: Array<String>) {
    runApplication<ToyProjectApplication>(*args)
}
