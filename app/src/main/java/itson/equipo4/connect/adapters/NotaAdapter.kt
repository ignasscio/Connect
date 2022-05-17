package itson.equipo4.connect.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import itson.equipo4.connect.R
import itson.equipo4.connect.activities.DocumentoActivity
import itson.equipo4.connect.objetosnegocio.Nota

class NotaAdapter(val context: Context, val notasList: ArrayList<Nota>) :
    RecyclerView.Adapter<NotaAdapter.NotaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.accesorapido_view, parent, false)
        return NotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val currentUser = notasList[position]
        holder.content.text = currentUser.nombre

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DocumentoActivity::class.java)

            intent.putExtra("name", currentUser.nombre)
            intent.putExtra("uid", currentUser.uid)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return notasList.size
    }

    class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content = itemView.findViewById<TextView>(R.id.documento_tv_contenido)
    }
}