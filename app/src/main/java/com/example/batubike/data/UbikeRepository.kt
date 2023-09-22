package com.example.batubike.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.batubike.data.paging.StationPagingSource
import com.example.batubike.data.api.UbikeApi

class UbikeRepository {
    val stations = Pager(
        config = PagingConfig(pageSize = 25, enablePlaceholders = false),
        pagingSourceFactory = { StationPagingSource(service = UbikeApi.ubikeApiService) }
    ).flow
}