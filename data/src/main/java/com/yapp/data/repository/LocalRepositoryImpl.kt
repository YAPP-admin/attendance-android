package com.yapp.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yapp.domain.repository.LocalRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import java.io.IOException
import javax.inject.Inject


class LocalRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocalRepository {

    companion object {
        private const val DATASTORE_NAME = "member_information"
        private val MEMBER_ID = longPreferencesKey("KakaoMemeberId")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATASTORE_NAME)

    override suspend fun getMemberId(): Flow<Long?> {

        return context.dataStore.data.map { it[MEMBER_ID] }
//        return context.dataStore.data
//            .catch {
//                if (it is IOException) {
//                    emit(emptyPreferences())
//                } else {
//                    throw it
//                }
//            }
//            .map { preferences ->
//                preferences[MEMBER_ID]
//            }
    }

    override suspend fun setMemberId(memberId: Long) {
        context.dataStore.edit { preferences ->
            preferences[MEMBER_ID] = memberId
        }
    }
}