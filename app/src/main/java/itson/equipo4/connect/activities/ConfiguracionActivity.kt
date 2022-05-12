package itson.equipo4.connect.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import itson.equipo4.connect.databinding.ActivityConfiguracionBinding

class ConfiguracionActivity : AppCompatActivity() {

    lateinit var binding: ActivityConfiguracionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //define el toolbar
        val toolbar = binding.mainToolbar as Toolbar
        setSupportActionBar(toolbar)

        binding.configuracionSwitchNotificaciones.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
            } else {
            }
        }

        binding.configuracionSwitchSonido.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
            } else {
            }
        }

        binding.configuracionSwitchTema.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

    }
}