package itson.equipo4.connect.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import itson.equipo4.connect.adapters.ChatAdapter
import itson.equipo4.connect.databinding.FragmentChatBinding
import itson.equipo4.connect.objetosnegocio.Chat
import itson.equipo4.connect.objetosnegocio.Mensaje
import itson.equipo4.connect.objetosnegocio.Usuario


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mAuth: FirebaseAuth
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatList: ArrayList<Chat>
    private lateinit var adapter: ChatAdapter
    private lateinit var mDbRef: DatabaseReference

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDbRef = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()

        chatList = ArrayList()
        adapter = ChatAdapter(view.context, chatList)

        chatRecyclerView = binding.amigosRecyclerview
        chatRecyclerView.layoutManager = LinearLayoutManager(view.context)
        chatRecyclerView.adapter = adapter

        showChats(FirebaseAuth.getInstance().currentUser!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun showChats(user: FirebaseUser) {
        mDbRef.child("user").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()

                for (postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(Usuario::class.java)

                    if (user.uid != currentUser?.uid) {

                        val lastQuery: Query =
                            mDbRef.child("chats").child(user.uid + currentUser?.uid)
                                .child("messages").orderByKey().limitToLast(1)

                        lastQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (data in dataSnapshot.children) {
                                    val message = data.getValue(Mensaje::class.java)

                                    chatList.add(
                                        Chat(
                                            currentUser?.uid,
                                            currentUser?.nombre,
                                            message?.message.toString()
                                        )
                                    )
                                }

                                adapter.notifyDataSetChanged()
                            }

                            override fun onCancelled(databaseError: DatabaseError) {}
                        })
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}