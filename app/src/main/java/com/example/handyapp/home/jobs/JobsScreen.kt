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
    Surface(
        modifier = Modifier.padding(16.dp),
        color = Color.White // Set background color to white
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
                )
            }

            item {
                TaskItem(
                    title = "I need a professional painter to Paint 3 Rooms",
                    description = "A professional painter to finish the work of 3 large rooms (7 ft x 10 ft (70 square feet))as...",
                    budget = "10,600",
                    bids = 2,
                    daysLeft = 1
                )
            }
            item {
                TaskItem(
                    title = " Repair Kitchen Sink",
                    description = "Need a plumber to fix my kitchen's sink, it has been leaking water for a while now...",
                    budget = "20,500",
                    bids = 12,
                    daysLeft = 2
                )
            }
        }
    }
}

@Composable
fun TaskItem(title: String, description: String, budget: String, bids: Int, daysLeft: Int) {
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
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black, // Set very dark color for the title
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
                color = Color.Black // Set black color for the description text
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Budget: $budget DA",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Blue // Set black color for the budget text
                )
                Text(
                    text = "Days left: $daysLeft",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black // Set black color for the days left text
                )
            }
            Text(
                text = "Bids: $bids",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 4.dp),
                color = Color.Black // Set black color for the bids text
            )
        }
    }
}