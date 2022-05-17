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
import itson.equipo4.connect.objetosnegocio.Chat

class ChatAdapter(val context: Context, val chatList: ArrayList<Chat>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.conversation_view, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]
        holder.friendName.text = chat.nombre
        holder.lastMessage.text = chat.lastMessage

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)

            intent.putExtra("name", chat.nombre)
            intent.putExtra("uid", chat.uid)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendName = itemView.findViewById<TextView>(R.id.conversation_friendName)!!
        val lastMessage = itemView.findViewById<TextView>(R.id.conversation_friendLastMessage)!!
    }
}