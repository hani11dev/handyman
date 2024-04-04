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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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

class JobsViewModel : ViewModel() {

    private val _jobs = MutableStateFlow<List<Job>>(emptyList())
    val jobs: StateFlow<List<Job>> = _jobs

    fun getJobs() {
        viewModelScope.launch {
            val jobsList = mutableListOf<Job>()
            val db = Firebase.firestore
            val jobsCollection = db.collection("Jobs")
            jobsCollection.get().addOnSuccessListener { result ->
                for (document in result.documents) {
                    val jobId = document.id
                    val job = document.toObject(Job::class.java)!!
                    job.id = jobId
                    jobsList.add(job)
                }
                _jobs.value = jobsList
            }.addOnFailureListener { exception ->
            }
        }
    }
}
@Composable
fun JobItem(job: Job) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp),
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
fun JobsScreen() {
    val viewModel = JobsViewModel()

    LaunchedEffect(Unit) {
        viewModel.getJobs()
    }

    val jobs by viewModel.jobs.collectAsState(emptyList())

    LazyColumn {
        items(jobs) { job ->
            JobItem(job)
        }
    }
}