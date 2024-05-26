import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.handyapp.R

import com.example.handyapp.home.jobs.JobsDetailsViewModel
import com.example.handyapp.home.jobs.PageIndicator
import com.example.handyapp.navigation.Screen
import com.google.accompanist.insets.LocalWindowInsets
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.min

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
        /*HorizontalPager(state = pagerState) {
            Image(
                painter = rememberImagePainter(data = images[it]),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp),
                contentScale = ContentScale.Crop
            )
        }*/
        val scrollState = rememberLazyListState()
        /*job?.let { job ->
            JobDetailsPage(
                job = job,
                images = images,
                rootNavController = navHostController,
                jobID = jobID,
                totalBids = totalBids,
                totalPrice = totalPrice,
                ScrollState = scrollState// Pass total price to JobDetailsPage
            )
        } ?: run {
            Text(text = "Loading job details...")
        }

        CollapsingTopBar(scrollState = scrollState, images, job?.title ?: "")*/

        if (job != null){
            jobDetails2(
                job = job!!,
                images = images,
                rootNavController = navHostController,
                jobID = jobID,
                totalBids = totalBids,
                totalPrice = totalPrice,
                ScrollState = scrollState// Pass total price to JobDetailsPage
            )

        }


    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun jobDetails2(job: Job,
               images: SnapshotStateList<String>,
               rootNavController: NavHostController,
               jobID: String,
               totalBids: Int,
               totalPrice: Int, // Add parameter for total price,
               ScrollState: LazyListState){
    val pagerState = rememberPagerState(initialPage = 0) {
        images.size
    }
    Column (modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)){


        LazyColumn(
            modifier = Modifier
                .fillMaxSize().weight(1f)
                //.padding(16.dp)
            , state = ScrollState
        ) {
            item {


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(196.dp)
                        //.padding(16.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    if (images.isNotEmpty()) {
                        HorizontalPager(state = pagerState,) {
                            Image(
                                painter = if (images.isNotEmpty()) rememberImagePainter(data = images[it])
                                else painterResource(
                                    id = R.drawable.chat_back_dark
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(196.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                        PageIndicator(
                            modifier = Modifier
                                .width(32.dp)
                                .padding(bottom = 8.dp),
                            selectedPage = pagerState.currentPage,
                            pagesSize = images.take(3).size
                        )
                    } else {
                        Image(
                            painter = painterResource(
                                id = R.drawable.nophotos
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = job.title, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = job.city + ", " + job.wilya,
                        fontSize = 14.sp,
                        maxLines = 1,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${job.day}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )

                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${job.min} - ${job.max} DA",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 22.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Description:", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    text = job.description,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))
                BidSection(jobID = jobID, totalBids, totalPrice)


            }
        }
        ExtendedFloatingActionButton(
            onClick = { rootNavController.navigate(Screen.BidScreen.route + "/${jobID}") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            //containerColor = MaterialTheme.colorScheme.primary

        ) {
            Text(text = "Apply Now")
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CollapsingTopBar(
    scrollState: LazyListState,
    images: List<String>,
    title: String,
    context: Context = LocalContext.current
) {
    val imageHeight = 400.dp - 56.dp

    val maxOffset = with(LocalDensity.current) {
        imageHeight.roundToPx()
    } - LocalWindowInsets.current.systemBars.layoutInsets.top

    val offset = min(scrollState.firstVisibleItemScrollOffset, maxOffset)

    val offsetProgress = max(0f, offset * 3f - 2f * maxOffset) / maxOffset
    val pagerState = rememberPagerState(initialPage = 0) {
        images.size
    }
    androidx.compose.material.TopAppBar(
        contentPadding = PaddingValues(),
        backgroundColor = Color.White,
        modifier = Modifier
            .height(
                400.dp
            )
            .offset {
                IntOffset(x = 0, y = -offset)
            },
        elevation = if (offset == maxOffset) 4.dp else 0.dp
    ) {
        val GradientOne = Color(0xFFff422b)
        val GradientTwo = Color(0xFFff9400)
        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .graphicsLayer {
                        alpha = 1f - offsetProgress
                    }
                /*.background(
                    Brush.horizontalGradient(
                        listOf(
                            GradientOne, GradientTwo
                        )
                    )
                )*/,
                contentAlignment = Alignment.BottomCenter
            ) {
                if (images.isNotEmpty()) {
                    HorizontalPager(state = pagerState) {
                        Image(
                            painter = if (images.isNotEmpty()) rememberImagePainter(data = images[it])
                            else painterResource(
                                id = R.drawable.chat_back_dark
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    PageIndicator(
                        modifier = Modifier
                            .width(32.dp)
                            .padding(bottom = 16.dp),
                        selectedPage = pagerState.currentPage,
                        pagesSize = images.size
                    )
                } else {
                    Image(
                        painter = painterResource(
                            id = R.drawable.nophotos
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        contentScale = ContentScale.Crop
                    )
                }


            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalArrangement = Arrangement.Center
            ) {
                androidx.compose.material.Text(
                    text = title,
                    fontSize = 25.sp,
                    color = Color.Black,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = (16 + 28 * offsetProgress).dp)
                        .scale(1f - 0.25f * offsetProgress)
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JobDetailsPage(
    job: Job,
    images: SnapshotStateList<String>,
    rootNavController: NavHostController,
    jobID: String,
    totalBids: Int,
    totalPrice: Int, // Add parameter for total price,
    ScrollState: LazyListState
) {
    var selectedImage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    val pagerState = rememberPagerState(initialPage = 0) {
        images.size
    }
    Column {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                //.verticalScroll(scrollState)
                //.padding(end = 12.dp)
                .weight(1f),
            state = ScrollState,
            contentPadding = PaddingValues(top = 400.dp)
        ) {
            item {
                JobDetailsHeader(job = job, images)


                /*Spacer(modifier = Modifier.height(16.dp))

                ImageSection(images = images, onImageSelected = { imageUrl ->
                    selectedImage = imageUrl
                })*/

                Spacer(modifier = Modifier.height(16.dp))
                /*Button(
                    onClick = { rootNavController.navigate(Screen.BidScreen.route + "/${jobID}") },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Apply Now")
                }*/
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {
                    // Display total bids count
                    Text(
                        text = "Total Bids: $totalBids",
                    )

                    // Display average price
                    Text(
                        text = "Average Price: ${if (totalBids > 0) totalPrice / totalBids else 0} DA",
                    )
                }

                BidSection(jobID , totalBids, totalPrice)

                selectedImage?.let { imageUrl ->
                    AlertDialog(
                        onDismissRequest = { selectedImage = null },
                        title = { Text(text = "Image Detail") },
                        text = {
                            Image(
                                painter = rememberImagePainter(imageUrl),
                                contentDescription = null
                            )
                        },
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


        ExtendedFloatingActionButton(
            onClick = { rootNavController.navigate(Screen.BidScreen.route + "/${jobID}") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            //containerColor = MaterialTheme.colorScheme.primary

        ) {
            Text(text = "Apply Now")
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
fun BidSection(jobID: String , totalBids : Int , totalPrice : Int) {
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
        modifier = Modifier.fillMaxWidth(),
    ) {
        /*Text(
            text = "Bids",
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )*/
        Row (modifier = Modifier.fillMaxWidth() , verticalAlignment = Alignment.CenterVertically){
            Text(
                text = "Bids",
                modifier = Modifier
                    .padding(start = 0.dp).weight(1f),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.bids_icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = totalBids.toString(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    painter = painterResource(id = R.drawable.average_bids),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = totalPrice.toString() + "DA",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        /*Text(
            text = "Bids",
            modifier = Modifier
                .padding(start = 16.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )*/

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

    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            ,//.padding(vertical = 8.dp, horizontal = 8.dp),
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
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .animateContentSize()
                            .clickable { expanded = !expanded },
                        maxLines = if (expanded) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis
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
                    text = "Price: $price DA",
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
            if (itemRef.path.endsWith(".jpg") || itemRef.path.endsWith(".png") || itemRef.path.endsWith(
                    ".jpeg"
                )
            ) {
                val imageUrl = itemRef.downloadUrl.await().toString()
                images.add(imageUrl)
            }
        }
    } catch (e: Exception) {
        Log.e("loadImages", "Error loading images: ${e.message}", e)
    }
    return@withContext images
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JobDetailsHeader(job: Job, images: List<String>) {
    val pagerState = rememberPagerState(initialPage = 0) {
        images.size
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            /*.background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )*/
        //.padding(16.dp)

    ) {

        Text(
            text = "Description",
            modifier = Modifier
                .padding(start = 8.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = job.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
            fontSize = 17.sp,
            modifier = Modifier
                .padding(start = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Location",
            modifier = Modifier
                .padding(start = 8.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${job.street}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
            fontSize = 17.sp,
            modifier = Modifier
                .padding(start = 8.dp)
        )

        /*Text(
            text = "Location: ${job.street}, ${job.city}, ${job.wilya}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )*/

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
                text = "${job.min} - ${job.max} DA",
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
    val totalPriceState = mutableStateOf(0) // Add mutable state for total price
    private var jobID: String? = null

    fun setJobID(jobID: String) {
        this.jobID = jobID
        fetchSubcollectionData()
    }

    private fun fetchSubcollectionData() {
        jobID?.let { id ->
            val collectionRef = firestore.collection("Jobs")
            val subcollectionRef = collectionRef.document(id).collection("bids")

            subcollectionRef.get()
                .addOnSuccessListener { documents ->
                    val count = documents.size()
                    subcollectionCountState.value = count

                    var totalPrice = 0
                    documents.forEach { document ->
                        val price = document.getLong("price")?.toInt() ?: 0
                        totalPrice += price
                    }
                    totalPriceState.value = totalPrice
                }
                .addOnFailureListener { exception ->
                    // Handle any errors
                }
        }
    }
}
