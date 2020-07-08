package dimitrijestefan.mosis.ehelp.ViewModels

import android.net.Uri
import androidx.lifecycle.ViewModel

class SignUpViewModel:ViewModel() {
    lateinit var photoUri:Uri
    var currentPhotoPath: String = ""
    fun isInitPhotoUri()= this::photoUri.isInitialized
}