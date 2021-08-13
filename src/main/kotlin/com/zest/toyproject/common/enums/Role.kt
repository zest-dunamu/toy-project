package com.zest.toyproject.common.enums

enum class Role {
    USER,
    ADMIN;

    fun getRoleName() = "ROLE_${this.name}"

    companion object {
        fun of(name: String): Role {
            return values().first { role -> role.name == name.uppercase() }
        }
    }
}