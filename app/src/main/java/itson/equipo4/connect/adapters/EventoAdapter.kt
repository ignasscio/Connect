package itson.equipo4.connect.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import itson.equipo4.connect.R
import itson.equipo4.connect.objetosnegocio.Evento
import java.text.SimpleDateFormat

class EventoAdapter (val context: Context, val eventosList: ArrayList<Evento>): RecyclerView.Adapter<EventoAdapter.EventoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.evento_view, parent, false)
        return EventoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val currentEvent = eventosList[position]
        holder.titulo.text = currentEvent.nombre
        holder.descripcion.text = currentEvent.descripcion

        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val sdfHora = SimpleDateFormat("HH:mm")
        val eventFecha: String = sdf.format(currentEvent.fecha)
        val eventHora: String = sdfHora.format(currentEvent.fecha)

        holder.fecha.text = eventFecha
        holder.hora.text = eventHora

//        holder.itemView.setOnClickListener {
//            val intent = Intent(context, DocumentoActivity::class.java)
//
//            intent.putExtra("name", currentEvent.nombre)
//
//            context.startActivity(intent)
//        }
    }

    override fun getItemCount(): Int {
        return eventosList.size
    }

    class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.evento_titulo)
        val descripcion: TextView = itemView.findViewById(R.id.evento_descripcion)
        val fecha: TextView = itemView.findViewById(R.id.evento_fecha)
        val hora: TextView = itemView.findViewById(R.id.evento_hora)
    }
}