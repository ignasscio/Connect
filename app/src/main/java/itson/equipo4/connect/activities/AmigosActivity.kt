package itson.equipo4.connect.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import itson.equipo4.connect.Utils
import itson.equipo4.connect.adapters.UsuarioAdapter
import itson.equipo4.connect.databinding.ActivityAmigosBinding
import itson.equipo4.connect.fragments.AgregarAmigoFragmentDialog
import itson.equipo4.connect.fragments.AlertaAmistadFragmentDialog
import itson.equipo4.connect.fragments.NuevoGrupoFragmentDialog
import itson.equipo4.connect.objetosnegocio.Usuario

class AmigosActivity : AppCompatActivity() {

    lateinit var binding: ActivityAmigosBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var mAuth: FirebaseAuth
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<Usuario>
    private lateinit var adapter: UsuarioAdapter
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAmigosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.extras?.get("user") as FirebaseUser
        mDbRef = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()

        userList = ArrayList()
        adapter = UsuarioAdapter(this, userList)

        userRecyclerView = binding.amigosRecyclerview
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

        showFriendsList(user)

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
        mDbRef.child("user").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                for (postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(Usuario::class.java)

                    if (mAuth.currentUser?.email != currentUser?.email) {
                        userList.add(currentUser!!)
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}