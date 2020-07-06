package dimitrijestefan.mosis.ehelp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.collections.MarkerManager
import dimitrijestefan.mosis.ehelp.Adapters.CustomInfoWindowFriendAdapter
import dimitrijestefan.mosis.ehelp.CustomMarker.ClusterMarker
import dimitrijestefan.mosis.ehelp.CustomMarker.MyClusterManagerRenderer
import dimitrijestefan.mosis.ehelp.Data.AllHelpRequestsData
import dimitrijestefan.mosis.ehelp.Data.FriendData
import dimitrijestefan.mosis.ehelp.Data.MyHelpRequestsData
import dimitrijestefan.mosis.ehelp.Data.UsersLocationData
import dimitrijestefan.mosis.ehelp.Models.Friend
import dimitrijestefan.mosis.ehelp.Models.GeoPoint
import dimitrijestefan.mosis.ehelp.Models.HelpRequest
import dimitrijestefan.mosis.ehelp.Models.User
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_rank_list.*

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var fusedLocationClient:FusedLocationProviderClient

    private lateinit var googleMap: GoogleMap
    private lateinit var markerPerson: HashMap<User, Int>

    private lateinit var mClusteManagerRenderer: MyClusterManagerRenderer
    private lateinit var mClusterManager: ClusterManager<ClusterMarker>
    private lateinit var mUsersMarkerCollection: MarkerManager.Collection
    private lateinit var mHelpRequetsMarkerCollection : MarkerManager.Collection
    private lateinit var markerHelpRequestsIdMap:HashMap<Marker,Int>
    private var mClusterMarkers: ArrayList<ClusterMarker> = ArrayList<ClusterMarker>()
    private lateinit var markerUsersIdMap: HashMap<Marker, Int>
    private lateinit var markerFriendsIdMap: HashMap<ClusterMarker, Int>




    private val addObjectViewModel by lazy {
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

    private fun getLastLocation(){
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            mapViewModel.current_location = LatLng(it.latitude,it.longitude)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapViewModel.current_location,15f))
            Toast.makeText(requireContext(),mapViewModel.current_location.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map;
        getLastLocation()
        //zar me treba poziv kasnije?
        //  googleMap?.isMyLocationEnabled=true;
        if (ContextCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        )
        {
            requestPermissions(Array<String>(1) { android.Manifest.permission.ACCESS_FINE_LOCATION }, 102)
        }
        else {

            googleMap.isMyLocationEnabled = true;
            if (addObjectViewModel.select) {
                setOnMapClickListener()

            }

            filterButton.setOnClickListener {
                this.findNavController().navigate(R.id.filterMap)
            }
            mClusterManager = ClusterManager<ClusterMarker>(requireActivity().applicationContext, googleMap)

//            if (mapViewModel.filter == true) {
//                AllHelpRequestsData.filterRequests(mapViewModel.title,mapViewModel.category,mapViewModel.urgency,mapViewModel.current_location,mapViewModel.radius)
//                onLocationUpdate()
//            } else {
//                onLocationUpdate()
//            }


            onLocationUpdate()
            UsersLocationData.onUserLocationChanged.observe(viewLifecycleOwner, Observer {
                onLocationUpdate()
            })
            UsersLocationData.onFriendLocationChanged.observe(viewLifecycleOwner, Observer {
                onLocationUpdate()
            })

            AllHelpRequestsData.onRequestsChange.observe(viewLifecycleOwner, Observer {

                if (mapViewModel.filter == true) {
                //TODO pozovi filter f-ju
                    AllHelpRequestsData.filterRequests(mapViewModel.title,mapViewModel.urgency,mapViewModel.category,mapViewModel.current_location,mapViewModel.radius)
                    onLocationUpdate()
                } else {
                    onLocationUpdate()
                }

            })

        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            102 -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                )
                {
                    this.googleMap.isMyLocationEnabled=true
                    getLastLocation()
                    if (addObjectViewModel.select) {
                        setOnMapClickListener()

                    }

                    filterButton.setOnClickListener {
                        this.findNavController().navigate(R.id.filterMap)
                    }
                    mClusterManager = ClusterManager<ClusterMarker>(requireActivity().applicationContext, googleMap)

//            if (mapViewModel.filter == true) {
//                AllHelpRequestsData.filterRequests(mapViewModel.title,mapViewModel.category,mapViewModel.urgency,mapViewModel.current_location,mapViewModel.radius)
//                onLocationUpdate()
//            } else {
//                onLocationUpdate()
//            }

                    onLocationUpdate()
                    UsersLocationData.onUserLocationChanged.observe(viewLifecycleOwner, Observer {
                        onLocationUpdate()
                    })
                    UsersLocationData.onFriendLocationChanged.observe(viewLifecycleOwner, Observer {
                        onLocationUpdate()
                    })

                    AllHelpRequestsData.onRequestsChange.observe(viewLifecycleOwner, Observer {

                        if (mapViewModel.filter == true) {
                            //TODO pozovi filter f-ju
                            AllHelpRequestsData.filterRequests(mapViewModel.title,mapViewModel.urgency,mapViewModel.category,mapViewModel.current_location,mapViewModel.radius)
                            onLocationUpdate()
                        } else {
                            onLocationUpdate()
                        }

                    })
                } else {

                    Toast.makeText(requireContext(), "Sorry, granted permission is necessary.", Toast.LENGTH_LONG).show()
                }
                return

            }
        }

    }

    private fun onLocationUpdate() {
        googleMap.clear()
        addUsersMarkers()
        addFriendsMarkers()
        addHelpRequestsMarker()
        googleMap.setOnMarkerClickListener(mClusterManager)
        googleMap.setInfoWindowAdapter(mClusterManager.markerManager)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return inflater.inflate(R.layout.fragment_map, container, false)
    }


    fun setOnMapClickListener() {

        googleMap.setOnMapClickListener { lokacija ->

            addObjectViewModel.location = lokacija
            addObjectViewModel.changeSelect()

            this.findNavController().navigate(R.id.returnCoords)
        }

    }


    private fun addHelpRequestsMarker(){
        var helpRequest: ArrayList<HelpRequest>
        if(AllHelpRequestsData.filtered_requests.size>0 && mapViewModel.filter){
            helpRequest = AllHelpRequestsData.filtered_requests
        }else if (AllHelpRequestsData.filtered_requests.size==0 && mapViewModel.filter){
            helpRequest = ArrayList()
        }else{
            helpRequest = AllHelpRequestsData.requests
        }
        //var helpRequest: ArrayList<HelpRequest> = AllHelpRequestsData.requests
        mHelpRequetsMarkerCollection = mClusterManager.markerManager.newCollection()
        markerHelpRequestsIdMap = HashMap<Marker, Int>()
        var loc: LatLng
        var markerOptions: MarkerOptions
        var requestmarker: Marker
        for ((index, value) in helpRequest.withIndex()) {
            loc = LatLng(value.latitude!!.toDouble(), value.longitude!!.toDouble())
            markerOptions = MarkerOptions()
            markerOptions.position(loc)

            if(value.userId == FirebaseAuth.getInstance().currentUser!!.uid){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ehelplogored))
            }else{
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ehelplogo48dp))
            }

            //usermarker = googleMap.addMarker(markerOptions)
            requestmarker = mHelpRequetsMarkerCollection.addMarker(markerOptions)
            //  usermarker.tag = OnUserMarkerClickListener
            markerHelpRequestsIdMap.put(requestmarker, index)
        }
        mHelpRequetsMarkerCollection.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(p0: Marker?): Boolean {
                Toast.makeText(context, "Kliknuto na helpRequest", Toast.LENGTH_LONG).show()
                return true
            }

        })

    }

    private fun addUsersMarkers() {
        var users: ArrayList<GeoPoint> = UsersLocationData.usersLocation
        mUsersMarkerCollection = mClusterManager.markerManager.newCollection()
        markerUsersIdMap = HashMap<Marker, Int>()
        var loc: LatLng
        var markerOptions: MarkerOptions
        var usermarker: Marker
        for ((index, value) in users.withIndex()) {
            loc = LatLng(value.latitude, value.longitude)
            markerOptions = MarkerOptions()
            markerOptions.position(loc)
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_map))
            usermarker = mUsersMarkerCollection.addMarker(markerOptions)
            markerUsersIdMap.put(usermarker, index)
        }
        mUsersMarkerCollection.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(p0: Marker?): Boolean {
                //Toast.makeText(context, "Kliknuto na usera", Toast.LENGTH_LONG).show()
                return true
            }

        })

    }


    private fun addFriendsMarkers() {

        mClusteManagerRenderer =
            MyClusterManagerRenderer(requireActivity(), googleMap, mClusterManager)
        mClusterManager.renderer = mClusteManagerRenderer

        var friends: ArrayList<GeoPoint> = UsersLocationData.friendsLocation
        markerFriendsIdMap = HashMap<ClusterMarker, Int>()
        var customMarker: ClusterMarker
        var friend: Friend
        var index1: Int

        for ((index, value) in friends.withIndex()) {
            index1 = FriendData.friendsListIndexMapping.get(value.userId)!!
            friend = FriendData.friendsList.get(index1)
            customMarker = ClusterMarker(
                LatLng(value.latitude, value.longitude),
                friend.email,
                "Nekiiisasasaasaassasa",
                friend
            )
            mClusterManager.addItem(customMarker)
            mClusterMarkers.add(customMarker)
        }
        mClusterManager.markerCollection.setInfoWindowAdapter(CustomInfoWindowFriendAdapter(mapViewModel,requireContext()))

        mClusterManager.setOnClusterItemClickListener(object :
            ClusterManager.OnClusterItemClickListener<ClusterMarker> {
            override fun onClusterItemClick(item: ClusterMarker?): Boolean {
             //   Log.e("Custom marker click", item?.title.toString())

               // Toast.makeText(context, item?.title.toString(), Toast.LENGTH_SHORT).show()
                mapViewModel.clickedFriend= item?.getFriend()!!
                return false
            }

        })
        mClusterManager.cluster()

    }


    private fun addMarker() {

    }

}
