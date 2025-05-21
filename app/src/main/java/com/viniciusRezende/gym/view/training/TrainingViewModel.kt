package com.viniciusRezende.gym.view.training

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.viniciusRezende.gym.models.TrainingModel


class TrainingViewModel :ViewModel(){
    val db = Firebase.firestore
    private var data: ArrayList<TrainingModel> = ArrayList()
    fun getData(callback: (result:ArrayList<TrainingModel>) -> Unit) {
        data =ArrayList()


        db.collection("trainings").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val timestamp = document.data["date"] as com.google.firebase.Timestamp
                    val date = timestamp.toDate()
                    println(date)
                    data.add(
                        TrainingModel(
                            document.id,
                            document.data["name"].toString(),
                            document.data["description"].toString(),
                            date
                        )
                    )
                }
                callback.invoke(data)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents.", exception)
            }
    }


    fun save(training: TrainingModel, callback: (result: Boolean) -> Unit) {
        db.collection("trainings")
            .add(training)
            .addOnSuccessListener { _ ->
                callback.invoke(true)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding document", e)
                callback.invoke(false)
            }
    }
    fun update(training: TrainingModel, callback: (result: Boolean) -> Unit) {
        db.collection("trainings")
            .document(training.id).set(training)
            .addOnSuccessListener { _ ->
                callback.invoke(true)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding document", e)
            }
    }
    fun delete(training: TrainingModel) {
        db.collection("trainings").document(training.id).delete()
            .addOnSuccessListener { _ ->

            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding document", e)
            }
    }
}