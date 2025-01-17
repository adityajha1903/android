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
package photos.network.domain.photos.usecase

import kotlinx.coroutines.flow.Flow
import photos.network.data.photos.repository.Photo
import photos.network.data.photos.repository.PhotoRepository

/**
 * Load a list of phots from persistency.
 */
class GetPhotoUseCase(
    private val photoRepository: PhotoRepository,
) {
    operator fun invoke(identifier: String): Flow<Photo?> {
        return photoRepository.getPhoto(identifier)
    }
}
