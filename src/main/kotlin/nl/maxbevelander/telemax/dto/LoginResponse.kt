package nl.maxbevelander.telemax.dto

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val username: String? = null,
    val token: String? = null
)
