package itson.equipo4.connect.objetosnegocio

import android.net.Uri

data class Usuario(
    var email:String? = null,
    var password:String? = null,
    var nombre:String? = null,
    var fotoPerfil: Uri? = null
)