package com.yapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yapp.domain.repository.LocalRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class LocalRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocalRepository {

    companion object {
        private const val DATASTORE_NAME = "member_information"
        private val MEMBER_ID = longPreferencesKey("KakaoMemeberId")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATASTORE_NAME)

    override fun getMemberId(): Flow<Long?> {
        return context.dataStore.data
            .map { preferences ->
                preferences[MEMBER_ID]
            }
    }

    override fun setMemberId(id: Long): Flow<Boolean> {
        return flow {
            runCatching {
                context.dataStore.edit { preferences ->
                    preferences[MEMBER_ID] = id
                }
            }.fold(
                onSuccess = {
                    emit(true)
                },
                onFailure = {
                    emit(false)
                }
            )
        }
    }
}