package com.viniciusRezende.gym.view.exercise

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.viniciusRezende.gym.models.ExerciseModel
import java.net.URL


class ExerciseViewModel : ViewModel() {
    private val db = Firebase.firestore
    private var trainingId: String = ""
    private var data: ArrayList<ExerciseModel> = ArrayList()
    private val storageRef = FirebaseStorage.getInstance().reference
    fun getData(callback: (result: ArrayList<ExerciseModel>) -> Unit) {
        data = ArrayList()
        Log.d("tag", trainingId)
        db.collection("trainings").document(trainingId).collection("exercises").get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    data.add(
                        ExerciseModel(
                            document.id,
                            document.data["name"].toString(),
                            document.data["observations"].toString(),
                            URL(document.data["image"].toString())
                        )
                    )
                }
                callback.invoke(data)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    fun setTrainingId(id: String) {
        trainingId = id
    }

    private fun saveImage(data: ByteArray, name: String, callback: (URL) -> Unit) {


        // Create a reference to "mountains.jpg"
        val imageRef = storageRef.child("$trainingId$name.jpg")
        val uploadTask = imageRef.putBytes(data)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result

                callback.invoke(URL(downloadUri.toString()))
            } else {
                // Handle failures
                // ...
            }
        }
    }

    fun save(exercise: ExerciseModel, data: ByteArray, callback: (result: Boolean) -> Unit) {
        saveImage(data, exercise.name) {
            exercise.image = it
            db.collection("trainings").document(trainingId).collection("exercises").add(exercise)
                .addOnSuccessListener { _ ->
                    callback.invoke(true)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }

    fun update(exercise: ExerciseModel, data: ByteArray, callback: (result: Boolean) -> Unit) {
        saveImage(data, exercise.name) {
            exercise.image = it
            db.collection("trainings").document(trainingId).collection("exercises")
                .document(exercise.id).set(exercise)
                .addOnSuccessListener { _ ->
                    callback.invoke(true)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }

    }
    fun deleteImage(name: String){

        val desertRef = storageRef.child("$trainingId$name.jpg")

        desertRef.delete().addOnSuccessListener {
            // File deleted successfully
        }.addOnFailureListener {
            // Uh-oh, an error occurred!
        }
    }
    fun delete(exercise: ExerciseModel) {

        db.collection("trainings").document(trainingId).collection("exercises")
            .document(exercise.id).delete()
            .addOnSuccessListener { _ ->
                deleteImage(exercise.name)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}