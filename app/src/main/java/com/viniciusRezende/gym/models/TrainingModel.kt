package com.viniciusRezende.gym.models

import java.io.Serializable
import java.util.Date
data class TrainingModel(var id:String, val name:String, val description:String, val date: Date) : Serializable