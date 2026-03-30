package com.example.movielog.features.library.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LibraryRemoteDataSource {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun getUserId(): String {
        return auth.currentUser?.uid
            ?: throw Exception("User not logged in")
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

    // 🔹 GET ALL CONTENT
    suspend fun getAllContent(): List<Map<String, Any>> {
        val snapshot = getLibraryCollection()
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.data }
    }

    // 🔹 DELETE CONTENT
    suspend fun deleteContent(contentId: String) {
        getLibraryCollection()
            .document(contentId)
            .delete()
            .await()
    }
}