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
import itson.equipo4.connect.objetosnegocio.Documento

class DocumentoAdapter(val context: Context, val documentsList: ArrayList<Documento>) :
    RecyclerView.Adapter<DocumentoAdapter.DocumentoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentoViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.accesorapido_view, parent, false)
        return DocumentoViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentoViewHolder, position: Int) {
        val currentUser = documentsList[position]
        holder.content.text = currentUser.nombre

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DocumentoActivity::class.java)

            intent.putExtra("name", currentUser.nombre)
            intent.putExtra("uid", currentUser.uid)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return documentsList.size
    }

    class DocumentoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content = itemView.findViewById<TextView>(R.id.documento_tv_contenido)
    }
}