package com.example.tallerinventario.data.repository

import java.security.MessageDigest

/**
 * Autenticación con una cuenta FICTICIA (requisito del caso).
 * Usuario: admin  |  Clave: taller2026
 * La clave no se guarda como texto: solo se compara su hash SHA-256.
 */
class AuthRepository {

    private val usuarioDemo = "admin"
    private val hashClaveDemo =
        "6ab2cfc43c9f72168285d5f9ce945990c8665d0c808202dbd667dfffa3e6ebd7"

    fun autenticar(usuario: String, clave: String): Boolean =
        usuario.trim().equals(usuarioDemo, ignoreCase = true) && sha256(clave) == hashClaveDemo

    private fun sha256(texto: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(texto.toByteArray())
            .joinToString("") { "%02x".format(it) }
}
