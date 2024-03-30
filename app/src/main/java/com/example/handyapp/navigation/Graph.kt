package com.example.handyapp.navigation

sealed class Graph (val route : String){
    object Auth : Graph("authentification_graph")
    object onBoarding : Graph("onBoarding_graph")
    object Browse : Graph("Browse_graph")
    object Waiting : Graph("waiting_graph")
    object Refused : Graph("refused_graph")
    object Accepted: Graph("accepted_graph")

}
