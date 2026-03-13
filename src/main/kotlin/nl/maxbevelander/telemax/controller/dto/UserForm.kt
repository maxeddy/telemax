package nl.maxbevelander.telemax.controller.dto

data class UserForm(
    var username: String = "",
    var password: String = "",
    var role: String = "ADMIN"
)
