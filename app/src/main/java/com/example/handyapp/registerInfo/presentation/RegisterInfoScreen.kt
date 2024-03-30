import android.content.ContentResolver
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.handyapp.navigation.Screen
import com.example.handyapp.register.domain.components.RegisterInfoEvent
import com.example.handyapp.registerInfo.presentation.RegisterInfoViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

@Composable
fun RegisterInfoScreen(context: Context, navController: NavController) {
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val selectedFileUri = remember { mutableStateOf<Uri?>(null) }
    val uploadedImageName = remember { mutableStateOf<String?>(null) }
    val uploadedFileName = remember { mutableStateOf<String?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    val viewModel = viewModel<RegisterInfoViewModel>()
    val state = viewModel.state
    LaunchedEffect(key1 = context){
        viewModel.validationEvents.collect{event ->
            when(event){
                is RegisterInfoViewModel.ValidationEvent.Success ->{
                    navController.navigate(Screen.Waiting.route)
                }
            }
        }
    }
    Column (
        modifier = Modifier.verticalScroll(rememberScrollState())
    ){
        TextField(value = state.firstName, onValueChange = {
            viewModel.onEvent(RegisterInfoEvent.FirstNameChanged(it))},
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "First Name") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        if(state.firstNameError != null){
            Text(text = state.firstNameError, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = state.lastName, onValueChange = {
            viewModel.onEvent(RegisterInfoEvent.LastNameChanged(it))},
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Last Name") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        if(state.lastNameError != null){
            Text(text = state.lastNameError, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row {
            TextField(value = state.day, onValueChange = {
                viewModel.onEvent(RegisterInfoEvent.DayChanged(it))},
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                placeholder = { Text(text = "Day") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            TextField(value = state.month, onValueChange = {
                viewModel.onEvent(RegisterInfoEvent.MonthChanged(it))},
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                placeholder = { Text(text = "Month") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            TextField(value = state.year, onValueChange = {
                viewModel.onEvent(RegisterInfoEvent.YearChanged(it))},
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                placeholder = { Text(text = "Year") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        if(state.dayError != null){
            Text(text = state.dayError, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))
        if(state.monthError != null){
            Text(text = state.monthError, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))
        if(state.yearError != null){
            Text(text = state.yearError, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Upload your image")
        Spacer(modifier = Modifier.height(16.dp))

        val imageLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri.value = uri
        }

        Button(onClick = { imageLauncher.launch("image/*") }) {
            Text("Select Image")
        }

        selectedImageUri.value?.let { uri ->
            val imageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
            LaunchedEffect(uri) {
                val bitmap = withContext(Dispatchers.IO) {
                    try {
                        uri.toBitmap(context.contentResolver)
                    } catch (e: IOException) {
                        null
                    }
                }
                imageBitmap.value = bitmap
            }

            imageBitmap.value?.let { bitmap ->
                Image(bitmap = bitmap, contentDescription = null, modifier = Modifier.size(200.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Upload your file")
        Spacer(modifier = Modifier.height(16.dp))

        val fileLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedFileUri.value = uri
            selectedFileName = uri?.lastPathSegment?.substringAfterLast("/")
        }

        Button(onClick = { fileLauncher.launch("application/pdf") }) {
            Text("Select PDF File")
        }

        selectedFileName?.let { name ->
            Text("Selected File: $name")
        }
        Spacer(modifier = Modifier.height(16.dp))


        uploadedImageName.value?.let { name ->
            Text("Uploaded Image: $name")
        }

        uploadedFileName.value?.let { name ->
            Text("Uploaded File: $name")
        }
        Button(onClick = {
            viewModel.onEvent(RegisterInfoEvent.Submit)
            selectedImageUri.value?.let { uri ->
                uploadToFirebase(context.contentResolver, uri, "profile_pictures") { name ->
                    uploadedImageName.value = name
                }
            }
            selectedFileUri.value?.let { uri ->
                uploadToFirebase(context.contentResolver, uri, "certificates") { name ->
                    uploadedFileName.value = name
                }
            }
        }) {
            Text(text = "Submit")
        }
    }
}

private suspend fun Uri.toBitmap(contentResolver: ContentResolver): ImageBitmap {
    val inputStream = contentResolver.openInputStream(this)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    return bitmap.asImageBitmap()
}

private fun uploadToFirebase(contentResolver: ContentResolver, uri: Uri, path: String, callback: (String) -> Unit) {
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser
    if(user != null){
        val fileRef = storageRef.child("$path/${user.uid}")

        val inputStream = contentResolver.openInputStream(uri)
        val uploadTask = inputStream?.let { fileRef.putStream(it) }
        uploadTask?.addOnSuccessListener { _ ->
            callback.invoke(uri.lastPathSegment ?: "Unknown")
        }?.addOnFailureListener {
            // Error uploading file
        }
    }

}
