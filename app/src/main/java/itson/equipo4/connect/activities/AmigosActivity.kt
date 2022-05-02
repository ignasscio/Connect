package itson.equipo4.connect.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import itson.equipo4.connect.Utils
import itson.equipo4.connect.databinding.ActivityAmigosBinding
import itson.equipo4.connect.fragments.AgregarAmigoFragmentDialog
import itson.equipo4.connect.fragments.AlertaAmistadFragmentDialog
import itson.equipo4.connect.fragments.NuevoGrupoFragmentDialog

class AmigosActivity : AppCompatActivity() {

    lateinit var binding: ActivityAmigosBinding
    var contador: Int = 0;
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAmigosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.extras?.get("user") as FirebaseUser

        binding.amigosAmigos.setOnClickListener {
            showFriendsList(user)
        }

        binding.amigosGrupos.setOnClickListener {
            showGroups(user)
        }

        binding.amigosAgregarAmigo.setOnClickListener {
            showAddFriendsFragment(user)
        }

        binding.amigosSolicitudesAmistad.setOnClickListener {
            showFriendRequests(user)
        }

    }

    /**
     * This shows a dialog with a friend request that you can accept or reject.
     * It also comes with a little friendly message to convince you to accept!
     */
    private fun showAddFriendsFragment(user: FirebaseUser) {
        val bundle = Bundle()
        bundle.putString("saludo", "hola")
        bundle.putParcelable("user", user)
        val dialog = AgregarAmigoFragmentDialog()
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "AgregarAmigoFragmentDialog")
    }

    /**
     * This shows all the groups you're part of
     */
    private fun showGroups(user: FirebaseUser) {
        val dialog = NuevoGrupoFragmentDialog()
        dialog.show(supportFragmentManager, "NuevoGrupoFragmentDialog")
    }

    /**
     * Let's just imagine the hypothetical scenario where people have sent you friend requests;
     * this shows you a list of pending friend requests
     */
    private fun showFriendRequests(user: FirebaseUser) {
        var lista: ArrayList<String>

        db.collection("solicitudes").document(user.email!!).get().addOnSuccessListener { document ->
            if (document["emails"] == null) {
                Utils.displayShortToast("No tienes solicitudes de amistad.", this)
            } else {
                lista = document["emails"] as ArrayList<String>

                val dialog = AlertaAmistadFragmentDialog()
                val bundle = Bundle()

                bundle.putParcelable("user", user)
                bundle.putStringArrayList("lista", lista)
                dialog.arguments = bundle
                dialog.show(supportFragmentManager, "AlertaAmistadFragmentDialog")
            }
        }.addOnFailureListener {
            Utils.displayShortToast("No tienes solicitudes de amistad.", this)
        }
    }

    /**
     * Shows your massive friends list!
     * ... or that's what this is supposed to do in the future.
     *
     * For now it just pretends to close the app if you tap too many times on the button
     */
    private fun showFriendsList(user: FirebaseUser) {
        if (contador < 10) {
            when (contador) {
                5 -> Toast.makeText(this, "¡DEJA DE HACER ESO!", Toast.LENGTH_SHORT).show()
                7 -> Toast.makeText(this, "NOO!!", Toast.LENGTH_SHORT).show()
                8 -> Toast.makeText(this, "¡PARA!", Toast.LENGTH_SHORT).show()
                9 -> {
                    Toast.makeText(this, "F. Cerraste la app.", Toast.LENGTH_SHORT).show()
                }
            }
            contador++
        }
    }
}