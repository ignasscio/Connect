package itson.equipo4.connect.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import itson.equipo4.connect.databinding.ActivityRegistroBinding

class CrearCuentaActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}