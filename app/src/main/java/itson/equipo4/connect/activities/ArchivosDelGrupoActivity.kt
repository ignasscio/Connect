package itson.equipo4.connect.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import itson.equipo4.connect.databinding.ActivityAmigosBinding

class ArchivosDelGrupoActivity : AppCompatActivity() {

    lateinit var binding: ActivityAmigosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAmigosBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}