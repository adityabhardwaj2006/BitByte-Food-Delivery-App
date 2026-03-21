package com.example.zomatoclone.domain.useCases

import com.example.zomatoclone.common.ResultState
import com.example.zomatoclone.data.models.UserData
import com.example.zomatoclone.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(val repo : Repo){

    fun loginUser(userData : UserData): Flow<ResultState<String>> {
        return repo.loginWithEmailAndPassword(userData)
    }

}