package com.nikede.forecast.data.db.entity

data class Error (
    val code: Int,
    val type: String,
    val info: String
)