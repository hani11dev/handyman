import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.handyapp.Response
import com.example.handyapp.home.jobs.JobsViewModel
import com.example.handyapp.navigation.Screen
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Job(
    var id: String = "",
    val category: String = "",
    val city: String = "",
    val day: String = "",
    val description: String = "",
    val hour: String = "",
    val max: Int = 0,
    val min: Int = 0,
    val status: String = "",
    val street: String = "",
    val title: String = "",
    val userId: String = "",
    val wilaya: String = "",
)


@Composable
fun JobItem(job: Job , navHostController: NavHostController) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable {
                       navHostController.navigate(Screen.JobsDetails.route + "/${job.id}")
            },
        elevation =  CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "ID: ${job.id}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Blue
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = job.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = job.category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = job.description,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Budget: ${job.min} - ${job.max} DA",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Blue
                )

                IconButton(onClick = { }) {
                    Icon(Icons.Default.Favorite, contentDescription = "Favorite")
                }
            }
        }
    }
}

@Composable
fun JobsScreen(navHostController: NavHostController , viewModel: JobsViewModel = hiltViewModel() , context : Context = LocalContext.current) {
   // val viewModel = JobsViewModel()

    /*LaunchedEffect(Unit) {
        viewModel.getJobs()
    }*/

    //val jobs by viewModel.jobs.collectAsState(emptyList())
    val lists = listOf(
        Job("safsa" , "painter" , "asfas" , "fasfa" , "faslj" , "" , 2420 , 1512 , "" , "","title", "" , ""),
        Job("safsa" , "painter" , "asfas" , "fasfa" , "faslj" , "" , 2420 , 1512 , "" , "","title", "" , ""),
        Job("safsa" , "painter" , "asfas" , "fasfa" , "faslj" , "" , 2420 , 1512 , "" , "","title", "" , ""),
        Job("safsa" , "painter" , "asfas" , "fasfa" , "faslj" , "" , 2420 , 1512 , "" , "","title", "" , ""),
        Job("safsa" , "painter" , "asfas" , "fasfa" , "faslj" , "" , 2420 , 1512 , "" , "","title", "" , ""),
        Job("safsa" , "painter" , "asfas" , "fasfa" , "faslj" , "" , 2420 , 1512 , "" , "","title", "" , ""),
    )

    when(val resp = viewModel.jobs.value){
        is Response.onLoading -> {
            //Toast.makeText(context , "load" ,Toast.LENGTH_SHORT).show()
        }
        is Response.onFaillure -> {Toast.makeText(context , "failled" ,Toast.LENGTH_SHORT).show()}
        is Response.onSuccess -> {
            //Toast.makeText(context , "succes" ,Toast.LENGTH_SHORT).show()
            if (resp.data.isNotEmpty()){
                LazyColumn {
                    items(resp.data) { job ->
                        JobItem(job , navHostController)
                    }
                }
            }
        }
    }


}