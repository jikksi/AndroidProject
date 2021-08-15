package ge.gjikia.messagej

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {


    val PICK_IMAGE_REQUESR = 1
    lateinit var imageUri : Uri
    lateinit var storage:FirebaseStorage
    lateinit var lister: FragmentActionListener;
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var signOut:Button;
    lateinit var update : Button;
    lateinit var sharedPref:SharedPreferences
    lateinit var key : String;
    lateinit var account: Account;
    lateinit var nickname:  String;
    lateinit var whatIdDo : String
    lateinit var nickEdit : EditText;
    lateinit var whatIDoEdit: EditText;
    lateinit var floatingActionButton: FloatingActionButton
    lateinit var imageView:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storage = Firebase.storage
        sharedPref = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        key = sharedPref.getString("id",null).toString()
        println("############ $key ##############")
        key?.let {
            val account = Firebase.database.getReference("accounts").child(key);
            account.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nick = snapshot.child("nickName").getValue() as String
                    val whatIDo = snapshot.child("whatIDo").getValue() as String
                    activity?.runOnUiThread{
                        nickEdit.setText(nick.toString());
                        whatIDoEdit.setText(whatIDo.toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
        val fileRef = storage.getReference("images").child("$key")
        fileRef.downloadUrl.addOnSuccessListener {
            Picasso.get().load(it).into(imageView)
        }.addOnFailureListener{
            imageView?.setBackgroundResource(R.drawable.avatar_image_placeholder)
        }

    }

    private fun  setNickname(){

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationView = view.findViewById(R.id.navigation_view)
        bottomNavigationView.background = null
        bottomNavigationView.selectedItemId = R.id.setting_page
        setNavigationItemListeners()

        nickEdit = view.findViewById(R.id.nick_name_edittext_id)
        whatIDoEdit = view.findViewById(R.id.what_i_do_edittext_id);

        signOut = view.findViewById(R.id.sign_out_btn);
        signOut.setOnClickListener {
            lister.signOut()
        }

        update = view.findViewById(R.id.update_btn);
        update.setOnClickListener {
            val account = Firebase.database.getReference("accounts").child(key);
            account.child("nickName").setValue(nickEdit.text.toString())
            account.child("whatIDo").setValue(whatIDoEdit.text.toString())


            if(imageUri != null){
                println("####### update ############")
                val mime = getFileExtension(imageUri)
                val fileRef = storage.getReference("images").child("$key")
                fileRef.putFile(imageUri).addOnSuccessListener {
                    println("####### uploaded ############")
                }.addOnFailureListener {

                }
            }

        }

        floatingActionButton = view.findViewById(R.id.fab_button)
        setNavigationItemListeners()

        floatingActionButton.setOnClickListener{
            lister.openSearchPage()
        }

        imageView = view.findViewById(R.id.avatar_icon_id)

        imageView.setOnClickListener{
            openFileChooser()
        }
    }

    private fun openFileChooser(){
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent,PICK_IMAGE_REQUESR)
    }

    private fun getFileExtension(uri: Uri):String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity?.contentResolver?.getType(uri))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUESR &&
            resultCode == RESULT_OK &&
            data != null && data.data != null ){
            imageUri = data.data as Uri
            Picasso.get().load(imageUri).fit().into(imageView)
        }
    }

    private fun setNavigationItemListeners(){
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.home_page -> lister.openHomePage()
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
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}