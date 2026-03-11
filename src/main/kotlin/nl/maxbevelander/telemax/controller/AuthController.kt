package nl.maxbevelander.telemax.controller

import nl.maxbevelander.telemax.dto.LoginRequest
import nl.maxbevelander.telemax.dto.LoginResponse
import nl.maxbevelander.telemax.config.jwt.JwtUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/public/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        return try {
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginRequest.username,
                    loginRequest.password
                )
            )

            SecurityContextHolder.getContext().authentication = authentication

            val token = jwtUtil.generateToken(loginRequest.username)

            ResponseEntity.ok(
                LoginResponse(
                    success = true,
                    message = "Login successful",
                    username = loginRequest.username,
                    token = token
                )
            )
        } catch (e: BadCredentialsException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                LoginResponse(
                    success = false,
                    message = "Invalid username or password"
                )
            )
        }
    }
}
