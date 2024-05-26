import android.content.ContentResolver
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.handyapp.R
import com.example.handyapp.Response
import com.example.handyapp.navigation.Screen
import com.example.handyapp.register.domain.components.RegisterInfoEvent
import com.example.handyapp.registerInfo.presentation.RegisterInfoViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterInfoScreen(context: Context, navController: NavController , viewModel : RegisterInfoViewModel = hiltViewModel()) {
    var day = rememberSaveable { mutableStateOf("") }
    val calendarState = rememberSheetState()
    CalendarDialog(state = calendarState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
        ),
        selection = CalendarSelection.Date { date ->
            day.value = date.toString()
        })

    val selectedImageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val selectedFileUri = remember { mutableStateOf<Uri?>(null) }
    val uploadedImageName = remember { mutableStateOf<String?>(null) }
    val uploadedFileName = remember { mutableStateOf<String?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    val viewModel = viewModel<RegisterInfoViewModel>()
    //val state = viewModel.state

    val imageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) selectedImageUri.value = uri

        }
    val painter =
        rememberAsyncImagePainter(model = if (selectedImageUri.value == null) R.drawable.default_profile else selectedImageUri.value.toString())

    /*LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is RegisterInfoViewModel.ValidationEvent.Success -> {
                    navController.navigate(Screen.Waiting.route)
                }
            }
        }
    }*/
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            //.border(width = 1.dp, color = Color.White) // Add border here
            .padding(horizontal = 16.dp), // Add padding for the content
        verticalArrangement = Arrangement.Center,
        //horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Image(
                painter = painter,
                contentDescription = "Profile image",
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape)
                    .border(4.dp, color = MaterialTheme.colorScheme.onBackground, CircleShape)
                    .clickable {
                        imageLauncher.launch("image/*")
                    },
                contentScale = ContentScale.Crop

            )

        }

        Spacer(modifier = Modifier.height(24.dp))


        var firstName by rememberSaveable {
            mutableStateOf("")
        }
        var firstNameError by rememberSaveable {
            mutableStateOf(false)
        }
        var firstNameSupportingText by rememberSaveable{ mutableStateOf("") }

        var lastName by rememberSaveable {
            mutableStateOf("")
        }
        var lastNameError by rememberSaveable {
            mutableStateOf(false)
        }
        var lastNameSupportingText by rememberSaveable{ mutableStateOf("") }

        var day by rememberSaveable {
            mutableStateOf("")
        }
        var dayError by rememberSaveable {
            mutableStateOf(false)
        }
        var daySupportingText by rememberSaveable{ mutableStateOf("") }

        var month by rememberSaveable {
            mutableStateOf("")
        }
        var monthError by rememberSaveable {
            mutableStateOf(false)
        }
        var monthSupportingText by rememberSaveable{ mutableStateOf("") }
        var year by rememberSaveable {
            mutableStateOf("")
        }
        var yearError by rememberSaveable {
            mutableStateOf(false)
        }
        var yearSupportingText by rememberSaveable{ mutableStateOf("") }


        TextField(
            value = firstName, onValueChange = {
                //viewModel.onEvent(RegisterInfoEvent.FirstNameChanged(it))
                                               firstName = it
                firstNameError = false
                firstNameSupportingText = ""
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "First Name") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Edit, contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            isError = firstNameError,
            supportingText = {Text(text = firstNameSupportingText,color = MaterialTheme.colorScheme.error)}
        )
        /*if (state.firstNameError != null) {
            Text(text = state.firstNameError, color = MaterialTheme.colorScheme.error)
        }*/
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = lastName, onValueChange = {
                //viewModel.onEvent(RegisterInfoEvent.LastNameChanged(it))
                                              lastName = it
                lastNameError = false
                lastNameSupportingText = ""
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Last Name") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Edit, contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            isError = lastNameError,
            supportingText = {Text(text = lastNameSupportingText,color = MaterialTheme.colorScheme.error)}
        )
        /*if (state.lastNameError != null) {
            Text(text = state.lastNameError, color = MaterialTheme.colorScheme.error)
        }*/

        Spacer(modifier = Modifier.height(16.dp))

        var birthday by rememberSaveable {
            mutableStateOf("")
        }
        var birthdayError by rememberSaveable {
            mutableStateOf(false)
        }
        var birthdaySupportingText by rememberSaveable{ mutableStateOf("") }

        val calendarState = rememberSheetState()
        CalendarDialog(state = calendarState,
            config = CalendarConfig(
                monthSelection = true,
                yearSelection = true,
            ),
            selection = CalendarSelection.Date { date ->
                birthday = date.toString()
                day = date.dayOfMonth.toString()
                month = date.month.value.toString()
                year = date.year.toString()
                birthdayError = false
                birthdaySupportingText = ""

            })

        TextField(
            value = birthday, onValueChange = {
                //viewModel.onEvent(RegisterInfoEvent.LastNameChanged(it))
                birthday = it
                birthdayError = false
                birthdaySupportingText = ""
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Day of Birth") },
            leadingIcon = {
                IconButton(onClick = {
                    calendarState.show()
                }){
                    Icon(imageVector = Icons.Filled.CalendarMonth, contentDescription = null)

                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            isError = birthdayError,
            supportingText = {Text(text = birthdaySupportingText,color = MaterialTheme.colorScheme.error)},
            readOnly = true

        )

        /*Row {
            TextField(
                value = day, onValueChange = {
                    //viewModel.onEvent(RegisterInfoEvent.DayChanged(it))
                                             day = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                placeholder = { Text(text = "Day") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.DateRange, contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = dayError,
                supportingText = {Text(text = daySupportingText,color = MaterialTheme.colorScheme.error)}
            )
            TextField(
                value = month, onValueChange = {
                    //viewModel.onEvent(RegisterInfoEvent.MonthChanged(it))
                                               month = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                placeholder = { Text(text = "Month") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = monthError,
                supportingText = {Text(text = monthSupportingText,color = MaterialTheme.colorScheme.error)}
            )
            TextField(
                value = year, onValueChange = {
                    //viewModel.onEvent(RegisterInfoEvent.YearChanged(it))
                                              year = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                placeholder = { Text(text = "Year") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = yearError,
                supportingText = {Text(text = yearSupportingText,color = MaterialTheme.colorScheme.error)}
            )
        }*/
        /*if (state.dayError != null) {
            Text(text = state.dayError, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (state.monthError != null) {
            Text(text = state.monthError, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (state.yearError != null) {
            Text(text = state.yearError, color = MaterialTheme.colorScheme.error)
        }*/
        //Spacer(modifier = Modifier.height(16.dp))

        //Text(text = "Upload your image")
        //Spacer(modifier = Modifier.height(16.dp))


        // Button(onClick = { imageLauncher.launch("image/*") }) {
        //   Text("Select Image")
        //}

        /*selectedImageUri.value?.let { uri ->
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
        }*/

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Upload your file")
        Spacer(modifier = Modifier.height(16.dp))

        val fileLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
                selectedFileUri.value = uri
                selectedFileName = uri?.lastPathSegment?.substringAfterLast("/")
            }

        Button(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp) , shape = RoundedCornerShape(8.dp),onClick = { fileLauncher.launch("application/pdf") }) {
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
        var progressState by rememberSaveable { mutableStateOf(false) }
        Button(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp) , shape = RoundedCornerShape(8.dp),onClick = {

            if (firstName.isEmpty()) {
                firstNameError = true
                firstNameSupportingText = "First Name can't be empty"
            } else if (firstName.length < 3) {
                firstNameError = true
                firstNameSupportingText = "First Name should be more 2 char"
            }
            if (lastName.isEmpty()) {
                lastNameError = true
                lastNameSupportingText = "Last Name can't be empty"
            } else if (lastName.length < 3) {
                lastNameError = true
                lastNameSupportingText = "Last Name should be more 2 char"
            }

            if (birthday.isEmpty()){
                birthdayError = true
                birthdaySupportingText = "Select your BirthDay"
            }
            if (selectedImageUri.value == null){
                Toast.makeText(context , "select image" , Toast.LENGTH_SHORT).show()
            }
            if (selectedFileUri.value == null) {
                Toast.makeText(context , "select file" , Toast.LENGTH_SHORT).show()
            }
            if (firstNameError == false && lastNameError == false && birthdayError == false && selectedImageUri.value != null && selectedFileUri.value != null ){
                progressState = true
                viewModel.updateRegisterInfo(firstName , lastName , day , month , year , selectedImageUri.value!! , selectedFileUri.value!!)
                /*selectedImageUri.value?.let { uri ->
                    uploadToFirebase(context.contentResolver, uri, "profile_pictures") { name ->
                        uploadedImageName.value = name
                    }
                }
                selectedFileUri.value?.let { uri ->
                    uploadToFirebase(context.contentResolver, uri, "certificates") { name ->
                        uploadedFileName.value = name
                    }
                }*/
            }
            /*viewModel.onEvent(RegisterInfoEvent.Submit)
            selectedImageUri.value?.let { uri ->
                uploadToFirebase(context.contentResolver, uri, "profile_pictures") { name ->
                    uploadedImageName.value = name
                }
            }
            selectedFileUri.value?.let { uri ->
                uploadToFirebase(context.contentResolver, uri, "certificates") { name ->
                    uploadedFileName.value = name
                }
            }*/
        }) {
            when(val resp = viewModel.updateState.value){
                is Response.onLoading -> {
                }
                is Response.onFaillure -> {
                    progressState = false
                    Toast.makeText(context , resp.message , Toast.LENGTH_SHORT).show()}
                is Response.onSuccess -> {
                    progressState = false
                    navController.navigate(Screen.Waiting.route){
                        navController.popBackStack()
                    }
                }
            }
            Text(text = "Submit")
            if (progressState) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier
                        .size(26.dp)
                        .fillMaxSize(),
                    strokeWidth = 2.5.dp
                )
            }
        }
    }
}

private suspend fun Uri.toBitmap(contentResolver: ContentResolver): ImageBitmap {
    val inputStream = contentResolver.openInputStream(this)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    return bitmap.asImageBitmap()
}

private fun uploadToFirebase(
    contentResolver: ContentResolver,
    uri: Uri,
    path: String,
    callback: (String) -> Unit
) {
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser
    if (user != null) {
        val fileRef = storageRef.child("$path/${user.uid}")

        val inputStream = contentResolver.openInputStream(uri)
        val uploadTask = inputStream?.let { fileRef.putStream(it) }
        uploadTask?.addOnSuccessListener { s ->
            if (path == "profile_pictures") {
                GlobalScope.launch {
                    val ur = s.storage.downloadUrl.await()
                    db.collection("HandyMan").document(user.uid).update("ProfileImage", ur).await()
                    //Log.d("uploadedUri" ,ur.toString())
                    callback.invoke(uri.lastPathSegment ?: "Unknown")

                }

            }
        }?.addOnFailureListener {
            // Error uploading file
        }
    }

}
