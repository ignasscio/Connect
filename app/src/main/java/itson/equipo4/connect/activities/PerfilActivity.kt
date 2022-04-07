package itson.equipo4.connect.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import itson.equipo4.connect.databinding.ActivityPerfilBinding

class PerfilActivity : AppCompatActivity() {

    lateinit var binding: ActivityPerfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}