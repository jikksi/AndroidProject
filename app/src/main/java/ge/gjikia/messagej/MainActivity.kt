package ge.gjikia.messagej

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity(),FragmentActionListener{

    lateinit var singInFragment: SingInFragment
    lateinit var singUpFragment: SingUpFragment
    lateinit var fragmentManager: FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(isLoggedIn()){
            val intent = Intent(this, MainActivity2::class.java)
            // start your next activity
            startActivity(intent)
        }else{
            init()
            changeFragment(singInFragment)
        }
    }

    private  fun isLoggedIn():Boolean{
        return true;
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

    override fun signIn() {
        println("####### SignIn ##########")
    }

    override fun signUp() {
        println("####### signUp ##########")
    }

}