package com.joist.echoapp.data

import com.joist.echoapp.domain.TextRepository
import kotlinx.coroutines.delay

internal class FakeTextRepository : TextRepository {
    override suspend fun validate(text: String): Result<String> {
        delay(1000)
        return if (text.isBlank())
            Result.failure(Exception("Text cannot be empty"))
        else
            Result.success(text)
    }
}
