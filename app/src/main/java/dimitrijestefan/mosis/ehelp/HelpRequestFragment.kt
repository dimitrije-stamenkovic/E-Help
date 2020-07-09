package dimitrijestefan.mosis.ehelp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dimitrijestefan.mosis.ehelp.Data.AllHelpRequestsData
import dimitrijestefan.mosis.ehelp.Data.UserData
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
    lateinit var helpRequestRef:DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user_id = FirebaseAuth.getInstance().currentUser!!.uid
        arguments?.let {
            //param1 = it.getString(ARG_PARAM1)
            request = it.getString(ARG_PARAM1)?.let { it1 -> AllHelpRequestsData.getRequest(it1) }!!
        }
        helpRequestRef=FirebaseDatabase.getInstance().getReference().child("Requests")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_help_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title.text = request.title
        urgency.text = request.urgency
        category.text = request.category
        about.text = request.about

        when{
            request.userId == user_id -> {
                help_button.isEnabled=false
                Toast.makeText(requireContext(),"You cannot answer your own help request.",Toast.LENGTH_LONG).show()
            }
            AllHelpRequestsData.distance(mapViewModel.current_location.latitude,mapViewModel.current_location.longitude,request.latitude!!.toDouble(),request.longitude!!.toDouble())*1000>50 ->{
                help_button.isEnabled=false
                Toast.makeText(requireContext(),"Help request is far away",Toast.LENGTH_LONG).show()
            }
        }

        help_button.setOnClickListener {
            Help(request)
        }

    }

    fun Help(helpRequest: HelpRequest){
        when(helpRequest.urgency){
            "Urgently"->UserData.updateUserPoints(15)
            "Mixed"->UserData.updateUserPoints(10)
            "Not Urgently"->UserData.updateUserPoints(5)
        }
        helpRequestRef.child(helpRequest.key)
            .removeValue()
            .addOnCompleteListener {task ->
                        if(task.isSuccessful){
                            this.findNavController().navigate(R.id.mapFragment)
                        }
            }
    }




}