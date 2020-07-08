package dimitrijestefan.mosis.ehelp


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dimitrijestefan.mosis.ehelp.Data.AllHelpRequestsData
import kotlinx.android.synthetic.main.add_object_fragment.*
import kotlinx.android.synthetic.main.add_object_fragment.spinner_category
import kotlinx.android.synthetic.main.add_object_fragment.spinner_urgency
import kotlinx.android.synthetic.main.fragment_map_filter.*

/**
 * A simple [Fragment] subclass.
 */
class MapFilterFragment : Fragment() {


    private val mapViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MapViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val urgency = resources.getStringArray(R.array.Urgency)
        val urgency_adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,urgency)
        spinner_urgency.adapter = urgency_adapter

        val category = resources.getStringArray(R.array.Category)
        val category_adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,category)
        spinner_category.adapter = category_adapter



        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(seekBar: SeekBar?) {

                seekBar?.let {
                    kmLabel.text= seekBar.progress.toString() + "km"
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

                seekBar?.let {
                    kmLabel.text= seekBar.progress.toString() + "km"
                }

            }

            override fun onProgressChanged(seekBar: SeekBar,i:Int,boolean: Boolean){
                kmLabel.text= i.toString() + "km"
            }
        })


        filterButton.setOnClickListener {

            if(mapViewModel.reset==true){
                mapViewModel.filter =  false
                mapViewModel.reset = false
            }else
                mapViewModel.filter =  true


            mapViewModel.title = edit_title2.text.toString()
            mapViewModel.urgency = spinner_urgency.selectedItem.toString()
            mapViewModel.category = spinner_category.selectedItem.toString()
            mapViewModel.radius = seekBar.progress.toString()
            this.findNavController().navigate(R.id.action_mapFilterFragment_to_mapFragment)
        }

        textView11.setOnClickListener {
            Toast.makeText(requireContext(),"Filters have been reset",Toast.LENGTH_SHORT).show()
            edit_title2?.setText("")
            spinner_category.setSelection(0)
            spinner_urgency.setSelection(0)
            AllHelpRequestsData.filtered_requests.clear()
            //Log.d("DISTANE",AllHelpRequestsData.filtered_requests.toString())
            mapViewModel.reset = true
        }

        textView12.setOnClickListener {
            mapViewModel.filter = false

            //Log.d("DISTANE",AllHelpRequestsData.filtered_requests.toString())
            this.findNavController().navigate(R.id.action_mapFilterFragment_to_mapFragment)
        }



    }
}
