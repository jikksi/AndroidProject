package ge.gjikia.messagej.adapters
import android.annotation.SuppressLint
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import ge.gjikia.messagej.Account
import ge.gjikia.messagej.FragmentActionListener
import ge.gjikia.messagej.Message
import ge.gjikia.messagej.R
import org.w3c.dom.Text

class ChatListAdapter(private val dataSet: ArrayList<Message>,private val signedKey : String) :
    RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var sendersView : View?
        var reciversView:View?
        var recieversMessageTextView : TextInputEditText?
        var recieversDateTextView : TextView?

        var senderMessageTextView : TextInputEditText?

        var sendersDateTextView : TextView?
        init {
            sendersView = view.findViewById(R.id.senders_view)
            reciversView = view.findViewById(R.id.recievers_view)
            recieversMessageTextView = view.findViewById(R.id.reciebers_message_text_view)
            senderMessageTextView = view.findViewById(R.id.senders_search_edit_text)
            recieversDateTextView = view.findViewById(R.id.recievers_date_text_view)
            sendersDateTextView = view.findViewById(R.id.senders_date_text_view)
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.chat_list_item, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("ResourceType")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val message = dataSet[position];
        if(message.recipient_id == signedKey){
            viewHolder.reciversView?.visibility = View.VISIBLE
            viewHolder.sendersView?.visibility = View.GONE
            viewHolder.recieversMessageTextView?.setText(message.message)
            viewHolder.recieversDateTextView?.setText(message.date?.split(" ")?.get(1))
        }else{
            viewHolder.sendersView?.visibility = View.VISIBLE
            viewHolder.reciversView?.visibility = View.GONE
            viewHolder.senderMessageTextView?.setText(message.message)
            viewHolder.sendersDateTextView?.setText(message.date?.split(" ")?.get(1))
            println("############ {${message.message}} #########")
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}