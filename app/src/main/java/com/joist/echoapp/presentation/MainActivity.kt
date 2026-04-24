package com.joist.echoapp.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.joist.echoapp.data.FakeTextRepository
import com.joist.echoapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels {
        MainViewModel.factory(FakeTextRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitButton.setOnClickListener {
            val text = binding.textInput.text?.toString() ?: ""
            viewModel.submit(text)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is EchoUiState.Idle -> showIdle()
                        is EchoUiState.Loading -> showLoading()
                        is EchoUiState.Success -> showSuccess(state.text)
                        is EchoUiState.Error -> showError(state.message)
                    }
                }
            }
        }
    }

    private fun showIdle() {
        binding.progressBar.visibility = View.GONE
        binding.outputText.visibility = View.GONE
        binding.errorText.visibility = View.GONE
        binding.submitButton.isEnabled = true
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.outputText.visibility = View.GONE
        binding.errorText.visibility = View.GONE
        binding.submitButton.isEnabled = false
    }

    private fun showSuccess(text: String) {
        binding.progressBar.visibility = View.GONE
        binding.outputText.visibility = View.VISIBLE
        binding.errorText.visibility = View.GONE
        binding.outputText.text = text
        binding.submitButton.isEnabled = true
    }

    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.outputText.visibility = View.GONE
        binding.errorText.visibility = View.VISIBLE
        binding.errorText.text = message
        binding.submitButton.isEnabled = true
    }
}
