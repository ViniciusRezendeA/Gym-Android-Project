package com.viniciusRezende.gym.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.viniciusRezende.gym.databinding.TrainingCardBinding
import com.viniciusRezende.gym.models.TrainingModel


class TrainingAdapter(
    private val onItemClick: (TrainingModel) -> Unit,
    private var dataSet: ArrayList<TrainingModel>
) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        TrainingViewHolder.create(parent, onItemClick)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is TrainingViewHolder -> {
                holder.bind(dataSet[position])
            }
        }
    }

    fun getData(): ArrayList<TrainingModel> {
        return dataSet
    }
    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    class TrainingViewHolder(
        var binding: TrainingCardBinding,
        var onItemClick: (TrainingModel) -> Unit
    ) : ViewHolder(binding.root) {
        fun bind(training: TrainingModel) {
           binding.apply {
               trainingTitle.text = training.name
               trainingDescription.text = training.description
               trainingDate.text = DateFormat.format("yyyy-mm-dd hh:ss",training.date)
               cardBody.setOnClickListener {onItemClick.invoke(training)  }
           }

        }

        companion object {
            fun create(parent: ViewGroup,onItemClick: (TrainingModel) -> Unit): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = TrainingCardBinding.inflate(inflater, parent, false)
                return TrainingViewHolder(binding,onItemClick)
            }
        }
    }
}
