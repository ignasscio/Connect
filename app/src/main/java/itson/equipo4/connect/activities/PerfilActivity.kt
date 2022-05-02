package itson.equipo4.connect.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.storage.FirebaseStorage
import itson.equipo4.connect.databinding.ActivityPerfilBinding
import itson.equipo4.connect.objetosnegocio.Usuario
import java.io.File
import java.io.IOException

class PerfilActivity : AppCompatActivity() {

    lateinit var binding: ActivityPerfilBinding
    private val PICK_IMAGE_REQUEST = 1234
    private var direccionImagenPerfil: Uri?=null
    private val storageReference = FirebaseStorage.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val extras = intent.extras
        val user = extras?.get("user") as FirebaseUser
        val store = FirebaseStorage.getInstance().reference.child("imagesProfile/"+user.email)
        val localFile = File.createTempFile("tempImage","jpg")
        store.getFile(localFile).addOnSuccessListener {
            val fileImage = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.perfilFotoPerfil.setImageBitmap(fileImage)
        }.addOnFailureListener{e->
            Toast.makeText(baseContext,"Error: "+e.message, Toast.LENGTH_LONG);
        }



        val db = FirebaseFirestore.getInstance()
        db.collection("usuarios").document(user.email!!).get().addOnSuccessListener { document ->
            val usuario = document.toObject(Usuario::class.java)
            binding.perfilEtNombreUsuario.setText(usuario?.nombre)
            binding.perfilEtCorreoElectronico.setText(usuario?.email)
        }

        binding.perfilIvCambiarFotoPerfil.setOnClickListener {
            showFileChooser()
        }

        binding.perfilBtnGuardar.setOnClickListener {
            val nombre = binding.perfilEtNombreUsuario.text.toString().trim()
            var password = binding.perfilEtCambiarContrasena.text.toString().trim()
            var oldPassword:String? = ""
            val email = binding.perfilEtCorreoElectronico.text.toString().trim()

            db.collection("usuarios").document(user.email!!).get().addOnSuccessListener { document ->
                val viejoUsuario = document.toObject(Usuario::class.java)
                oldPassword = viejoUsuario?.password
            }.addOnFailureListener{e->
                Toast.makeText(baseContext,"Error al obtener viejo usuario: "+e.message, Toast.LENGTH_SHORT).show()
            }

            if(nombre.isEmpty() || email.isEmpty()){
                Toast.makeText(baseContext, "Nombre y email no pueden quedar vacíos", Toast.LENGTH_SHORT).show()
            }else{
                db.collection("usuarios").document(user.email!!).set(
                    Usuario(email, password, nombre, "imagesProfile/"+user.email)
                ).addOnSuccessListener {
                    Toast.makeText(baseContext, "Éxito al actualizar datos", Toast.LENGTH_SHORT).show()
                    user.updateEmail(email)
                    if(!password.isEmpty()) {user.updatePassword(password)}
                    uploadFile(user)
                }.addOnFailureListener { e ->
                    Toast.makeText(baseContext, "Error al actualizar datos: "+e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun verificarContrasena(pass:String):Boolean{
        //val pattern = Regex("^(?=(?:.*[a-z]){3})(?=(?:.*[A-Z]){2})(?=.*[?!@#*%^&-+])(?=(?:.*){2})[a-zA-Z?!@#*%^&-+]{8}$")
        return true
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
                binding.perfilFotoPerfil.setImageBitmap(bitmap)
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
            storageReference!!.child("imagesProfile/"+user.email).putFile(direccionImagenPerfil!!).addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Éxito al subir imagen de perfil",Toast.LENGTH_LONG).show()
            }.addOnFailureListener {e ->
                progressDialog.dismiss()
                Toast.makeText(this,"Error al subir imagen de perfil. "+e.message,Toast.LENGTH_LONG).show()
            }.addOnProgressListener { task ->
                val progress = 100.0 * task.bytesTransferred / task.totalByteCount
                progressDialog.setMessage(progress.toInt().toString()+"% subido...")
            }
        }
    }
}