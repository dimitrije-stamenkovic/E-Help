package dimitrijestefan.mosis.ehelp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dimitrijestefan.mosis.ehelp.Models.FriendRequest
import dimitrijestefan.mosis.ehelp.Models.User
import dimitrijestefan.mosis.ehelp.R

//class RecycleViewAdapterAddFriends(val userList:ArrayList<User>, var itemClickListener:OnItemClickListener,
//                                   var imageClickListener: OnImageClickListener):
//    RecyclerView.Adapter<RecycleViewAdapterAddFriends.ViewHolder>() {
//
//    lateinit var  mDeleteImage: ImageView
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//       var view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_friend,parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun getItemCount(): Int {
//       return userList.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        var user:User = userList[position]
//        holder.title.text=user.name
//        holder.title2.text=user.lastname
//        holder.bind(user,itemClickListener,position,imageClickListener)
//    }
//
////    class ViewHolder(item : View):RecyclerView.ViewHolder(item){
////        var title:TextView= item.findViewById(R.id.titleAdd)
////        var title2:TextView=item.findViewById(R.id.titleAdd2)
////        var image:ImageView=item.findViewById(R.id.deleteImage)
////
////        fun bind(user:User, clickListener:OnItemClickListener,position:Int,imgClick:OnImageClickListener){
////            title.text=user.name
////            title2.text=user.email
////            itemView.setOnClickListener {
////                clickListener.onItemClick(position.toString())
////            }
////            image.setOnClickListener {
////                imgClick.onImageClick(position)
////            }
////        }
////
////    }


  public  interface OnItemClickListener{
       fun onItemClick(position:String?)
    }

    public interface OnImageClickListener{
        fun onImageClick(position:Int)
    }

    // novi interfejs

    public interface OnUserClickListener{
        fun onUserClick(userId:String?)
    }


    public interface OnDeleteImgClickListener{
        fun onDeleteImgClick(userId: String?)
    }
    public interface OnAddImgClickListener{
        fun onAddImgClick(userId: String?)
    }




//}

public class UsersViewHolder(var mview : View) : RecyclerView.ViewHolder(mview) {

  //  var title: TextView = mview.findViewById(dimitrijestefan.mosis.ehelp.R.id.userNameF)
   // var title2: TextView =mview.findViewById(dimitrijestefan.mosis.ehelp.R.id.userNameF)
    var name:TextView= mview.findViewById(R.id.userNameF)
    var lastname:TextView= mview.findViewById(R.id.userLastnameF)
    var email:TextView= mview.findViewById(R.id.userEmailF)
    fun bind(user:User, clickListener: OnUserClickListener, userId:String?){
        itemView.setOnClickListener {
            clickListener.onUserClick(userId)
        }
    }

}

public  class RequestsViewHolder(var mview:View): RecyclerView.ViewHolder(mview){

    var name: TextView=mview.findViewById(R.id.ReqName)
    var lastname: TextView=mview.findViewById(R.id.ReqLastname)
    var email: TextView=mview.findViewById(R.id.ReqEmail)

    var imageDelete:ImageView= mview.findViewById(R.id.deleteImage)
    var imageAdd:ImageView=mview.findViewById(R.id.addImage)

    fun bind(friendReq:FriendRequest,deleteClickListener:OnDeleteImgClickListener
             ,addClickListener:OnAddImgClickListener )
    {
        imageDelete.setOnClickListener {
            deleteClickListener.onDeleteImgClick(friendReq.uidSender)
        }
        imageAdd.setOnClickListener {
            addClickListener.onAddImgClick(friendReq.uidSender)
        }
    }
}