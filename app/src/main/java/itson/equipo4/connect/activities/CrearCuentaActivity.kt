package itson.equipo4.connect.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import itson.equipo4.connect.databinding.ActivityCrearCuentaBinding
import itson.equipo4.connect.databinding.ActivityRegistroBinding
import itson.equipo4.connect.objetosnegocio.Usuario
import java.io.IOException
import kotlin.properties.Delegates

class CrearCuentaActivity : AppCompatActivity() {

    lateinit var binding: ActivityCrearCuentaBinding

    private var email by Delegates.notNull<String>()
    private var password by Delegates.notNull<String>()
    private var nombre by Delegates.notNull<String>()
    //private var direccionImagenPerfil by Delegates.notNull<Uri>()
    private var direccionImagenPerfil:Uri?=null
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    private val fAouth = FirebaseAuth.getInstance()
    private val PICK_IMAGE_REQUEST = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.crearCuentaIvNuevaFotoPerfil.setOnClickListener {
            showFileChooser()
        }

        binding.registroBtnComienza.setOnClickListener {
            nombre = binding.registroEtNombre.text.toString().trim();
            if(TextUtils.isEmpty(nombre)){
                Toast.makeText(this, "Por favor ingrese su nombre.", Toast.LENGTH_SHORT).show()
            }else if(direccionImagenPerfil == null){
                Toast.makeText(this, "Por favor seleccione una imagen de perfil.", Toast.LENGTH_SHORT).show()
            } else{
                val extras = intent.extras
                email = extras?.getString("email")!!;
                password = extras?.getString("password")!!;
                print("email -> "+email)
                print("password ->" +password)
                fAouth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(this){
                    val user: FirebaseUser = fAouth.currentUser!!
                    verifyEmail(user)
                    db.collection("usuarios").document(email).set(
                        hashMapOf(
                            "email" to email,
                            "password" to password,
                            "nombre" to nombre
                        )
                    ).addOnSuccessListener {
                        uploadFile(user)
                        goToLoginActivity()
                    }.addOnFailureListener{e ->
                        goToRegistroActivity(e)
                    }

                }.addOnFailureListener {e ->
                    goToRegistroActivity(e)
                }
            }
        }

    }

    fun showFileChooser(){
        val intent = Intent()
        intent.type="image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"),PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data!= null){
            direccionImagenPerfil = data!!.data
            try{
                val bitmap= MediaStore.Images.Media.getBitmap(contentResolver, direccionImagenPerfil)
                binding.crearCuentaIvFotoPerfil.setImageBitmap(bitmap)
            }catch(e: IOException){
                e.printStackTrace()
                Toast.makeText(this, "Error: "+e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun uploadFile(user:FirebaseUser){
        if(direccionImagenPerfil != null){
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Cargando...")
            progressDialog.show()

            storageReference!!.child("imagesProfile/"+user.uid).putFile(direccionImagenPerfil!!).addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Éxito al subir imagen de perfil",Toast.LENGTH_LONG).show()
            }.addOnFailureListener {e ->
                progressDialog.dismiss()
                Toast.makeText(this,"Error al subir imagen de perfil. "+e.message,Toast.LENGTH_LONG).show()
                goToRegistroActivity(e)
            }.addOnProgressListener { task ->
                val progress = 100.0 * task.bytesTransferred / task.totalByteCount
                progressDialog.setMessage(progress.toInt().toString()+"% subido...")
            }
        }
    }

    fun goToRegistroActivity(e:Exception){
        Toast.makeText(this, "Error al registrarte, "+nombre.toString()+". "+e.message.toString(), Toast.LENGTH_LONG).show()
        val intent:Intent = Intent(this, RegistroActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun goToLoginActivity(){
        Toast.makeText(this, "Éxito al crear tu cuenta, "+nombre.toString(), Toast.LENGTH_SHORT).show()
        val intent:Intent = Intent(this, InicioSesionActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun verifyEmail(user:FirebaseUser){
        user.sendEmailVerification().addOnCompleteListener(this){ Task ->
            if(Task.isSuccessful){
                Toast.makeText(this, "Se ha enviado un correo de verificación a "+email, Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Error al enviar un correo de verificación a "+email, Toast.LENGTH_SHORT).show()
            }
        }
    }



}