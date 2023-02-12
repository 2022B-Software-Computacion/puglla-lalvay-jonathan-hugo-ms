package com.example.jhpl_instagram.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cometchat.pro.core.CometChat
import com.example.jhpl_instagram.R
import com.example.jhpl_instagram.activity.MainActivity.Companion.setProgressDialog
import com.example.jhpl_instagram.adapter.ProfilePostAdapter
import com.example.jhpl_instagram.utils.Constants
import com.example.jhpl_instagram.model.Post
import com.example.jhpl_instagram.model.UserModel
import com.google.firebase.database.*

/* Global Parameters */
/* ---------------------------------------------------------------- */
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {
    /* Attributes */
    /* ---------------------------------------------------------------- */
    private var param1: String? = null
    private var param2: String? = null
    private var authorAvatarImageView: ImageView? = null
    private var postImageView: ImageView? = null
    private var videoImageView: ImageView? = null
    private var postBottomLine: View? = null
    private var videoBottomLine: View? = null
    private var nPostsTxt: TextView? = null
    private var nFollowersTxt: TextView? = null
    private var postRecyclerView: RecyclerView? = null
    private var progressDialog: AlertDialog? = null
    private var database: DatabaseReference? = null

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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initEvents()
        initFirebaseDatabase()
        getProfile()
        getPosts(1)
    }

    private fun initViews() {
        val view = this.requireView()

        authorAvatarImageView = view.findViewById(R.id.authorAvatarIv)
        nPostsTxt = view.findViewById(R.id.nPostsTxt)
        nFollowersTxt = view.findViewById(R.id.nFollowersTxt)
        postImageView = view.findViewById(R.id.postIv)
        videoImageView = view.findViewById(R.id.videoIv)
        postBottomLine = view.findViewById(R.id.postBottomLine)
        videoBottomLine = view.findViewById(R.id.videoBottomLine)
        postRecyclerView = view.findViewById(R.id.profilePostRv)
        videoBottomLine?.isVisible = false
        progressDialog = this.context?.let { setProgressDialog(it, "Loading") }
        progressDialog!!.setCanceledOnTouchOutside(false)
    }

    private fun initEvents() {
        postImageView?.setOnClickListener {
            postBottomLine?.isVisible = true
            videoBottomLine?.isVisible = false
            getPosts(1)
        }
        videoImageView?.setOnClickListener {
            videoBottomLine?.isVisible = true
            postBottomLine?.isVisible = false
            getPosts(2)
        }
    }

    private fun initFirebaseDatabase() {
        database = FirebaseDatabase.getInstance(Constants.FIREBASE_REALTIME_DATABASE_URL).reference
    }

    private fun getProfile() {
        val cometChatUser = CometChat.getLoggedInUser()
        if (cometChatUser != null) {
            val cometChatUserId = cometChatUser.uid
            if (cometChatUserId != null) {
                progressDialog!!.show()
                database?.child("users")?.child(cometChatUserId)?.get()?.
                addOnSuccessListener {
                    progressDialog!!.dismiss()
                    val user = it.getValue(UserModel::class.java)
                    Glide.with(this)
                        .load(user!!.avatar)
                        .circleCrop()
                        .into(authorAvatarImageView!!)
                    nPostsTxt?.text = if (user.nPosts !== null) user.nPosts.toString() else "0"
                    nFollowersTxt?.text =
                        if (user.nFollowers !== null) user.nFollowers.toString() else "0"
                }?.addOnFailureListener {
                    progressDialog!!.dismiss()
                }
            }
        }
    }

    private fun initRecyclerView(posts: ArrayList<Post>?) {
        if (posts == null) {
            return
        }
        postRecyclerView!!.layoutManager = GridLayoutManager(this.context, 3)
        val adapter = this.context?.let { ProfilePostAdapter(it, posts) }
        postRecyclerView!!.adapter = adapter
        progressDialog!!.dismiss()
    }

    private fun getPosts(postCategory: Int) {
        val cometChatUser = CometChat.getLoggedInUser()
        if (cometChatUser != null) {
            progressDialog!!.show()
            database?.child(Constants.FIREBASE_POSTS)?.orderByChild(Constants.FIREBASE_ID_KEY)
                ?.addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val posts = ArrayList<Post>()
                        if (dataSnapshot.children.count() > 0) {
                            for (postSnapshot in dataSnapshot.children) {
                                val post = postSnapshot.getValue(Post::class.java)
                                if (
                                    post != null && post.author!!.uid.equals(cometChatUser.uid)
                                    && post.postCategory == postCategory
                                ) {
                                    posts.add(post)
                                }
                            }
                        } else {
                            progressDialog!!.dismiss()
                        }
                        initRecyclerView(posts)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        progressDialog!!.dismiss()
                        Toast.makeText(
                            context,
                            "Cannot fetch list of posts",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}