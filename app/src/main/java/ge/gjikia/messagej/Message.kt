package ge.gjikia.messagej

import com.google.firebase.database.IgnoreExtraProperties
import java.text.SimpleDateFormat
import java.util.*


@IgnoreExtraProperties
data class Message(val date:String? = null,val message:String? = null,val recipient_id : String? = null,val sender_id : String? = null):Comparable<Message> {
    override fun compareTo(other: Message): Int {
        val format : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val currentDate = format.parse(date)
        val otherDate =format.parse(other.date)
        if(currentDate.after(otherDate))return -1;
        if(currentDate.before(otherDate))return 1;
        return  0
    }
}
