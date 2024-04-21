package com.viniciusRezende.gym.models

import java.io.Serializable
import java.net.URL

data class ExerciseModel(var id:String,val name:String, val observations:String, val image: URL):
    Serializable