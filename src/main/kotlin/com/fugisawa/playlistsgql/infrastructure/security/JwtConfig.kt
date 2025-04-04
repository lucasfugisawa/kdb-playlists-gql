package com.fugisawa.playlistsgql.infrastructure.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.fugisawa.playlistsgql.domain.entities.User
import com.fugisawa.playlistsgql.domain.entities.UserRole
import io.ktor.server.config.ApplicationConfig
import java.util.Date
import java.util.UUID

object JwtConfig {
    private lateinit var secret: String
    private lateinit var issuer: String
    private lateinit var audience: String
    private var expirationMillis: Long = 3600000L
    private lateinit var algorithm: Algorithm
    private lateinit var verifier: JWTVerifier

    fun initialize(config: ApplicationConfig) {
        secret = config.property("jwt.secret").getString()
        issuer = config.property("jwt.issuer").getString()
        audience = config.property("jwt.audience").getString()
        expirationMillis = config.property("jwt.expiration").getString().toLong()
        algorithm = Algorithm.HMAC256(secret)
        verifier =
            JWT
                .require(algorithm)
                .withIssuer(issuer)
                .withAudience(audience)
                .build()
    }

    fun generateToken(user: User): String {
        val now = Date()
        val expiration = Date(now.time + expirationMillis)

        return JWT
            .create()
            .withSubject(user.id.toString())
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("username", user.username)
            .withArrayClaim("roles", user.roles.map { it.name }.toTypedArray())
            .withIssuedAt(now)
            .withExpiresAt(expiration)
            .sign(algorithm)
    }

    fun getVerifier(): JWTVerifier = verifier

    fun getUserId(token: String): UUID {
        val decodedJWT = verifier.verify(token)
        return UUID.fromString(decodedJWT.subject)
    }

    fun getRoles(token: String): List<UserRole> {
        val decodedJWT = verifier.verify(token)
        val roleNames = decodedJWT.getClaim("roles").asArray(String::class.java)
        return roleNames.mapNotNull { roleName ->
            try {
                UserRole.valueOf(roleName)
            } catch (_: IllegalArgumentException) {
                null
            }
        }
    }
}
