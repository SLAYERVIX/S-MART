package com.example.data.remote

import com.example.domain.states.RegistrationResult
import com.example.domain.states.UpdateProfileResult
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseAuthService(private val firebaseAuth: FirebaseAuth) {
    fun createAccountWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<RegistrationResult> =
        callbackFlow {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    trySend(RegistrationResult.Completed)
                    task.addOnSuccessListener {
                        trySend(RegistrationResult.Success)
                        return@addOnSuccessListener
                    }
                    task.addOnFailureListener {
                        when (it) {
                            is FirebaseAuthUserCollisionException -> {
                                trySend(RegistrationResult.UserCollision)
                                return@addOnFailureListener
                            }

                            is FirebaseNetworkException -> {
                                trySend(RegistrationResult.ConnectionProblem)
                            }
                        }
                    }
                }
            awaitClose()
        }

    fun updateProfileData(displayName: String): Flow<UpdateProfileResult> = callbackFlow {
        firebaseAuth.currentUser?.let {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()

            it.updateProfile(profileUpdates).addOnCompleteListener { task ->
                trySend(UpdateProfileResult.Completed)
                task.addOnSuccessListener {
                    trySend(UpdateProfileResult.Success)
                }
            }
        }
        awaitClose()
    }
}