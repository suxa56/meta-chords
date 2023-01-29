package uz.suxa.metachords.domain.repo

interface AuthRepo {
    suspend fun signIn()

    fun signOut()
}