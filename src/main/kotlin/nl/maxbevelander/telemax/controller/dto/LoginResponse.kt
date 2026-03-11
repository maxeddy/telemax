package nl.maxbevelander.telemax.controller.dto

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val username: String? = null,
    val token: String? = null
)
