package com.example.zomatoclone.domain.repo

import com.example.zomatoclone.common.ResultState
import com.example.zomatoclone.data.models.UserData
import kotlinx.coroutines.flow.Flow


interface Repo {
    fun loginWithEmailAndPassword(userData : UserData): Flow<ResultState<String>>
    fun registerWithEmailAndPassword(userData: UserData): Flow<ResultState<String>>


}