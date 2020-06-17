package dimitrijestefan.mosis.ehelp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import dimitrijestefan.mosis.ehelp.Data.AllHelpRequests
import dimitrijestefan.mosis.ehelp.Data.MyHelpRequests


import kotlinx.android.synthetic.main.add_object_fragment.*



class AddObjectFragment : Fragment() {





    companion object {
        fun newInstance() = AddObjectFragment()
    }

    private  val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(AddObjectViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_object_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val urgency = resources.getStringArray(R.array.Urgency)
        val urgency_adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,urgency)
        spinner_urgency.adapter = urgency_adapter

        val category = resources.getStringArray(R.array.Category)
        val category_adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,category)
        spinner_category.adapter = category_adapter


         viewModel.location?.let { item ->
             edit_lon.setText(item.longitude.toString())
             edit_lat.setText(item.latitude.toString())
         }

        viewModel.title?.let {
            edit_title.setText(it)
        }

        viewModel.about?.let {
            edit_about.setText(it)
        }

        viewModel.urgency?.let {
            spinner_urgency.setSelection(it)
        }

        viewModel.category?.let {
            spinner_category.setSelection(it)
        }





        button_addObject.setOnClickListener {





            if(edit_title.text.toString().length > 0 &&
                spinner_urgency.selectedItem.toString().length > 0 &&
                spinner_category.selectedItem.toString().length > 0 &&
                edit_about.text.toString().length > 0 &&
                edit_lon.text.toString().length > 0 &&
                edit_lat.text.toString().length>0 ){

                MyHelpRequests.addNewHelpRequest(edit_title.text.toString(),spinner_urgency.selectedItem.toString()
                    ,spinner_category.selectedItem.toString(),
                    edit_lat.text.toString()
                    ,edit_lon.text.toString(),
                    edit_about.text.toString())
                AllHelpRequests.addNewHelpRequest(edit_title.text.toString(),spinner_urgency.selectedItem.toString()
                    ,spinner_category.selectedItem.toString(),
                    edit_lat.text.toString()
                    ,edit_lon.text.toString(),
                    edit_about.text.toString())
                Toast.makeText(context,MyHelpRequests.requests.toString(),Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context,"Invalid request",Toast.LENGTH_LONG).show()
            }



        }

        button_getCoords.setOnClickListener {

            viewModel.changeSelect()
            viewModel.category = spinner_category.selectedItemPosition
            viewModel.urgency = spinner_urgency.selectedItemPosition
            viewModel.about = edit_about.text.toString()
            viewModel.title = edit_title.text.toString()

            it.findNavController().navigate(R.id.getCoords)


        }


//
//        viewModel.getLiveData().observe(viewLifecycleOwner,object : Observer<ArrayList<HelpRequest>>{
//            override fun onChanged(t: ArrayList<HelpRequest>?) {
//                Toast.makeText(requireContext(),viewModel.getLiveData().value.toString(),Toast.LENGTH_SHORT).show()
//            }
//        })


    }



}
