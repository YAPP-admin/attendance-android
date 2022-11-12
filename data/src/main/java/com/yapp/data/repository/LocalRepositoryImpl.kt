package com.yapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yapp.domain.repository.LocalRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


class LocalRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : LocalRepository {

    companion object {
        private const val DATASTORE_NAME = "member_information"
        private val MEMBER_ID = longPreferencesKey("KakaoMemeberId")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATASTORE_NAME)

    override suspend fun getMemberId(): Result<Long?> {
        return kotlin.runCatching {
            context.dataStore.data
                .catch {
                    if (it is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw it
                    }
                }
                .map { preferences ->
                    preferences[MEMBER_ID]
                }.first()
        }
    }

    override suspend fun setMemberId(memberId: Long): Result<Unit> {
        return kotlin.runCatching {
            context.dataStore.edit { preferences ->
                preferences[MEMBER_ID] = memberId
            }
        }
    }

    override suspend fun deleteAllUserInfo(): Result<Unit> {
        return kotlin.runCatching {
            context.dataStore.edit { preferences ->
                preferences.clear()
            }
        }
    }
}