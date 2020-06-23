package dimitrijestefan.mosis.ehelp
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.fragment_map.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import dimitrijestefan.mosis.ehelp.CustomMarker.ClusterMarker
import dimitrijestefan.mosis.ehelp.CustomMarker.MyClusterManagerRenderer
import dimitrijestefan.mosis.ehelp.Data.AllHelpRequestsData
import dimitrijestefan.mosis.ehelp.Data.UsersLocationData
import dimitrijestefan.mosis.ehelp.Models.GeoPoint
import dimitrijestefan.mosis.ehelp.Models.User

class MapFragment : Fragment(),OnMapReadyCallback {


    private lateinit var googleMap:GoogleMap
    private lateinit var markerPerson:HashMap<User,Int>
    private lateinit var mClusteManagerRenderer:MyClusterManagerRenderer
    private lateinit var mClusterManager:ClusterManager<ClusterMarker>
    private var mClusterMarkers:ArrayList<ClusterMarker> = ArrayList<ClusterMarker>()


    private  val addObjectViewModel by lazy {
        ViewModelProvider(requireActivity()).get(AddObjectViewModel::class.java)
    }

    private val mapViewModel by lazy {
       ViewModelProvider(requireActivity()).get(MapViewModel::class.java)
   }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)
    }



    override fun onMapReady(map: GoogleMap){
        googleMap=map;
     //  googleMap?.isMyLocationEnabled=true;
        if(ContextCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
            !=PackageManager.PERMISSION_GRANTED){
           requestPermissions( Array<String>(1){android.Manifest.permission.ACCESS_FINE_LOCATION},101)

        }else
        {
            googleMap.isMyLocationEnabled=true;

            if(addObjectViewModel.select){
                setOnMapClickListener()

            }


            if(mapViewModel.filter==true){
                Toast.makeText(requireContext(),mapViewModel.filtered_requests.toString(),Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(requireContext(),AllHelpRequestsData.requests.toString(),Toast.LENGTH_LONG).show()
            }


            filterButton.setOnClickListener {
                this.findNavController().navigate(R.id.filterMap)
            }
             UsersLocationData.onUserLocationChanged.observe(viewLifecycleOwner, Observer {
            addCustomMarkers()
              })
            setOnMarkerClickListener()
            //addUsersMarker()
            addCustomMarkers()
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // UsersLocationData.onUserLocationChanged.observe(viewLifecycleOwner, Observer {
           // addUsersMarker()
      //  })
    }




    fun setOnMapClickListener(){

            googleMap.setOnMapClickListener { lokacija ->

                addObjectViewModel.location = lokacija
                addObjectViewModel.changeSelect()

                this.findNavController().navigate(R.id.returnCoords)
            }

    }

    fun setOnMarkerClickListener() {
        googleMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(p0: Marker?): Boolean {
                var listener: GoogleMap.OnMarkerClickListener =
                    p0?.tag as GoogleMap.OnMarkerClickListener
                return listener.onMarkerClick(p0)
            }
        })
    }

    private fun addUsersMarker() {
        var users: ArrayList<GeoPoint> = UsersLocationData.usersLocation
        var markerUsersIdMap: HashMap<Marker, Int> = HashMap<Marker, Int>()
        var loc: LatLng
        var markerOptions: MarkerOptions
        var usermarker: Marker
        for ((index, value) in users.withIndex()) {
            loc = LatLng(value.latitude, value.longitude)
            markerOptions = MarkerOptions()
            markerOptions.position(loc)
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_add_object))
            usermarker = googleMap.addMarker(markerOptions)
            usermarker.tag = OnUserMarkerClickListener
            markerUsersIdMap.put(usermarker, index)
        }

    }

    private fun renderFriensdMarkers(){
        var friends: ArrayList<GeoPoint> =UsersLocationData.friendsLocation
        var markerFriendsIdMap: HashMap<Marker, Int> = HashMap<Marker, Int> ()
        var location:LatLng
        var markerOptions:MarkerOptions
        var friendMarker:Marker
        for ((index,value) in friends.withIndex())
        {
            location= LatLng(value.latitude,value.longitude)
            markerOptions= MarkerOptions()
            markerOptions.position(location)
           // markerOptions.icon()

        }
    }

    private fun addCustomMarkers(){
        if(googleMap!=null){
         //   if(mClusterManager==null){
                mClusterManager= ClusterManager<ClusterMarker>(requireActivity().applicationContext,googleMap)
          //  }
         //   if(mClusteManagerRenderer==null){
                mClusteManagerRenderer= MyClusterManagerRenderer(requireActivity(),googleMap,mClusterManager)
                mClusterManager.renderer=mClusteManagerRenderer
          //  }
            var friends: ArrayList<GeoPoint> =UsersLocationData.usersLocation
            var customMarker:ClusterMarker
            for ((index,value) in friends.withIndex()){
                customMarker= ClusterMarker(
                    LatLng(value.latitude,value.longitude),
                    "prijatelj",
                    "opis",
                    "ut;;;"
                )
                mClusterManager.addItem(customMarker)
                mClusterMarkers.add(customMarker)
            }
            mClusterManager.cluster()

        }
    }




    private var OnUserMarkerClickListener= object: GoogleMap.OnMarkerClickListener {
        override fun onMarkerClick(p0: Marker?): Boolean {
            Log.e("Kliknuto na marker user", "Na usera")
            return  true
        }

    }

    private var OnFriendMarkerClickListener=object :GoogleMap.OnMarkerClickListener{
        override fun onMarkerClick(p0: Marker?): Boolean {
            Log.e("Kliknuto na marker friend","Na frienda")
            return true
        }

    }


    private fun addMarker(){

    }

}
