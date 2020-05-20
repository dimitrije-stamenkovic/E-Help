package dimitrijestefan.mosis.ehelp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import kotlinx.android.synthetic.main.add_object_fragment.*
import kotlinx.android.synthetic.main.add_object_fragment.spinner_category
import kotlinx.android.synthetic.main.add_object_fragment.spinner_urgency
import kotlinx.android.synthetic.main.fragment_map_filter.*

/**
 * A simple [Fragment] subclass.
 */
class MapFilterFragment : Fragment() {

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

        }

    }
}
