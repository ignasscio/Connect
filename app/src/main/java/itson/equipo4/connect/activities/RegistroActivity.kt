package itson.equipo4.connect.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import itson.equipo4.connect.databinding.ActivityRegistroBinding
import kotlin.properties.Delegates


class RegistroActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistroBinding

    private var email by Delegates.notNull<String>();
    private var password by Delegates.notNull<String>();

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registroTvYaTienesCuenta.setOnClickListener {
            goToInicioSesion()
        }

        binding.registroBtnIniciarSesion.setOnClickListener {
            goToInicioSesion()
        }


        binding.registroBtnCrearCuenta.setOnClickListener {
            var bundle:Bundle = Bundle();
            var regexEmail = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}".toRegex();
            email = binding.registroEtCorreoElectronico.text.toString().trim();
            password = binding.registroEtContrasena.text.toString().trim();
            if(email == "" && password == ""){
                Toast.makeText(this, "Ingrese un correo electrónico y una contraseña", Toast.LENGTH_SHORT).show();
            } else if(email == ""){
                Toast.makeText(this, "Ingrese un correo electrónico", Toast.LENGTH_SHORT).show();
            }else if(password == ""){
                Toast.makeText(this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
            }else if(!regexEmail.containsMatchIn(email)){
                Toast.makeText(this, "Por favor ingrese un correo electrónico válido", Toast.LENGTH_SHORT).show();
            }else{
                val intent:Intent = Intent(this, CrearCuentaActivity::class.java)
                intent.putExtra("email", email);
                intent.putExtra("password", password)
                startActivity(intent);
            }
        }

        binding.registroBtnIniciarSesion.setOnClickListener {
            Toast.makeText(this,"Not implemented yet.",Toast.LENGTH_SHORT).show()
        }

    }

    fun goToInicioSesion(){
        val intent:Intent = Intent(this, InicioSesionActivity::class.java);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}