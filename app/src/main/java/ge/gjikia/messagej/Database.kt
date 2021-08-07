package ge.gjikia.messagej

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class Database {
    companion object{
        private val accountsRef = Firebase.database.getReference("accounts");
        private val chatsRef = Firebase.database.getReference("chats");
        fun createAccount(account: Account):String?{
            accountsRef.push().key?.let {
                accountsRef.child(it).setValue(account)
                return  it;
            }
            return null;
        }


        fun createChat(message: Message){
            chatsRef.push().key?.let {
                chatsRef.child(it).setValue(message)
            }
        }


    }
}