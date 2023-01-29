package uz.suxa.metachords.domain.usecase

import uz.suxa.metachords.domain.repo.AuthRepo

class SignInUseCase(
    private val repo: AuthRepo
) {
    suspend fun invoke() {
        repo.signIn()
    }
}