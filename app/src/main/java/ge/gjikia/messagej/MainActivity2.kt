package ge.gjikia.messagej

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity2 : AppCompatActivity(),FragmentActionListener {
    lateinit var  homeFragment: HomeFragment
    lateinit var  chatFragment: ChatFragment
    lateinit var  profileFragment: ProfileFragment
    lateinit var  searchFragment: SearchFragment
    lateinit var fragmentManager: FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction
    lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        init()
        var fragment = HomeFragment.newInstance()
        fragment.lister = this
        changeFragment(fragment)
    }

    private  fun init(){
        fragmentManager = supportFragmentManager
        sharedPref = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
    }

    private fun changeFragment(fragment:Fragment){
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container,fragment)
        fragmentTransaction.commit();
    }

    override fun openSignUpPage() {

    }

    override fun openSignInPage() {

    }

    override fun signIn(key: String?) {

    }

    override fun signUp(key: String?) {

    }

    override fun openSettingPage() {
        println("### openSettingPage")
        var profileFragment = ProfileFragment.newInstance()
        profileFragment.lister = this
        changeFragment(profileFragment)
    }

    override fun openHomePage() {
        println("#### openHomePage")
        var homeFragment = HomeFragment.newInstance()
        homeFragment.lister = this
        changeFragment(homeFragment)

    }

    override fun openChatPage() {
        println("#### openChatPage")
        var chatFragment = ChatFragment.newInstance()
        chatFragment.lister = this
        changeFragment(chatFragment)
    }

    override fun openSearchPage() {
        println("#### openSearchPage")
        var searchFragment = SearchFragment.newInstance()
        searchFragment.lister = this
        changeFragment(searchFragment)
    }

    override fun signOut() {
        with(sharedPref.edit()){
            putString("id",null)
            apply();
            logOut()
        }
    }

    private fun logOut(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


}