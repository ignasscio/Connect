package itson.equipo4.connect.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import itson.equipo4.connect.adapters.MensajeAdapter
import itson.equipo4.connect.databinding.ActivityChatBinding
import itson.equipo4.connect.objetosnegocio.Mensaje

class ChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatBinding

    private lateinit var messageAdapter: MensajeAdapter
    private lateinit var messageList: ArrayList<Mensaje>
    private lateinit var mDbRef: DatabaseReference
    private lateinit var chatRecyclerView: RecyclerView

    var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        mDbRef = FirebaseDatabase.getInstance().reference

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        binding.chatNombre.title = name
        binding.chatIntegrantes.text = ""

        messageList = ArrayList()
        messageAdapter = MensajeAdapter(this, messageList)

        chatRecyclerView = binding.rvMessages
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Mensaje::class.java)
                        messageList.add(message!!)
                    }

                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        binding.btnSend.setOnClickListener {
            val message = binding.etMessage.text.toString()
            val messageObject = Mensaje(message, senderUid)

            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }

            binding.etMessage.setText("")
        }
    }
}