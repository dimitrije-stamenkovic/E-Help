package dimitrijestefan.mosis.ehelp

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.collections.MarkerManager
import dimitrijestefan.mosis.ehelp.CustomMarker.ClusterMarker
import dimitrijestefan.mosis.ehelp.CustomMarker.MyClusterManagerRenderer
import dimitrijestefan.mosis.ehelp.Data.AllHelpRequestsData
import dimitrijestefan.mosis.ehelp.Data.FriendData
import dimitrijestefan.mosis.ehelp.Data.UsersLocationData
import dimitrijestefan.mosis.ehelp.Models.Friend
import dimitrijestefan.mosis.ehelp.Models.GeoPoint
import dimitrijestefan.mosis.ehelp.Models.User
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : Fragment(), OnMapReadyCallback {


    private lateinit var googleMap: GoogleMap
    private lateinit var markerPerson: HashMap<User, Int>

    private lateinit var mClusteManagerRenderer: MyClusterManagerRenderer
    private lateinit var mClusterManager: ClusterManager<ClusterMarker>
    private lateinit var mUsersMarkerCollection: MarkerManager.Collection
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


    override fun onMapReady(map: GoogleMap) {
        googleMap = map;
        //  googleMap?.isMyLocationEnabled=true;
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                Array<String>(1) { android.Manifest.permission.ACCESS_FINE_LOCATION },
                101
            )
        } else {
            googleMap.isMyLocationEnabled = true;
            if (addObjectViewModel.select) {
                setOnMapClickListener()

            }

            if (mapViewModel.filter == true) {
                Toast.makeText(
                    requireContext(),
                    mapViewModel.filtered_requests.toString(),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    AllHelpRequestsData.requests.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }

            filterButton.setOnClickListener {
                this.findNavController().navigate(R.id.filterMap)
            }
            mClusterManager =
                ClusterManager<ClusterMarker>(requireActivity().applicationContext, googleMap)

            onLocationUpdate()
            UsersLocationData.onUserLocationChanged.observe(viewLifecycleOwner, Observer {
                onLocationUpdate()
            })
            UsersLocationData.onFriendLocationChanged.observe(viewLifecycleOwner, Observer {
                onLocationUpdate()
            })

        }

    }

    private fun onLocationUpdate() {
        googleMap.clear()
        addUsersMarkers()
        addFriendsMarkers()
        googleMap.setOnMarkerClickListener(mClusterManager)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }


    fun setOnMapClickListener() {

        googleMap.setOnMapClickListener { lokacija ->

            addObjectViewModel.location = lokacija
            addObjectViewModel.changeSelect()

            this.findNavController().navigate(R.id.returnCoords)
        }

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
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_profile))
            //usermarker = googleMap.addMarker(markerOptions)
            usermarker = mUsersMarkerCollection.addMarker(markerOptions)
            //  usermarker.tag = OnUserMarkerClickListener
            markerUsersIdMap.put(usermarker, index)
        }
        mUsersMarkerCollection.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(p0: Marker?): Boolean {
                Toast.makeText(context, "Kliknuto na usera", Toast.LENGTH_LONG).show()
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

        mClusterManager.setOnClusterItemClickListener(object :
            ClusterManager.OnClusterItemClickListener<ClusterMarker> {
            override fun onClusterItemClick(item: ClusterMarker?): Boolean {
                Log.e("Custom marker click", item?.title.toString())
                Toast.makeText(context, item?.title.toString(), Toast.LENGTH_SHORT).show()
                return true
            }

        })
        mClusterManager.cluster()

    }


    private fun addMarker() {

    }

}
