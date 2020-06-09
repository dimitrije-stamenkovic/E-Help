package dimitrijestefan.mosis.ehelp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dimitrijestefan.mosis.ehelp.R
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
       btnDontHaveAcc.setOnClickListener {
           this.startActivity(Intent(this, SingUpActivity::class.java));
       }
    }
}
