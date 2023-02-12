package com.example.jhpl_instagram.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.core.CometChat.CallbackListener
import com.example.jhpl_instagram.R
import com.example.jhpl_instagram.activity.MainActivity.Companion.setProgressDialog
import com.example.jhpl_instagram.model.UserModel
import com.example.jhpl_instagram.utils.Constants
import com.google.firebase.auth.ktx.auth

class SignUpActivity : AppCompatActivity(), View.OnClickListener {
    /* Attributes */
    /* ---------------------------------------------------------------- */
    private var userAvatarImageView: ImageView? = null
    private var userAvatarTextView: TextView? = null
    private var fullNameEditText: EditText? = null
    private var emailEditText: EditText? = null
    private var passwordEditText: EditText? = null
    private var confirmPasswordEditText: EditText? = null
    private var registerButton: Button? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var progressDialog: AlertDialog? = null
    private var uploadedUri: String? = null

    /* Methods */
    /* ---------------------------------------------------------------- */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initViews()
        initEvents()
    }

    private fun initViews() {
        userAvatarImageView = findViewById(R.id.userAvatarIv)
        userAvatarTextView = findViewById(R.id.userAvatarTxt)
        fullNameEditText = findViewById(R.id.fullnameEdt)
        emailEditText = findViewById(R.id.emailEdt)
        passwordEditText = findViewById(R.id.passwordEdt)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEdt)
        registerButton = findViewById(R.id.registerBtn)

        progressDialog = setProgressDialog(this, "Loading")
        progressDialog!!.setCanceledOnTouchOutside(false)
    }

    private fun initEvents() {
        registerButton?.setOnClickListener(this)
        userAvatarImageView?.setOnClickListener(this)
    }

    @SuppressLint("IntentReset")
    private fun chooseImage() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            intent.type = "image/*"
            intent.putExtra("crop", "true")
            intent.putExtra("scale", true)
            intent.putExtra("aspectX", 16)
            intent.putExtra("aspectY", 9)
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                return
            }
            val uri = data?.data
            uploadedUri = uri.toString()
            if (uri != null) {
                val imageBitmap = uriToBitmap(uri)
                Glide.with(this)
                    .load(imageBitmap)
                    .circleCrop()
                    .into(userAvatarImageView!!)
                userAvatarTextView?.isVisible = false
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // pick image after request permission success
                    chooseImage()
                }
            }
        }
    }

    private fun uriToBitmap(uri: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
    }

    private fun validate(
        fullName: String?, email: String?, password: String?, confirmPassword: String?
    ): Boolean {
        if (uploadedUri == null || uploadedUri.equals(EMPTY_STRING)) {
            Toast.makeText(
                this@SignUpActivity, "Please upload your avatar", Toast.LENGTH_LONG
            ).show()
            return false
        }
        if (fullName == null || fullName == EMPTY_STRING) {
            Toast.makeText(
                this@SignUpActivity, "Please input your full name", Toast.LENGTH_LONG
            ).show()
            return false
        }
        if (email == null || email == EMPTY_STRING) {
            Toast.makeText(
                this@SignUpActivity, "Please input your email", Toast.LENGTH_LONG
            ).show()
            return false
        }
        if (password == null || password == EMPTY_STRING) {
            Toast.makeText(
                this@SignUpActivity, "Please input your password", Toast.LENGTH_LONG
            ).show()
            return false
        }
        if (confirmPassword == null || confirmPassword == EMPTY_STRING) {
            Toast.makeText(
                this@SignUpActivity,
                "Please input your confirm password",
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        if (password != confirmPassword) {
            Toast.makeText(
                this@SignUpActivity,
                "Your password and confirm password must be matched",
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }

    private fun goToLoginActivity() {
        intent = Intent(this@SignUpActivity, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun createCometChatAccount(userId: String?, fullName: String?, avatar: String?) {
        val authKey = Constants.COMETCHAT_AUTH_KEY // Replace with your API Key.
        val user = User()
        user.uid = userId // Replace with your uid for the user to be created.
        user.name = fullName // Replace with the name of the user
        user.avatar = avatar
        CometChat.createUser(user, authKey, object : CallbackListener<User>() {
            override fun onSuccess(user: User) {
                progressDialog!!.dismiss()
                Toast.makeText(
                    this@SignUpActivity,
                    "$fullName was created successfully",
                    Toast.LENGTH_LONG
                ).show()
                goToLoginActivity()
            }
            override fun onError(e: CometChatException) {
                progressDialog!!.dismiss()
                Log.d("Error", e.printStackTrace().toString())
                Toast.makeText(
                    this@SignUpActivity,
                    e.printStackTrace().toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun insertFirebaseDatabase(
        userId: String?, fullName: String?, email: String?, avatar: String?
    ) {
        val userModel = UserModel()
        userModel.uid = userId
        userModel.name = fullName
        userModel.email = email
        userModel.avatar = avatar
        database = Firebase.database.reference
        database.child("users").child(userId!!).setValue(userModel)
    }

    private fun createFirebaseAccount(
        fullName: String?, email: String?, password: String?, avatar: String?
    ) {
        if (email != null && password != null) {
            firebaseAuth = Firebase.auth
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userId = UUID.randomUUID()
                        insertFirebaseDatabase(userId.toString(), fullName, email, avatar)
                        createCometChatAccount(userId.toString(), fullName, avatar)
                    } else {
                        progressDialog!!.dismiss()
                        Toast.makeText(
                            this@SignUpActivity,
                            "Cannot create your account, please try again",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        } else {
            progressDialog!!.dismiss()
            Toast.makeText(
                this@SignUpActivity,
                "Please provide your email and password",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun uploadUserAvatar(fullName: String?, email: String?, password: String?) {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val uuid = UUID(1, 1)
        val avatarRef = storageRef.child("users/$uuid.jpeg")
        userAvatarImageView?.isDrawingCacheEnabled = true
        userAvatarImageView?.buildDrawingCache()
        val bitmap = (userAvatarImageView?.drawable as BitmapDrawable).bitmap
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val data = byteArrayOutputStream.toByteArray()
        val uploadTask = avatarRef.putBytes(data)
        uploadTask.addOnFailureListener {
            progressDialog!!.dismiss()
            Toast.makeText(
                this, "Cannot upload your avatar", Toast.LENGTH_LONG
            ).show()
        }.addOnSuccessListener {
            avatarRef.downloadUrl.addOnSuccessListener { uri ->
                if (uri != null) {
                    this.createFirebaseAccount(fullName, email, password, uri.toString())
                }
            }
        }
    }

    private fun register() {
        val fullName = fullNameEditText!!.text.toString().trim()
        val email = emailEditText!!.text.toString().trim()
        val password = passwordEditText!!.text.toString().trim()
        val confirmPassword = confirmPasswordEditText!!.text.toString().trim()
        if (validate(fullName, email, password, confirmPassword)) {
            progressDialog!!.show()
            uploadUserAvatar(fullName, email, password)
        }
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.userAvatarIv -> chooseImage()
            R.id.registerBtn -> register()
            else -> {}
        }
    }

    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 1000
        const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 1001
        const val EMPTY_STRING = ""
    }
}