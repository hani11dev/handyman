import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


data class Task(
    val client:String,
    val category:String,
    val title:String,
    val description:String,
    val time_day: String,
    val time_hour: String,
    val Price:Int,
    val localisation:String
)

val lists= listOf(
    Task("Louis Do",
        "paint a room",
        "paint a room",
        "paint a room",
        "10-4-2024","10:00",
        600,
        "Alger"),
    Task("Louis moo",
        "paint a room",
        "paint a room",
        "paint a room",
        "12-3-2024","9:00",
        600,
        "Alger"),
)

@Composable
fun HeaderRow(
    //navController: NavHostController,
    title: String,
    //onClick: (navController: NavHostController) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier
                .padding(start = 16.dp),
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun Taskcard (task: Task/*,navController: NavHostController*/) {
    var paused by remember { mutableStateOf(false) }
    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondary),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            //.background(color =if(!paused) MaterialTheme.colorScheme. else  MaterialTheme.colorScheme. )
            .clickable {
                //navController.navigate(Route.)
            },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Box(modifier = Modifier
            .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 12.dp)
                    .align(Alignment.TopStart)
                ,verticalArrangement = Arrangement.Center
            )
            {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    /* Icon(
                         painter = painterResource(id = R.drawable.user),
                         contentDescription = "user",
                         tint = MaterialTheme.colorScheme.primary
                     )*/
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = task.client, textAlign = TextAlign.Center,fontWeight = FontWeight.Medium)
                }
                Text(
                    text = task.title,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal,
                )
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    /*Icon(
                        painter = painterResource(id = R.drawable.location),
                        contentDescription = "location",
                        tint = MaterialTheme.colorScheme.primary
                    )*/
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = task.localisation, textAlign = TextAlign.Center,fontWeight = FontWeight.Medium)
                }
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = task.time_day, textAlign = TextAlign.Center,fontWeight = FontWeight.Light)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = task.time_hour, textAlign = TextAlign.Center,fontWeight = FontWeight.Light)
                }
            }
            Column(
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 12.dp)
                    .align(Alignment.TopEnd),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append("Price ")
                    }
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append(text = task.Price.toString() + "DA")
                    }
                    withStyle(SpanStyle(fontWeight = FontWeight.Light)) {
                        append("/hr")
                    }
                })
                OutlinedButton(
                    onClick = {},
                    modifier=Modifier.padding(2.dp),
                    colors =ButtonDefaults.outlinedButtonColors(containerColor =Color.Red,
                        contentColor = MaterialTheme.colorScheme.onSecondary )

                ){
                    Text(text = "cancel",
                        fontWeight = FontWeight.Bold,
                    )
                }
                OutlinedButton(
                    onClick = {
                        paused=!paused
                    },
                    modifier=Modifier.padding(2.dp),
                    colors =ButtonDefaults.outlinedButtonColors(containerColor =Color.LightGray )
                ){
                    if (!paused){
                        Text(text = "Pause",
                            fontWeight = FontWeight.Bold
                        )}
                    else{
                        Text(text = "resume",
                            fontWeight = FontWeight.Bold
                        )}
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTasksScreen(navController: NavHostController) {
    Scaffold(modifier = Modifier
        .fillMaxSize()
    )
    { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background else colorResource(
                        id = Color.LightGray.toArgb()
                    )
                )
        ) {
            item {
                HeaderRow(/*navController = navController,*/ title = "Tasks"/*, onClick = {}*/)
            }
            items(lists) { item ->
                Taskcard(item/*, navController*/)
            }
        }
    }
}