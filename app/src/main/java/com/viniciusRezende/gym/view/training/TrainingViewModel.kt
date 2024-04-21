package com.viniciusRezende.gym.view.training

import androidx.lifecycle.ViewModel
import com.viniciusRezende.gym.models.ExerciseModel
import java.net.URL

class TrainingViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    public val traningName = "Treino de bicepis "
    private val data: ArrayList<ExerciseModel> = ArrayList();
    fun loadData(){
        var exerciseModel = ExerciseModel("Agachamento","15 repeticoes", URL("https://assets-global.website-files.com/62d964a7de0430c6f4c45556/62e164d7ff091ae19393b01f_MG2_3238%20(1).jpeg"))

        data.add(exerciseModel)
        exerciseModel = ExerciseModel("Levantamento de halter","15 repeticoes", URL("https://assets-global.website-files.com/62d964a7de0430c6f4c45556/62e164d7ff091ae19393b01f_MG2_3238%20(1).jpeg"))
        data.add(exerciseModel)
    }
    fun getData(): ArrayList<ExerciseModel> {
        loadData()
        return data;
    }
}