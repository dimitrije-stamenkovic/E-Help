const functions = require('firebase-functions');
const admin = require('firebase-admin');
const { user } = require('firebase-functions/lib/providers/auth');
admin.initializeApp(functions.config().firebase);



exports.rankFunction =functions.https.onRequest(async (request,response)=>{
let userId=request.body.userId;
let users =admin.database().ref('Users').orderByChild('email');
 users.on('value', snap=>{
    let usersArray=[]
   usersArray= snap;
   let rank=1;
    usersArray.forEach(element => {
        if(element.key===userId)
        {
            return rank;
        }else
        rank++;
        console.log(rank.toString());  
    });
     
    response.json({"rank":rank})
 });

});
