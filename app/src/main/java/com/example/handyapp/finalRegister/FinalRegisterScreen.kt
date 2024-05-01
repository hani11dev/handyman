package com.example.handyapp.finalRegister

import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.handyapp.R
import com.example.handyapp.Response
import com.example.handyapp.navigation.Graph
import com.example.handyapp.navigation.Screen
import com.example.handyapp.register.domain.components.RegistrationEvent
import com.example.handyapp.register.presentation.RegisterViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

@Composable
fun FinalRegisterScreen(navController: NavController , VM : FRegViewModel = hiltViewModel()){
    val viewModel = viewModel<FinalRegisterViewModel>()
    val state = viewModel.state
    val context = LocalContext.current

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

    LaunchedEffect(key1 = context){
        viewModel.validationEvents.collect{event ->
            when(event){
                is FinalRegisterViewModel.ValidationEvent.Success ->{
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
    when(val resp = VM.location.value){
        is Response.onLoading ->{}
        is Response.onFaillure ->{Toast.makeText(context , resp.message , Toast.LENGTH_SHORT).show()}
        is Response.onSuccess ->{

            if (resp.data != null){
                //Toast.makeText(context , resp.data.address.state , Toast.LENGTH_SHORT).show()
                viewModel.onEvent(FinalRegistrationEvent.StreetChanged(resp.data.display_name.toString().lowercase()))
                viewModel.onEvent(FinalRegistrationEvent.WilayaChanged(resp.data.address?.state.toString().lowercase()))
                viewModel.onEvent(FinalRegistrationEvent.CityChanged(resp.data.address?.town.toString().lowercase()))
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .border(width = 1.dp, color = Color.White) // Add border here
                .padding(horizontal = 16.dp), // Add padding for the content
            verticalArrangement = Arrangement.Center
        ) {
            TextField(value = state.about, onValueChange = {
                viewModel.onEvent(FinalRegistrationEvent.AboutChanged(it))},
                isError = state.aboutError != null,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "About your self") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Create, contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(value = state.averageSalary, onValueChange = {
                viewModel.onEvent(FinalRegistrationEvent.AverageSalaryChanged(it))},
                isError = state.averageSalaryError != null,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Average salary") },

                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { showMap = !showMap } , modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp)) {
                Text("Select on Map")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(value = state.wilaya, onValueChange = {
                    viewModel.onEvent(FinalRegistrationEvent.WilayaChanged(it))},
                    isError = state.wilayaError != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    placeholder = { Text(text = "Wilaya") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.LocationOn, contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text
                    )
                )

                TextField(value = state.city, onValueChange = {
                    viewModel.onEvent(FinalRegistrationEvent.CityChanged(it))},
                    isError = state.cityError != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    placeholder = { Text(text = "City") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.LocationOn, contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text
                    )
                )
            }
            TextField(value = state.street, onValueChange = {
                viewModel.onEvent(FinalRegistrationEvent.StreetChanged(it))},
                isError = state.streetError != null,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Street") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.LocationOn, contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(value = state.workingAreas, onValueChange = {
                viewModel.onEvent(FinalRegistrationEvent.WorkingAreaChanged(it))},
                isError = state.workingAreasError != null,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Working Areas") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.LocationOn, contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {viewModel.onEvent(FinalRegistrationEvent.Submit)}) {
                Text(text = "Submit")
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
                            viewModel.onEvent(FinalRegistrationEvent.LatitudeChanged(point?.latitude().toString()))
                            viewModel.onEvent(FinalRegistrationEvent.LongitudeChanged(point?.longitude().toString()))
                            VM.getLocation(point!!.latitude().toString() , point!!.longitude().toString())
                            //showLocationField = true

                        }else Toast.makeText(context , "select location" , Toast.LENGTH_SHORT).show()


                    }) {

                        Text(text = "Select")
                    }

                }

            }
        }

    }
}
















