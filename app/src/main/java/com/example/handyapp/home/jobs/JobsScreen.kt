import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun JobsScreen() {
    val backgroundColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.White
    val itemColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor // Set background color based on the theme
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                TaskItem(
                    title = "looking for someone to install Shower Drain",
                    description = "Need someone with good experience on April 13 to install the shower drain in a day.",
                    budget = "20,500",
                    bids = 12,
                    daysLeft = 2,
                    postedDate = "11-12-2024"
                )
            }

            item {
                TaskItem(
                    title = "I need a professional painter to Paint 3 Rooms",
                    description = "A professional painter to finish the work of 3 large rooms (7 ft x 10 ft (70 square feet))as...",
                    budget = "10,600",
                    bids = 2,
                    daysLeft = 1,
                    postedDate = "11-10-2022"
                )
            }
            item {
                TaskItem(
                    title = " Repair Kitchen Sink",
                    description = "Need a plumber to fix my kitchen's sink, it has been leaking water for a while now...",
                    budget = "20,500",
                    bids = 12,
                    daysLeft = 2,
                    postedDate = "11-11-2021"
                )
            }
        }
    }
}


@Composable
fun TaskItem(title: String, description: String, budget: String, bids: Int, daysLeft: Int, postedDate: String) {
    Surface(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        color = Color.LightGray, // Set background color to light grey
        shape = RoundedCornerShape(35.dp) // Apply more rounded shape
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = if (isSystemInDarkTheme()) Color.DarkGray else Color.Black, // Adjust title color based on theme
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Divider(
                color = Color.White, // Set white color for the divider
                thickness = 2.dp, // Adjust thickness as needed
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp),
                color = if (isSystemInDarkTheme()) Color.DarkGray else Color.Black // Adjust description color based on theme
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Budget: $budget DA",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isSystemInDarkTheme()) Color.Black else Color.Blue // Adjust budget color based on theme
                )
                Text(
                    text = "Days left: $daysLeft",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSystemInDarkTheme()) Color.DarkGray else Color.Black // Adjust days left color based on theme
                )
            }
            Text(
                text = "Bids: $bids",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 4.dp),
                color = if (isSystemInDarkTheme()) Color.DarkGray else Color.Black // Adjust bids color based on theme
            )
            Text(
                text = "Posted: $postedDate",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp).offset(110.dp), // Adjust offset here
                color = if (isSystemInDarkTheme()) Color.DarkGray else Color.Black // Adjust posted date color based on theme
            )
        }
    }
}
