package itson.equipo4.connect.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import itson.equipo4.connect.adapters.DocumentoAdapter
import itson.equipo4.connect.adapters.UsuarioAdapter
import itson.equipo4.connect.databinding.ActivityAmigosBinding
import itson.equipo4.connect.databinding.ActivityDocumentoBinding
import itson.equipo4.connect.objetosnegocio.Documento
import itson.equipo4.connect.objetosnegocio.Usuario

class DocumentoActivity : AppCompatActivity() {

    lateinit var binding: ActivityDocumentoBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var mAuth: FirebaseAuth
    private lateinit var documentoRecyclerView: RecyclerView
    private lateinit var documentsList: ArrayList<Documento>
    private lateinit var adapter: DocumentoAdapter
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.extras?.get("user") as FirebaseUser
        mDbRef = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()

        documentsList = ArrayList()
        adapter = DocumentoAdapter(this, documentsList)

        documentoRecyclerView = binding.documentoRv
        documentoRecyclerView.layoutManager = LinearLayoutManager(this)
        documentoRecyclerView.adapter = adapter

        showDocumentContent(user)
    }

    private fun showDocumentContent(user: FirebaseUser) {
        mDbRef.child("user").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                documentsList.clear()

                for (postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(Documento::class.java)

                    if (mAuth.currentUser?.uid == currentUser?.usuario) {
                        documentsList.add(currentUser!!)
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}