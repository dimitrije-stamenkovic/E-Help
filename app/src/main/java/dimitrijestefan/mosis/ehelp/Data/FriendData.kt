package dimitrijestefan.mosis.ehelp.Data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import dimitrijestefan.mosis.ehelp.Models.Friend
import dimitrijestefan.mosis.ehelp.Models.GeoPoint

object FriendData {
    private var currentUser: FirebaseUser?
    private lateinit var friendsRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var currentUserId:String
    private  var childRef:String="Friends"
     lateinit var friendsList:ArrayList<Friend>
    private set
    lateinit var friendsListIndexMapping:HashMap<String, Int>
    private set
    private lateinit var friendsChildListener:ChildEventListener
    lateinit var onFriendsListChanged: MutableLiveData<ArrayList<Friend>>



    init {
        mAuth=FirebaseAuth.getInstance()
        currentUser= mAuth.currentUser
        currentUserId= currentUser!!.uid
        friendsRef= FirebaseDatabase.getInstance().getReference().child(childRef).child(currentUserId)
        friendsList=ArrayList<Friend>()
        friendsListIndexMapping=HashMap<String,Int>()
        onFriendsListChanged=MutableLiveData<ArrayList<Friend>>()


        friendsChildListener=object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                Log.e("On child Added friends","Nesto1")
              var key:String? =p0.key
                if(key!=null){
                    var newFriend:Friend?=p0.getValue(Friend::class.java)
                    if(newFriend!=null)
                    {
                        newFriend.userId=key
                        friendsList.add(newFriend)
                        friendsListIndexMapping.put(key, friendsList.size-1)
                        onFriendsListChanged.value= friendsList

                    }
                }
            }

            override fun onChildRemoved(dataSnap: DataSnapshot) {
               var key:String?=dataSnap.key
                if(key!=null)
                {
                    if(friendsListIndexMapping.containsKey(key)){
                        var index:Int= friendsListIndexMapping.get(key)!!
                        friendsList.removeAt(index)
                        recreateKeyIndexMappingFriendsList()

                    }
                }
            }

        }
        friendsRef.addChildEventListener(friendsChildListener)

    }



    private fun recreateKeyIndexMappingFriendsList(){
        friendsListIndexMapping.clear()
        for ((index,value) in friendsList.withIndex()){
            friendsListIndexMapping.put(value.userId,index)
        }
    }

}