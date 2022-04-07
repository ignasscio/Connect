package itson.equipo4.connect.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import itson.equipo4.connect.databinding.ActivityInicioBinding
import itson.equipo4.connect.databinding.ActivityRegistroBinding
import itson.equipo4.connect.fragments.SectionsPagerAdapter

class InicioActivity : AppCompatActivity() {

    lateinit var binding: ActivityInicioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //define el toolbar
        var toolbar = binding.mainToolbar as Toolbar
        toolbar.title=""
        setSupportActionBar(toolbar);

        //es el pageviewer de esta actividad
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

        //listeners
        binding.inicioAjustes.setOnClickListener{
            var intent = Intent(this, ConfiguracionActivity::class.java)
            startActivity(intent)
            //Toast.makeText(this, "aaaaaaajustes", Toast.LENGTH_LONG).show()
        }

        binding.inicioAmigos.setOnClickListener{
            //Toast.makeText(this, "aaaaaaamigos", Toast.LENGTH_LONG).show()
            var intent = Intent(this, AmigosActivity::class.java)
            startActivity(intent)
        }

        binding.inicioPerfil.setOnClickListener{
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
            //Toast.makeText(this, "not impleeeemteeeeed yeeeeeet", Toast.LENGTH_LONG).show()
        }

    }
}