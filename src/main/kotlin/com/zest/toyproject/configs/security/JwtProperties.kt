package com.zest.toyproject.configs.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "token")
data class JwtProperties(
    val expirationTime: Long,
    val secretKey: String,
)