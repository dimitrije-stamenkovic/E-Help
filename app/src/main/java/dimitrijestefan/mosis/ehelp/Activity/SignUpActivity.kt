package dimitrijestefan.mosis.ehelp.Activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dimitrijestefan.mosis.ehelp.MainActivity
import dimitrijestefan.mosis.ehelp.Models.User
import dimitrijestefan.mosis.ehelp.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


class SignUpActivity : AppCompatActivity() {

    private var currentPhotoPath: String = ""
    lateinit var photoUri: Uri
    var name: String = ""
    var lastname: String = ""
    var phoneNumber: String = ""
    var email: String = ""
    var password: String = ""
    var username: String = ""
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        txtSignIn.setOnClickListener {
            this.startActivity(Intent(this, SignInActivity::class.java));
        }

        btnSignUp.setOnClickListener {
            this.CreateAccount()
        }

        imageViewSignUp.setOnClickListener {
            this.TakePhoto()
        }
        progressDialog = ProgressDialog(this@SignUpActivity)
    }


    private fun CreateAccount() {
        name = nameSignUp.text.toString()
        lastname = lastnameSignUp.text.toString()
        phoneNumber = number.text.toString()
        email = emailSignUp.text.toString()
        password = passwordSignUp.text.toString()
        username = usernameSignUp.text.toString()
        when {
              TextUtils.isEmpty(name) -> Toast.makeText(this, "Please enter first name", Toast.LENGTH_LONG).show()
              TextUtils.isEmpty(lastname) -> Toast.makeText(this, "Please enter last name", Toast.LENGTH_LONG).show()
              TextUtils.isEmpty(phoneNumber) -> Toast.makeText(this, "Please enter phone number.", Toast.LENGTH_LONG).show()
              TextUtils.isEmpty(username) -> Toast.makeText(this, "Please enter username.", Toast.LENGTH_LONG).show()
              TextUtils.isEmpty(email) -> Toast.makeText(this, "Please enter email.", Toast.LENGTH_LONG).show()
              TextUtils.isEmpty(password) -> Toast.makeText(this, "Please enter password.", Toast.LENGTH_LONG).show()
              !this::photoUri.isInitialized ->Toast.makeText(this, "Please take photo",Toast.LENGTH_LONG).show()

            else -> {

                progressDialog.setTitle("Sign up")
                progressDialog.setMessage("Sign up, please wait.")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()
                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            UploadImageToFirebaseStorage()
                        }

                    }
                    .addOnFailureListener {
                        mAuth.signOut()
                        progressDialog.dismiss()
                        NotifyUser(it)
                    }
            }
        }

    }

    private fun UploadImageToFirebaseStorage() {
        val filename = UUID.randomUUID().toString()
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val storageRef =
            FirebaseStorage.getInstance().getReference("usersPhoto/$currentUserId/$filename")
        storageRef.putFile(this.photoUri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener {
                this.SaveUserToDatabase(it.toString())
            }
        }
    }

    private fun SaveUserToDatabase(photoUrl: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val newUser: User = User(
            this.username,
            this.password,
            this.email,
            this.name,
            this.lastname,
            this.phoneNumber,
            photoUrl
        )
        val usersRef = FirebaseDatabase.getInstance().reference.child("Users")
        usersRef.child(currentUserId).setValue(newUser).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                progressDialog.dismiss()
                Toast.makeText(this, "Uspesno ste kreirali nalog", Toast.LENGTH_LONG).show()
                val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            } else {
                val message = task.exception!!.toString()
                Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
                FirebaseAuth.getInstance().signOut()
                progressDialog.dismiss()
            }
        }
    }

    private fun TakePhoto() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                Array<String>(1) { android.Manifest.permission.WRITE_EXTERNAL_STORAGE },
                101
            )

        }
        else {
            val fileName: String = "photo"
            val storageDirectory: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val imageFile: File = File.createTempFile(fileName, ".jpg", storageDirectory)
            this.currentPhotoPath = imageFile.absolutePath
            photoUri = FileProvider.getUriForFile(
                this@SignUpActivity,
                "dimitrijestefan.mosis.ehelp",
                imageFile
            )
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(cameraIntent, 1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            101 -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    this.TakePhoto()
                } else {

                    Toast.makeText(
                        this,
                        "Sorry, granted permission is necessary.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return

            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val bitmapForView: Bitmap = BitmapFactory.decodeFile(this.currentPhotoPath)
           // val roundBitmap:RoundedBitmapDrawable= RoundedBitmapDrawableFactory.create(resources, bitmapForView)
           // roundBitmap.isCircular=true
          //  imageViewSignUp.setImageDrawable(roundBitmap)

            imageViewSignUp.setImageBitmap(bitmapForView)

           // btnTakePhoto.setBackgroundDrawable(BitmapDrawable(bitmapForView))
        }
    }

    private fun NotifyUser(ex: Exception) {

        when (ex){

             is FirebaseAuthUserCollisionException -> {
               Toast.makeText(this, " The email is already in use.", Toast.LENGTH_LONG).show()
            }
             is FirebaseAuthWeakPasswordException -> {
                Toast.makeText(this, "Password should be at least 6 characters.", Toast.LENGTH_LONG).show()

            }
            is FirebaseAuthInvalidCredentialsException -> {
                Toast.makeText(this, "The email is badly formatted.", Toast.LENGTH_LONG).show()
            }
            else->{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        }

    }

}
