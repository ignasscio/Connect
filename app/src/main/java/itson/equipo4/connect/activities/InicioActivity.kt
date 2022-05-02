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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import itson.equipo4.connect.databinding.ActivityInicioBinding
import itson.equipo4.connect.databinding.ActivityRegistroBinding
import itson.equipo4.connect.fragments.SectionsPagerAdapter
import itson.equipo4.connect.objetosnegocio.Usuario

class InicioActivity : AppCompatActivity() {


    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    lateinit var binding: ActivityInicioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //da la bienvenida
        val extras = intent.extras
        val user = extras?.get("user") as FirebaseUser
        var usuario = Usuario("un email", "una contraseña", "un nombre")
        db.collection("usuarios").get().addOnSuccessListener { result ->
            for(document in result){
                if(document.id == user.email){
                    usuario = document.toObject(Usuario::class.java)
                }
            }
            Toast.makeText(baseContext, "Bienvenido, "+usuario.nombre,Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{e ->
            Toast.makeText(baseContext, "Error: "+e.message, Toast.LENGTH_SHORT).show()
        }

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
            gotoAmigos(user)
        }

        binding.inicioPerfil.setOnClickListener{
            gotoPerfil(user)
        }

    }

    fun gotoPerfil(user:FirebaseUser?){
        val intent = Intent(this, PerfilActivity::class.java)
        intent.putExtra("user",user)
        startActivity(intent)
    }

    fun gotoAmigos(user:FirebaseUser?){
        val intent = Intent(this, AmigosActivity::class.java)
        intent.putExtra("user",user)
        startActivity(intent)
    }

}