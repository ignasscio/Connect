package itson.equipo4.connect.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import itson.equipo4.connect.Utils
import itson.equipo4.connect.databinding.ActivityInicioSesionBinding

class InicioSesionActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    lateinit var binding: ActivityInicioSesionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioSesionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registroBtnIniciarSesion.setOnClickListener {
            signUserIn()
        }

    }

    /**
     * Here we perform a simple check on both text fields to see if any of them
     * is empty; if neither are empty then we ask Firebase to fetch the account
     * from the database.
     *
     * If the email doesn't exist or the password is incorrect then we don't allow
     * the user to log in; otherwise, we store the session and send the user to
     * the home page.
     */
    private fun signUserIn() {
        val mEmail = binding.registroEtCorreoElectronico.text.toString().trim()
        val mPassword = binding.registroEtContrasena.text.toString().trim()

        if (mEmail.isEmpty() && mPassword.isEmpty()) {
            Utils.displayShortToast("Por favor ingrese sus datos correctamente", baseContext)
            return
        }

        auth.signInWithEmailAndPassword(mEmail, mPassword).addOnSuccessListener {
            if (auth.currentUser!!.isEmailVerified) {
                goToInicio(auth.currentUser)
            } else {
                Utils.displayLongToast(
                    "Error al iniciar sesion: Correo electronico sin verificar",
                    baseContext
                )
            }
        }.addOnFailureListener { e ->
            Utils.displayLongToast("Error al iniciar sesion: " + e.message, baseContext)
        }
    }

    private fun goToInicio(user: FirebaseUser?) {
        val intent = Intent(this, InicioActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("user", user)
        startActivity(intent)
    }
}