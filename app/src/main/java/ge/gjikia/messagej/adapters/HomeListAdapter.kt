package ge.gjikia.messagej.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.gjikia.messagej.Account
import ge.gjikia.messagej.FragmentActionListener
import ge.gjikia.messagej.Message
import ge.gjikia.messagej.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class HomeListAdapter(private val dataSet: ArrayList<Message>, private val signedKey : String,private val listener: FragmentActionListener) :
    RecyclerView.Adapter<HomeListAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView:ImageView?
        var nick_name_text_view:TextView?
        var time_text_view:TextView?
        var message_text_view:TextView?
        init {
            imageView = view.findViewById(R.id.icon_image_view)
            nick_name_text_view = view.findViewById(R.id.nick_name_text_view)
            time_text_view = view.findViewById(R.id.time_passed_text_view)
            message_text_view = view.findViewById(R.id.message_text_view)
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.home_list_item, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("ResourceType")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val message = dataSet[position];
        viewHolder.message_text_view?.text = message.message
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val dateTime = LocalDateTime.parse(message.date, formatter)
        val currentDateTime = LocalDateTime.now()
        val minutes = ChronoUnit.MINUTES.between(dateTime,currentDateTime)
        val days = ChronoUnit.DAYS.between(dateTime,currentDateTime)
        val hours = ChronoUnit.HOURS.between(dateTime,currentDateTime)
        if(minutes < 60){
            viewHolder.time_text_view?.text = "$minutes min"
        }else if(hours < 24){
            viewHolder.time_text_view?.text = "$hours hour"
        }else{
                viewHolder.time_text_view?.text = dateTime.format(DateTimeFormatter.ofPattern("d MMM"))
        }
        setNickname(viewHolder,message);

        val validKey =  if (signedKey == message.recipient_id) message.sender_id else message.recipient_id
        viewHolder.itemView.setOnClickListener{
            listener.openChatPage(validKey)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }


    private fun setNickname(viewHolder:ViewHolder,message: Message){
        val validKey =  if (signedKey == message.recipient_id) message.sender_id else message.recipient_id
        val query: Query = Firebase.database.getReference("accounts")
            .orderByKey()
            .equalTo(validKey);
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    println("########## snapshot.exists() #########")
                    for (snapshot in snapshot.getChildren()) {
                        val account: Account = snapshot.getValue(Account::class.java)!!
                        viewHolder.nick_name_text_view?.text = account.nickName
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}