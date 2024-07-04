package cirqle.com.Chat.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.Chat.Models.MessageResponseModel
import cirqle.com.R

class MessageAdapter(private val messages: List<MessageResponseModel>, private val userId:String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SENDER = 1
        private const val VIEW_TYPE_RECEIVER = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SENDER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_sender_message_rv, parent, false)
                SenderViewHolder(view)
            }
            VIEW_TYPE_RECEIVER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_receiver_message_rv, parent, false)
                ReceiverViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder.itemViewType) {
            VIEW_TYPE_SENDER -> {
                val senderHolder = holder as SenderViewHolder
                senderHolder.textViewMessage.text = message.content
            }
            VIEW_TYPE_RECEIVER -> {
                val receiverHolder = holder as ReceiverViewHolder
                receiverHolder.textViewMessage.text = message.content
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.sender._id == userId) {
            VIEW_TYPE_SENDER
        } else {
            VIEW_TYPE_RECEIVER
        }
    }

    class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewMessage: TextView = itemView.findViewById(R.id.sender_tv)
    }

    class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewMessage: TextView = itemView.findViewById(R.id.receiver_tv)
    }
}
