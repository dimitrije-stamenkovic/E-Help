package dimitrijestefan.mosis.ehelp
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.fragment_map.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dimitrijestefan.mosis.ehelp.Data.AllHelpRequestsData
import dimitrijestefan.mosis.ehelp.Models.User

class MapFragment : Fragment(),OnMapReadyCallback {


    private lateinit var googleMap:GoogleMap
    private lateinit var markerPerson:HashMap<User,Int>

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

        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }



    fun setOnMapClickListener(){

            googleMap.setOnMapClickListener { lokacija ->

                addObjectViewModel.location = lokacija
                addObjectViewModel.changeSelect()

                this.findNavController().navigate(R.id.returnCoords)
            }

    }

    private fun addMarker(){

    }

}
