package itson.equipo4.connect.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import itson.equipo4.connect.databinding.ActivityMainBinding

//148

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logo.setOnClickListener{
            var intent: Intent =  Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }


    }
}