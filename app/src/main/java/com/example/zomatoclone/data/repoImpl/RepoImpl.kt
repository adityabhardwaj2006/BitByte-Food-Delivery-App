package com.example.zomatoclone.data.repoImpl

import android.util.Log
import com.example.zomatoclone.common.ResultState
import com.example.zomatoclone.common.USER_COLLECTION
import com.example.zomatoclone.data.models.User
import com.example.zomatoclone.data.models.UserData
import com.example.zomatoclone.domain.repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RepoImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStore: FirebaseFirestore
) : Repo {

    override fun registerWithEmailAndPassword(
        userData: UserData
    ): Flow<ResultState<String>> = callbackFlow {

        trySend(ResultState.Loading)

        firebaseAuth.createUserWithEmailAndPassword(
            userData.email,
            userData.password
        )
            .addOnSuccessListener { authResult ->

                val uid = authResult.user?.uid

                if (uid == null) {
                    trySend(ResultState.Error("UID not found"))
                    close()
                    return@addOnSuccessListener
                }
                val userProfile = User(
                    email = userData.email,
                    name = userData.name,
                    address = userData.address
                )


                firebaseStore.collection(USER_COLLECTION)
                    .document(uid)
                    .set(userProfile)
                    .addOnSuccessListener {
                        Log.d("FIRESTORE_SAVE","SAVED UID : $uid")
                        trySend(ResultState.Success("User Registered Successfully"))
                        close()
                    }
                    .addOnFailureListener { e ->
                        trySend(ResultState.Error(e.localizedMessage ?: "Firestore error"))
                        close()
                    }
            }
            .addOnFailureListener { e ->
                trySend(ResultState.Error(e.localizedMessage ?: "Auth error"))
                close()
            }

        awaitClose { }
    }


    override fun loginWithEmailAndPassword(userData: UserData): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseAuth.signInWithEmailAndPassword(userData.email, userData.password)
            .addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    trySend(ResultState.Success("User Logged In Successfully"))
                    close()
                } else {
                    trySend(ResultState.Error(authTask.exception?.localizedMessage ?: "Unknown Error"))
                    close()
                }
            }

        awaitClose { }
    }
}

