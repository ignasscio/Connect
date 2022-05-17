package itson.equipo4.connect.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import itson.equipo4.connect.databinding.FragmentEditorEventoDialogBinding
import itson.equipo4.connect.objetosnegocio.Evento
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditorEventoFragmentDialog.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditorEventoFragmentDialog : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentEditorEventoDialogBinding? = null
    private val binding get() = _binding!!

    private val fAouth = FirebaseAuth.getInstance()
    private lateinit var mDbRef: DatabaseReference

    private var selectedColor: String = "verde"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * .85).toInt()
        val height = (resources.displayMetrics.heightPixels * .4).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditorEventoDialogBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editorEventoAceptar.setOnClickListener { addEvent() }

        val rg = binding.editorEventoColorEvento as RadioGroup

        rg.setOnCheckedChangeListener { _: RadioGroup, checkedId ->
            when (checkedId) {
                binding.verde.id -> selectedColor = "verde"
                binding.morado.id -> selectedColor = "morado"
                binding.amarillo.id -> selectedColor = "amarillo"
                binding.rojo.id -> selectedColor = "rojo"
            }
        }

        binding.editorEventoCancelar.setOnClickListener { dismiss() }

    }

    private fun addEvent() {
        val user: FirebaseUser = fAouth.currentUser!!

        mDbRef = FirebaseDatabase.getInstance().reference

        val formatoFecha = SimpleDateFormat("dd/MM/yyyy HH:mm")
        formatoFecha.isLenient = false
        try {
            val date: Date =
                formatoFecha.parse(binding.editorEventoFechaEvento.text.toString() + " " + binding.editorEventoHoraEvento.text.toString()) as Date
            val tituloEvento = binding.editorEventoTituloEvento.text.toString()
            val descripcionEvento = binding.editorEventoDescripcionEvento.text.toString()

            mDbRef.child("user").child(user.uid).child("eventos").push()
                .setValue(
                    Evento(
                        tituloEvento,
                        descripcionEvento,
                        date,
                        selectedColor
                    )
                )

            dismiss()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditorEventoFragmentDialog.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditorEventoFragmentDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}