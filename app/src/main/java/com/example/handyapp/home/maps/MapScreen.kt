//package com.example.handyapp.home.maps
//
//import android.content.Context
//import android.content.pm.PackageManager
//import android.graphics.Point
//import android.location.Location
//import android.widget.Toast
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Accessibility
//import androidx.compose.material3.Button
//import androidx.compose.material3.Icon
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.core.content.ContextCompat
//import androidx.core.graphics.drawable.toBitmap
//import com.example.handyapp.R
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.location.Priority
//import com.google.android.gms.tasks.CancellationTokenSource
//import com.mapbox.geojson.Point
//import com.mapbox.maps.CameraOptions
//import com.mapbox.maps.MapInitOptions
//import com.mapbox.maps.Style
//import com.mapbox.maps.extension.compose.MapboxMap
//import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
//import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
//import com.mapbox.maps.plugin.attribution.generated.AttributionSettings
//import com.mapbox.maps.plugin.compass.generated.CompassSettings
//import com.mapbox.maps.plugin.gestures.generated.GesturesSettings
//import com.mapbox.maps.plugin.scalebar.generated.ScaleBarSettings
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await
//
//
//@Composable
//fun MapScreen( lat : String , log : String , context : Context = LocalContext.current){
//    val mapViewportState = rememberMapViewportState {
//        setCameraOptions {
//            center(Point.fromLngLat(2.8770072419209303, 36.50321008456727))
//            zoom(3.0)
//            pitch(0.0)
//        }
//    }
//
//    val mapBoxUiSettings: GesturesSettings by remember {
//        mutableStateOf(GesturesSettings {
//            rotateEnabled = true
//            pinchToZoomEnabled = true
//            pitchEnabled = true
//        })
//    }
//
//    val compassSettings: CompassSettings by remember {
//        mutableStateOf(CompassSettings { enabled = false })
//    }
//
//    val scaleBarSetting: ScaleBarSettings by remember {
//        mutableStateOf(ScaleBarSettings { enabled = false })
//    }
//
//    var currentLoc by rememberSaveable {
//        mutableStateOf<Point?>(null)
//    }
//    val permissions = arrayOf(
//        android.Manifest.permission.ACCESS_FINE_LOCATION,
//        android.Manifest.permission.ACCESS_COARSE_LOCATION
//    )
//    var locationInfo: Location? = null
//    val locationClient =
//        remember { LocationServices.getFusedLocationProviderClient(context) }
//
//    val launcherMultiplePermissions =
//        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { permissionMaps ->
//            val areGaranted = permissionMaps.values.reduce { acc, b -> acc && b }
//            if (areGaranted) {
//                //locationRequired = true
//                Toast.makeText(context, "permission garanted", Toast.LENGTH_LONG).show()
//            } else {
//                Toast.makeText(context, "permission deneid", Toast.LENGTH_LONG).show()
//            }
//        }
//
//    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
//        MapboxMap(
//            modifier = Modifier.fillMaxSize(),
//            mapInitOptionsFactory = { context ->
//                MapInitOptions(
//                    context = context,
//                    styleUri = Style.OUTDOORS,
//                )
//            },
//            mapViewportState = mapViewportState,
//            compassSettings = compassSettings,
//            scaleBarSettings = scaleBarSetting,
//            gesturesSettings = mapBoxUiSettings,
//            attributionSettings = AttributionSettings {
//                enabled = false
//            },
//
//        ) {
//
//            if (currentLoc != null) {
//                PointAnnotation(point = currentLoc!!,
//                    iconImageBitmap = context.getDrawable(R.drawable.current_location)!!
//                        .toBitmap(),
//                    onClick = {
//                        Toast.makeText(
//                            context,
//                            "my location",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        true
//                    }
//                )
//            }
//            if (lat.isNotEmpty()) {
//                PointAnnotation(point = Point.fromLngLat(log.toDouble() , lat.toDouble()),
//                    iconImageBitmap = context.getDrawable(R.drawable.location)!!
//                        .toBitmap(),
//                    onClick = {
//
//                        true
//                    }
//                )
//            }
//        }
//        val scope = rememberCoroutineScope()
//        Column {
//            Button(shape = CircleShape, onClick = {
//
//                scope.launch {
//                    while (locationInfo?.latitude == null) {
//                        if (permissions.all {
//                                ContextCompat.checkSelfPermission(
//                                    context, it
//                                ) == PackageManager.PERMISSION_GRANTED
//                            }) {
//                            //get Location
//
//                            locationInfo = locationClient.getCurrentLocation(
//                                Priority.PRIORITY_HIGH_ACCURACY,
//                                CancellationTokenSource().token
//                            ).await()
//                            //locationInfo = locationClient.getLastLocation().await()
//
//
//                            if (locationInfo?.latitude != null && locationInfo?.longitude != null)
//                                mapViewportState.flyTo(
//                                    CameraOptions.Builder().zoom(16.0).center(
//                                        Point.fromLngLat(
//                                            locationInfo!!.longitude,
//                                            locationInfo!!.latitude
//                                        )
//                                    ).build()
//                                )
//                        } else launcherMultiplePermissions.launch(permissions)
//                        if (locationInfo?.latitude != null) {
//                            currentLoc =
//                                Point.fromLngLat(
//                                    locationInfo!!.longitude,
//                                    locationInfo!!.latitude
//                                )
//                        }
//                    }
//
//                }
//            }) {
//                Icon(
//                    imageVector = Icons.Filled.Accessibility,
//                    contentDescription = "current location"
//                )
//            }
//
//
//        }
//
//    }
//}