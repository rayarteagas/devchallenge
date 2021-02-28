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
package com.example.androiddevchallenge.puppieslist

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.androiddevchallenge.PuppiesApplication
import com.example.androiddevchallenge.data.PuppiesRepository
import com.example.androiddevchallenge.db.PuppiesDatabase
import com.example.androiddevchallenge.fakeapi.FakePuppiesService
import com.example.androiddevchallenge.models.Puppy
import com.example.androiddevchallenge.puppiesdetail.PuppiesDetailActivity
import com.example.androiddevchallenge.ui.theme.PuppiesTheme
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class PuppiesListActivity : AppCompatActivity() {

    val model =
        PuppiesListViewModel(PuppiesRepository(FakePuppiesService(), PuppiesDatabase.INSTANCE))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PuppiesTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    PuppiesList(model.getPuppiesList())
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun PuppiesList(puppies: Flow<PagingData<Puppy>>) {
        val lazyPuppyItems: LazyPagingItems<Puppy> = puppies.collectAsLazyPagingItems()
        LazyColumn {
            items(lazyPuppyItems) { puppy ->
                PuppyItem(puppy!!)
            }
        }
    }

    @Composable
    fun PuppyItem(puppy: Puppy) {

        Row(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .clickable {
                    startActivity(Intent(this@PuppiesListActivity, PuppiesDetailActivity::class.java).putExtra("puppyId", puppy.id))
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MovieTitle(
                puppy.name,
                modifier = Modifier.weight(1f)
            )
            MovieImage(
                puppy.photoUrl,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun MovieImage(
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

    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!", Modifier.padding(50.dp))
        Text(text = "Hello $name!")
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        PuppiesTheme {
            Greeting("Android")
        }
    }
}
