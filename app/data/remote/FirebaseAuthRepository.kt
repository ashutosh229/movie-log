class FirebaseAuthRepository : AuthRepository {

    private val auth = com.google.firebase.auth.FirebaseAuth.getInstance()

    override suspend fun register(email: String, password: String): Result<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user!!
            Result.success(
                User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user!!
            Result.success(
                User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): User? {
        val user = auth.currentUser
        return user?.let {
            User(uid = it.uid, email = it.email)
        }
    }

    override fun logout() {
        auth.signOut()
    }
}