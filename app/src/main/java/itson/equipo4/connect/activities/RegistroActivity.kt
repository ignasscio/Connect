package itson.equipo4.connect.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import itson.equipo4.connect.databinding.ActivityRegistroBinding


class RegistroActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registroBtnIniciarSesion.setOnClickListener {
            var intent: Intent =  Intent(this, InicioActivity::class.java)
            startActivity(intent)
        }

    }
}