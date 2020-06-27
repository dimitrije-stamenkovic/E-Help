package dimitrijestefan.mosis.ehelp.Data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import dimitrijestefan.mosis.ehelp.Models.GeoPoint
import dimitrijestefan.mosis.ehelp.Models.HelpRequest

object AllHelpRequestsData {
    var onRequestsChange : MutableLiveData<ArrayList<HelpRequest>>
    var requests : ArrayList<HelpRequest>
    var filtered_requests : ArrayList<HelpRequest>
    private var mCurrentUser: FirebaseUser?
    val current_uid: String
    private  var database : DatabaseReference
    private  var myHelpRequestsIndexMapping = HashMap<String,Int>()




    init {
        mCurrentUser = FirebaseAuth.getInstance().currentUser
        current_uid = mCurrentUser!!.uid
        database = FirebaseDatabase.getInstance().getReference().child("Requests")
        requests = ArrayList()
        filtered_requests = ArrayList()
        onRequestsChange = MutableLiveData()





        var childEventListener = object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val myHelpRequestKey = p0.key
                if(!myHelpRequestsIndexMapping.containsKey(myHelpRequestKey)){
                    var request = p0.getValue(HelpRequest::class.java)
                    if (myHelpRequestKey != null) {
                        if (request != null) {
                            request.key = myHelpRequestKey
                        }
                    }
                    if (request != null) {
                        requests.add(request)
                    }
                    if (myHelpRequestKey != null) {
                        myHelpRequestsIndexMapping.put(myHelpRequestKey, requests.size-1)
                    }
                }

                onRequestsChange.value = requests
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val myHelpRequestKey = p0.key
                var myHelpRequest  = p0.getValue(HelpRequest::class.java)
                if (myHelpRequestKey != null) {
                    if (myHelpRequest != null) {
                        myHelpRequest.key = myHelpRequestKey
                    }
                }

                if(myHelpRequestsIndexMapping.containsKey(myHelpRequestKey)){
                    val index = myHelpRequestsIndexMapping.get(myHelpRequestKey)
                    myHelpRequest?.let { index?.let { it1 ->


                        requests.set(it1,it)


                    } }
                }else{
                    myHelpRequest?.let {
                        requests.add(it)
                        if (myHelpRequestKey != null) {
                            myHelpRequestsIndexMapping.put(myHelpRequestKey, requests.size-1)
                        }

                    }
                    //myHelpRequestKey?.let { myHelpRequestsIndexMapping.put(it, niz.value!!.size-1) }

                }

                onRequestsChange.value = requests

            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val myHelpRequestKey = p0.key

                if(myHelpRequestsIndexMapping.containsKey(myHelpRequestKey)){
                    val index = myHelpRequestsIndexMapping.get(myHelpRequestKey)
                    index?.let {
                        requests.removeAt(it)
                        recreateKeyIndexMapping()

                    }
                    onRequestsChange.value = requests
                }

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        }

        database.addChildEventListener(childEventListener)




        var parentEventListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                onRequestsChange.value = requests

            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }
        database.addListenerForSingleValueEvent(parentEventListener)
    }



    fun recreateKeyIndexMapping(){
        myHelpRequestsIndexMapping.clear()
//        for ( i in 0..(requests.size)){
//            myHelpRequestsIndexMapping.put(requests.get(i).key,i)
//        }

        for(request in requests.indices){
            myHelpRequestsIndexMapping.put(requests.get(request).key,request)
        }
    }



    fun addNewHelpRequest(title:String,urgency:String,category:String,lat:String,lon:String,about:String){

        var key2 = database.push().key.toString()
        var request = HelpRequest(current_uid,title,urgency,category,about,lat,lon)
        request.key=key2
        requests.add(request)
        myHelpRequestsIndexMapping.put(key2,requests.size-1)
        database.child(key2).setValue(request.toMap())

//        onRequestsChange.value = requests

        //key2?.let { myHelpRequestsIndexMapping.put(it, niz.value!!.size-1) }

    }

    fun deleteHelpRequest(index:Int){
        database.child(requests.get(index).key).removeValue()
//        requests.removeAt(index)
//        recreateKeyIndexMapping()
    }


    fun filterRequests(title:String,urgency: String,category: String,loc:LatLng,radius:String){
        filtered_requests.clear()
        var current_loc = GeoPoint(loc.latitude,loc.longitude)
        var radius_double = radius.toDouble()
        Log.d("Distane",urgency)

        if(title == ""){
            for (request in requests){
                var req_loc = GeoPoint(request.latitude!!.toDouble(),request.longitude!!.toDouble())
                if(distance(current_loc.latitude,current_loc.longitude,req_loc.latitude,req_loc.longitude)<radius_double && request.urgency==urgency && request.category==category ){
                    filtered_requests.add(request)
                }
            }
        }else{
            for (request in requests){
                var req_loc = GeoPoint(request.latitude!!.toDouble(),request.longitude!!.toDouble())
                if(distance(current_loc.latitude,current_loc.longitude,req_loc.latitude,req_loc.longitude)<radius_double && request.urgency==urgency && request.category==category && request.title!!.contains(title)){
                    filtered_requests.add(request)
                }
            }
        }


    }


    fun distance(lat1:Double,lon1:Double,lat2:Double,lon2:Double):Double{
        var theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1))*Math.sin(deg2rad(lat2))+Math.cos(deg2rad(lat1))*Math.cos(deg2rad(lat2))*Math.cos(deg2rad(theta))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return dist
    }

    fun rad2deg(rad:Double): Double {
        return rad*180.0/Math.PI
    }

    fun deg2rad(deg:Double):Double{
        return deg*Math.PI/180.0
    }
}