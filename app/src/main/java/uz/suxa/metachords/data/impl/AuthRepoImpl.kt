package uz.suxa.metachords.data.impl

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import uz.suxa.metachords.domain.repo.AuthRepo
import uz.suxa.metachords.ui.MainActivity

class AuthRepoImpl: AuthRepo {

    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    private fun init() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(OAUTH_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(MainActivity(), gso)

        auth = FirebaseAuth.getInstance()
    }

    override suspend fun signIn() {
        init()
        val signInIntent = googleSignInClient.signInIntent
        val task = GoogleSignIn.getSignedInAccountFromIntent(signInIntent)
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)!!
            Log.d("firebaseAuthWithGoogle", "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w("firebaseAuthWithGoogle", "Google sign in failed", e)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(MainActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("signInWithCredential", "signInWithCredential:success")
                    val user = auth.currentUser
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("signInWithCredential", "signInWithCredential:failure", task.exception)
//                    updateUI(null)
                }
            }
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }

    companion object{
        private const val OAUTH_CLIENT_ID = "974482926450-aeanfqvil9dvcmi0n3io0vf240k3dver.apps.googleusercontent.com"
    }
}