package ge.gjikia.messagej
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(),FragmentActionListener{

    lateinit var singInFragment: SingInFragment
    lateinit var singUpFragment: SingUpFragment
    lateinit var fragmentManager: FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction
    lateinit var accountId : String
    lateinit var sharedPref:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPref = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE)

        if(isLoggedIn()){
           logIn()
        }else{
            init()
            changeFragment(singInFragment)
        }
    }

    private  fun isLoggedIn():Boolean{

        val id = sharedPref.getString("id",null);
        println("####### $id ###########}")
        id?.let {
            accountId = it
            println("####### $accountId ###########}")
        }
        return id != null;
    }


    private  fun init(){
        singInFragment = SingInFragment.newInstance()
        singInFragment.lister = this
        singUpFragment = SingUpFragment.newInstance()
        singUpFragment.lister = this
        fragmentManager = supportFragmentManager
    }

    private fun changeFragment(fragment:Fragment){
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container,fragment)
        fragmentTransaction.commit();
    }

    override fun openSignUpPage() {
        println("####### openSignUpPage #######")
        changeFragment(singUpFragment)
    }

    override fun openSignInPage() {
        println("####### openSignInPage #######")
        changeFragment(singInFragment)
    }

    override fun signIn(key: String?) {
        saveKey(key);
        logIn();
    }

    override fun signUp(key : String?) {
        println("####### signUp ##########")
        saveKey(key);
        logIn()
    }

    override fun openSettingPage() {
        TODO("Not yet implemented")
    }

    override fun openHomePage() {
        TODO("Not yet implemented")
    }

    override fun openChatPage() {
        TODO("Not yet implemented")
    }

    override fun openSearchPage() {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }

    private  fun  logIn(){
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent)
    }

    private fun  saveKey(key : String?){
        key?.let {
            with(sharedPref.edit()){
                putString("id",it)
                apply()
            }
        }
    }

}