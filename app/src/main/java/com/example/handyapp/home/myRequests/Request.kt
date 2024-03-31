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
import androidx.navigation.NavController
import com.example.handyapp.home.myRequests.MyRequestsScreenReal
import com.example.handyapp.home.myRequests.MyRequestsScreenReal
import com.example.handyapp.home.myRequests.getCollectionData
import com.example.handyapp.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun MyRequestsScreen(navController: NavController) {

    val requestsCollectionRef = Firebase.firestore.collection("requests")
    val clientsCollectionRef = Firebase.firestore.collection("Clients")
    val requests = remember { mutableStateListOf<Map<String, Any>>() }
    val currentUser = FirebaseAuth.getInstance().currentUser
    var userId = ""
    if(currentUser != null){
        userId = currentUser.uid
    }
    LaunchedEffect(key1 = Unit) {
        requests.addAll(
            getCollectionData(
                requestsCollectionRef,
                userId
            )
        )

    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(requests) { request ->
            MyRequestsScreenReal(
                context = LocalContext.current, // Pass context here
                clientRef = clientsCollectionRef,
                request = request,
                navController = navController,

            )
            Spacer(modifier = Modifier.height(16.dp))
        }

    }
}
