package com.example.batubike.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.batubike.MenuUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MenuViewModel : ViewModel() {
    private val _menuState = MutableStateFlow(
        mutableListOf(
            MenuUiState(0, text = "使用說明"),
            MenuUiState(1, text = "收費方式"),
            MenuUiState(2, text = "站點資訊", color = Color.White),
            MenuUiState(3, text = "最新消息"),
            MenuUiState(4, text = "活動專區")
        )
    )
    val menuState: StateFlow<List<MenuUiState>> = _menuState

    fun onClickUpdate(prevId: Int, curId: Int) {
        val updatedList = _menuState.value.mapIndexed { index, menuUiState ->
            when (index) {
                prevId -> {
                    menuUiState.copy(color = Color.Black)
                }

                curId -> {
                    menuUiState.copy(color = Color.White)
                }

                else -> menuUiState
            }
        }.toMutableList()
        _menuState.value = updatedList
    }
}