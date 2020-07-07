package dimitrijestefan.mosis.ehelp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dimitrijestefan.mosis.ehelp.Data.AllHelpRequestsData
import dimitrijestefan.mosis.ehelp.Models.HelpRequest
import kotlinx.android.synthetic.main.fragment_help_request.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "helpRequestId"



class HelpRequestFragment : Fragment() {

    private val mapViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MapViewModel::class.java)
    }

    // TODO: Rename and change types of parameters

    //private var param1: String? = null
    lateinit var request : HelpRequest
    lateinit var user_id : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user_id = FirebaseAuth.getInstance().currentUser!!.uid
        arguments?.let {
            //param1 = it.getString(ARG_PARAM1)
            request = it.getString(ARG_PARAM1)?.let { it1 -> AllHelpRequestsData.getRequest(it1) }!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        //Toast.makeText(requireContext(),request.toString(), Toast.LENGTH_SHORT).show()


        return inflater.inflate(R.layout.fragment_help_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title.text = request.title
        urgency.text = request.urgency
        category.text = request.category
        latitude.text = request.latitude
        longitude.text = request.longitude
        about.text = request.about

        if(AllHelpRequestsData.distance(mapViewModel.current_location.latitude,mapViewModel.current_location.longitude,request.latitude!!.toDouble(),request.longitude!!.toDouble())*1000>10 || request.userId == user_id){
            help_button.isEnabled = false
        }

        help_button.setOnClickListener {
            Toast.makeText(requireContext(),"URADI",Toast.LENGTH_SHORT).show()
        }


    }

}