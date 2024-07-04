package cirqle.com.SeniorCirqle.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.R
import cirqle.com.SeniorCirqle.Models.AddCommentResponseModel
import cirqle.com.customLayout.CircularImageView
import com.squareup.picasso.Picasso

class CommentAdapter(private var context: Context, private var list:List<AddCommentResponseModel> ): RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_comment_rv,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name_tv.text=list[position].user.name.toString()
        holder.text_tv.text=list[position].comment.toString()
        if(!list[position].user.profilePic.isNullOrBlank()) {
            Picasso.get().load(list[position].user.profilePic.toString()).into(holder.profile_pic)
        }
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val profile_pic:CircularImageView=itemView.findViewById(R.id.profile_pic)
        val name_tv:TextView=itemView.findViewById(R.id.name_tv)
        val text_tv:TextView=itemView.findViewById(R.id.text_tv)

    }
}