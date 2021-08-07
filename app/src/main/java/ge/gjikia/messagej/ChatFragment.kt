package ge.gjikia.messagej

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.gjikia.messagej.adapters.ChatListAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : Fragment() {
    lateinit var lister: FragmentActionListener;
    private var key: String? = null
    lateinit var bottomAppBar: BottomAppBar
    lateinit var materialToolbar: MaterialToolbar;
    lateinit var recycler : RecyclerView;
    lateinit var list : ArrayList<Message>
    lateinit var adapter : ChatListAdapter
    lateinit var sharedPref: SharedPreferences
    lateinit var signedKey : String
    lateinit var textInputEditText: TextInputEditText;
    lateinit var textInputLayout: TextInputLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            key = it.getString(ARG_PARAM1)
        }

        sharedPref = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        signedKey = sharedPref.getString("id",null).toString()

        val query: Query = Firebase.database.getReference("accounts")
            .orderByKey()
            .equalTo(key);

        println("####################### $key #########################")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    println("########## snapshot.exists() #########")
                    for (snapshot in snapshot.getChildren()) {
                        val account: Account = snapshot.getValue(Account::class.java)!!
                        materialToolbar.title = account.nickName
                        materialToolbar.subtitle = account.whatIDo

                        println("################## {${account.nickName}}")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        val chatHistory : Query  = Firebase.database.getReference("chats");

//        chatHistory.addChildEventListener(object : ChildEventListener{
//            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                if (snapshot.exists()) {
//                    println("########## chatHistory snapshot.exists() #########")
//                    for (snapshot in snapshot.getChildren()) {
////                        val message: Message = snapshot.getValue(Message::class.java)!!
//                        val message = snapshot.child("message")
//
//                        println("############ ${snapshot.toString()} ###########")
//                    }
//                }
//            }
//
//            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onChildRemoved(snapshot: DataSnapshot) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })

        chatHistory.addListenerForSingleValueEvent(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    println("########## snapshot.exists() #########")
                    for (snapshot in snapshot.getChildren()) {
                        val message: Message = snapshot.getValue(Message::class.java)!!
                        if((message.recipient_id == key && message.sender_id == signedKey)|| (message.recipient_id == signedKey && message.sender_id == key)){
                            println("############# add ##############")
                            list.add(message)
                            list.sortedByDescending {
                                it.date
                            }
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomAppBar = view.findViewById(R.id.bottomAppbar)
        bottomAppBar.background = null

        materialToolbar  = view.findViewById(R.id.toolbar)
        materialToolbar.setNavigationOnClickListener{
            lister.openHomePage()
        }

        list = ArrayList()
        recycler = view.findViewById(R.id.my_list)
        recycler.layoutManager = LinearLayoutManager(this.context)
        adapter = ChatListAdapter(list,signedKey)
        recycler.adapter = adapter

        textInputEditText = view.findViewById(R.id.search_edit_text)
        textInputLayout = view.findViewById(R.id.text_input_layout)
        textInputLayout.setEndIconOnClickListener{
            val currentDateTime = LocalDateTime.now()
            val text = textInputEditText.text
            Database.createChat(Message(currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm")),text.toString(),key,signedKey))
            textInputEditText.setText("")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(key : String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, key)
                }
            }
    }
}