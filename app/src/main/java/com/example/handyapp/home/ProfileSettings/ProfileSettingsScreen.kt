package com.example.handyapp.home.ProfileSettings


import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.tween
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarHalf
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
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

import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.handyapp.R
import com.example.handyapp.Response
import com.example.handyapp.domain.model.HandyMan
import kotlinx.coroutines.delay
import java.text.BreakIterator
import java.text.StringCharacterIterator


@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class
)
@Composable
fun ProfileSettingsScreen(viewModel: ProfileSettingsViewModel = hiltViewModel() , context : Context = LocalContext.current) {
    //Column(modifier = Modifier.fillMaxSize()) {

    var changed by rememberSaveable {
        mutableStateOf(false)
    }
    var about by rememberSaveable {
        mutableStateOf("")
    }
    var services by rememberSaveable {
        mutableStateOf("")
    }
    var workingAreas by rememberSaveable {
        mutableStateOf("")
    }
    var clientView by rememberSaveable {
        mutableStateOf(true)
    }
        when (val resp = viewModel.cardInfo.value) {
            is Response.onLoading -> {}
            is Response.onFaillure -> {}
            is Response.onSuccess -> {
                //cardProvider(item = resp.data)
                val item = resp.data
                var substringText by remember {
                    mutableStateOf("")
                }
                Column(modifier = Modifier.fillMaxSize()) {
                    ElevatedCard(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(136.dp)
                            .padding(vertical = 2.dp, horizontal = 8.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .padding(8.dp), contentAlignment = Alignment.TopStart
                            ) {

                                AsyncImage(
                                    model = item.ProfileImage,
                                    contentDescription = "HandyMen Image",
                                    modifier = Modifier
                                        .size(128.dp)
                                        .clip(
                                            RoundedCornerShape(8.dp)
                                        ),
                                    contentScale = ContentScale.Crop
                                )
                                if (item.Rating > 0.0) {
                                    ElevatedCard(
                                        modifier = Modifier.padding(start = 2.dp, top = 2.dp),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {

                                        Box(
                                            modifier = Modifier
                                                .wrapContentSize()
                                                .background(MaterialTheme.colorScheme.background),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Rounded.Star,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(18.dp),
                                                    tint = colorResource(id = R.color.star)
                                                )
                                                Text(
                                                    text = item.Rating.toDouble().toString(),
                                                    textAlign = TextAlign.Center,
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 16.sp,
                                                    modifier = Modifier.padding(end = 2.dp),
                                                    letterSpacing = -1.sp
                                                )
                                            }
                                        }
                                    }

                                }
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(vertical = 12.dp), verticalArrangement = Arrangement.Center
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .weight(1f),
                                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.lightGray))
                                    ) {
                                        Text(
                                            text = item.Category,
                                            modifier = Modifier.padding(horizontal = 4.dp),
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    var text by rememberSaveable {
                                        mutableStateOf("${item.FirstName} ${item.LastName}")
                                    }
                                    val breakIterator =
                                        remember(text) { BreakIterator.getCharacterInstance() }
                                    val typingDelayInMs = 50L


                                    LaunchedEffect(text) {
                                        // Initial start delay of the typing animation
                                        delay(1000)
                                        breakIterator.text = StringCharacterIterator(text)

                                        var nextIndex = breakIterator.next()
                                        // Iterate over the string, by index boundary
                                        while (nextIndex != BreakIterator.DONE) {
                                            substringText = text.subSequence(0, nextIndex).toString()
                                            // Go to the next logical character boundary
                                            nextIndex = breakIterator.next()
                                            delay(typingDelayInMs)
                                        }
                                    }
                                    Text(
                                        text = substringText,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.loc),
                                            contentDescription = "location",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(text = "${item.City},${item.Wilaya}", textAlign = TextAlign.Center)
                                    }
                                    Text(text = buildAnnotatedString {
                                        withStyle(
                                            SpanStyle(
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        ) {
                                            append(text = item.AverageSalary.toString() + "DA")
                                        }
                                        withStyle(SpanStyle(fontWeight = FontWeight.Light)) {
                                            append("/hr")
                                        }
                                    })
                                }
                            }
                        }
                    }
                    var state by remember { mutableStateOf(0) }
                    val titles = listOf("Personal", "Professional")
                    val pagerState = rememberPagerState(initialPage = 0) {
                        titles.size
                    }
                    LaunchedEffect(key1 = pagerState.currentPage) {
                        state = pagerState.currentPage
                    }
                    SecondaryTabRow(selectedTabIndex = state) {
                        titles.forEachIndexed { index, title ->
                            Tab(selected = state == index, onClick = { state = index },
                                text = { Text(text = title) })
                        }
                    }
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        var progressState by rememberSaveable {
                            mutableStateOf(false)
                        }
                        if (progressState) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f), contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(
                                        64.dp
                                    )
                                )
                            }
                        }
                        AnimatedContent(targetState = state, transitionSpec = {
                            slideIntoContainer(
                                animationSpec = tween(300, easing = EaseInBounce),
                                towards = AnimatedContentTransitionScope.SlideDirection.Up
                            ).with(
                                slideOutOfContainer(
                                    animationSpec = tween(300, easing = EaseOutBounce),
                                    towards = AnimatedContentTransitionScope.SlideDirection.Down
                                )
                            )
                        }) {
                            when (state) {
                                0 -> {
                                }
                                1 -> {
                                    var progressState by remember {
                                        mutableStateOf(false)
                                    }
                                    if (progressState) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize(), contentAlignment = Alignment.Center
                                        ) {
                                            androidx.compose.material3.CircularProgressIndicator(modifier = Modifier.size(64.dp))
                                        }
                                    }

                                    var nbRating by rememberSaveable {
                                        mutableStateOf("")
                                    }
                                    when (val resp = viewModel.profileInfo.value) {
                                        is Response.onLoading -> {
                                            progressState = true
                                        }

                                        is Response.onFaillure -> {
                                            progressState = false
                                            Toast.makeText(LocalContext.current, "failled", Toast.LENGTH_SHORT).show()
                                        }

                                        is Response.onSuccess -> {
                                            progressState = false
                                            val info = resp.data
                                            about = info.About
                                            services = info.SubCategory
                                            workingAreas = info.WorkingAreas


                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(colorResource(id = R.color.lightGray))
                                                    .verticalScroll(rememberScrollState())
                                                    .padding(bottom = 64.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(95.dp)
                                                        .background(MaterialTheme.colorScheme.background)
                                                        .padding(horizontal = 8.dp)
                                                ) {
                                                    Row(
                                                        modifier = Modifier.fillMaxSize(),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.Center
                                                    ) {
                                                        Column(
                                                            modifier = Modifier
                                                                .weight(0.3f)
                                                                .fillMaxHeight(),
                                                            verticalArrangement = Arrangement.Center,
                                                            horizontalAlignment = Alignment.CenterHorizontally
                                                        ) {
                                                            when(val respo = viewModel.rating.value){
                                                                is Response.onLoading -> {}
                                                                is Response.onFaillure -> {
                                                                    Toast.makeText(context , respo.message , Toast.LENGTH_SHORT).show()}
                                                                is Response.onSuccess -> {
                                                                    var ratingHash = respo.data
                                                                    var rating = ratingHash["Rating"] as Float
                                                                    nbRating = ratingHash["nbRating"].toString()

                                                                    val mod = rating % 1
                                                                    if (mod > 0 && mod < 0.25) rating = rating.toInt().toFloat()
                                                                    if (mod > 0.25 && mod < 0.75) rating = rating.toInt() + 0.5F
                                                                    if (mod > 0.75 && mod < 1) rating = rating.toInt() + 1F
                                                                    var halfStar = if (!rating.isNaN()){ (rating % 1) != 0F} else false
                                                                    Text(
                                                                        text = if (rating.isNaN()) 0F.toString() else rating.toString(),
                                                                        fontWeight = FontWeight.Bold,
                                                                        fontSize = 36.sp,
                                                                    )
                                                                    Row(modifier = Modifier) {
                                                                        for (i in 1..5) {
                                                                            Icon(
                                                                                modifier = Modifier.weight(1f),
                                                                                tint = colorResource(R.color.star),
                                                                                imageVector = if (i <= rating) {
                                                                                    Icons.Rounded.Star
                                                                                } else {
                                                                                    if (halfStar) {
                                                                                        halfStar = false
                                                                                        Icons.Rounded.StarHalf
                                                                                    } else Icons.Rounded.StarOutline
                                                                                }, contentDescription = null
                                                                            )
                                                                        }
                                                                    }

                                                                }
                                                            }
                                                        }
                                                        Column(
                                                            modifier = Modifier
                                                                .weight(1f)
                                                                .padding(horizontal = 8.dp)
                                                        ) {
                                                            Text(text = buildAnnotatedString {
                                                                withStyle(
                                                                    SpanStyle(
                                                                        fontSize = 14.sp,
                                                                        fontWeight = FontWeight.SemiBold
                                                                    )
                                                                ) {
                                                                    append(nbRating.toString())
                                                                }
                                                                withStyle(SpanStyle(color = Color.Gray)) {
                                                                    append(" Reviews")
                                                                }
                                                            })
                                                            Text(text = buildAnnotatedString {
                                                                withStyle(
                                                                    SpanStyle(
                                                                        fontSize = 14.sp,
                                                                        fontWeight = FontWeight.SemiBold
                                                                    )
                                                                ) {
                                                                    append(info.OrdersCompleted)
                                                                }
                                                                withStyle(SpanStyle(color = Color.Gray)) {
                                                                    append(" Orders Completed")
                                                                }
                                                            })
                                                        }
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(8.dp))



                                                Text(
                                                    text = "About Profissional",
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 16.sp,
                                                    modifier = Modifier.padding(start = 16.dp),
                                                    color = Color.Black
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))

                                                var expanded by remember {
                                                    mutableStateOf(
                                                        false
                                                    )
                                                }

                                                Column(modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(MaterialTheme.colorScheme.background)
                                                    .animateContentSize()
                                                    .pointerInput(true) {
                                                        detectTapGestures(
                                                            onLongPress = { clientView = false },
                                                            onTap = {
                                                                expanded = !expanded
                                                            }
                                                        )
                                                    }
                                                    ) {

                                                    AnimatedVisibility(visible = !clientView) {
                                                        TextField(
                                                            value = about,
                                                            onValueChange = { about = it
                                                                            changed = true},
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(8.dp)

                                                        )
                                                    }

                                                    AnimatedVisibility(visible = clientView) {
                                                        Text(
                                                            text = info.About, maxLines = if (!expanded) 4 else Int.MAX_VALUE,
                                                            modifier = Modifier.padding(8.dp),
                                                            fontSize = 12.sp,
                                                            overflow = TextOverflow.Ellipsis
                                                        )

                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = "Services",
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 16.sp,
                                                    modifier = Modifier.padding(start = 16.dp),
                                                    color = Color.Black
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .background(MaterialTheme.colorScheme.background)
                                                        .animateContentSize()
                                                        .pointerInput(true) {
                                                            detectTapGestures(
                                                                onLongPress = {
                                                                    clientView = false
                                                                },
                                                            )
                                                        }
                                                ) {
                                                    AnimatedVisibility(visible = !clientView) {
                                                        TextField(
                                                            value = services,
                                                            onValueChange = { services = it
                                                                            changed = true},
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(8.dp)

                                                        )
                                                    }
                                                    AnimatedVisibility(visible = clientView) {
                                                        Text(
                                                            text = services, //servicesText,
                                                            modifier = Modifier.padding(8.dp),
                                                            fontSize = 12.sp,
                                                            overflow = TextOverflow.Ellipsis
                                                        )
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = "Working areas",
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 16.sp,
                                                    modifier = Modifier.padding(start = 16.dp),
                                                    color = Color.Black
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .background(MaterialTheme.colorScheme.background)
                                                        .animateContentSize()
                                                        .pointerInput(true) {
                                                            detectTapGestures(
                                                                onLongPress = {
                                                                    clientView = false
                                                                },
                                                            )
                                                        }
                                                ) {
                                                    AnimatedVisibility(visible = !clientView) {
                                                        TextField(
                                                            value = workingAreas,
                                                            onValueChange = { workingAreas = it
                                                                            changed = true},
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(8.dp)

                                                        )
                                                    }
                                                    AnimatedVisibility(visible = clientView) {
                                                        Text(
                                                            text = workingAreas,//workingAreasText,
                                                            modifier = Modifier.padding(8.dp),
                                                            fontSize = 12.sp,
                                                            overflow = TextOverflow.Ellipsis
                                                        )

                                                    }
                                                }


                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                    var progressState by rememberSaveable { mutableStateOf(false) }
                    AnimatedVisibility(visible = changed) {
                        Row(verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(horizontal = 16.dp , vertical = 4.dp)) {
                            ExtendedFloatingActionButton(
                                onClick = {
                                    if (about.isNotEmpty() && services.isNotEmpty() && workingAreas.isNotEmpty()){
                                        progressState = true
                                        viewModel.updateProfessionalInfo(hashMapOf<String , String>(
                                            "About" to about,
                                            "WorkingAreas" to workingAreas,
                                            "SubCategory" to services
                                        ))

                                    }

                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    //.padding(horizontal = 16.dp, vertical = 4.dp)
                                    .height(48.dp)
                                    .weight(1f),
                                containerColor = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(8.dp),
                                elevation = FloatingActionButtonDefaults.elevation(0.dp)
                            ) {
                                Row (verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.spacedBy(4.dp)){

                                    when(val resp = viewModel.updateProfessionalInfoStatus.value){
                                        is Response.onFaillure -> {
                                            progressState = false
                                            Toast.makeText(context , resp.message , Toast.LENGTH_SHORT).show()
                                        }
                                        is Response.onLoading -> {
                                        }
                                        is Response.onSuccess -> {
                                            LaunchedEffect(true) {
                                                progressState = false
                                                clientView = true
                                                Toast.makeText(context , "Update successful" , Toast.LENGTH_SHORT).show()

                                            }
                                        }
                                    }
                                    Text(
                                        text = "Save Changes",
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                        fontSize = 22.sp
                                    )
                                    AnimatedVisibility(visible = progressState) {
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
                            /*SmallFloatingActionButton(
                                onClick = { clientView = true },
                                modifier = Modifier.size(48.dp),
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Visibility,
                                    contentDescription = null
                                )
                            }*/

                        }

                    }


                }
            }

        }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun cardProvider(
    item: HandyMan
) {

    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondary),
        modifier = Modifier
            .fillMaxWidth()
            .height(136.dp)
            .padding(vertical = 2.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(8.dp)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(8.dp), contentAlignment = Alignment.TopStart
            ) {

                AsyncImage(
                    model = item.ProfileImage,
                    contentDescription = "HandyMen Image",
                    modifier = Modifier
                        .size(128.dp)
                        .clip(
                            RoundedCornerShape(8.dp)
                        ),
                    contentScale = ContentScale.Crop
                )
                if (item.Rating > 0.0) {
                    ElevatedCard(
                        modifier = Modifier.padding(start = 2.dp, top = 2.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .wrapContentSize()
                                .background(MaterialTheme.colorScheme.background),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = colorResource(id = R.color.star)
                                )
                                Text(
                                    text = item.Rating.toDouble().toString(),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(end = 2.dp),
                                    letterSpacing = -1.sp
                                )
                            }
                        }
                    }

                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 12.dp), verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier
                            .wrapContentSize()
                            .weight(1f),
                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.lightGray))
                    ) {
                        Text(
                            text = item.Category,
                            modifier = Modifier.padding(horizontal = 4.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    val text =
                        "This text animates as though it is being typed \uD83E\uDDDE\u200D♀\uFE0F \uD83D\uDD10  \uD83D\uDC69\u200D❤\uFE0F\u200D\uD83D\uDC68 \uD83D\uDC74\uD83C\uDFFD"
                    val breakIterator =
                        remember("${item.FirstName} ${item.LastName}") { BreakIterator.getCharacterInstance() }
                    val typingDelayInMs = 50L

                    var substringText by remember {
                        mutableStateOf("")
                    }
                    LaunchedEffect(text) {
                        // Initial start delay of the typing animation
                        delay(1000)
                        breakIterator.text = StringCharacterIterator(text)

                        var nextIndex = breakIterator.next()
                        // Iterate over the string, by index boundary
                        while (nextIndex != BreakIterator.DONE) {
                            substringText = text.subSequence(0, nextIndex).toString()
                            // Go to the next logical character boundary
                            nextIndex = breakIterator.next()
                            delay(typingDelayInMs)
                        }
                    }
                    Text(
                        text = substringText,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.loc),
                            contentDescription = "location",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(text = "${item.City},${item.Wilaya}", textAlign = TextAlign.Center)
                    }
                    Text(text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append(text = item.AverageSalary.toString() + "DA")
                        }
                        withStyle(SpanStyle(fontWeight = FontWeight.Light)) {
                            append("/hr")
                        }
                    })
                }
            }
        }
    }


}
