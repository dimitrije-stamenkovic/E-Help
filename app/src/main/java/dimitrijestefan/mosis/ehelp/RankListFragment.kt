package dimitrijestefan.mosis.ehelp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import dimitrijestefan.mosis.ehelp.Data.MyHelpRequests
import kotlinx.android.synthetic.main.fragment_rank_list.*

/**
 * A simple [Fragment] subclass.
 */
class RankListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rank_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


       MyHelpRequests.onRequestsChange.observe(viewLifecycleOwner, Observer {

           if (MyHelpRequests.requests != null){
               temp.text = MyHelpRequests.requests.toString()

           }

       })
    }
}
