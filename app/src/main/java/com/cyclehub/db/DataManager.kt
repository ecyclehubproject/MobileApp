package com.cyclehub.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataManager(context: Context) {

    private val dataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("data_prefs")
            }
        )

    companion object {
        val IS_LOGIN = booleanPreferencesKey("IS_LOGIN")
        val IS_ADDRESS_ADDED = booleanPreferencesKey("IS_ADDRESS_ADDED")
        val USER_TOKEN = stringPreferencesKey("USER_TOKEN")
        val USER_TYPE = stringPreferencesKey("USER_TYPE")


    }

    suspend fun storeData(
        isLogin: Boolean = false,
        userToken: String = "",
        isAddressAdded: Boolean = false,
        userType: String = ""
    ) {
        dataStore.edit {
            it[IS_LOGIN] = isLogin
            it[USER_TOKEN] = userToken
            it[IS_ADDRESS_ADDED] = isAddressAdded
            it[USER_TYPE] = userType
        }

    }

    val isLogin: Flow<Boolean> = dataStore.data.map {
        it[IS_LOGIN] ?: false
    }
    val userToken: Flow<String> = dataStore.data.map {
        it[USER_TOKEN] ?: ""
    }
    val userType: Flow<String> = dataStore.data.map {
        it[USER_TYPE] ?: ""
    }
    val isAddressAdded: Flow<Boolean> = dataStore.data.map {
        it[IS_ADDRESS_ADDED] ?: false
    }

    suspend fun clearData() {
        dataStore.edit {
            if (it.contains(IS_LOGIN)) {
                it.remove(IS_LOGIN)
            }
            if (it.contains(USER_TOKEN)) {
                it.remove(USER_TOKEN)
            }
            if (it.contains(IS_ADDRESS_ADDED)) {
                it.remove(IS_ADDRESS_ADDED)
            }
            if (it.contains(USER_TYPE)) {
                it.remove(USER_TYPE)
            }
            it.clear()
        }
    }


    suspend fun clearUserToken() {
        dataStore.edit {
            if (it.contains(USER_TOKEN))
                it.remove(USER_TOKEN)
        }
    }
}