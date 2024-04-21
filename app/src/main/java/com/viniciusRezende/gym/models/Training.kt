package com.example.gym.models

import com.viniciusRezende.gym.models.ExerciseModel
import java.sql.Timestamp

data class Training(val name:Number, val description:String, val date:Timestamp, val exerciseModels:ArrayList<ExerciseModel>)
