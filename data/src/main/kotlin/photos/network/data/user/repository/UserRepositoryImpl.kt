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
package photos.network.data.user.repository

import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.runBlocking
import logcat.LogPriority
import logcat.logcat
import photos.network.data.user.network.UserApi
import photos.network.data.user.persistence.UserStorage
import java.net.ConnectException
import photos.network.data.user.persistence.User as DatabaseUser

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val userStorage: UserStorage
) : UserRepository {
    private var currentUser: DatabaseUser? = null

    override fun currentUser(): DatabaseUser? {
        // from memory
        if (currentUser != null) {
            return currentUser
        }

        // from storage
        currentUser = userStorage.read().takeIf {
            it != null
        }

        // from api
        runBlocking {
            logcat(LogPriority.INFO) { "userApi.getUser(): ${userApi.getUser()}" }
            userApi.getUser()?.let {
                logcat(LogPriority.INFO) { "userApi.getUser()?.let: $it" }
                val user = DatabaseUser(
                    id = it.id,
                    lastname = it.lastname,
                    firstname = it.firstname,
                    profileImageUrl = "",
                )
                currentUser = user
                userStorage.save(user)
            }
            logcat(LogPriority.INFO) { "getUser done! User: $currentUser" }
        }

        return currentUser
    }

    /**
     * Invalidate users authorization and remove all user related app data.
     */
    override suspend fun invalidateAuthorization() {
        logcat(LogPriority.INFO) { "delete currently logged in User." }
        currentUser = null
        userStorage.delete()
    }

    override suspend fun accessTokenRequest(authCode: String): Boolean {
        return userApi.accessTokenRequest(authCode)
    }

    override suspend fun verifyServerHost(host: String): Boolean {
        try {
            return userApi.verifyServerHost(host)
        } catch (exception: ServerResponseException) {
            logcat(LogPriority.WARN) { "verifyServerHost() failed" }
        } catch (exception: ConnectException) {
            logcat(LogPriority.WARN) { "verifyServerHost() failed" }
        }

        return false
    }

    override suspend fun verifyClientId(clientId: String): Boolean {
        return userApi.verifyClientId(clientId)
    }
}
