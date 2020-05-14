package dimitrijestefan.mosis.ehelp.domain

//enum class Urgency(val urgency: Int){
//    URGENTLY(1),
//    MIXED(2),
//    NOT_URGENTLY(3)
//}
//
//enum class Category(val category: Int){
//    MEDICINE(1),
//    FOOD(2),
//    OTHER(3)
//}

data class HelpRequest(val title:String,
                       val urgency : String,
                       val category: String,
                       val About:String,
                       val latitude:String,
                       val longitude:String)

