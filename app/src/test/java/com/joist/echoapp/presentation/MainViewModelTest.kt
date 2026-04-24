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
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @Mock
    private lateinit var repository: TextRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `submit with non-empty text emits Success`() = runTest(testDispatcher) {
        whenever(repository.validate(any())).thenReturn(Result.success("Hello"))
        val viewModel = MainViewModel(repository)

        viewModel.submit("Hello")
        advanceUntilIdle()

        assertEquals(EchoUiState.Success("Hello"), viewModel.uiState.value)
        verify(repository).validate("Hello")
    }

    @Test
    fun `submit with empty text emits Error`() = runTest(testDispatcher) {
        whenever(repository.validate(any())).thenReturn(
            Result.failure(Exception("Text cannot be empty"))
        )
        val viewModel = MainViewModel(repository)

        viewModel.submit("")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is EchoUiState.Error)
        verify(repository).validate("")
    }
}
