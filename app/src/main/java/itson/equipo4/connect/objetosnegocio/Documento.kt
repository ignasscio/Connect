package itson.equipo4.connect.objetosnegocio

class Documento {
    var uid: String? = null
    var usuario: String? = null
    var nombre: String? = null
    var ruta: String = ""

    constructor() {}

    constructor(uid: String?, usuario: String?, nombre: String?, ruta: String) {
        this.uid = uid
        this.nombre = nombre
        this.usuario = usuario
        this.ruta = ruta
    }
}