package com.example.jhpl_instagram.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jhpl_instagram.R
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cometchat.pro.core.CometChat
import com.example.jhpl_instagram.activity.MainActivity.Companion.setProgressDialog
import com.example.jhpl_instagram.adapter.NotificationAdapter
import com.example.jhpl_instagram.utils.Constants
import com.example.jhpl_instagram.model.Notification
import com.google.firebase.database.*

/* Global Parameters */
/* ---------------------------------------------------------------- */
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NotificationFragment : Fragment() {
    /* Attributes */
    /* ---------------------------------------------------------------- */
    private var param1: String? = null
    private var param2: String? = null
    private var notificationRv: RecyclerView? = null
    private var pDialog: AlertDialog? = null
    private var mDatabase: DatabaseReference? = null

    /* Methods */
    /* ---------------------------------------------------------------- */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initFirebaseDatabase()
        getNotifications()
    }

    private fun initViews() {
        notificationRv = this.requireView().findViewById(R.id.notificationRv)
        pDialog = this.context?.let { setProgressDialog(it, "Loading") }
        pDialog!!.setCanceledOnTouchOutside(false)
    }

    private fun initFirebaseDatabase() {
        mDatabase =
            FirebaseDatabase.getInstance(Constants.FIREBASE_REALTIME_DATABASE_URL).reference
    }

    private fun initRecyclerView(notifications: ArrayList<Notification>?) {
        if (notifications == null) {
            return
        }
        notificationRv!!.layoutManager = LinearLayoutManager(this.context)
        val adapter = this.context?.let { NotificationAdapter(it, notifications) }
        notificationRv!!.adapter = adapter
        pDialog!!.dismiss()
    }

    private fun getNotifications() {
        val cometChatUser = CometChat.getLoggedInUser()
        if (cometChatUser != null) {
            pDialog!!.show()
            mDatabase?.child(Constants.FIREBASE_NOTIFICATIONS)?.orderByChild(Constants.FIREBASE_ID_KEY)
                ?.addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val notifications = ArrayList<Notification>()
                        if (dataSnapshot.children.count() > 0) {
                            for (notificationSnapshot in dataSnapshot.children) {
                                val notification = notificationSnapshot.getValue(Notification::class.java)
                                if (notification != null && notification.receiverId!! == cometChatUser.uid) {
                                    notifications.add(notification)
                                }
                            }
                        } else {
                            pDialog!!.dismiss()
                        }
                        initRecyclerView(notifications)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        pDialog!!.dismiss()
                        Toast.makeText(
                            context,
                            "Cannot fetch list of notifications",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}