package com.example.batubike

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StationViewModel : ViewModel() {
    private val repo = UbikeRepository()

    fun getStations(): Flow<PagingData<StationUiState>> {
        var i = 0
        return repo.stations.map { pagingData ->
            pagingData.map {
                StationUiState(i++, it.id, it.area, it.name)
            }
        }.cachedIn(viewModelScope)
    }
}

data class StationUiState(
    val index: Int,
    val id: String,
    val area: String,
    val name: String
)