package dimitrijestefan.mosis.ehelp.Data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import dimitrijestefan.mosis.ehelp.Models.User

interface OnGetDataListener {
    fun onStart()
    fun onSuccess(data : DataSnapshot)
    fun onFailed(databaseError: DatabaseError)
}