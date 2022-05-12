package itson.equipo4.connect.activities

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import itson.equipo4.connect.Utils
import itson.equipo4.connect.databinding.ActivityCrearCuentaBinding
import itson.equipo4.connect.objetosnegocio.Usuario
import java.io.IOException
import kotlin.properties.Delegates

class CrearCuentaActivity : AppCompatActivity() {

    lateinit var binding: ActivityCrearCuentaBinding

    private var email by Delegates.notNull<String>()
    private var password by Delegates.notNull<String>()
    private var nombre by Delegates.notNull<String>()
    private var direccionImagenPerfil: Uri? = null
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    private val fAouth = FirebaseAuth.getInstance()
    private lateinit var mDbRef: DatabaseReference

    private val selectImageFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            // Handle the returned Uri
            handleImageChooserResult(uri)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.crearCuentaIvNuevaFotoPerfil.setOnClickListener {
            showFileChooser()
        }

        binding.registroBtnComienza.setOnClickListener {
            registerUser()
        }
    }

    /**
     * Once the user has clicked on the Sign Up button we have to make sure all info
     * provided is valid so it can be processed by the Firebase Auth API.
     *
     * Assuming all info is valid, we ask Firebase to create a new user using the
     * email and password provided and then instruct the user to verify their account
     * through their email. Otherwise, we just cancel the registration and ask the
     * user to complete any missing steps of the registration.
     */
    private fun registerUser() {
        nombre = binding.registroEtNombre.text.toString().trim()

        if (TextUtils.isEmpty(nombre)) {
            Utils.displayShortToast("Por favor ingrese su nombre", this)
            return
        }

        if (direccionImagenPerfil == null) {
            Utils.displayShortToast("Por favor seleccione una imagen de perfil", this)
            return
        }

        val extras = intent.extras

        email = extras?.getString("email")!!
        password = extras.getString("password")!!

        fAouth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(this) {
            insertCreatedUserToDatabase()
        }.addOnFailureListener { e ->
            launchRegistro(e)
        }
    }

    /**
     * In order for the newly created user to persist we have to create the User and
     * send all its data to the database in Firebase.
     *
     * Storing String data is fairly quick, but profile pictures might take longer to
     * be uploaded; therefore, in order not to make the user think that the app has frozen
     * we display the upload progress to the user. Once everything is uploaded and ready to
     * go, the user will be taken to the login activity where they can now log in with their
     * new account.
     */
    private fun insertCreatedUserToDatabase() {
        val user: FirebaseUser = fAouth.currentUser!!

        sendEmailVerification(user)

        db.collection("usuarios").document(user.uid).set(
            Usuario(user.uid, nombre, email, "***", "imagesProfile/" + user.uid)
        ).addOnSuccessListener {
            uploadFile(user)
            launchLogin()
        }.addOnFailureListener { e ->
            launchRegistro(e)
        }

        mDbRef = FirebaseDatabase.getInstance().reference

        mDbRef.child("user").child(user.uid)
            .setValue(Usuario(user.uid, nombre, email, "***", "imagesProfile/" + user.uid))
    }

    /**
     * Here we ask the user to choose an image to use as their profile picture
     */
    private fun showFileChooser() {
        selectImageFromGallery.launch("image/*")
    }

    /**
     * This takes the picture chosen by the user and displays it as the profile picture, as well
     * as storing the URI so we can properly upload it later.
     *
     * Isn't it prettier when deprecated methods are nowhere to be seen anymore?
     */
    private fun handleImageChooserResult(uri: Uri?) {
        direccionImagenPerfil = uri

        try {
            val bitmap: Bitmap? = if (Build.VERSION.SDK_INT >= 29) {
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(this.contentResolver, direccionImagenPerfil!!)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, direccionImagenPerfil)
            }

            binding.crearCuentaIvFotoPerfil.setImageBitmap(bitmap)
        } catch (e: IOException) {
            Utils.displayLongToast("Error: " + e.message, this)
            e.printStackTrace()
        }
    }

    private fun uploadFile(user: FirebaseUser) {
        if (direccionImagenPerfil != null) {
            showProgressBar()
            storageReference.child("imagesProfile/" + user.email).putFile(direccionImagenPerfil!!)
                .addOnSuccessListener {
                    hideProgressBar()
                    Utils.displayLongToast("Éxito al subir imagen de perfil", this)
                }.addOnFailureListener { e ->
                    hideProgressBar()
                    Utils.displayLongToast("Error al subir imagen de perfil. " + e.message, this)
                    launchRegistro(e)
                }
        }
    }

    private fun launchRegistro(e: Exception) {
        Utils.displayLongToast(
            "Error al registrarte, " + nombre + ". " + e.message.toString(),
            this
        )
        val intent = Intent(this, RegistroActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun launchLogin() {
        Utils.displayShortToast("Éxito al crear tu cuenta, $nombre", this)
        val intent = Intent(this, InicioSesionActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener(this) { Task ->
            if (Task.isSuccessful) {
                Utils.displayShortToast("Se ha enviado un correo de verificación a " + email, this)

            } else {
                Utils.displayShortToast(
                    "Error al enviar un correo de verificación a " + email,
                    this
                )
            }
        }
    }

    /**
     * ProgressDialog is deprecated in API level 26, but ProgressBar comes to the rescue with
     * a very pretty progress bar (woo!).
     *
     * This locks all interaction on the window and displays the progress bar.
     */
    private fun showProgressBar() {
        lockUserInteraction()
        binding.progressbar.visibility = View.VISIBLE
    }

    /**
     * Once all progress is done and ready we can hide the progress bar and give back control
     * to the user.
     */
    private fun hideProgressBar() {
        restoreUserInteraction()
        binding.progressbar.visibility = View.GONE
    }

    /**
     * While uploading the profile picture we don't want the user meddling around with
     * other stuff and risk breaking the app, so we block any interaction on the window
     * during the process.
     */
    private fun lockUserInteraction() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    /**
     * Once the profile picture is uploaded (or failed to be uploaded), the user can
     * now regain control of the window and find another way to break the app.
     */
    private fun restoreUserInteraction() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}