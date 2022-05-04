package itson.equipo4.connect.objetosnegocio

data class Usuario(
    var email: String? = null,
    var password: String? = null,
    var nombre: String? = null,
    var imgPerfil: String = "",
    //var grupos:List<Grupo>?
)