package com.example.devmobile

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devmobile.ui.theme.DevMobileTheme

// Create 5 events
val eventTest = listOf(
    Event(1, "Event 1", "Technologies", "Paris", 200),
    Event(2, "Event 2", "Business", null, 100), // No location (null)
    Event(3, "Event 3", "Design", "Lyon", 0),   // Fully booked (0 capacity)
    Event(4, "Event 4", "Sport", "Marseille", 50),
    Event(5, "Event 5", "Career", "Lille", 300)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DevMobileTheme {
                EventListScreen(events = eventTest)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(events: List<Event>) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("DevMobile") })
        }
    ) { innerPadding ->
        // LazyColumn optimized for large lists, rendering only visible items
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize()
        ) {
            // Every item is uniquely identified by its Event ID to maintain scroll state
            items(events, key = { it.id }) { event ->
                EventCard(
                    event = event,
                    onCardClick = { Log.d("DevMobile", "Click on ${event.title}") },
                    onBook = { Log.d("DevMobile", "Reservation for ${event.title}") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }
}

// Stateless component: receives data and callbacks, but manages no internal state
@Composable
fun EventCard(
    event: Event,
    onCardClick: () -> Unit,
    onBook: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onCardClick,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Event details layout
            Text("Title: " + event.title)
            Text("Category: " + event.category)

            // send location default if the location event is Null
            Text("Location: " + (event.location ?: "Location DEFAULT"))
            Text("Capacity: " + event.capacity)

            Button(
                onClick = onBook,
                // Automatically disable the button if capacity is empty
                enabled = event.capacity > 0
            ) {
                Text("Book")
            }
        }
    }
}

