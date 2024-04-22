package com.viniciusRezende.gym.view.training


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.viniciusRezende.gym.R
import com.viniciusRezende.gym.databinding.FragmentRegisterTrainingBinding
import com.viniciusRezende.gym.models.TrainingModel
import java.time.Instant
import java.util.Date


class RegisterTrainingFragment : Fragment() {


    private val viewModel: TrainingViewModel by viewModels()

    private var _binding: FragmentRegisterTrainingBinding? = null
    private var training: TrainingModel? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterTrainingBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButton()
        training = arguments?.getSerializable("training") as? TrainingModel
        if (training != null) {
            setupUpdateScreen()
        }
    }

    private fun setupButton() {
        binding.sendButton.setOnClickListener {
            var id = ""
            if (training?.id != null) {
                id = training!!.id
            }

            val newTraining = TrainingModel(
                id,
                binding.nameEditText.text.toString(),
                binding.descriptionEditText.text.toString(),
                Date.from(Instant.now())
            )
            //add logica de salvar
            if (training?.id != null) {
                viewModel.update(newTraining)
            } else {
                viewModel.save(newTraining)
            }
            returnToLastScreen()
        }
    }

    private fun setupUpdateScreen() {
        training?.let { training ->
            binding.apply {

                descriptionEditText.setText(training.description)
                nameEditText.setText(training.name)
                sendButton.setText(R.string.atualizar)
            }
        }
    }



    private fun returnToLastScreen() {
        findNavController().popBackStack();
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
