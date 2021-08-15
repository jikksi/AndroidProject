package ge.gjikia.messagej

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.gjikia.messagej.adapters.ChatListAdapter
import ge.gjikia.messagej.adapters.HomeListAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    lateinit var lister: FragmentActionListener;
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var floatingActionButton: FloatingActionButton
    lateinit var recycler: RecyclerView
    lateinit var list : ArrayList<Message>
    lateinit var signedKey : String
    lateinit var sharedPref: SharedPreferences
    lateinit var adapter: HomeListAdapter
    lateinit var searchInput : TextInputEditText;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        sharedPref = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        signedKey = sharedPref.getString("id",null).toString()
        val chatHistory : Query = Firebase.database.getReference("chats")
        chatHistory.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    val message: Message = snapshot.getValue(Message::class.java)!!
                    if(message.recipient_id == signedKey || message.sender_id == signedKey){
                        println("############# add ##############")
                        addItem(message)
                        list.sort();
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun addItem(current: Message){
        list.forEachIndexed { index, message ->
            if(message.sender_id == current.sender_id && message.recipient_id == current.recipient_id ||
                message.sender_id == current.recipient_id && message.recipient_id == current.sender_id){
                if(current.compareTo(message) <= 0){
                    list.set(index,current)
                }
                return
            }
        }
        list.add(current)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationView = view.findViewById(R.id.navigation_view)
        bottomNavigationView.background = null
        bottomNavigationView.selectedItemId = R.id.home_page
        floatingActionButton = view.findViewById(R.id.fab_button)
        setNavigationItemListeners()

        floatingActionButton.setOnClickListener{
            lister.openSearchPage()
        }
        list = ArrayList()
        recycler = view.findViewById(R.id.my_list)
        recycler.layoutManager = LinearLayoutManager(this.context)
        adapter = HomeListAdapter(list,signedKey,lister)
        recycler.adapter = adapter


        searchInput = view.findViewById(R.id.search_edit_text)

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                list.clear()
                val chatHistory : Query = Firebase.database.getReference("chats")
                chatHistory.addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        if (snapshot.exists()) {
                            val message: Message = snapshot.getValue(Message::class.java)!!
                            if(message.recipient_id == signedKey || message.sender_id == signedKey){
                                println("############# add ##############")
                                val validKey =  if (signedKey == message.recipient_id) message.sender_id else message.recipient_id
                                val query: Query = Firebase.database.getReference("accounts")
                                    .orderByKey()
                                    .equalTo(validKey);
                                println("####################### $validKey #########################")
                                query.addListenerForSingleValueEvent(object : ValueEventListener {
                                    @SuppressLint("NotifyDataSetChanged")
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            println("########## snapshot.exists() #########")
                                            for (snapshot in snapshot.getChildren()) {
                                                val account: Account = snapshot.getValue(Account::class.java)!!
                                                if(account.nickName?.contains(text) == true){
                                                    addItem(message)
                                                    list.sort();
                                                }
                                                println("################## {${account.nickName}}")
                                            }
                                        }
                                        adapter.notifyDataSetChanged()
                                    }

                                    override fun onCancelled(error: DatabaseError) {

                                    }
                                })
                            }

                        }
                    }

                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                        TODO("Not yet implemented")
                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        TODO("Not yet implemented")
                    }

                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                        TODO("Not yet implemented")
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }

        })
    }


    private fun setNavigationItemListeners(){
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.setting_page -> lister.openSettingPage()
            }
            true
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}