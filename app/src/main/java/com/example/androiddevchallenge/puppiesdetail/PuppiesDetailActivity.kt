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
package com.example.androiddevchallenge.puppiesdetail

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import com.example.androiddevchallenge.PuppiesApplication
import com.example.androiddevchallenge.data.PuppiesRepository
import com.example.androiddevchallenge.db.PuppiesDatabase
import com.example.androiddevchallenge.fakeapi.FakePuppiesService
import com.example.androiddevchallenge.models.Puppy
import com.example.androiddevchallenge.models.PuppyData
import com.example.androiddevchallenge.ui.theme.PuppiesTheme

@ExperimentalPagingApi
class PuppiesDetailActivity : AppCompatActivity() {

    val model =
        PuppiesDetailViewModel(PuppiesRepository(FakePuppiesService(), PuppiesDatabase.INSTANCE))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PuppiesTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val puppyId = intent.extras?.getLong("puppyId")
                        ?: throw IllegalArgumentException("Puppy id was null")
                    PuppyInfo(model.getPuppiesData(puppyId), model.getPuppy(puppyId))
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun PuppyInfo(puppyData: PuppyData, puppy: Puppy) {
        Column(
            Modifier.fillMaxWidth().fillMaxHeight().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PuppyPhoto(
                puppy.photoUrl,
                Modifier.fillMaxWidth().wrapContentHeight()
            )
            Text(text = "Name: ${puppy.name}")
            Text(text = "Age: ${puppyData.ageInDays} days")
            Text(text = "Bread: ${puppyData.bread}")
            Text(text = "Weaned: ${if (puppyData.weaned) "Yes" else "No"}")
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun PuppyPhoto(
        imageUrl: String,
        modifier: Modifier = Modifier
    ) {

        Image(
            bitmap =
            PuppiesApplication.INSTANCE.getAssets().open(imageUrl).use {
                BitmapFactory.decodeStream(it).asImageBitmap()
            },
            contentDescription = ""
        )
    }

    @Composable
    fun MovieTitle(
        title: String,
        modifier: Modifier = Modifier
    ) {
        Text(
            modifier = modifier,
            text = title,
            maxLines = 2,
            style = MaterialTheme.typography.h6,
            overflow = TextOverflow.Ellipsis
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        PuppiesTheme {
            PuppyInfo(PuppyData(1L, "jmper", 250, true), Puppy("balto", 1L, "puppy_1.jpeg"))
        }
    }
}
