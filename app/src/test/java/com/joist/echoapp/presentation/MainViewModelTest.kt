package com.joist.echoapp.presentation

import com.joist.echoapp.domain.TextRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `submit with non-empty text emits Success`() = runTest {
        val repo = object : TextRepository {
            override suspend fun validate(text: String) = Result.success(text)
        }
        val viewModel = MainViewModel(repo)

        viewModel.submit("Hello")
        advanceUntilIdle()

        assertEquals(EchoUiState.Success("Hello"), viewModel.uiState.value)
    }

    @Test
    fun `submit with empty text emits Error`() = runTest {
        val repo = object : TextRepository {
            override suspend fun validate(text: String) =
                Result.failure<String>(Exception("Text cannot be empty"))
        }
        val viewModel = MainViewModel(repo)

        viewModel.submit("")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is EchoUiState.Error)
    }
}
