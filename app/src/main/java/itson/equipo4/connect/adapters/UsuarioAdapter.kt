package itson.equipo4.connect.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import itson.equipo4.connect.R
import itson.equipo4.connect.activities.ChatActivity
import itson.equipo4.connect.objetosnegocio.Usuario

class UsuarioAdapter(val context: Context, val userList: ArrayList<Usuario>) :
    RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.amigo_view, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.textName.text = currentUser.nombre

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)

            intent.putExtra("name", currentUser.nombre)
            intent.putExtra("uid", currentUser.uid)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName = itemView.findViewById<TextView>(R.id.amigo_nombreAmigo)
    }
}