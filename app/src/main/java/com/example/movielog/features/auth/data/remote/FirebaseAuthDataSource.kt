package com.example.movielog.features.auth.data.remote

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthDataSource {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun register(email: String, password: String) =
        auth.createUserWithEmailAndPassword(email, password).await()

    suspend fun login(email: String, password: String) =
        auth.signInWithEmailAndPassword(email, password).await()

    fun getCurrentUser() = auth.currentUser

    fun logout() = auth.signOut()
}