package com.example.handyapp.home.jobs

//import Job
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handyapp.Response
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
//
//@HiltViewModel
//class JobsViewModel @Inject constructor() : ViewModel() {
//
//
//
//    /*private val _jobs = MutableStateFlow<List<Job>>(emptyList())
//        val jobs: StateFlow<List<Job>> = _jobs*/
//    private val _jobs: MutableState<Response<List<Job>>> = mutableStateOf(Response.onLoading)
//    val jobs: State<Response<List<Job>>> = _jobs
//
//    init {
//        getJobs()
//    }
//
//    private fun getJobs() {
//        viewModelScope.launch {
//            getJobsData().collect {
//                _jobs.value = it
//            }
//        }
//    }
//
//    private fun getJobsData(): Flow<Response<List<Job>>> = callbackFlow {
//        Response.onLoading
//        //val jobsList = mutableListOf<Job>()
//        val db = Firebase.firestore
//        val jobsCollection = db.collection("Jobs").addSnapshotListener { value, error ->
//            val resp: Response<List<Job>> =
//                if (value != null) {
//                    var jobs = arrayListOf<Job>()
//                    value.documents.forEach {
//                        val jobId = it.id
//                        val job = it.toObject(Job::class.java)!!
//                        job.id = jobId
//                        jobs.add(job)
//                    }
//                    Response.onSuccess(jobs)
//                } else Response.onFaillure(error?.localizedMessage ?: "Unknown error")
//
//            trySend(resp).isSuccess
//        }
//
//        awaitClose {
//            jobsCollection.remove()
//        }
//
//
//    }
//}

@HiltViewModel
class JobsViewModel @Inject constructor() : ViewModel() {
    private val firestore = Firebase.firestore

    private val _jobs = MutableStateFlow<Response<List<Job>>>(Response.onLoading)
    val jobs: StateFlow<Response<List<Job>>> = _jobs

    fun getHandymanJobs(handymanId: String) {
        viewModelScope.launch {
            try {
                val handymanRef = firestore.collection("Handyman").document(handymanId)
                val handymanDoc = handymanRef.get().await()

                if (handymanDoc.exists()) {
                    val handymanCategory = handymanDoc.getString("category")
                    if (handymanCategory != null) {
                        val jobsQuery = firestore.collection("Jobs")
                            .whereEqualTo("category", handymanCategory)
                            .get()
                            .await()

                        val jobsList = jobsQuery.documents.mapNotNull { jobDoc ->
                            jobDoc.toObject(Job::class.java)
                        }

                        _jobs.value = Response.onSuccess(jobsList)
                    } else {
                        _jobs.value = Response.onFaillure("Handyman category not found")
                    }
                } else {
                    _jobs.value = Response.onFaillure("Handyman not found")
                }
            } catch (e: Exception) {
                _jobs.value = Response.onFaillure(e.message ?: "An error occurred")
            }
        }
    }
}
