package dimitrijestefan.mosis.ehelp.ViewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import dimitrijestefan.mosis.ehelp.Data.HttpRequestsSingleton
import org.json.JSONObject

class RankViewModel: ViewModel(){
    var userRank=MutableLiveData<String>()

    fun getUserRank(uid:String, context:Context){
        val url="https://us-central1-ehelp-277212.cloudfunctions.net/rankFunction"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,url,JSONObject().put("userId",uid),
            Response.Listener<JSONObject>(){ response ->
                //  Log.e("Rank list",response.getString("rank").toString())
                //txtLabel.setText(response.getString("rank"))
                userRank.value=response.getString("rank")
            },
            Response.ErrorListener { error: VolleyError? ->
                Log.e("Rank list errro", error.toString())
            }

        )

        HttpRequestsSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)

    }

}