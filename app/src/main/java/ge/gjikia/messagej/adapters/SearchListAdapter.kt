package ge.gjikia.messagej.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import ge.gjikia.messagej.Account
import ge.gjikia.messagej.FragmentActionListener
import ge.gjikia.messagej.R

class SearchListAdapter(private val dataSet: ArrayList<Account>,private val listener: FragmentActionListener) :
    RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView : ImageView?
        var nicknameTextView:TextView?
        var whatIDo : TextView?
        init {
            imageView = view.findViewById(R.id.image_view)
            nicknameTextView = view.findViewById(R.id.nick_name_text_view)
            whatIDo = view.findViewById(R.id.what_i_do_text_view)
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.search_list_item, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("ResourceType")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.nicknameTextView?.setText(dataSet[position].nickName)
        viewHolder.whatIDo?.setText(dataSet[position].whatIDo)
        val storage = Firebase.storage
        val id = dataSet[position].id
        val fileRef = storage.getReference("images").child("$id")
        fileRef.downloadUrl.addOnSuccessListener {
            Picasso.get().load(it).into(viewHolder.imageView)
        }.addOnFailureListener{
            viewHolder.imageView?.setBackgroundResource(R.drawable.avatar_image_placeholder)
        }


        viewHolder.itemView.setOnClickListener{
            println("########### $id ##############")
            listener.openChatPage(id)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
