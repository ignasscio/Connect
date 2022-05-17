package itson.equipo4.connect.objetosnegocio

class Chat {
    var uid: String? = null
    var nombre: String? = null
    var lastMessage: String? = null

    constructor() {}

    constructor(uid: String?, nombre: String?, lastMessage: String?) {
        this.uid = uid
        this.nombre = nombre
        this.lastMessage = lastMessage
    }
}