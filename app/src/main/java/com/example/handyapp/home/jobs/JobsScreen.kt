import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.handyapp.R
import com.example.handyapp.Response
import com.example.handyapp.home.jobs.JobItemViewModel
import com.example.handyapp.home.jobs.JobsViewModel
import com.example.handyapp.navigation.Screen
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
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
    val wilya: String = "",
    val saved : Boolean = false,
    val addingDate : Timestamp = Timestamp.now()
)


@Composable
fun JobItem(job: Job, navHostController: NavHostController , jobItemViewModel: JobItemViewModel = hiltViewModel()) {
    var saved by rememberSaveable {
        mutableStateOf(job.saved)
    }
    var showBottomSheet by remember { mutableStateOf(false) }
    ElevatedCard(
        onClick = {
            navHostController.navigate(Screen.JobsDetails.route + "/${job.id}")
        },
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .fillMaxWidth()
            .height(144.dp),
        shape = RoundedCornerShape(8.dp),
        //colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val formattedDate = String.format("%02d/%02d/%04d", day, month, year)
                val s = job.addingDate.toDate().time


                Text(
                    text = "Posted: ${SimpleDateFormat("dd/MM/yyyy" , Locale.getDefault()).format(
                        Date(s)
                    )}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = job.category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = job.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )

            Text(
                text = job.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.loc),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${
                        job.city.toLowerCase().replaceFirstChar {
                            it.uppercase()
                        }
                    }, ${
                        job.wilya.toLowerCase().replaceFirstChar {
                            it.uppercase()
                        }
                    }",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,

                    )

            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var isFavourite by rememberSaveable {
                    mutableStateOf(false)
                }
                Text(
                    text = "Budget: ${job.min} - ${job.max} DA",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                IconButton(onClick = {
                    //saved = !saved
                    if (saved) {
                        //viewModel.removeSavedHandyMen(item.id)
                        jobItemViewModel.removeJob(job.id)
                        saved = false
                        //showBottomSheet = true
                    } else {
                        jobItemViewModel.saveJob(job.id)
                        saved = true
                    }
                }) {
                    Icon(
                        imageVector = if (saved) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite"
                    )
                }
            }
        }
    }

}


@Composable
fun JobsScreen(
    navHostController: NavHostController,
    viewModel: JobsViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {

    var searchValue by rememberSaveable {
        mutableStateOf("")
    }

    when (val resp = viewModel.jobs.value) {
        is Response.onLoading -> {
            //Toast.makeText(context , "load" ,Toast.LENGTH_SHORT).show()
        }

        is Response.onFaillure -> {
            Toast.makeText(context, "failled", Toast.LENGTH_SHORT).show()
        }

        is Response.onSuccess -> {
            //Toast.makeText(context , "succes" ,Toast.LENGTH_SHORT).show()
            if (resp.data.isNotEmpty()) {
                LazyColumn(
                    //modifier = Modifier.background(colorResource(id = R.color.lightGray))
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = searchValue,
                                onValueChange = {
                                    searchValue = it
                                },
                                placeholder = {
                                    Text(
                                        text = "Search",
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 16.sp,
                                        color = Color.DarkGray
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.filled_search),
                                        contentDescription = "Search",
                                        modifier = Modifier.size(20.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                shape = RoundedCornerShape(6.dp)
                            )

                            Spacer(modifier = Modifier.width(4.dp))
                            SmallFloatingActionButton(
                                onClick = { /*TODO*/ },
                                modifier = Modifier.size(56.dp),
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.settings),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                    items(resp.data.filter {
                        (
                                it.title.contains(searchValue) ||
                                        it.category.uppercase().contains(searchValue.uppercase()) ||
                                        it.city.uppercase().contains(searchValue.uppercase()) ||
                                        it.description.uppercase().contains(searchValue.uppercase()) ||
                                        it.wilya.uppercase().contains(searchValue.uppercase())
                                )
                    }) { job ->
                        JobItem(job, navHostController)
                    }
                }
            }
        }
    }


}