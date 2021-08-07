package ge.gjikia.messagej

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SingUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SingUpFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var signInBtn:Button
    lateinit var signUpBtn:Button
    lateinit var lister: FragmentActionListener;
    lateinit var nickname:EditText;
    lateinit var password:EditText;
    lateinit var whatIDo :EditText;

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
        return inflater.inflate(R.layout.fragment_sing_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpBtn = view.findViewById(R.id.sing_up_btn);
        nickname = view.findViewById(R.id.nick_name_edittext_id);
        password = view.findViewById(R.id.password_edittext_id);
        whatIDo = view.findViewById(R.id.what_i_do_edittext_id);
        setListeners()
    }



    private  fun  setListeners(){
        signUpBtn.setOnClickListener {
            val nickname = nickname.text.toString()
            val password  = password.text.toString()
            val whatIDo = whatIDo.text.toString()
            if(nickname == "")Toast.makeText(this.context,"nickname  required!",Toast.LENGTH_LONG).show();
            if(password == "")Toast.makeText(this.context,"Password  required!",Toast.LENGTH_LONG).show();
            if(whatIDo == "")Toast.makeText(this.context,"What i do  required!",Toast.LENGTH_LONG).show();
            val key = Database.createAccount(Account(nickname,password,whatIDo));
            key?.let {
                lister.signUp(it);
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SingUpFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            SingUpFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}