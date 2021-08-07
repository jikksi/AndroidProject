package ge.gjikia.messagej

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.gjikia.messagej.adapters.SearchListAdapter
import com.google.firebase.database.DataSnapshot




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    lateinit var lister: FragmentActionListener
    lateinit var imageButton: ImageButton
    lateinit var recycler:RecyclerView
    lateinit var list : ArrayList<Account>
    lateinit var  adapter : SearchListAdapter
    lateinit var searchInput : TextInputEditText;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setData()
    }


    private  fun setData(){
        val accountsRef = Firebase.database.getReference("accounts");
        accountsRef.orderByKey().addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val key = snapshot.key
                println("######## $key ##########")
                val nick: String = snapshot.child("nickName").getValue() as String
                val pass: String = snapshot.child("password").getValue() as String
                val whatIDo: String = snapshot.child("whatIDo").getValue() as String
                activity?.runOnUiThread{
                    list.add(Account(nick,pass,whatIDo,key))
                    adapter.notifyDataSetChanged();
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageButton =  view.findViewById(R.id.back_to_home_btn)
        recycler = view.findViewById(R.id.my_list)
        recycler.layoutManager = LinearLayoutManager(this.context)
        list = ArrayList()
        adapter = SearchListAdapter(list,lister)
        recycler.adapter = adapter;
        imageButton.setOnClickListener {
            lister.openHomePage()
        }

        searchInput = view.findViewById(R.id.search_edit_text)

        searchInput.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()

                val query: Query = Firebase.database.getReference("accounts")
                    .orderByChild("nickName")
                    .startAt(text)
                    .endAt("$text\uf8ff");

                query.addListenerForSingleValueEvent(object : ValueEventListener{
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        list.clear()
                        println("##### cleared list ######")
                        if (snapshot.exists()) {
                            println("########## snapshot.exists() #########")
                            for (snapshot in snapshot.getChildren()) {
                                val artist: Account = snapshot.getValue(Account::class.java)!!
                                println("############ ${artist.nickName} ###########")
                                list.add(artist)
                            }
                        }
                        adapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
                println("############### $text ###########")
            }

        })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            SearchFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}