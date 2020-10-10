const functions = require('firebase-functions');
const admin = require('firebase-admin');
const { user } = require('firebase-functions/lib/providers/auth');
admin.initializeApp(functions.config().firebase);
const toRad=Math.PI/180;
const R=6371e3;




exports.rankFunction =functions.https.onRequest(async (request,response)=>{
let userId=request.body.userId;
let users =admin.database().ref('Users').orderByChild('points');
 users.on('value', snap=>{
    let usersArray=[]
   usersArray= snap;
   let rank=0;
   let numberOfUsers= snap.numChildren();
    usersArray.forEach(element => {
        if(element.key===userId)
        {
            return rank;
        }else
        rank++;
         
    });
    let usersRank=numberOfUsers-rank;
     console.log(usersRank);
     console.log("ce daju majicu valjda");

    response.json({"rank":usersRank});
 });

});


exports.nearUserObject=functions.database.ref('/UsersLocations/{userId}')
.onWrite(async (snapshot, context)=> {
    var podaci=snapshot.after.val();
    var userLatitude=podaci.latitude;
    var userLongitude=podaci.longitude;
    let usersLocationQuery=admin.database().ref('UsersLocations');

 usersLocationQuery.once('value', async snap=>{
     sendToUserNearUser=false;
    snap.forEach(function(location){     
        if(location.key!==context.params.userId){
           var locationpom=location.val();
           var near= distance(userLatitude, userLongitude,locationpom.latitude,locationpom.longitude);
            if(near){
                if(sendToUserNearUser)
                    sendNotification(location.key);
                 else{
                     sendNotification(context.params.userId);
                     sendNotification(location.key);
                     sendToUserNearUser=true;
                     }
            }
            else{
                console.log("Nije blizu");
            }

         }

     })

     let requestsQuery=admin.database().ref('Requests');
     requestsQuery.once('value',snapData=>{
         snapData.forEach(request=>{
         var requestpom=request.val();
         var near= distance(userLatitude, userLongitude,requestpom.latitude,requestpom.longitude);
         if(near){
            sendNotificationHelp(context.params.userId)
            return;
         }

         })
     })
 })  
})



 sendNotification= async(userId)=> {
const deviceToken=await admin.database().ref(`Users/${userId}/mToken`).once('value');
const payload = 
		{
			notification:
			{
				title: "E-help",
				body: `E-help user is nearby!`,
				icon: "default"
			}
		};
await admin.messaging().sendToDevice(deviceToken.val(),payload);
}

sendNotificationHelp= async(userId)=> {
    const deviceToken=await admin.database().ref(`Users/${userId}/mToken`).once('value');
    const payload = 
            {
                notification:
                {
                    title: "E-help",
                    body: `Help request is nearby!`,
                    icon: "default"
                }
            };
    await admin.messaging().sendToDevice(deviceToken.val(),payload);
    }


function distance(lat1,lon1,lat2, lon2){
    const f1= lat1*toRad;
    const f2=lat2*toRad;

    const deltaF=(lat2-lat1)*toRad;
    const deltaLam=(lon2-lon1)*toRad;

    const a=Math.sin(deltaF/2)* Math.sin(deltaF/2)+ Math.cos(f1)*Math.cos(f2)* Math.sin(deltaLam/2)*Math.sin(deltaLam/2);

    const c= 2* Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    const d= R* c; 
    if(d < 150) 
    return true;
    else
    return false;
}