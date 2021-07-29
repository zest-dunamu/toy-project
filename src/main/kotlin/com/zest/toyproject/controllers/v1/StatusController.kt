package com.zest.toyproject.controllers.v1

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class StatusController {

    @GetMapping("/health")
    fun healthCheck() : String {
        return "health ok"
    }
}
