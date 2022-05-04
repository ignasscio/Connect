package itson.equipo4.connect.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import itson.equipo4.connect.databinding.ActivityConfiguracionBinding

class ConfiguracionActivity : AppCompatActivity() {

    lateinit var binding: ActivityConfiguracionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //define el toolbar
        var toolbar = binding.mainToolbar as Toolbar
        setSupportActionBar(toolbar);

    }
}