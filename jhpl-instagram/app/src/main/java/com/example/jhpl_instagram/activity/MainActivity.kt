package com.example.jhpl_instagram.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import com.cometchat.pro.core.AppSettings
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.example.jhpl_instagram.utils.Constants
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.cometchat.pro.uikit.ui_components.cometchat_ui.CometChatUI
import com.example.jhpl_instagram.R
import com.example.jhpl_instagram.fragment.FeedFragment
import com.example.jhpl_instagram.fragment.NotificationFragment
import com.example.jhpl_instagram.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    /* Attributes */
    /* ---------------------------------------------------------------- */
    private var plusImageView: ImageView? = null
    private var chatImageView: ImageView? = null
    private var logoutImageView: ImageView? = null
    private var bottomNavigationView: BottomNavigationView? = null
    private var progressDialog: AlertDialog? = null

    /* Methods */
    /* ---------------------------------------------------------------- */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initEvents()
        initCometChat()
        initFragment(savedInstanceState)
    }

    private fun initViews() {
        plusImageView = findViewById(R.id.plusIv)
        chatImageView = findViewById(R.id.chatIv)
        logoutImageView = findViewById(R.id.logoutIv)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        progressDialog = setProgressDialog(this, "Loading")
        progressDialog!!.setCanceledOnTouchOutside(false)
    }

    private fun initEvents() {
        plusImageView!!.setOnClickListener(this)
        chatImageView!!.setOnClickListener(this)
        logoutImageView!!.setOnClickListener(this)
        bottomNavigationView!!.setOnNavigationItemSelectedListener(this)
    }

    private fun initCometChat() {
        val appSettings =
            AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().
            setRegion(Constants.COMETCHAT_REGION).build()

        CometChat.init(
            this,
            Constants.COMETCHAT_APP_ID,
            appSettings,
            object : CometChat.CallbackListener<String>()
            {
            override fun onSuccess(successMessage: String) {
            }

            override fun onError(e: CometChatException) {
            }
        })
    }

    private fun initFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val fragment = FeedFragment()
            supportFragmentManager.beginTransaction().replace(
                R.id.container, fragment, fragment.javaClass.simpleName
            ).commit()
        }
    }

    private fun goToLoginActivity() {
        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        if (CometChat.getLoggedInUser() == null) {
            goToLoginActivity()
        }
    }

    private fun goToCreatePost() {
        intent = Intent(this@MainActivity, CreateActivity::class.java)
        startActivity(intent)
    }

    private fun handleLogout() {
        progressDialog!!.show()
        CometChat.logout(object : CometChat.CallbackListener<String>() {
            override fun onSuccess(p0: String?) {
                goToLoginActivity()
                progressDialog!!.dismiss()
            }
            override fun onError(p0: CometChatException?) {
                progressDialog!!.dismiss()
                Toast.makeText(
                    this@MainActivity,
                    "Cannot logout, please try again",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun logout() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Do you want to logout ?")
            .setCancelable(false)
            .setPositiveButton("Proceed") { _, _ ->
                handleLogout()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("Logout")
        alert.show()
    }

    private fun goToChat() {
        startActivity(Intent(this@MainActivity, CometChatUI::class.java))
    }

    override fun onClick(view: View?) {
        when(view!!.id) {
            R.id.plusIv -> goToCreatePost()
            R.id.logoutIv -> logout()
            R.id.chatIv -> goToChat()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_feed -> {
                val fragment = FeedFragment()
                supportFragmentManager.beginTransaction().replace(
                    R.id.container, fragment, fragment.javaClass.simpleName
                ).commit()
                return true
            }
            R.id.navigation_notification -> {
                val fragment = NotificationFragment()
                supportFragmentManager.beginTransaction().replace(
                    R.id.container, fragment, fragment.javaClass.simpleName
                ).commit()
                return true
            }
            R.id.navigation_profile -> {
                val fragment = ProfileFragment()
                supportFragmentManager.beginTransaction().replace(
                    R.id.container, fragment, fragment.javaClass.simpleName
                ).commit()
                return true
            }
        }
        return false
    }

//    bottomNavigationView?.setOnItemSelectedListener {
//        // do stuff
//
//        return@setOnItemSelectedListener true
//    }

    companion object {
        fun setProgressDialog(context: Context, message:String): AlertDialog {
            val llPadding = 30
            val ll = LinearLayout(context)
            ll.orientation = LinearLayout.HORIZONTAL
            ll.setPadding(llPadding, llPadding, llPadding, llPadding)
            ll.gravity = Gravity.CENTER
            var llParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            llParam.gravity = Gravity.CENTER
            ll.layoutParams = llParam

            val progressBar = ProgressBar(context)
            progressBar.isIndeterminate = true
            progressBar.setPadding(0, 0, llPadding, 0)
            progressBar.layoutParams = llParam

            llParam = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            llParam.gravity = Gravity.CENTER
            val tvText = TextView(context)
            tvText.text = message
            tvText.setTextColor(Color.parseColor("#000000"))
            tvText.textSize = 20.toFloat()
            tvText.layoutParams = llParam

            ll.addView(progressBar)
            ll.addView(tvText)

            val builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            builder.setView(ll)

            val dialog = builder.create()
            val window = dialog.window
            if (window != null) {
                val layoutParams = WindowManager.LayoutParams()
                layoutParams.copyFrom(dialog.window?.attributes)
                layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                dialog.window?.attributes = layoutParams
            }

            return dialog
        }
    }
}