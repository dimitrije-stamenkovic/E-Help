package dimitrijestefan.mosis.ehelp.Data

import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import dimitrijestefan.mosis.ehelp.Models.GeoPoint
import dimitrijestefan.mosis.ehelp.Models.HelpRequest
import dimitrijestefan.mosis.ehelp.Models.UserLocation

object UsersLocationData {
    private var currentUser:FirebaseUser?
    private lateinit var usersLocationRef:DatabaseReference
    private lateinit var mAuth: FirebaseAuth
     lateinit var usersLocation:ArrayList<GeoPoint>
    private set
    private lateinit var usersLocationIndexMapping:HashMap<String,Int>
     lateinit var friendsLocation:ArrayList<GeoPoint>
    private set
    private lateinit var childEventListener:ChildEventListener
    private lateinit var friendsLocationIndexMapping:HashMap<String,Int>
    lateinit var onUserLocationChanged: MutableLiveData<ArrayList<GeoPoint>>
    lateinit var onFriendLocationChanged:MutableLiveData<ArrayList<GeoPoint>>
     lateinit var currentUserId:String
    private set
        //  private lateinit var friendsLocation:ArrayList<>
    private  var child:String="UsersLocations"


    init{
        usersLocationRef=FirebaseDatabase.getInstance().getReference(child)
        mAuth=FirebaseAuth.getInstance()
        currentUser= mAuth.currentUser
        currentUserId= currentUser!!.uid
        usersLocation= ArrayList()
        usersLocationIndexMapping=HashMap<String,Int>()
        friendsLocationIndexMapping=HashMap<String,Int>()
        friendsLocation=ArrayList<GeoPoint>()
        onUserLocationChanged= MutableLiveData()
        onFriendLocationChanged= MutableLiveData()




        childEventListener=object :ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                var key: String = p0.key!!
                if (key != null) {
                    var userLocation: GeoPoint = p0.getValue(GeoPoint::class.java) ?: GeoPoint()
                    userLocation.userId = key
                    when {
                        userLocation != null -> {
                            when {
                                !key.equals(currentUserId) -> {
                                    when {
                                        FriendData.friendsListIndexMapping.containsKey(key) && usersLocationIndexMapping.containsKey(key) -> {
                                            //postali su prijatelji i promenio je lokaciju
                                            var index: Int = usersLocationIndexMapping.get(key)!!
                                            usersLocation.removeAt(index)
                                            recreateKeyIndexMappingUsersLocation()
                                            onUserLocationChanged.value= usersLocation

                                            friendsLocation.add(userLocation)
                                            friendsLocationIndexMapping.put(key, friendsLocation.size - 1)
                                            onFriendLocationChanged.value = friendsLocation
                                        }

                                        FriendData.friendsListIndexMapping.containsKey(key) -> {
                                            //prijatelji su i ima ga vec u listilokacija
                                            if (friendsLocationIndexMapping.containsKey(key)) {
                                                var index: Int = friendsLocationIndexMapping.get(key)!!
                                                friendsLocation.set(index, userLocation)
                                                onFriendLocationChanged.value = friendsLocation
                                            }
                                            else {
                                               // postali su prijatelji
                                                friendsLocation.add(userLocation)
                                                friendsLocationIndexMapping.put(
                                                    key,
                                                    friendsLocation.size - 1
                                                )
                                                onFriendLocationChanged.value = friendsLocation
                                            }

                                        }
                                        //nisu prijatelji
                                        usersLocationIndexMapping.containsKey(key) -> {
                                            //Log.e("Uso u chang3", key)
                                            var index: Int = usersLocationIndexMapping.get(key)!!
                                            usersLocation.set(index, userLocation)
                                            onUserLocationChanged.value = usersLocation
                                        }
                                        else -> {
                                          //  Log.e("Uso u else", key)
                                            usersLocation.add(userLocation)
                                            usersLocationIndexMapping.put(key, usersLocation.size - 1)
                                            onUserLocationChanged.value = usersLocation
                                        }
                                    }
                                }
                            }
                        }
                    }

                }

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                var key: String? = p0.key ?: null
                if (key != null) {
                    var userLocation: GeoPoint =
                        p0.getValue(GeoPoint::class.java) ?: GeoPoint(0.0, 0.0)
                    userLocation.userId = key
                    when {
                        usersLocation != null -> {
                            if (!key.equals(currentUserId)) {
                                when {

                                    FriendData.friendsListIndexMapping.containsKey(key) && !usersLocationIndexMapping.containsKey(key) -> {
                                        Log.e("Add loc frieend",key.toString())
                                        if (!friendsLocationIndexMapping.containsKey(key)) {
                                            friendsLocation.add(userLocation)
                                            friendsLocationIndexMapping.put(key, friendsLocation.size - 1)
                                            onFriendLocationChanged.value = friendsLocation
                                            //kad se obrise, pa se doda opet
                                        }
                                    }

//                                    FriendData.friendsListIndexMapping.containsKey(key) && usersLocationIndexMapping.containsKey(key) -> {
//                                        //postali su prijatelji
//                                        var index: Int = usersLocationIndexMapping.get(key)!!
//                                        usersLocation.removeAt(index)
//                                        recreateKeyIndexMappingUsersLocation()
//                                        if(!friendsLocationIndexMapping.containsKey(key)) {
//                                            friendsLocation.add(userLocation)
//                                            friendsLocationIndexMapping.put(key, friendsLocation.size - 1)
//                                            onFriendLocationChanged.value = friendsLocation
//                                        }
//                                    }
                                    else -> {
                                        if (!usersLocationIndexMapping.containsKey(key)) {
                                            Log.e("Add loc users",key.toString())
                                            usersLocation.add(userLocation)
                                            usersLocationIndexMapping.put(key, usersLocation.size - 1)
                                            onUserLocationChanged.value = usersLocation

                                        }
                                    }


                                }
                            }

                        }

                    }
                }

            }


            override fun onChildRemoved(p0: DataSnapshot) {
                var key:String =p0.key!!
                if(key!=null){
                    if(friendsLocationIndexMapping.containsKey(key)){
                        var index:Int= friendsLocationIndexMapping.get(key)!!
                        friendsLocation.removeAt(index)
                        recreateKeyIndexMappingFriendsLocation()
                        onFriendLocationChanged.value= friendsLocation
                    }else if(usersLocationIndexMapping.containsKey(key)){
                        var index:Int= usersLocationIndexMapping.get(key)!!
                        usersLocation.removeAt(index)
                        recreateKeyIndexMappingUsersLocation()
                        onFriendLocationChanged.value= usersLocation

                    }
                }
            }

        }

        usersLocationRef.addChildEventListener(childEventListener)
        FriendData.onFriendsListChanged.observeForever(Observer {
            usersLocation= ArrayList()
            usersLocationIndexMapping=HashMap<String,Int>()
            friendsLocationIndexMapping=HashMap<String,Int>()
            friendsLocation=ArrayList<GeoPoint>()
            usersLocationRef.addChildEventListener(childEventListener)

        })
    }


    private fun recreateKeyIndexMappingUsersLocation(){
        usersLocationIndexMapping.clear()
        for((index, value) in usersLocation.withIndex()  ){
            usersLocationIndexMapping.put(value.userId, index)
        }
    }
    private fun recreateKeyIndexMappingFriendsLocation(){
        friendsLocationIndexMapping.clear()
        for((index, value) in friendsLocation.withIndex()){
            friendsLocationIndexMapping.put(value.userId,index)
        }
    }

    fun changeUserReference(uidUser:String){
        currentUserId =uidUser
        usersLocation= ArrayList()
        usersLocationIndexMapping=HashMap<String,Int>()
        friendsLocationIndexMapping=HashMap<String,Int>()
        friendsLocation=ArrayList<GeoPoint>()
        usersLocationRef.addChildEventListener(childEventListener)
        //FriendData.friendsRef = FirebaseDatabase.getInstance().getReference().child(FriendData.childRef).child(uidUser)
       // FriendData.friendsRef.addChildEventListener(FriendData.friendsChildListener)
    }



}

