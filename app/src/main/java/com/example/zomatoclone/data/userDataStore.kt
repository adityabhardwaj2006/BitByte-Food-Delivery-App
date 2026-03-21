package com.example.zomatoclone.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "userDataStore"
)

class UserDataStore @Inject constructor(
    @ApplicationContext private val context : Context
) {

    private val KEY_VAL1 = booleanPreferencesKey("USER_KEY1")
    private val KEY_VAL2 = booleanPreferencesKey("USER_KEY2")
    private val KEY_VAL3 = intPreferencesKey("USER_KEY3")
    private val key = stringPreferencesKey("UID")

    private val key2 = stringPreferencesKey("ADDRESS")
    private val key3 = stringPreferencesKey("USERNAME")
    suspend fun saveUser(value : String){
        context.dataStore.edit {
            it[key] = value
        }
    }
     suspend fun getUser(): String? {
        return context.dataStore.data.map {
            it[key]
        }.first()
    }

    suspend fun deleteUser(){
        context.dataStore.edit {
            it.remove(key)
            it.remove(KEY_VAL1)
            it.remove(KEY_VAL2)
            it.remove(KEY_VAL3)
            it.remove(key2)
            it.remove(key3)
        }
    }

    suspend fun saveVegToggle(data : Boolean){

        context.dataStore.edit {
            it[KEY_VAL1] = data
        }

    }

    fun getVegToggle() : Flow<Boolean> {

        return context.dataStore.data.map {
            it[KEY_VAL1]?:true
        }

    }


    suspend fun saveHealthyToggle(data : Boolean){

        context.dataStore.edit {
            it[KEY_VAL2] = data
        }

    }

    fun getHealthyToggle() : Flow<Boolean> {

        return context.dataStore.data.map {
            it[KEY_VAL2]?:false
        }

    }
    suspend fun savePaymentOption(data : Int){
        context.dataStore.edit {
            it[KEY_VAL3] = data
        }
    }
    fun getPaymentOption() : Flow<Int> {

        return context.dataStore.data.map {
            it[KEY_VAL3]?:0
        }

    }


    suspend fun setAddress(data : String) {
        context.dataStore.edit {
            it[key2] = data
        }
    }


    fun getAddress() : Flow<String>{

        return context.dataStore.data.map {
            it[key2]?:""
        }

    }

    suspend fun setUsername(data : String) {
        context.dataStore.edit {
            it[key3] = data
        }
    }


    fun getUsername() : Flow<String>{

        return context.dataStore.data.map {
            it[key3]?:""
        }

    }



}




