package com.example.movielog.features.library.data.remote

import com.example.movielog.core.auth.AuthManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class LibraryRemoteDataSource {

    private val firestore = FirebaseFirestore.getInstance()

    private fun getUserId(): String {
        val userId = AuthManager.getCurrentUserId()
        return userId ?: throw Exception("User not logged in")
    }

    private fun getLibraryCollection() =
        firestore.collection("users")
            .document(getUserId())
            .collection("library")

    // 🔹 ADD / UPDATE CONTENT
    suspend fun addOrUpdateContent(data: Map<String, Any>) {
        val contentId = data["id"] as String
        getLibraryCollection()
            .document(contentId)
            .set(data)
            .await()
    }

    // 🔥 REACTIVE STREAM (REAL-TIME UPDATES)
    fun observeAllContent(): Flow<List<Map<String, Any>>> = callbackFlow {

        val listener: ListenerRegistration =
            getLibraryCollection()
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val data = snapshot.documents.mapNotNull { it.data }
                        trySend(data)
                    }
                }

        awaitClose {
            listener.remove()
        }
    }

    // 🔹 DELETE CONTENT
    suspend fun deleteContent(contentId: String) {
        getLibraryCollection()
            .document(contentId)
            .delete()
            .await()
    }
}