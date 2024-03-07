package com.example.mobilecomputingfinalproject

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import java.io.File
import kotlin.random.Random


@Composable
fun CameraScreen(
    navController: NavController,
    onEvent: (MessageEvent) -> Unit,
    context: Context
){

    val previewView: PreviewView = remember { PreviewView(context) }

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    controller.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    controller.bindToLifecycle(lifecycleOwner)

    previewView.controller = controller


    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )


        Row(horizontalArrangement = Arrangement.Center) {

            IconButton(
                onClick = {
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(Screen.MainScreen.route) {
                            inclusive = true
                        }
                    }
                })
            {
                Icon(
                    Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Navigate back to main screen",
                    modifier = Modifier.size(25.dp)
                )

            }

            IconButton(
                onClick = {
                    takePhoto(context, controller, onEvent)
                })
            {
                Icon(
                    Icons.Filled.Camera,
                    contentDescription = "Button for taking a photo",
                    modifier = Modifier.size(25.dp)
                )

            }

        }
    }
}

private fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onEvent: (MessageEvent) -> Unit
){
    // Get a random message from SampleData
    val messages = SampleData.conversationSample
    val randomIndex = Random.nextInt(messages.size)
    val randomMessage = messages[randomIndex]

    val photoFile = File(context.filesDir, "photo_" +  System.currentTimeMillis().toString() + ".jpg")
    val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    val executor = ContextCompat.getMainExecutor(context)
    controller.takePicture(
        outputFileOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val photoUri = outputFileResults.savedUri.toString()
                onEvent(MessageEvent.UpdateMessageState("", "You", photoUri))
                onEvent(MessageEvent.SaveMessage)
                onEvent(MessageEvent.UpdateMessageState(randomMessage.content!!, randomMessage.author, randomMessage.uri))
                onEvent(MessageEvent.SaveMessage)
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("Camera", "Couldn't take photo: ", exception)
            }

        }
    )
}
