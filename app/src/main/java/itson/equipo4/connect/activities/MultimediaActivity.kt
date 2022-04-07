package itson.equipo4.connect.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import itson.equipo4.connect.databinding.ActivityAmigosBinding
import itson.equipo4.connect.databinding.ActivityMultimediaBinding

class MultimediaActivity : AppCompatActivity() {

    lateinit var binding: ActivityMultimediaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultimediaBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}