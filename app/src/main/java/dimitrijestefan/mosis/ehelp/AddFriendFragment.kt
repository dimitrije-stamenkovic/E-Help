package dimitrijestefan.mosis.ehelp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import dimitrijestefan.mosis.ehelp.Adapters.RecycleViewAdapterAddFriends
import dimitrijestefan.mosis.ehelp.Models.User
import kotlinx.android.synthetic.main.fragment_add_friend.*

/**
 * A simple [Fragment] subclass.
 */
class AddFriendFragment : Fragment(),RecycleViewAdapterAddFriends.OnItemClickListener,RecycleViewAdapterAddFriends.OnImageClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_friend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycleViewAddFriend.layoutManager= LinearLayoutManager(requireContext(),LinearLayout.VERTICAL, false)
        var users= ArrayList<User>()
        users.add(User("Maname","Maname","Maname","Maname","Maname","Maname","Maname"))
        users.add(User("Maname1","Maname","Maname","Maname","Maname","Maname","Maname"))
    var adapter= RecycleViewAdapterAddFriends(users, this, this)
        recycleViewAddFriend.setAdapter(adapter)
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this.activity, position.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onImageClick(position: Int) {
        Toast.makeText(this.activity, "kliknuta slika "+position.toString(), Toast.LENGTH_LONG).show()
    }

}
