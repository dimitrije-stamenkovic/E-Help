package dimitrijestefan.mosis.ehelp

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import dimitrijestefan.mosis.ehelp.domain.HelpRequest
import dimitrijestefan.mosis.ehelp.domain.HelpRequestManager

import kotlinx.android.synthetic.main.add_object_fragment.*


class AddObjectFragment : Fragment() {




    companion object {
        fun newInstance() = AddObjectFragment()
    }

    private  val viewModel by lazy {
        //ViewModelProviders.of(requireActivity()).get(AddObjectViewModel::class.java)
        ViewModelProvider(requireActivity()).get(AddObjectViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_object_fragment, container, false)
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(AddObjectViewModel::class.java)
//
//        // TODO: Use the ViewModel
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // viewModel = ViewModelProviders.of(this).get(AddObjectViewModel::class.java)

        val urgency = resources.getStringArray(R.array.Urgency)
        val urgency_adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,urgency)
        spinner_urgency.adapter = urgency_adapter

        val category = resources.getStringArray(R.array.Category)
        val category_adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,category)
        spinner_category.adapter = category_adapter




        button_addObject.setOnClickListener {
            val manager = HelpRequestManager



            val request = HelpRequest(edit_title.text.toString()
                ,spinner_urgency.selectedItem.toString()
                ,spinner_category.selectedItem.toString()
                ,edit_about.text.toString()
                ,"22","22")


            viewModel.addRequest(request)

            viewModel.getLat().observe(requireActivity(), Observer<String> {string -> Toast.makeText(requireContext(),string,Toast.LENGTH_LONG).show()
            })


            Toast.makeText(context,manager.getHelpRequestsList().toString(),Toast.LENGTH_LONG).show()

        }

        button_getCoords.setOnClickListener {
           viewModel.getLat().observe(requireActivity(), Observer<String> {string -> edit_lat.setText(string)
            })
          //  Toast.makeText(requireContext(), viewModel.pom.toString(),Toast.LENGTH_LONG).show();
        }



    }



}
