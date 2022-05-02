package itson.equipo4.connect.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import itson.equipo4.connect.databinding.ActivityAmigosBinding
import itson.equipo4.connect.fragments.AgregarAmigoFragmentDialog
import itson.equipo4.connect.fragments.AlertaAmistadFragmentDialog
import itson.equipo4.connect.fragments.NuevoGrupoFragmentDialog
import kotlin.system.exitProcess

class AmigosActivity : AppCompatActivity() {

    lateinit var binding:ActivityAmigosBinding
    var contador:Int = 0;
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAmigosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.extras?.get("user") as FirebaseUser

        binding.amigosAmigos.setOnClickListener {
            if(contador < 10){
                when(contador){
                    5 -> Toast.makeText(this,"¡DEJA DE HACER ESO!",Toast.LENGTH_SHORT).show()
                    7 -> Toast.makeText(this,"NOO!!",Toast.LENGTH_SHORT).show()
                    8 -> Toast.makeText(this,"¡PARA!",Toast.LENGTH_SHORT).show()
                    9 -> {
                        Toast.makeText(this,"F. Cerraste la app.",Toast.LENGTH_SHORT).show()
                    }
                }
                contador++
            }
        }

        binding.amigosGrupos.setOnClickListener {
            var dialog = NuevoGrupoFragmentDialog()
            dialog.show(supportFragmentManager, "NuevoGrupoFragmentDialog")
        }

        binding.amigosAgregarAmigo.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("saludo","hola")
            bundle.putParcelable("user",user)
            var dialog = AgregarAmigoFragmentDialog()
            dialog.arguments = bundle
            dialog.show(supportFragmentManager,"AgregarAmigoFragmentDialog")
        }

        binding.amigosSolicitudesAmistad.setOnClickListener {
            var hayAmigos:Boolean = false
            var lista:ArrayList<String> = ArrayList<String>()
            db.collection("solicitudes").document(user.email!!).get().addOnSuccessListener { document ->
                if(!(document["emails"] == null)){
                    lista = document["emails"] as ArrayList<String>
                    hayAmigos = true
                }
                if(hayAmigos){
                    var dialog = AlertaAmistadFragmentDialog()
                    var bundle = Bundle()
                    bundle.putParcelable("user",user)
                    bundle.putStringArrayList("lista",lista)
                    dialog.arguments = bundle
                    dialog.show(supportFragmentManager, "AlertaAmistadFragmentDialog")
                }else{
                    Toast.makeText(this,"No tienes solicitudes de amistad.",Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                Toast.makeText(this,"No tienes solicitudes de amistad.",Toast.LENGTH_SHORT).show()
            }


        }

    }
}