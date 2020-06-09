package dimitrijestefan.mosis.ehelp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dimitrijestefan.mosis.ehelp.R
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sing_up.*

class SingUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)
        btnHaveAcc.setOnClickListener {
            this.startActivity(Intent(this,SignInActivity::class.java));
        }
    }
}
