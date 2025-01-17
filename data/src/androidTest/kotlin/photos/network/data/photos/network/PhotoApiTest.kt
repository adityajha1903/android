/*
 * Copyright 2020-2022 Photos.network developers
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
package photos.network.data.photos.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import photos.network.data.PhotosNetworkMockFileReader
import photos.network.data.settings.persistence.SettingsStorage
import photos.network.data.settings.repository.SettingsRepositoryImpl

/**
 * Test the REST interface to the photos.network core instance.
 */
@RunWith(AndroidJUnit4::class)
class PhotoApiTest {

    private val settingsRepository = SettingsRepositoryImpl(
        SettingsStorage(
            context = InstrumentationRegistry.getInstrumentation().context
        )
    )

    @Test
    fun request_photos_with_valid_data() = runBlocking {
        // given
        val fakeResponse = PhotosNetworkMockFileReader.readStringFromFile("photos_response_success.json")
        val mockEngine = MockEngine {
            respond(
                content = fakeResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val photoApi = PhotoApiImpl(
            httpClient = createHttpClient(mockEngine),
            settingsRepository = settingsRepository
        )

        // when
        val result: Photos = photoApi.getPhotos()

        // then
        assertEquals(result.size, 2)
        assertEquals(result.offset, 13)
        assertEquals(result.limit, 25)
        assertEquals(result.results.size, 2)

        assertEquals(result.results[0].id, "a1")
        assertEquals(result.results[0].name, "xy")

        assertEquals(result.results[1].id, "b2")
        assertEquals(result.results[1].name, "yz")
    }

    @Test(expected = Exception::class)
    fun request_photos_with_empty_response() = runBlocking {
        // given
        val mockEngine = MockEngine {
            respond(
                content = "{}",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val photoApi = PhotoApiImpl(
            httpClient = createHttpClient(mockEngine),
            settingsRepository = settingsRepository
        )

        // when
        photoApi.getPhotos()

        // then
        // assert should be skipped, exception should already been raised
        assertFalse(true)
    }

    private fun createHttpClient(engine: MockEngine): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json()
            }
        }
    }
}
