interface AuthRepository {
    suspend fun register(email: String, password: String): Result<User>
    suspend fun login(email: String, password: String): Result<User>
    fun getCurrentUser(): User?
    fun logout()
}