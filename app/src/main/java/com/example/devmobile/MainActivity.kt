package com.example.devmobile

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import com.example.devmobile.ui.theme.DevMobileTheme

// Create list of 5 events
val eventTest = listOf(
    Event(1, "Event 1", "Technologies", "Paris", 200, "Event 1 Technologies"),
    Event(2, "Event 2", "Business", null, 1, ""),
    Event(3, "Event 3", "Design", "Lyon", 0, ""),
    Event(4, "Event 4", "Sport", "Marseille", 50, ""),
    Event(5, "Event 5", "Career", "Lille", 100, "Event 5 Career")
)

// --- MAIN ACTIVITY ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Allows the app to draw under the system bars (battery, clock, navigation area)
        enableEdgeToEdge()

        // setContent bridges the Android system and Jetpack Compose.
        // Everything inside here is UI drawn by Compose.
        setContent {
            // Applies your app's global colors, fonts, and shapes
            DevMobileTheme {
                // Calls your main screen component and passes the fake data
                EventListScreen(events = eventTest)
            }
        }
    }
}

// @OptIn suppresses warnings because TopAppBar is currently an "experimental" Material3 feature
@OptIn(ExperimentalMaterial3Api::class)
// @Composable tells the compiler: "This function describes a UI element"
@Composable
fun EventListScreen(events: List<Event>) {

    // .map takes only the "category" from each event. .distinct removes the duplicates
    val categories = events.map { it.category }.distinct()

    // Scaffold provides the standard Material Design screen structure
    Scaffold(
        // The slot for the top app bar
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.app_name)) })
        }
    ) { innerPadding ->
        // innerPadding is the safe area left below the TopAppBar
        // If we don't apply it, our list will go under the top bar!

        // LazyColumn is a vertical scrolling list that only renders items currently on screen
        LazyColumn(
            contentPadding = innerPadding, // Applies the safe area padding here
            modifier = Modifier.fillMaxSize() // Tells the list to take all available screen space
        ) {

            // --- CAROUSEL HEADER ---
            // 'item' (singular) adds exactly ONE block inside the scrolling list
            item {
                // LazyRow is a horizontal scrolling list
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Space between each chip
                ) {
                    // 'items' loops through the categories list to create multiple chips
                    items(categories) { category ->
                        FilterChip( // A clickable Material Design pill/chip
                            selected = false,
                            onClick = {}, // Nothing happens on click yet
                            label = { Text(category) } // The text inside the chip
                        )
                    }
                }
            }

            // --- EVENT CARDS LIST ---
            // 'items' loops through our event list.
            // 'key' helps Compose remember the scroll position efficiently
            items(events, key = { it.id }) { event ->
                // For each event, we call our EventCard component
                EventCard(
                    event = event,
                    // Log.d prints a message in console
                    onCardClick = { Log.d("DevMobile", "Click on ${event.title}") },
                    onBook = { Log.d("DevMobile", "Reservation for ${event.title}") },
                    // Modifier chain: take full width, then add 8dp margin around the card
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
    onCardClick: () -> Unit, // Function passed from parent when card is clicked
    onBook: () -> Unit,      // Function passed from parent when book button is clicked
    modifier: Modifier = Modifier
) {
    // Card creates a surface with rounded corners and a slight shadow
    Card(
        onClick = onCardClick, // Triggers the lambda when the card is tapped
        modifier = modifier
    ) {
        // Column stacks its children vertically
        Column(modifier = Modifier.padding(16.dp)) {

            // Event details layout
            Text(stringResource(R.string.label_title, event.title))
            Text(stringResource(R.string.label_category, event.category))

            val locationText = event.location ?: stringResource(R.string.location_tbd)
            Text(stringResource(R.string.label_location, locationText))

            //use plurals string for available seats
            Text(pluralStringResource(R.plurals.available_seats, event.capacity, event.capacity))

            Button(onClick = onBook, enabled = event.capacity > 0) {
                Text(stringResource(R.string.button_book))
            }
        }
    }
}

@Composable
fun EventDetailScreen(event: Event, onBook: () -> Unit) {
    // verticalScroll allows scrolling if the content is too tall
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Star icon
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        // Text with String resources for plurals
        Text(stringResource(R.string.label_title, event.title))
        Text(stringResource(R.string.label_category, event.category))
        val loc = event.location ?: stringResource(R.string.location_tbd)
        Text(stringResource(R.string.label_location, loc))

        // Description
        Text("Description: " + event.description)
        Text(pluralStringResource(R.plurals.available_seats, event.capacity, event.capacity))

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onBook,
            enabled = event.capacity > 0,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.button_book))
        }
    }
}


// preview event
@Preview(showBackground = true)
@Composable
fun PreviewEventCardFull() {
    DevMobileTheme {
        EventCard(
            event = Event(3, "Design", "Art", null, 0, "Complet"),
            onCardClick = {},
            onBook = {}
        )
    }
}

// preview Dark/Light for PreviewEventList
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewEventList() {
    DevMobileTheme {
        EventListScreen(events = eventTest)
    }
}

// Preview Dark/Light Mode for PreviewEventDetail
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewEventDetail() {
    DevMobileTheme {
        EventDetailScreen(
            event = eventTest[0],
            onBook = {}
        )
    }
}