package itson.equipo4.connect.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import itson.equipo4.connect.adapters.EventoAdapter
import itson.equipo4.connect.databinding.FragmentCalendarioBinding
import itson.equipo4.connect.objetosnegocio.Evento
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarioFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarioFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mAuth: FirebaseAuth
    private lateinit var eventosRecyclerView: RecyclerView
    private lateinit var eventosList: ArrayList<Evento>
    private lateinit var adapter: EventoAdapter
    private lateinit var mDbRef: DatabaseReference

    private lateinit var selectedDate: String

    private var _binding: FragmentCalendarioBinding? = null
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

        val nuevoEvento = binding.calendarioIvNuevoEvento as ImageView

        nuevoEvento.setOnClickListener {
            val dialog = EditorEventoFragmentDialog()
            val transaction = childFragmentManager.beginTransaction()
            dialog.show(transaction, "EditorEventoFragmentDialog")
        }

        mDbRef = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()

        eventosList = ArrayList()
        adapter = EventoAdapter(view.context, eventosList)

        eventosRecyclerView = binding.listaEventos
        eventosRecyclerView.layoutManager = LinearLayoutManager(view.context)
        eventosRecyclerView.adapter = adapter

        showEvents(FirebaseAuth.getInstance().currentUser)

        val myCalendar = binding.calendarView

        val date = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        selectedDate = sdf.format(date.time)

        myCalendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/$month/$year"
            showEvents(FirebaseAuth.getInstance().currentUser)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showEvents(user: FirebaseUser?) {
        if (user != null) {
            mDbRef.child("user").child(user.uid).child("eventos").addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    eventosList.clear()

                    for (postSnapshot in snapshot.children) {
                        val currentEvent = postSnapshot.getValue(Evento::class.java)

                        val date: Calendar = Calendar.getInstance()
                        val sdf = SimpleDateFormat("dd/MM/yyyy")
                        val curDate = sdf.format(currentEvent?.fecha!!.time)

                        if (curDate.equals(selectedDate)) {
                            eventosList.add(currentEvent)
                        }
                    }

                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CalendarioFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalendarioFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}