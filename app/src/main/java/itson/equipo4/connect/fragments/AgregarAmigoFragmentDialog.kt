package itson.equipo4.connect.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import itson.equipo4.connect.R
import itson.equipo4.connect.objetosnegocio.SolicitudAmistad

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AgregarAmigoFragmentDialog.newInstance] factory method to
 * create an instance of this fragment.
 */
class AgregarAmigoFragmentDialog : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var saludo:String? = "texto"
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels*.85).toInt()
        val height = (resources.displayMetrics.heightPixels*.4).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.fragment_agregar_amigo_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val et_email = view?.findViewById<EditText>(R.id.agregarAmigo_et_email)
        val btn_cancelar = view?.findViewById<Button>(R.id.agregarAmigo_btn_cancelar)
        val btn_aceptar = view?.findViewById<Button>(R.id.agregarAmigo_btn_enviar)
        val user = arguments?.get("user") as FirebaseUser

        btn_cancelar.setOnClickListener {
            dismiss()
        }

        btn_aceptar.setOnClickListener {
            if(et_email.text.toString().trim() == user.email){
                Toast.makeText(context, "Este eres tú aksdjkajsd", Toast.LENGTH_SHORT).show()
            }else if(!et_email.text.toString().trim().isEmpty()){
                db.collection("solicitudes").document(et_email.text.toString().trim()).update(
                    "emails", FieldValue.arrayUnion(user.email)
                ).addOnSuccessListener {
                    Toast.makeText(context, "Solicitud de amistad enviada", Toast.LENGTH_SHORT).show()
                    dismiss()
                }.addOnFailureListener {e->
                    var lista = arrayListOf<String>()
                    lista.add(user.email!!)
                    db.collection("solicitudes").document(et_email.text.toString().trim()).set(
                        hashMapOf(
                            "emails" to lista
                        )
                    )
                    //Toast.makeText(context, "Error al enviar solicitud de amistad: "+e.message, Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context, "Ingrese una cuenta de correo", Toast.LENGTH_SHORT).show()
            }
        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AgregarAmigoFragmentDialog.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AgregarAmigoFragmentDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}