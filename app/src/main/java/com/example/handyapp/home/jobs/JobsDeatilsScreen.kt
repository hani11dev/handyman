import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.handyapp.home.jobs.JobsDetailsViewModel
import com.example.handyapp.navigation.Screen
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

data class Bid(
    val description: String,
    val handymanID: String,
    val price: Int,
    val time: String
)

data class Handyman(
    val firstName: String,
    val lastName: String,
    val profileImageUrl: String
)

@Composable
fun JobDetailsScreen(
    viewModel: JobsDetailsViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val jobID: String = viewModel.jobID.value
    val db = Firebase.firestore
    var job by remember { mutableStateOf<Job?>(null) }
    val images = remember { mutableStateListOf<String>() }

    // Create an instance of SubcollectionCountViewModel
    val subcollectionCountViewModel: SubcollectionCountViewModel = hiltViewModel()

    // Pass the jobID to the ViewModel
    subcollectionCountViewModel.setJobID(jobID)

    // Observe total bids count and total price using SubcollectionCountViewModel
    val totalBids by subcollectionCountViewModel.subcollectionCountState
    val totalPrice by subcollectionCountViewModel.totalPriceState

    LaunchedEffect(key1 = jobID) {
        val jobDocument = db.collection("Jobs").document(jobID).get().await()
        if (jobDocument.exists()) {
            job = jobDocument.toObject(Job::class.java)
            images.clear()
            val loadedImages = try {
                loadImages(jobID)
            } catch (e: Exception) {
                Log.e("loadImages", "Error loading images: ${e.message}", e)
                emptyList()
            }
            images.addAll(loadedImages)
            Log.d("ImageURLs", "Image URLs: $loadedImages")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        job?.let { job ->
            JobDetailsPage(
                job = job,
                images = images,
                rootNavController = navHostController,
                jobID = jobID,
                totalBids = totalBids, // Pass total bids count to JobDetailsPage
                totalPrice = totalPrice // Pass total price to JobDetailsPage
            )
        } ?: run {
            Text(text = "Loading job details...")
        }
    }
}


@Composable
fun JobDetailsPage(
    job: Job,
    images: SnapshotStateList<String>,
    rootNavController: NavHostController,
    jobID: String,
    totalBids: Int,
    totalPrice: Int // New parameter for total price
) {
    var selectedImage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(end = 12.dp)
    ) {
        JobDetailsHeader(job = job)

        // Display total bids count
        Text(
            text = "Total Bids: $totalBids",
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Display average price
        Text(
            text = "Average Price: $${if (totalBids > 0) totalPrice / totalBids else 0}",
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ImageSection(images = images, onImageSelected = { imageUrl ->
            selectedImage = imageUrl
        })

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { rootNavController.navigate(Screen.BidScreen.route + "/${jobID}") },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Apply Now")
        }

        BidSection(jobID)

        selectedImage?.let { imageUrl ->
            AlertDialog(
                onDismissRequest = { selectedImage = null },
                title = { Text(text = "Image Detail") },
                text = { Image(painter = rememberImagePainter(imageUrl), contentDescription = null) },
                confirmButton = {
                    Button(
                        onClick = { selectedImage = null },
                    ) {
                        Text("Close")
                    }
                }
            )
        }
    }
}



@Composable
fun ImageSection(images: List<String>, onImageSelected: (String) -> Unit) {
    Column {
        Text(
            text = "Images",
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )
        LazyRow(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            items(images) { imageUrl ->
                Image(
                    painter = rememberImagePainter(imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp)
                        .padding(end = 16.dp)
                        .clickable {
                            onImageSelected(imageUrl)
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun BidSection(jobID: String) {
    val db = Firebase.firestore
    var bids by remember { mutableStateOf<List<Bid>?>(null) }
    var handymenMap by remember { mutableStateOf<Map<String, Handyman>?>(null) }

    LaunchedEffect(jobID) {
        val bidsSnapshot = db.collection("Jobs").document(jobID).collection("bids").get().await()
        bids = bidsSnapshot.documents.map { document ->
            val description = document.getString("description") ?: ""
            val handymanID = document.getString("handymandID") ?: ""
            val price = document.getLong("price")?.toInt() ?: 0
            val time = document.getString("time") ?: ""
            Bid(description, handymanID, price, time)
        }

        val handymanIds = bids?.map { it.handymanID }?.distinct()
        handymanIds?.let { ids ->
            val handymanDetails = mutableMapOf<String, Handyman>()
            ids.forEach { handymanId ->
                val handymanDocument = db.collection("HandyMan").document(handymanId).get().await()
                val firstName = handymanDocument.getString("FirstName") ?: ""
                val lastName = handymanDocument.getString("LastName") ?: ""
                val profileImageUrl = handymanDocument.getString("ProfileImage") ?: ""
                handymanDetails[handymanId] = Handyman(firstName, lastName, profileImageUrl)
            }
            handymenMap = handymanDetails
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Bids",
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )

        bids?.let { bidList ->
            if (bidList.isNotEmpty()) {
                bidList.forEachIndexed { index, bid ->
                    val handyman = handymenMap?.get(bid.handymanID)
                    handyman?.let {
                        Bid(
                            handymanImage = rememberImagePainter(handyman.profileImageUrl),
                            handymanName = "${handyman.firstName} ${handyman.lastName}",
                            description = bid.description,
                            time = bid.time,
                            price = bid.price
                        )
                        if (index < bidList.size - 1) {
                            Divider()
                        }
                    }
                }
            } else {
                Text(
                    text = "No bids available.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } ?: run {
            Text(
                text = "Loading bids...",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun Bid(
    handymanImage: Painter,
    handymanName: String,
    description: String,
    time: String,
    price: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = handymanImage,
                    contentDescription = "Handyman Image",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(percent = 50))
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = handymanName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(
                    text = "Time: $time",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Price: $$price",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}



private suspend fun loadImages(jobID: String): List<String> = withContext(Dispatchers.IO) {
    val storageRef = Firebase.storage.reference.child("Job/$jobID")
    val jobFolderRef = storageRef

    val images = mutableListOf<String>()
    try {
        val result = jobFolderRef.listAll().await()

        for (itemRef in result.items) {
            if (itemRef.path.endsWith(".jpg") || itemRef.path.endsWith(".png") || itemRef.path.endsWith(".jpeg")) {
                val imageUrl = itemRef.downloadUrl.await().toString()
                images.add(imageUrl)
            }
        }
    } catch (e: Exception) {
        Log.e("loadImages", "Error loading images: ${e.message}", e)
    }
    return@withContext images
}

@Composable
fun JobDetailsHeader(job: Job) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 32.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = job.title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Category: ${job.category}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Text(
                text = "Status: ${job.status}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Description:",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = job.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Location: ${job.street}, ${job.city}, ${job.wilaya}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Date: ${job.day} ${job.hour}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Budget:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Text(
                text = "$${job.min} - $${job.max}",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

//;;;;;;;;;;;;
class SubcollectionCountViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    val subcollectionCountState = mutableStateOf(0)
    val totalPriceState = mutableStateOf(0)
    private var jobID: String? = null

    fun setJobID(jobID: String) {
        this.jobID = jobID
        fetchSubcollectionCount()
    }

    private fun fetchSubcollectionCount() {
        jobID?.let { id ->
            val collectionRef = firestore.collection("Jobs")
            val subcollectionRef = collectionRef.document(id).collection("bids")

            subcollectionRef.get()
                .addOnSuccessListener { documents ->
                    subcollectionCountState.value = documents.size()
                    var total = 0
                    documents.forEach { document ->
                        total += document.getLong("price")?.toInt() ?: 0
                    }
                    totalPriceState.value = total
                }
                .addOnFailureListener { exception ->
                    // Handle any errors
                }
        }
    }
}
