package com.example.s_mart.core.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Date {
    val date: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("M / d / yyyy | HH:mm"))
}