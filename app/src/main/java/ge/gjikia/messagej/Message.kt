package ge.gjikia.messagej

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class Message(val date:String? = null,val message:String? = null,val recipient_id : String? = null,val sender_id : String? = null)
