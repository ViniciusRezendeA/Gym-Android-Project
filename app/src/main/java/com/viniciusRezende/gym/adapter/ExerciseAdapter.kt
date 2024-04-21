package com.viniciusRezende.gym.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.gym.models.TrainingModel
import com.viniciusRezende.gym.databinding.ExerciseCardBinding
import com.viniciusRezende.gym.models.ExerciseModel


class ExerciseAdapter(
    private val onItemClick: (ExerciseModel) -> Unit,
    private var dataSet: ArrayList<ExerciseModel>
) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ExerciseViewHolder.create(parent, onItemClick)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ExerciseViewHolder -> {
                holder.bind(dataSet[position])
            }
        }
    }
    fun getData(): ArrayList<ExerciseModel> {
        return dataSet
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    class ExerciseViewHolder(
        var binding: ExerciseCardBinding,
        var onItemClick: (ExerciseModel) -> Unit
    ) : ViewHolder(binding.root) {
        fun bind(exercise: ExerciseModel) {
           binding.apply {
               exerciseTitle.text = exercise.name
               exerciseObservations.text = exercise.observations
               Glide.with(this.root).load(exercise.image).into(exerciseImage)
               cardBody.setOnClickListener { onItemClick.invoke(exercise) }
           }

        }

        companion object {
            fun create(parent: ViewGroup, onItemClick: (ExerciseModel) -> Unit,): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ExerciseCardBinding.inflate(inflater, parent, false)
                return ExerciseViewHolder(binding,onItemClick)
            }
        }
    }
}
