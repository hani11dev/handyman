package com.example.handyapp.home.jobs

import Job
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handyapp.Response
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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
        val auth = FirebaseAuth.getInstance()
        var savedList: List<String>? = null
        val x = db.collection("HandyMan").document(auth.currentUser!!.uid).get().await()
        if (x.exists()) {
            savedList = x.get("SavedJobs") as? List<String>
        }
        val handyMan = db.collection("HandyMan").document(auth.currentUser!!.uid).get().await()
        val cat = handyMan.getString("Category")
        val jobsCollection = db.collection("Jobs").whereEqualTo("category" , cat).addSnapshotListener { value, error ->
            val resp: Response<List<Job>> =
                if (value != null) {
                    var jobs = arrayListOf<Job>()
                    value.documents.forEach {
                        val jobId = it.id
                        //val job = it.toObject(Job::class.java)!!
                        val job = Job(
                            id = it.id,
                            category = it.getString("category")?:"",
                            city = it.getString("city")?:"",
                            day = it.getString("day")?:"",
                            description = it.getString("description")?:"",
                            hour = it.getString("hour")?:"",
                            max = it.getLong("max")?.toInt()?:0,
                            min = it.getLong("min")?.toInt()?:0,
                            status = it.getString("status")?:"",
                            street = it.getString("street")?:"",
                            title = it.getString("title")?:"",
                            userId = it.getString("userId")?:"",
                            wilya = it.getString("wilya")?:"",
                            addingDate = it.getTimestamp("addingDate")?: Timestamp.now()
                        )
                        job.id = jobId
                        jobs.add(Job(
                            job.id,
                            job.category,
                            job.city,
                            job.day,
                            job.description,
                            job.hour,
                            job.max,
                            job.min,
                            job.status,
                            job.street,
                            job.title,
                            job.userId,
                            job.wilya,
                            savedList?.contains(it.id) ?: false,
                            addingDate = job.addingDate
                        ))
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