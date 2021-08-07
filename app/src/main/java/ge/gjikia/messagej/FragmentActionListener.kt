package ge.gjikia.messagej

interface FragmentActionListener {
    fun openSignUpPage()
    fun openSignInPage()
    fun signIn(key : String?)
    fun signUp(key : String?)
    fun openSettingPage()
    fun openHomePage()
    fun openChatPage(id : String?)
    fun openSearchPage()
    fun  signOut();

}