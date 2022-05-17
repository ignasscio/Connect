package itson.equipo4.connect.objetosnegocio

import java.util.*

class Evento {
    var nombre: String? = null
    var descripcion: String = ""
    var fecha: Date? = null
    var color: String? = null

    constructor() {}

    constructor(nombre: String?, descripcion: String, fecha: Date?, color: String?) {
        this.nombre = nombre
        this.descripcion = descripcion
        this.fecha = fecha
        this.color = color
    }
}