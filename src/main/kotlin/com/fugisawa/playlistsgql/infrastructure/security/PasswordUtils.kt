package com.fugisawa.playlistsgql.infrastructure.security

import org.mindrot.jbcrypt.BCrypt

object PasswordUtils {
    fun hashPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())

    fun verifyPassword(
        password: String,
        passwordHash: String,
    ): Boolean = BCrypt.checkpw(password, passwordHash)
}
