package com.example.batubike

import androidx.paging.Pager
import androidx.paging.PagingConfig

class UbikeRepository {
    val stations = Pager(
        config = PagingConfig(pageSize = 25, enablePlaceholders = false),
        pagingSourceFactory = { StationPagingSource(service = UbikeApi.ubikeApiService) }
    ).flow
}