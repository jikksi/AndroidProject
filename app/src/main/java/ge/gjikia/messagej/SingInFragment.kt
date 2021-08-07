package ge.gjikia.messagej

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SingInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SingInFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var signInBtn: Button
    lateinit var signUpBtn: Button
    lateinit var lister: FragmentActionListener;
    lateinit var nickName:EditText;
    lateinit var password:EditText;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sing_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpBtn = view.findViewById(R.id.sing_up_page_btn);
        signInBtn = view.findViewById(R.id.sing_in_btn);
        nickName = view.findViewById(R.id.nick_name_edittext_id);
        password = view.findViewById(R.id.password_edittext_id);
        setListeners()
    }
    private  fun  setListeners(){
        signInBtn.setOnClickListener {
            println("########## here ##########")
            val nick = nickName.text.toString();
            val password = password.text.toString();
            if(nick == ""){
                val toast = Toast.makeText(this.context,"nickname required!",Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER,0,0)
                toast.show();
                return@setOnClickListener
            };
            if(password == ""){
                val toast = Toast.makeText(this.context,"Password required!",Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER,0,0)
                toast.show()
                return@setOnClickListener
            };
            signIn(nick,password);
        }
        signUpBtn.setOnClickListener {
            lister.openSignUpPage();
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SingInFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            SingInFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private fun signIn(nickName:String,password:String){
        val accountsRef = Firebase.database.getReference("accounts");
        accountsRef.orderByKey().addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val key = snapshot.key
                println("######## $key ##########")
                val nick: String = snapshot.child("nickName").getValue() as String
                val pass: String = snapshot.child("password").getValue() as String

                if(nick == nickName && pass == password){
                    activity?.let{
                        it.runOnUiThread {
                            lister.signIn(key)
                        }
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
}