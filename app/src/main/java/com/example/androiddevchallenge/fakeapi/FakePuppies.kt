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

import com.example.androiddevchallenge.models.Puppy

val fakePuppies = listOf(
    "Alfred",
    "Bolt",
    "Cricket",
    "Disk",
    "Esther",
    "Prince",
    "Florina",
    "Simpson",
    "Ball",
    "Pedro",
    "Woldemort",
    "Donald",
    "Jeff",
    "Warren",
    "Musk",
    "Yigit",
    "Lyla",
    "Raymond",
    "Pacman",
    "Rose",
    "Red",
    "Violet",
    "Blue",
    "P!nk",
    "Pavarotti",
    "Marie",
    "Newton",
    "GOOGl",
    "Muko",
    "Blossom",
).mapIndexed { index, name ->
    val index = index + 1
    Puppy(name, index.toLong(), "puppy_$index.jpeg")
}
