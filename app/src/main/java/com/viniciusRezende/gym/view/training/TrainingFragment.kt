package com.viniciusRezende.gym.view.training


import ExerciseAdapter
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.viniciusRezende.gym.models.ExerciseModel
import com.viniciusRezende.gym.databinding.FragmentExerciseBinding
class TrainingFragment : Fragment() {

    companion object {
        fun newInstance() = TrainingFragment()
    }

    private val viewModel: TrainingViewModel by viewModels()
    private lateinit var exerciseAdapter: ExerciseAdapter

    private var _binding: FragmentExerciseBinding? = null


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExerciseBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTrainingTittle()
        setupRecyclerView()
    }
    private fun setupTrainingTittle() {
        binding.trainingTextView.text = viewModel.traningName
    }
    private fun setupRecyclerView() {
        this.exerciseAdapter = ExerciseAdapter({ sendToExercisePage(it) },viewModel.getData())
        binding.exercisesRecyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.exercisesRecyclerView.adapter = exerciseAdapter
    }
    private fun sendToExercisePage(exerciseModel: ExerciseModel) {

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
