package itson.equipo4.connect.objetosnegocio

class Usuario {
    var email: String? = null
    var password: String? = null
    var nombre: String? = null
    var imgPerfil: String = ""
    var uid: String? = null
    //var grupos:List<Grupo>?

    constructor() {}

    constructor(
        uid: String?,
        nombre: String?,
        email: String?,
        password: String?,
        imgPerfil: String
    ) {
        this.uid = uid
        this.nombre = nombre
        this.email = email
        this.password = password
        this.imgPerfil = imgPerfil
    }
}