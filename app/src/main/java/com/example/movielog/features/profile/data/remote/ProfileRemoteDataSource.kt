package com.example.movielog.features.profile.data.remote

import com.example.movielog.core.auth.AuthManager
import com.example.movielog.features.profile.domain.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ProfileRemoteDataSource {
    private val firestore = FirebaseFirestore.getInstance()

    private fun currentUser() = AuthManager.requireCurrentUser()

    private fun userDocument() = firestore.collection("users").document(currentUser().uid)

    fun observeProfile(): Flow<UserProfile> = callbackFlow {
        val listener: ListenerRegistration = userDocument()
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val firebaseUser = currentUser()
                val email = firebaseUser.email.orEmpty()
                val defaultName = firebaseUser.displayName
                    ?.takeIf { it.isNotBlank() }
                    ?: email.substringBefore("@").ifBlank { "MovieLog User" }

                val data = snapshot?.data.orEmpty()
                val profile = UserProfile(
                    uid = firebaseUser.uid,
                    email = email,
                    displayName = (data["displayName"] as? String).orEmpty().ifBlank { defaultName },
                    createdAt = data["createdAt"] as? Long,
                    updatedAt = data["updatedAt"] as? Long
                )

                trySend(profile)
            }

        awaitClose { listener.remove() }
    }

    suspend fun ensureProfile(profile: UserProfile) {
        val snapshot = userDocument().get().await()
        if (!snapshot.exists()) {
            val now = System.currentTimeMillis()
            userDocument().set(
                mapOf(
                    "uid" to profile.uid,
                    "email" to profile.email,
                    "displayName" to profile.displayName,
                    "createdAt" to now,
                    "updatedAt" to now
                )
            ).await()
        }
    }

    suspend fun updateProfile(displayName: String) {
        val user = currentUser()
        val now = System.currentTimeMillis()

        AuthManager.updateDisplayName(displayName)

        userDocument().set(
            mapOf(
                "uid" to user.uid,
                "email" to user.email.orEmpty(),
                "displayName" to displayName,
                "updatedAt" to now
            ),
            com.google.firebase.firestore.SetOptions.merge()
        ).await()
    }

    suspend fun deleteAllUserData() {
        val libraryCollection = userDocument().collection("library")
        val documents = libraryCollection.get().await().documents
        documents.forEach { document ->
            libraryCollection.document(document.id).delete().await()
        }
        userDocument().delete().await()
    }
}
