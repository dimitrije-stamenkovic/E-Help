package dimitrijestefan.mosis.ehelp


import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.fragment_map.*
import java.util.jar.Manifest
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.add_object_fragment.*


/**
 * A simple [Fragment] subclass.
 */
class MapFragment : Fragment(),OnMapReadyCallback {


    private lateinit var googleMap:GoogleMap
    private  val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(AddObjectViewModel::class.java)
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

            if(viewModel.select){
                setOnMapClickListener()

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

                viewModel.location = lokacija
                viewModel.select = false

                this.findNavController().navigate(R.id.returnCoords)
            }

    }

}
