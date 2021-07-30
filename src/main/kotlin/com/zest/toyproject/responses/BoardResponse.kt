package com.zest.toyproject.responses

import jdk.jfr.Description

data class BoardResponse(
    val id: Long,
    val title: String,
    val description: String? = null,
)