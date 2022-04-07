package itson.equipo4.connect.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import itson.equipo4.connect.databinding.ActivityMaterialesBinding

class MaterialesActivity : AppCompatActivity() {

    lateinit var binding: ActivityMaterialesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaterialesBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}