package dimitrijestefan.mosis.ehelp.Activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dimitrijestefan.mosis.ehelp.MainActivity
import dimitrijestefan.mosis.ehelp.Models.User
import dimitrijestefan.mosis.ehelp.R
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        FirebaseApp.initializeApp(this)
        btnHaveAcc.setOnClickListener {
            this.startActivity(Intent(this,SignInActivity::class.java));
        }
        btnSignUp.setOnClickListener {
            this.CreateAccount()
        }
    }

    private fun CreateAccount() {

        val name=nameSignUp.text.toString()
        val lastname= lastnameSignUp.text.toString()
        val phoneNumber= number.text.toString()
        val email=email.text.toString()
        val password=passwordSignUp.text.toString()
        val username=usernameSignUp.text.toString()

        when{
            TextUtils.isEmpty(email)-> Toast.makeText(this, "email",Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(password)-> Toast.makeText(this, "sifra",Toast.LENGTH_LONG).show()

            else->{
                val progressDialog=ProgressDialog(this@SignUpActivity)
                progressDialog.setTitle("Sign up")
                progressDialog.setMessage("Kreiranje naloga")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener{task ->
                        if(task.isSuccessful){
                            saveUserInfo(name, lastname, phoneNumber, password, username,progressDialog ,email)
                        }
                        else {
                            val message= task.exception!!.toString()
                            Toast.makeText(this,"Error: $message",Toast.LENGTH_LONG).show()
                            mAuth.signOut()
                            progressDialog.dismiss()
                        }
                    }
            }
        }

    }

    private fun saveUserInfo(name: String, lastname: String, phoneNumber: String, password: String, username: String,progressDialog: ProgressDialog
    ,email:String) {

        val currentUserId=FirebaseAuth.getInstance().currentUser!!.uid
        var usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
        val user:User=User(username,password,email,name,lastname,phoneNumber)
        usersRef.child(currentUserId).setValue(user).addOnCompleteListener { task ->
            if(task.isSuccessful){
                progressDialog.dismiss()
                Toast.makeText(this, "Uspesno kreirano", Toast.LENGTH_LONG).show()
                val intent=Intent(this@SignUpActivity, MainActivity::class.java)
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
