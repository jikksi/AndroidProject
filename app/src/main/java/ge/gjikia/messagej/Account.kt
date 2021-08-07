package ge.gjikia.messagej

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Account(val nickName:String? = null,val password:String? = null,val whatIDo : String? = null,val id : String? = null)
