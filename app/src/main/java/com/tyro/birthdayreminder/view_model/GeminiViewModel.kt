package com.tyro.birthdayreminder.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyro.birthdayreminder.entity.WishItem
import com.tyro.birthdayreminder.repository.GeminiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeminiViewModel @Inject constructor(
    private val geminiRepository: GeminiRepository
) : ViewModel() {

    private val _giftItems = MutableStateFlow<List<WishItem>>(emptyList())
    val giftItems: StateFlow<List<WishItem>> = _giftItems

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchWishes(name: String, age: Int, interest: String, onDone: (List<WishItem>) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = geminiRepository.generateBirthdayWishes(name, age, interest)
            _giftItems.value = result
            _isLoading.value = false
            if (result.isNotEmpty()) onDone(result)
        }
    }
}
