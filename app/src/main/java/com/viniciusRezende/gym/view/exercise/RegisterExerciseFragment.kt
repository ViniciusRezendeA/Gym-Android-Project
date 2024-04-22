package com.viniciusRezende.gym.view.exercise


import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.viniciusRezende.gym.R
import com.viniciusRezende.gym.databinding.FragmentRegisterExerciseBinding
import com.viniciusRezende.gym.models.ExerciseModel
import com.viniciusRezende.gym.models.TrainingModel
import java.io.ByteArrayOutputStream


class RegisterExerciseFragment : Fragment() {


    private val viewModel: ExerciseViewModel by viewModels()

    private var _binding: FragmentRegisterExerciseBinding? = null
    private var training: TrainingModel? = null
    private var exercise: ExerciseModel? = null

    private val binding get() = _binding!!

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val galleryUri = it
        try{
            binding.imageView.setImageURI(galleryUri)
        }catch(e:Exception){
            e.printStackTrace()
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterExerciseBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButton()
        training = arguments?.getSerializable("training") as? TrainingModel
        if (training != null) {
            viewModel.setTrainingId(training!!.id)
        }
         exercise = arguments?.getSerializable("exercise") as? ExerciseModel
        if (exercise != null) {
            setupUpdateScreen()
        }
    }

    private fun drawableConverter(): ByteArray {
        binding.imageView.isDrawingCacheEnabled = true
        binding.imageView.buildDrawingCache()
        val bitmap = (binding.imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }

    private fun setupButton() {
        binding.sendButton.setOnClickListener {
            var id = ""
            if (training?.id != null) {
                id = training!!.id
            }

            val newExercise = ExerciseModel(
                id,
                binding.nameEditText.text.toString(),
                binding.observationEditText.text.toString(),
                null
            )
            if (training?.id != null) {
                viewModel.update(newExercise,drawableConverter()) {
                    returnToLastScreen()
                }
            } else {
                viewModel.save(newExercise,drawableConverter()) {
                    returnToLastScreen()
                }
            }
        }
        binding.getImageButton.setOnClickListener {
            galleryLauncher.launch("image/*")
        }
    }
    private fun setupUpdateScreen() {
        exercise?.let { exercise ->
            binding.apply {

                observationEditText.setText(exercise.observations)
                nameEditText.setText(exercise.name)
                Glide.with(this.root).load(exercise.image).into(imageView)
                sendButton.text = getString(R.string.atualizar)
            }
        }
    }



    private fun returnToLastScreen() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
