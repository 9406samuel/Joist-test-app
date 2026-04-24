package com.joist.echoapp.domain

interface TextRepository {
    suspend fun validate(text: String): Result<String>
}
