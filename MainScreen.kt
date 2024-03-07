package com.example.mobilecomputingfinalproject

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlin.random.Random

var username = "You"

@Composable
fun MainScreen(navController: NavController,
               state: MessageState,
               onEvent: (MessageEvent) -> Unit
) {

    Column(modifier=Modifier.padding(all=8.dp)/*, horizontalAlignment = Alignment.End*/) {

        // Loads all messages to state
        onEvent(MessageEvent.LoadMessages)

        Conversation(state.messages)

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.SpaceBetween){

            BoxWithConstraints{

                // TextField where you can write a message
                TextField(
                    value = state.content,
                    onValueChange = {
                        onEvent(MessageEvent.UpdateMessageState(it, username, ""))
                    },
                    modifier = Modifier.width(365.dp),
                    label = { Text("Write a message") }
                )

                // Here is the camera icon, overlapping text field
                IconButton(
                    onClick = {
                        navController.navigate(route="camera_screen"){
                            popUpTo(Screen.CameraScreen.route) {
                                inclusive = true
                            }
                        }
                        },
                    modifier = Modifier.align(Alignment.CenterEnd)

                ) {
                    Icon(
                        Icons.Filled.CameraAlt,
                        contentDescription = "Button for Camera",
                        modifier = Modifier.size(25.dp)
                    )
                }

            }

            // Get a random message from SampleData
            val messages = SampleData.conversationSample
            val randomIndex = Random.nextInt(messages.size)
            val randomMessage = messages[randomIndex]

            // Button for sending the message. This saves the message to database. Lexi answers automatically.
            IconButton(
                onClick = {
                    onEvent(MessageEvent.SaveMessage)
                    onEvent(MessageEvent.UpdateMessageState(randomMessage.content!!, randomMessage.author, randomMessage.uri))
                    onEvent(MessageEvent.SaveMessage)
                }

            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Button for sending the message",
                    modifier = Modifier.size(25.dp)
                )
            }
        }
    }
}

@Composable
fun MessageCard(msg: Message) {
    // Padding around message
    Row(modifier=Modifier.padding(all=8.dp)){

        var id = R.drawable.android_image
        var color = MaterialTheme.colorScheme.surface

        if(msg.author == "You"){
            id = R.drawable.mobcomp
            color = MaterialTheme.colorScheme.tertiary
        }

        Image(
            painter = painterResource(id= id),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        // Horizontal space between image and text
        Spacer(modifier = Modifier.width(8.dp))

        // Variable to keeping track of expanded messages
        var isExpanded by remember { mutableStateOf(false) }

        // surfaceColor will be updated gradually from one color to the other
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else color,
        )


        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }){
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )
            // Vertical space between the author and message
            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                // surfaceColor color will be changing gradually from primary to surface
                color = surfaceColor,
                // animateContentSize will change the Surface size gradually
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = msg.content!!,
                    modifier = Modifier.padding(all = 4.dp),
                    // If message is expanded, display all its content, else only the first line
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

    }
}

@Composable
fun Conversation(messages: List<Message>) {

    LazyColumn(modifier=Modifier.height(770.dp)) {
        items(messages) { message ->
            if (message.uri != ""){
                ImageCard(message)
            }else{
                MessageCard(message)
            }
        }
    }
}

@Composable
fun ImageCard(msg: Message) {
    // Padding around message
    Row(modifier = Modifier.padding(all = 8.dp)) {

        var id = R.drawable.android_image

        if (msg.author == "You") {
            id = R.drawable.mobcomp
        }

        Image(
            painter = painterResource(id = id),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        // Horizontal space between image and text
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )
            // Vertical space between the author and picture
            Spacer(modifier = Modifier.height(4.dp))

            // IMAGE HERE
            val imageBitmap = loadImageFromUri(msg.uri!!)
            Image(
                painter = imageBitmap,
                contentDescription = "Image from URI",
                modifier = Modifier
                    .size(300.dp)
            )

        }
    }
}

@Composable
fun loadImageFromUri(uri: String): Painter {
    return rememberAsyncImagePainter(uri)
}
