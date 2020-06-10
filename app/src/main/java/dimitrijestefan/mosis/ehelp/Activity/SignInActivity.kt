package dimitrijestefan.mosis.ehelp.Activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import dimitrijestefan.mosis.ehelp.MainActivity
import dimitrijestefan.mosis.ehelp.R
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
       btnDontHaveAcc.setOnClickListener {
           this.startActivity(Intent(this, SignUpActivity::class.java));}

           btnSignIn.setOnClickListener {
               this.loginUser()
               this.startActivity(Intent(this,MainActivity::class.java))
           }

    }

    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser!=null)
        {
            val intent=Intent(this@SignInActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun loginUser() {
      val email=usernameSignIn.text.toString()
        val password=passwordSignIn.text.toString()
        when{
            TextUtils.isEmpty(email)-> Toast.makeText(this, "email", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(password)-> Toast.makeText(this, "sifra", Toast.LENGTH_LONG).show()
            else->{
                val progressDialog= ProgressDialog(this@SignInActivity)
                progressDialog.setTitle("Sign in")
                progressDialog.setMessage("Logovanje")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        progressDialog.dismiss()
                       // Toast.makeText(this, "Uspesno kreirano", Toast.LENGTH_LONG).show()
                        val intent=Intent(this@SignInActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }else{
                        val message= task.exception!!.toString()
                        Toast.makeText(this,"Error: $message",Toast.LENGTH_LONG).show()
                        FirebaseAuth.getInstance().signOut()
                        progressDialog.dismiss()
                    }
                }
            }
        }

    }
}
