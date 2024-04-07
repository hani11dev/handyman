import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.handyapp.home.myRequests.MyRequestsScreenReal
import com.example.handyapp.home.myRequests.MyRequestsScreenReal
import com.example.handyapp.home.myRequests.getCollectionData
import com.example.handyapp.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase





data class REQUEST(
    var Title: String = "" ,
    var Description: String = "" ,
    var Wilaya: String = "" ,
    var City: String = "" ,
    var Street: String = "" ,
    var Day: String = "" ,
    var Hour: String = "" ,
    var Budget: Int = -1
)

@Composable
fun MyRequestsScreen(navController: NavHostController) {

    val requestsCollectionRef = Firebase.firestore.collection("requests")
    val clientsCollectionRef = Firebase.firestore.collection("Clients")

    val requests = remember { mutableStateListOf<Map<String, Any>>() }
    val currentUser = FirebaseAuth.getInstance().currentUser
    var userId = ""
    if(currentUser != null){
        userId = currentUser.uid
    }

    LaunchedEffect(key1 = Unit) {
        getCollectionData(requestsCollectionRef, userId) { updatedRequests ->
            requests.clear() // Clear the existing list
            requests.addAll(updatedRequests) // Add the updated requests
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(requests) { request ->
            MyRequestsScreenReal(
                context = LocalContext.current,
                clientRef = clientsCollectionRef,
                request = request,
                navController = navController
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


