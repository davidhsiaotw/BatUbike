package com.example.batubike.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.batubike.data.UbikeRepository
import com.example.batubike.data.model.Station
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StationViewModel() : ViewModel() {
    private val repo = UbikeRepository()

    fun getStations(area: String): Flow<PagingData<Station>> {
        return repo.stations.map { pagingData ->
            pagingData.filter {
                area.isBlank() || it.area.matches("^.*$area.*$".toRegex())
            }.map {
                it
            }
        }.cachedIn(viewModelScope)
    }
}