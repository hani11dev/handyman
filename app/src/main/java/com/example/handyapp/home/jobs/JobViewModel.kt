package com.example.handyapp.home.jobs

import Job
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
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobsViewModel @Inject constructor() : ViewModel() {

    /*private val _jobs = MutableStateFlow<List<Job>>(emptyList())
    val jobs: StateFlow<List<Job>> = _jobs*/
    private val _jobs: MutableState<Response<List<Job>>> = mutableStateOf(Response.onLoading)
    val jobs: State<Response<List<Job>>> = _jobs

    init {
        getJobs()
    }

    private fun getJobs() {
        viewModelScope.launch {
            getJobsData().collect {
                _jobs.value = it
            }
        }
    }

    private fun getJobsData(): Flow<Response<List<Job>>> = callbackFlow {
        Response.onLoading
        //val jobsList = mutableListOf<Job>()
        val db = Firebase.firestore
        val jobsCollection = db.collection("Jobs").addSnapshotListener { value, error ->
            val resp: Response<List<Job>> =
                if (value != null) {
                    var jobs = arrayListOf<Job>()
                    value.documents.forEach {
                        val jobId = it.id
                        val job = it.toObject(Job::class.java)!!
                        job.id = jobId
                        jobs.add(job)
                    }
                    Response.onSuccess(jobs)
                } else Response.onFaillure(error?.localizedMessage ?: "Unknown error")

            trySend(resp).isSuccess
        }

        awaitClose {
            jobsCollection.remove()
        }


    }
}