package jp.techacademy.shirou.ogata.qa_app

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.support.design.widget.Snackbar
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.util.Log

class QAApp: Application() {

    val mFavoriteHashMap = HashMap<String,Boolean>()
    private lateinit var mDatabaseReference: DatabaseReference
    private var mFavoriteRef: DatabaseReference? = null

    override fun onCreate() {
        super.onCreate()
        mDatabaseReference = FirebaseDatabase.getInstance().reference
        updateFavoriteList()

    }

    private val mEventListener = object : ChildEventListener {

        //QuesitionIDを入れていく
        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
            val key = dataSnapshot.key.toString()
            Log.d("QAAPP_log", "onChildAdded")
            Log.d("QAAPP_log", key)
            mFavoriteHashMap[key] = true
        }
        override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
            Log.d("QAAPP_log", "onChildChanged")
        }
        override fun onChildRemoved(p0: DataSnapshot) {
            Log.d("QAAPP_log", "onChildRemoved")
            val key = p0.key.toString()
            mFavoriteHashMap.remove(key)
        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            Log.d("QAAPP_log", "onChildMovedd")
        }
        override fun onCancelled(p0: DatabaseError) {
            Log.d("QAAPP_log", "onCanceled")
        }
    }

    fun getDatabaseRef():DatabaseReference{
        return mDatabaseReference
    }

    fun getFavoriteRef():DatabaseReference?{
        return mFavoriteRef
    }

    fun updateFavoriteList(){
        val user = FirebaseAuth.getInstance().uid
        if(user != null){
            mFavoriteRef = mDatabaseReference.child(FavoritesPATH).child(user.toString())
            mFavoriteRef!!.addChildEventListener(mEventListener)
            // --- ここまで追加する ---
            Log.d("QAAPP_log", user+"でログインしています")
        }else{
            Log.d("QAAPP_log", "ログインしていません")
        }
    }
}