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
package com.example.androiddevchallenge.fakeapi

import com.example.androiddevchallenge.api.PuppiesService
import com.example.androiddevchallenge.models.Puppy
import com.example.androiddevchallenge.models.PuppyData

class FakePuppiesService : PuppiesService {
    override suspend fun getPuppiesList() = fakePuppies

    override suspend fun getPuppiesList(minId: Long, pageSize: Int) = fakePuppies.run {
        val firstItemIndex = indexOfFirst { it.id == minId }
        val result = if (firstItemIndex == -1)
            listOf<Puppy>()
        else {
            val toIndex = (firstItemIndex + pageSize).coerceAtMost(size)
            val subl = subList(firstItemIndex, toIndex)
            subl
        }
        result
    }

    override suspend fun getPuppyData(puppyId: Long) = PuppyData(puppyId, fakeBreeds.random(), (0..365).random(), true)
}
