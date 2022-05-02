package itson.equipo4.connect.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import itson.equipo4.connect.databinding.ActivityInicioSesionBinding
import itson.equipo4.connect.databinding.ActivityRegistroBinding

class InicioSesionActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    lateinit var binding: ActivityInicioSesionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioSesionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registroBtnIniciarSesion.setOnClickListener {
            val mEmail = binding.registroEtCorreoElectronico.text.toString().trim()
            val mPassword = binding.registroEtContrasena.text.toString().trim()
            if(mEmail.isEmpty() && mPassword.isEmpty()){
                Toast.makeText(baseContext, "Por favor ingrese sus datos correctamente", Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(mEmail, mPassword).addOnSuccessListener {
                    goToInicio(auth.currentUser)
                }.addOnFailureListener { e ->
                    Toast.makeText(baseContext, "Error al iniciar sesion: "+e.message, Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    fun goToInicio(user:FirebaseUser?){
        val intent = Intent(this, InicioActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("user", user)
        startActivity(intent)
    }
}