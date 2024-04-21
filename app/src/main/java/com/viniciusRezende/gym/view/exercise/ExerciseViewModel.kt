package com.viniciusRezende.gym.view.exercise

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.viniciusRezende.gym.models.ExerciseModel
import java.net.URL


class ExerciseViewModel :ViewModel(){
    private val db = Firebase.firestore
    private var trainingId:String =""
    private var data: ArrayList<ExerciseModel> = ArrayList();
    fun getData(callback: (result:ArrayList<ExerciseModel>) -> Unit) {
        data =ArrayList()


        db.collection("exercises").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val timestamp = document.data["date"] as com.google.firebase.Timestamp
                    val date = timestamp.toDate()
                    println(date)
                    data.add(
                        ExerciseModel(
                            document.id,
                            document.data["name"].toString(),
                            document.data["observation"].toString(),
                            URL(document.data["url"].toString())
                        )
                    )
                }
                callback.invoke(data)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    fun setTrainingId(id: String){
        trainingId =  id
    }
    fun save(exercise: ExerciseModel) {
        db.collection("exercice").document(trainingId).collection("exercices").add(exercise)
            .addOnSuccessListener { _ ->

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
    fun update(exercise: ExerciseModel) {
        db.collection("trainings")
            .document(exercise.id).collection("exercices").document(exercise.id).set(exercise)
            .addOnSuccessListener { _ ->

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
    fun delete(exercise: ExerciseModel) {
        db.collection("trainings").document(exercise.id).delete()
            .addOnSuccessListener { _ ->

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}