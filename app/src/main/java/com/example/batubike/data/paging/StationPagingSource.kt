package com.example.batubike.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.batubike.data.api.UbikeApiService
import com.example.batubike.data.model.Station
import retrofit2.HttpException
import java.io.IOException

/**
 * [reference](https://medium.com/nerd-for-tech/pagination-in-android-with-paging-3-retrofit-and-kotlin-flow-2c2454ff776e)
 */
class StationPagingSource(private val service: UbikeApiService) : PagingSource<Int, Station>() {
    override fun getRefreshKey(state: PagingState<Int, Station>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(anchorPosition = it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition = it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Station> {
        val pageIndex = params.key ?: 1
        return try {
            val stations = service.getPhotos()
            val nextKey = if (stations.isEmpty()) {
                null
            } else {
                pageIndex + (params.loadSize / 25)
            }

            LoadResult.Page(
                data = stations,
                prevKey = if (pageIndex == 1) null else pageIndex,
                nextKey = nextKey
            )

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}