package com.example.zomatoclone.presentation.ViewModels

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zomatoclone.common.ResultState
import com.example.zomatoclone.common.USER_COLLECTION
import com.example.zomatoclone.data.OrderDatabase
import com.example.zomatoclone.data.UserDataStore
import com.example.zomatoclone.data.models.User
import com.example.zomatoclone.data.models.UserData
import com.example.zomatoclone.domain.useCases.CreateUserUseCase
import com.example.zomatoclone.domain.useCases.LoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ZomViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val loginUserUseCase: LoginUserUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val userDataStore: UserDataStore,
    private val rooms : OrderDatabase
) : ViewModel() {
    val userName = userDataStore.getUsername()
    val address = userDataStore.getAddress()

    private val _userProfile = MutableStateFlow<User?>(null)
    var userProfile = _userProfile.asStateFlow()

    private val signUpState = MutableStateFlow(signState())
    val _signUpState = signUpState.asStateFlow()

    private val logInState = MutableStateFlow(logState())
    val _logState = logInState.asStateFlow()

    init {
        fetchUserProfile()
    }
    suspend fun changeAddress(data: String){
        userDataStore.setAddress(data)
    }

    fun fetchUserProfile() {
        Log.d("ZOM_DEBUG", "FETCH CALLED")

        viewModelScope.launch {
            try {

                val uid = userDataStore.getUser() ?: firebaseAuth.currentUser?.uid
                Log.d("ZOM_DEBUG", "UID: $uid")

                if (uid == null) {
                    Log.d("ZOM_DEBUG", "UID is NULL")
                    _userProfile.value = null
                    return@launch
                }

                val snapshot = FirebaseFirestore.getInstance()
                    .collection(USER_COLLECTION)
                    .document(uid)
                    .get()
                    .await()

                Log.d("ZOM_DEBUG", "Firestore Snapshot: ${snapshot.data}")

                _userProfile.value = snapshot.toObject(User::class.java)

                Log.d("ZOM_DEBUG", "User Object: ${_userProfile.value}")

                userDataStore.setAddress(_userProfile.value?.address ?: "Unknown")
                userDataStore.setUsername(_userProfile.value?.name ?: "Guest")

            } catch (e: Exception) {

                Log.e("ZOM_DEBUG", "Error fetching profile", e)
                _userProfile.value = null
            }
        }
    }

     fun changeVegToggle(data : Boolean){
         viewModelScope.launch {
             userDataStore.saveVegToggle(data)
         }
    }
     fun changeHealthyToggle(data : Boolean) {
         viewModelScope.launch {
             userDataStore.saveHealthyToggle(data)
     }
    }
     fun changePaymentOption(data : Int){
         viewModelScope.launch {
             userDataStore.savePaymentOption(data)
         }
    }

    val vegToggle = userDataStore.getVegToggle()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false

        )
    val getHealthyToggle = userDataStore.getHealthyToggle()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false

        )
    val getPaymentOption = userDataStore.getPaymentOption()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    fun logOut() {
        viewModelScope.launch {
            userDataStore.deleteUser()
            rooms.orderDAO().deleteAllOrder()
            firebaseAuth.signOut()
            _userProfile.value = null
            logInState.value = logState()
            signUpState.value = signState()
        }
    }
    fun clearCart(){
        viewModelScope.launch {
            rooms
                .orderDAO().deleteAllOrder()
        }
    }

    fun createUser(userData: UserData) {
        viewModelScope.launch {

            createUserUseCase.createUser(userData).collect { result ->

                when (result) {

                    is ResultState.Success<*> -> {
                        val uid = firebaseAuth.currentUser?.uid
                        if (uid != null) {
                            userDataStore.saveUser(uid)
                        }

                        signUpState.value = signState(success = true)
                        fetchUserProfile()
                    }

                    is ResultState.Error<*> -> {
                        signUpState.value =
                            signState(error = result.message.toString())
                    }

                    is ResultState.Loading -> {
                        signUpState.value = signState(loading = true)
                    }
                }
            }
        }
    }

    fun loginUser(userData: UserData) {
        viewModelScope.launch {

            loginUserUseCase.loginUser(userData).collect { result ->

                when (result) {

                    is ResultState.Success<*> -> {
                        val uid = firebaseAuth.currentUser?.uid

                        if (uid != null) {
                            userDataStore.saveUser(uid)
                        }

                        fetchUserProfile()
                        logInState.value = logState(success = true)
                    }

                    is ResultState.Error<*> -> {
                        logInState.value =
                            logState(error = result.message.toString())
                    }

                    is ResultState.Loading -> {
                        logInState.value = logState(loading = true)
                    }
                }
            }
        }
    }
}



data class signState(
    val loading : Boolean = false,
    val success : Boolean= false,
    val error : String? = null

)
data class logState(
    val loading : Boolean = false,
    val success : Boolean = false,
    val error : String? = null

)

