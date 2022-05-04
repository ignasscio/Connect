package itson.equipo4.connect.activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import itson.equipo4.connect.Utils
import itson.equipo4.connect.databinding.ActivityPerfilBinding
import itson.equipo4.connect.objetosnegocio.Usuario
import java.io.File
import java.io.IOException
import java.util.regex.Pattern

class PerfilActivity : AppCompatActivity() {

    lateinit var binding: ActivityPerfilBinding
    private var direccionImagenPerfil: Uri? = null
    private val storageReference = FirebaseStorage.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    private val selectImageFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            // Handle the returned Uri
            handleImageChooserResult(uri)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val extras = intent.extras
        val user = extras?.get("user") as FirebaseUser
        val store = FirebaseStorage.getInstance().reference.child("imagesProfile/" + user.uid)
        val localFile = File.createTempFile("tempImage", "jpg")
        store.getFile(localFile).addOnSuccessListener {
            val fileImage = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.perfilFotoPerfil.setImageBitmap(fileImage)
        }.addOnFailureListener { e ->
            Utils.displayShortToast("Error: " + e.message, baseContext)
        }


        val db = FirebaseFirestore.getInstance()
        db.collection("usuarios").document(user.uid).get().addOnSuccessListener { document ->
            val usuario = document.toObject(Usuario::class.java)
            binding.perfilEtNombreUsuario.setText(usuario?.nombre)
            binding.perfilEtCorreoElectronico.setText(usuario?.email)
        }

        binding.perfilIvCambiarFotoPerfil.setOnClickListener {
            showFileChooser()
        }

        binding.perfilBtnGuardar.setOnClickListener {
            updateUserProfile(db, user)
        }

    }

    /**
     * In case the user wants to modify their profile we have to stop them from
     * clearing out their username, email and password...
     * <blank>@<blank>.<blank> it not a valid email address and it will never be!
     * Plus we heavily discourage having NO password for your account.
     *
     * This gets all the info from the text fields in the activity, as well as the user's
     * profile picture, and updates everything in the database.
     *
     * However, from what I can imagine looking at this code, I believe there might be an
     * oversight on the OnSuccess Event, where there doesn't seem to be any proper validation
     * checks for the email and password... or that might just be my imagination...
     *
     * The one writing this documentation (yes, I'm talking about me) has clearly not
     * fully studied this code YET, but still I can get a gut feeling about this part of the code,
     * so I'll put a SAFETY PIG for the meantime, until I can confirm my beliefs and fix this.
     *
     *   _
     *   _._ _..._ .-',     _.._(`))
     *   '-. `     '  /-._.-'    ',/
     *   )         \            '.
     *   / _    _    |             \
     *   |  a    a    /              |
     *   \   .-.                     ;
     *   '-('' ).-'       ,'       ;
     *   '-;           |      .'
     *   \           \    /
     *   | 7  .__  _.-\   \
     *   | |  |  ``/  /`  /
     *   /,_|  |   /,_/   /
     *   /,_/      '`-'
     */
    private fun updateUserProfile(db: FirebaseFirestore, user: FirebaseUser) {
        val nombre = binding.perfilEtNombreUsuario.text.toString().trim()
        val password = binding.perfilEtCambiarContrasena.text.toString().trim()
        val email = binding.perfilEtCorreoElectronico.text.toString().trim()

        if (nombre.isEmpty() || email.isEmpty()) {
            Utils.displayShortToast("Nombre e email no pueden quedar vacíos", baseContext)
            return
        }

        if (!verificarContrasena(password)) {
            Utils.displayShortToast("La contraseña es invalida", baseContext)
            return
        }

        db.collection("usuarios").document(user.uid).set(
            Usuario(email, password, nombre, "imagesProfile/" + user.uid)
        ).addOnSuccessListener {
            user.updateEmail(email)

            if (password.isNotEmpty()) {
                user.updatePassword(password)
            }

            uploadFile(user)

            Utils.displayShortToast("Éxito al actualizar datos", baseContext)
        }.addOnFailureListener { e ->
            Utils.displayShortToast("Error al actualizar datos: " + e.message, baseContext)
        }
    }

    /**
     * It's important to validate the password text field, which is what this does.
     *
     * This checks if the password is valid for Firebase:
     *
     * - Minimum 6 characters
     * - Any character allowed
     * - At least one uppercase character
     */
    private fun verificarContrasena(pass: String): Boolean {
        val patternString =
            "(?=.*[0-9]+)(?=.*[a-z]+)(?=.*[A-Z]+).{6,}"
        val pattern = Pattern.compile(patternString)
        val matcher = pattern.matcher(pass)

        return matcher.matches()
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

            binding.perfilFotoPerfil.setImageBitmap(bitmap)
        } catch (e: IOException) {
            Utils.displayLongToast("Error: " + e.message, this)
            e.printStackTrace()
        }
    }

    /**
     * Here's an slightly heavy method. This attempts to upload the user's profile
     * picture to the database, displaying a pretty progress bar on top.
     */
    private fun uploadFile(user: FirebaseUser) {
        if (direccionImagenPerfil != null) {
            showProgressBar()
            storageReference.child("imagesProfile/" + user.uid).putFile(direccionImagenPerfil!!)
                .addOnSuccessListener {
                    hideProgressBar()
                    Utils.displayLongToast("Éxito al subir imagen de perfil", this)
                }.addOnFailureListener { e ->
                    hideProgressBar()
                    Utils.displayLongToast("Error al subir imagen de perfil. " + e.message, this)
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