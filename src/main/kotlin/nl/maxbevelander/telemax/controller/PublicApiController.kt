package nl.maxbevelander.telemax.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/public")
class PublicApiController {

    @GetMapping("/test")
    fun test(): String {
        return "Hello world!"
    }
}
