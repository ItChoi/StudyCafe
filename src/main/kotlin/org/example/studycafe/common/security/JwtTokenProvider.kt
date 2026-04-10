package org.example.studycafe.common.security

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtTokenProvider(
    @Value("\${app.jwt.secret}") private val secret: String,
    @Value("\${app.jwt.expiration-hours:24}") private val expirationHours: Long,
) {
    private val signingKey by lazy {
        Keys.hmacShaKeyFor(secret.toByteArray(Charsets.UTF_8))
    }

    fun generateToken(memberId: Long): String {
        val now = Date()
        val expiration = Date(now.time + expirationHours * 60 * 60 * 1000)

        return Jwts.builder()
            .subject(memberId.toString())
            .issuedAt(now)
            .expiration(expiration)
            .signWith(signingKey)
            .compact()
    }

    fun getMemberIdFromToken(token: String): Long {
        val claims = Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .payload
        return claims.subject.toLong()
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
            true
        } catch (e: JwtException) {
            false
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}
