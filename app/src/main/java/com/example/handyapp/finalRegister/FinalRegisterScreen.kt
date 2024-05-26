package com.example.handyapp.finalRegister

import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.handyapp.R
import com.example.handyapp.Response
import com.example.handyapp.Wilaya_CommunesDBList
import com.example.handyapp.navigation.Graph
import com.example.handyapp.navigation.Screen
import com.example.handyapp.register.domain.components.RegistrationEvent
import com.example.handyapp.register.presentation.RegisterViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.koDea.fixMasterClient.domain.model.wilayas_communes.Baladyiat
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.attribution.generated.AttributionSettings
import com.mapbox.maps.plugin.compass.generated.CompassSettings
import com.mapbox.maps.plugin.gestures.generated.GesturesSettings
import com.mapbox.maps.plugin.scalebar.generated.ScaleBarSettings
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalRegisterScreen(navController: NavController, VM: FRegViewModel = hiltViewModel()) {
    val viewModel = viewModel<FinalRegisterViewModel>()
    val state = viewModel.state
    val context = LocalContext.current
    var about by rememberSaveable {
        mutableStateOf("")
    }
    var aboutError by rememberSaveable {
        mutableStateOf(false)
    }
    var aboutSupportingText by rememberSaveable {
        mutableStateOf("")
    }

    var city by rememberSaveable {
        mutableStateOf("")
    }
    var cityError by rememberSaveable {
        mutableStateOf(false)
    }
    var citySupportingText by rememberSaveable {
        mutableStateOf("")
    }
    var street by rememberSaveable {
        mutableStateOf("")
    }
    var streetError by rememberSaveable {
        mutableStateOf(false)
    }
    var streetSupportingText by rememberSaveable {
        mutableStateOf("")
    }
    var wilaya by rememberSaveable {
        mutableStateOf("")
    }
    var wilayaError by rememberSaveable {
        mutableStateOf(false)
    }
    var wilayaSupportingText by rememberSaveable {
        mutableStateOf("")
    }
    var averageSalary by rememberSaveable {
        mutableStateOf("")
    }
    var averageSalaryError by rememberSaveable {
        mutableStateOf(false)
    }
    var averageSalarySupportingText by rememberSaveable {
        mutableStateOf("")
    }
    var workingAreas by rememberSaveable {
        mutableStateOf("")
    }
    var workingAreasError by rememberSaveable {
        mutableStateOf(false)
    }
    var workingAreasSupportingText by rememberSaveable {
        mutableStateOf("")
    }

    var showMap by remember {
        mutableStateOf(false)
    }
    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            center(Point.fromLngLat(2.8770072419209303, 36.50321008456727))
            zoom(3.0)
            pitch(0.0)
        }
    }

    val mapBoxUiSettings: GesturesSettings by remember {
        mutableStateOf(GesturesSettings {
            rotateEnabled = true
            pinchToZoomEnabled = true
            pitchEnabled = true
        })
    }

    val compassSettings: CompassSettings by remember {
        mutableStateOf(CompassSettings { enabled = false })
    }

    val scaleBarSetting: ScaleBarSettings by remember {
        mutableStateOf(ScaleBarSettings { enabled = false })
    }
    var locationRequired: Boolean = false
    var locationInfo: Location? = null
    val locationClient =
        remember { LocationServices.getFusedLocationProviderClient(context) }
    val scope = rememberCoroutineScope()
    val launcherMultiplePermissions =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { permissionMaps ->
            val areGaranted = permissionMaps.values.reduce { acc, b -> acc && b }
            if (areGaranted) {
                locationRequired = true
                Toast.makeText(context, "permission garanted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "permission deneid", Toast.LENGTH_LONG).show()
            }
        }
    var point by rememberSaveable {
        mutableStateOf<Point?>(null)
    }
    var currentLoc by rememberSaveable {
        mutableStateOf<Point?>(null)
    }
    val permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is FinalRegisterViewModel.ValidationEvent.Success -> {
                    val db = FirebaseFirestore.getInstance()
// Get the current user ID (assuming the user is authenticated)
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    val userId = currentUser?.uid
                    if (userId != null) {
                        // Specify the document reference using the user ID
                        db.collection("handymen")
                            .document(userId)
                            .update(
                                hashMapOf<String, Any>(
                                    "Status" to "ACTIVE"
                                )
                            )
                            .addOnSuccessListener {

                            }
                            .addOnFailureListener { e ->
                                println("Error adding document: $e")
                            }
                    } else {
                        Log.d("Login", "login failed")
                    }
                    navController.navigate(Screen.FinishedSetupScreen.route)
                }
            }
        }
    }
    var showLocationField by remember{ mutableStateOf(false) }
    var isExpandedWilaya by remember {
        mutableStateOf(false)
    }
    var selectedStatusWilaya by remember {
        mutableStateOf("")
    }
    var selectedStatusCity by remember {
        mutableStateOf("")
    }
    when (val resp = VM.location.value) {
        is Response.onLoading -> {}
        is Response.onFaillure -> {
            Toast.makeText(context, resp.message, Toast.LENGTH_SHORT).show()
        }

        is Response.onSuccess -> {

            if (resp.data != null) {
                //Toast.makeText(context , resp.data.address.state , Toast.LENGTH_SHORT).show()
                //viewModel.onEvent(FinalRegistrationEvent.StreetChanged(resp.data.display_name.toString().lowercase()))
                street = resp.data.display_name.toString().toLowerCase()
                //viewModel.onEvent(FinalRegistrationEvent.WilayaChanged(resp.data.address?.state.toString().lowercase()))
                wilaya = resp.data.address?.state?.toLowerCase() ?: ""
                //viewModel.onEvent(FinalRegistrationEvent.CityChanged(resp.data.address?.town.toString().lowercase()))
                city = resp.data.address?.town.toString().toLowerCase() ?: ""
                selectedStatusCity = resp.data.address?.town.toString().toLowerCase()
                selectedStatusWilaya = resp.data.address?.state.toString().toLowerCase()
                cityError = false
                wilayaError = false
                streetError = false
                    showLocationField = true
            }
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .border(width = 1.dp, color = Color.White) // Add border here
                .padding(horizontal = 16.dp, vertical = 16.dp), // Add padding for the content
            verticalArrangement = Arrangement.Center
        ) {
            TextField(value = about, onValueChange = {
                //viewModel.onEvent(FinalRegistrationEvent.AboutChanged(it))
                about = it
                aboutError = false
                aboutSupportingText = ""
            },
                isError = aboutError,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "About your self") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Create, contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                supportingText = {
                    Text(
                        text = aboutSupportingText,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(value = averageSalary, onValueChange = {
                //viewModel.onEvent(FinalRegistrationEvent.AverageSalaryChanged(it))
                averageSalary = it
                averageSalaryError = false
                averageSalarySupportingText = ""
            },
                isError = averageSalaryError,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Average salary") },

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                supportingText = {
                    Text(
                        text = averageSalarySupportingText,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { showMap = !showMap }, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp)) {
                Text("Select your location on Map")
            }
            Spacer(modifier = Modifier.height(16.dp))


            AnimatedVisibility(visible = showLocationField) {
                Column {


                    Box(
                        modifier = Modifier.wrapContentSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = isExpandedWilaya,
                            onExpandedChange = { isExpandedWilaya = !isExpandedWilaya },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            TextField(
                                value = selectedStatusWilaya, onValueChange = {
                                    wilayaError = false
                                    wilayaSupportingText = ""
                                }, readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedWilaya)
                                }, modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                leadingIcon = {
                                    Icon(

                                        painter = painterResource(id = R.drawable.loc),
                                        contentDescription = null
                                    )
                                },
                                placeholder = {
                                    Text(text = "select wilaya")
                                },
                                shape = RoundedCornerShape(8.dp),
                                supportingText = {
                                    Text(
                                        text = "${wilayaSupportingText}",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            )
                            DropdownMenu(
                                expanded = isExpandedWilaya,
                                onDismissRequest = { isExpandedWilaya = false },
                                modifier = Modifier
                                    .exposedDropdownSize()
                                    .padding(2.dp)
                            ) {
                                Wilaya_CommunesDBList.sortedBy { it.name_en }.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it.name_en.lowercase()) },
                                        onClick = {
                                            selectedStatusWilaya = it.name_en
                                            wilaya = selectedStatusWilaya
                                            isExpandedWilaya = false
                                            wilayaError = false
                                            wilayaSupportingText = ""
                                            selectedStatusCity = ""
                                        }
                                    )
                                }

                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    var isExpandedCity by remember {
                        mutableStateOf(false)
                    }
                    Box(
                        modifier = Modifier.wrapContentSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = isExpandedCity,
                            onExpandedChange = { isExpandedCity = !isExpandedCity },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            TextField(
                                value = selectedStatusCity, onValueChange = {
                                    cityError = false
                                    citySupportingText = ""
                                }, readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedCity)
                                }, modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                leadingIcon = {
                                    Icon(

                                        painter = painterResource(id = R.drawable.loc),
                                        contentDescription = null
                                    )
                                },
                                placeholder = {
                                    Text(text = "select city")
                                },
                                shape = RoundedCornerShape(8.dp),
                                supportingText = {
                                    Text(
                                        text = "${citySupportingText}",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            )
                            DropdownMenu(
                                expanded = isExpandedCity,
                                onDismissRequest = { isExpandedCity = false },
                                modifier = Modifier
                                    .exposedDropdownSize()
                                    .padding(2.dp)
                            ) {
                                val wilayaSelected =
                                    Wilaya_CommunesDBList.find { it.name_en == selectedStatusWilaya }
                                val allBaladyiat = mutableListOf<Baladyiat>()
                                wilayaSelected?.dairats?.forEach {
                                    if (it.baladyiats != null)
                                        allBaladyiat.addAll(it.baladyiats)
                                }
                                allBaladyiat.sortedBy { it.name_en }.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it.name_en.lowercase()) },
                                        onClick = {
                                            selectedStatusCity = it.name_en.lowercase()
                                            city = selectedStatusCity
                                            isExpandedCity = false
                                            cityError = false
                                            citySupportingText = ""
                                        }
                                    )
                                }

                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(value = street, onValueChange = {
                        //viewModel.onEvent(FinalRegistrationEvent.StreetChanged(it))
                        street = it
                        streetError = false
                        streetSupportingText = ""
                    },
                        isError = streetError,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(text = "Street") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.LocationOn, contentDescription = null
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        ),
                        supportingText = {
                            Text(
                                text = streetSupportingText,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

            }
            /*Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(value = wilaya, onValueChange = {
                    //viewModel.onEvent(FinalRegistrationEvent.WilayaChanged(it))
                    wilaya = it
                },
                    isError = wilayaError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    placeholder = { Text(text = "Wilaya") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.LocationOn, contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    supportingText = {
                        Text(
                            text = wilayaSupportingText,
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    singleLine = true,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.width(8.dp))

                TextField(value = city, onValueChange = {
                    //viewModel.onEvent(FinalRegistrationEvent.CityChanged(it))
                    city = it
                    cityError = false
                    citySupportingText = ""
                },
                    isError = cityError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    placeholder = { Text(text = "City") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.LocationOn, contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    supportingText = {
                        Text(
                            text = citySupportingText,
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    singleLine = true,
                    maxLines = 1
                )
            }
            TextField(value = street, onValueChange = {
                //viewModel.onEvent(FinalRegistrationEvent.StreetChanged(it))
                street = it
                streetError = false
                streetSupportingText = ""
            },
                isError = streetError,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Street") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.LocationOn, contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                supportingText = {
                    Text(
                        text = streetSupportingText,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            )*/
            //Spacer(modifier = Modifier.height(16.dp))
            TextField(value = workingAreas, onValueChange = {
                //viewModel.onEvent(FinalRegistrationEvent.WorkingAreaChanged(it))
                workingAreas = it
                workingAreasError = false
                workingAreasSupportingText = ""
            },
                isError = workingAreasError,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Working Areas") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.LocationOn, contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                supportingText = {
                    Text(
                        text = workingAreasSupportingText,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            var progressBarState = remember {
                mutableStateOf(false)
            }
            var selectedImageUris by remember {
                mutableStateOf<List<Uri>>(emptyList())
            }
            val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickMultipleVisualMedia(),
                onResult = { uris -> selectedImageUris = uris })
            Button(onClick = { multiplePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            ) } , modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp) , shape = RoundedCornerShape(8.dp)) {
                Text("Upload Portfolio Image")
            }

            LazyVerticalGrid(columns = GridCells.Fixed(3) , modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 192.dp)) {
                items(selectedImageUris.take(9)){
                    AsyncImage(model = it, contentDescription = null, contentScale = ContentScale.Crop)
                }
            }


            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp) , shape = RoundedCornerShape(8.dp),onClick = {


                aboutError = false
                workingAreasError = false
                cityError = false
                wilayaError = false
                streetError = false
                //viewModel.onEvent(FinalRegistrationEvent.Submit)
                if (about.isEmpty()) {
                    Toast.makeText(context, "about empty", Toast.LENGTH_SHORT).show()
                    aboutError = true
                    aboutSupportingText = "can't be empty"
                }
                if (workingAreas.isEmpty()) {
                    Toast.makeText(context, "working empty", Toast.LENGTH_SHORT).show()
                    workingAreasError = true
                    workingAreasSupportingText = "can't be empty"
                }
                if (averageSalary.isEmpty()) {
                    Toast.makeText(context, "average empty", Toast.LENGTH_SHORT).show()
                    averageSalaryError = true
                    averageSalarySupportingText = "can't be empty"
                }
                if (point?.latitude() == null) {
                    Toast.makeText(context, "Select your location", Toast.LENGTH_SHORT).show()
                }
                if (!aboutError || !averageSalaryError || point?.latitude() != null || !workingAreasError) {
                    progressBarState.value = true
                    VM.updateInfo(
                        about = about,
                        workingAreas = workingAreas,
                        averageSalary = averageSalary.toDouble(),
                        city = city,
                        wilaya = wilaya,
                        street = street,
                        lat = point?.latitude().toString(),
                        long = point?.longitude().toString()
                    )
                }


            }) {

                when(val resp = VM.updateState.value){
                    is Response.onLoading -> {
                    }
                    is Response.onFaillure -> {
                        progressBarState.value = false
                        Toast.makeText(context , resp.message , Toast.LENGTH_SHORT).show()
                        val c = LocalClipboardManager.current.setText(AnnotatedString(resp.message))
                    }
                    is Response.onSuccess -> {
                        progressBarState.value = false
                        navController.navigate(Screen.FinishedSetupScreen.route){
                            navController.popBackStack()
                        }

                    }
                }
                Text(text = "Submit")
                if (progressBarState.value) {
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

        if (showMap) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                MapboxMap(
                    modifier = Modifier.fillMaxSize(),
                    mapInitOptionsFactory = { context ->
                        MapInitOptions(
                            context = context,
                            styleUri = Style.OUTDOORS,
                        )
                    },
                    mapViewportState = mapViewportState,
                    compassSettings = compassSettings,
                    scaleBarSettings = scaleBarSetting,
                    gesturesSettings = mapBoxUiSettings,
                    attributionSettings = AttributionSettings {
                        enabled = false
                    },
                    onMapClickListener = {
                        Toast.makeText(
                            context,
                            "${it.latitude()},${it.longitude()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        point = it

                        true
                    }
                ) {
                    if (point != null) {
                        PointAnnotation(point = point!!,
                            iconImageBitmap = context.getDrawable(R.drawable.loc)!!
                                .toBitmap(),
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "my location",
                                    Toast.LENGTH_SHORT
                                ).show()
                                true
                            }
                        )
                    }
                    if (currentLoc != null) {
                        PointAnnotation(point = currentLoc!!,
                            iconImageBitmap = context.getDrawable(R.drawable.current_location)!!
                                .toBitmap(),
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "my location",
                                    Toast.LENGTH_SHORT
                                ).show()
                                true
                            }
                        )
                    }
                }
                Column {
                    Button(shape = CircleShape, onClick = {

                        scope.launch {
                            while (locationInfo?.latitude == null) {
                                if (permissions.all {
                                        ContextCompat.checkSelfPermission(
                                            context, it
                                        ) == PackageManager.PERMISSION_GRANTED
                                    }) {
                                    //get Location

                                    locationInfo = locationClient.getCurrentLocation(
                                        Priority.PRIORITY_HIGH_ACCURACY,
                                        CancellationTokenSource().token
                                    ).await()
                                    //locationInfo = locationClient.getLastLocation().await()


                                    if (locationInfo?.latitude != null && locationInfo?.longitude != null)
                                        mapViewportState.flyTo(
                                            CameraOptions.Builder().zoom(16.0).center(
                                                Point.fromLngLat(
                                                    locationInfo!!.longitude,
                                                    locationInfo!!.latitude
                                                )
                                            ).build()
                                        )
                                } else launcherMultiplePermissions.launch(permissions)
                                if (locationInfo?.latitude != null) {
                                    currentLoc =
                                        Point.fromLngLat(
                                            locationInfo!!.longitude,
                                            locationInfo!!.latitude
                                        )
                                }
                            }

                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Accessibility,
                            contentDescription = "current location"
                        )
                    }
                    Button(onClick = {
                        if (point != null) {
                            showMap = !showMap
                            //viewModel.onEvent(FinalRegistrationEvent.LatitudeChanged(point?.latitude().toString()))

                            //viewModel.onEvent(FinalRegistrationEvent.LongitudeChanged(point?.longitude().toString()))
                            VM.getLocation(
                                point!!.latitude().toString(),
                                point!!.longitude().toString()
                            )

                            //showLocationField = true

                        } else Toast.makeText(context, "select location", Toast.LENGTH_SHORT).show()


                    }) {

                        Text(text = "Select")
                    }

                }

            }
        }

    }
}
















