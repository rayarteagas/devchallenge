/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.androiddevchallenge.api.PuppiesService
import com.example.androiddevchallenge.db.PuppiesDatabase
import com.example.androiddevchallenge.models.Puppy
import java.io.IOException
import java.lang.Exception

private const val STARTING_PAGE_INDEX = 1L

@OptIn(ExperimentalPagingApi::class)
class PuppiesRemoteMediator(
    private val service: PuppiesService,
    private val puppiesDatabase: PuppiesDatabase
) : RemoteMediator<Int, Puppy>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Puppy>): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> STARTING_PAGE_INDEX
            LoadType.PREPEND -> {
                val prevKey = getRemoteKeyForFirstItem(state)
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                prevKey
            }
            LoadType.APPEND -> getRemoteKeyForLastItem(state) ?: return MediatorResult.Success(endOfPaginationReached = true)
        }

        try {
            val puppies = service.getPuppiesList(page, state.config.pageSize)
            val endOfPaginationReached = puppies.isEmpty()
            puppiesDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    puppiesDatabase.puppiesDao().clearRepos()
                }
                puppiesDatabase.puppiesDao().insertAll(puppies)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private fun getRemoteKeyForLastItem(state: PagingState<Int, Puppy>): Long? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()?.id?.inc()
    }

    private fun getRemoteKeyForFirstItem(state: PagingState<Int, Puppy>): Long? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.id.takeIf { it != 1L }
    }
}
