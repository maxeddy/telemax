package nl.maxbevelander.telemax.config.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil {

    private val secretKey: SecretKey = Keys.hmacShaKeyFor(
        "telemax-secret-key-for-jwt-authentication-must-be-at-least-256-bits".toByteArray()
    )

    private val expirationTime: Long = 1000 * 60 * 60 * 24 // 24 hours

    fun generateToken(username: String): String {
        return Jwts.builder()
            .subject(username)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(secretKey)
            .compact()
    }

    fun extractUsername(token: String): String? {
        return try {
            extractClaims(token)?.subject
        } catch (e: Exception) {
            null
        }
    }

    fun validateToken(token: String, username: String): Boolean {
        return try {
            val extractedUsername = extractUsername(token)
            extractedUsername == username && !isTokenExpired(token)
        } catch (e: Exception) {
            false
        }
    }

    private fun extractClaims(token: String): Claims? {
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (_: Exception) {
            null
        }
    }

    private fun isTokenExpired(token: String): Boolean {
        val claims = extractClaims(token) ?: return true
        return claims.expiration.before(Date())
    }
}
