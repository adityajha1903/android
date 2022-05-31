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
package photos.network.data.user.network.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import photos.network.data.user.persistence.User as PersistenceUser
import photos.network.data.user.repository.User as DomainUser

@Serializable
data class User(
    @SerialName("id") val id: String,
    @SerialName("email") val email: String,
    @SerialName("lastname") val lastname: String,
    @SerialName("firstname") val firstname: String,
    @SerialName("lastSeen") val lastSeen: String,
) {
    fun toDomain(): DomainUser = DomainUser(
        id = id,
        lastname = lastname,
        firstname = firstname,
        profileImageUrl = "",
        accessToken = "",
        refreshToken = "",
    )

    fun toPersistence(): PersistenceUser = PersistenceUser(
        id = id,
        lastname = lastname,
        firstname = firstname,
        profileImageUrl = "",
        accessToken = "",
        refreshToken = "",
    )
}