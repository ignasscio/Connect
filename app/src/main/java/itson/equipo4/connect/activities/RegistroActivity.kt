package itson.equipo4.connect.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import itson.equipo4.connect.Utils
import itson.equipo4.connect.databinding.ActivityRegistroBinding
import kotlin.properties.Delegates


class RegistroActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistroBinding

    private var email by Delegates.notNull<String>()
    private var password by Delegates.notNull<String>()

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registroTvYaTienesCuenta.setOnClickListener {
            launchInicioSesion()
        }

        binding.registroBtnIniciarSesion.setOnClickListener {
            launchInicioSesion()
        }


        binding.registroBtnCrearCuenta.setOnClickListener {
            createAccount()
        }
    }

    private fun createAccount() {
        val regexEmail = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}".toRegex()
        email = binding.registroEtCorreoElectronico.text.toString().trim()
        password = binding.registroEtContrasena.text.toString().trim()

        if (email == "" || password == "") {
            Utils.displayShortToast("Ingrese un correo electrónico y una contraseña", this)
            return
        }

        if (!regexEmail.containsMatchIn(email)) {
            Utils.displayShortToast("Por favor ingrese un correo electrónico válido", this)
            return
        }

        launchCrearCuenta()
    }

    private fun launchCrearCuenta() {
        val intent = Intent(this, CrearCuentaActivity::class.java)
        intent.putExtra("email", email)
        intent.putExtra("password", password)
        startActivity(intent)
    }

    private fun launchInicioSesion() {
        val intent = Intent(this, InicioSesionActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}