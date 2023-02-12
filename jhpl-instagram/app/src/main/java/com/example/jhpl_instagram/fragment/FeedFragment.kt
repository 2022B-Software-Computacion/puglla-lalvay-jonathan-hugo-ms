package com.example.jhpl_instagram.fragment

import android.annotation.SuppressLint
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
import com.example.jhpl_instagram.adapter.PostAdapter
import com.example.jhpl_instagram.utils.Constants
import com.example.jhpl_instagram.model.Post
import com.example.jhpl_instagram.model.UserModel
import com.google.firebase.database.*

/* Global parameters */
/* ---------------------------------------------------------------- */
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FeedFragment : Fragment() {
    /* Attributes */
    /* ---------------------------------------------------------------- */
    private var param1: String? = null
    private var param2: String? = null
    private var postRecyclerView: RecyclerView? = null
    private var progressDialog: AlertDialog? = null
    private var database: DatabaseReference? = null
    private var posts: ArrayList<Post>? = null
    private var adapter: PostAdapter? = null

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
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initFirebaseDatabase()
        getPosts()
    }

    private fun initViews() {
        progressDialog = this.context?.let { setProgressDialog(it, "Loading") }
        progressDialog!!.setCanceledOnTouchOutside(false)
        postRecyclerView = requireView().findViewById(R.id.postRv)
    }

    private fun initFirebaseDatabase() {
        database =
            FirebaseDatabase.getInstance(Constants.FIREBASE_REALTIME_DATABASE_URL).reference
    }

    private fun initRecyclerView(posts: ArrayList<Post>?) {
        if (posts == null || posts.size == 0) {
            return
        }
        postRecyclerView!!.layoutManager = LinearLayoutManager(this.context)
        val cometChatUser = CometChat.getLoggedInUser()
        val cometChatUserId = cometChatUser.uid
        adapter = this.context?.let { PostAdapter(this, database!!, it, posts, cometChatUserId) }
        postRecyclerView!!.adapter = adapter
        progressDialog!!.dismiss()
    }

    private fun hasLiked(post: Post?, id: String?) {
        if (post?.likes == null || post.likes?.size === 0 || id == null) {
            post?.hasLiked = false
            return
        }
        for (like in post.likes!!) {
            if (like == id) {
                post.hasLiked = true
                return
            }
        }
        post.hasLiked = false
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun hasFollowed(index: Int?, post: Post?, id: String?) {
        if (post?.author == null || post.author?.uid == null || id == null) {
            return
        }
        val userId = post.author?.uid
        database?.child("users")?.child(userId!!)?.get()?.addOnSuccessListener {
            val user = it.getValue(UserModel::class.java)
            if (user?.followers == null || user.followers?.size == 0) {
                post.hasFollowed = false
            } else {
                for (follower in user.followers!!) {
                    if (follower == id) {
                        post.hasFollowed = true
                    }
                }
            }
            posts!![index!!] = post
            if (adapter != null) {
                adapter!!.notifyDataSetChanged()
            }
        }?.addOnFailureListener {
        }
    }

    private fun updateFollow() {
        val cometChatUser = CometChat.getLoggedInUser()
        val cometChatUserId = cometChatUser.uid
        for ((index, post) in posts!!.withIndex()) {
            hasFollowed(index, post, cometChatUserId)
        }
    }

    fun getPosts() {
        val cometChatUser = CometChat.getLoggedInUser()
        if (cometChatUser != null) {
            progressDialog!!.show()
            database?.child(Constants.FIREBASE_POSTS)?.orderByChild(Constants.FIREBASE_ID_KEY)
                ?.addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        posts = ArrayList()
                        if (dataSnapshot.children.count() > 0) {
                            for (postSnapshot in dataSnapshot.children) {
                                val post = postSnapshot.getValue(Post::class.java)
                                if (post != null) {
                                    hasLiked(post, cometChatUser.uid)
                                    posts!!.add(post)
                                }
                            }
                            initRecyclerView(posts)
                            updateFollow()
                        } else {
                            progressDialog!!.dismiss()
                        }
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
            FeedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}