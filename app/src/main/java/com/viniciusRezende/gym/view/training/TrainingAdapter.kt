package com.viniciusRezende.gym.view.training

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.viniciusRezende.gym.databinding.ExerciseCardBinding
import com.viniciusRezende.gym.models.ExerciseModel


class TrainingAdapter(
    private val onItemClick: (ExerciseModel) -> Unit,
    private var dataSet: ArrayList<ExerciseModel>
) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ExerciseViewHolder.create(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ExerciseViewHolder -> {
                holder.bind(dataSet[position])
                println("eu tentnei")
            }
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    class ExerciseViewHolder(
        var binding: ExerciseCardBinding
    ) : ViewHolder(binding.root) {
        fun bind(exercise: ExerciseModel) {
           binding.apply {
               exerciseTitle.text = exercise.nome
               exerciseObservations.text = exercise.observations
               Glide.with(this.root).load(exercise.image).into(exerciseImage)
           }

        }

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ExerciseCardBinding.inflate(inflater, parent, false)
                return ExerciseViewHolder(binding)
            }
        }
    }
}
