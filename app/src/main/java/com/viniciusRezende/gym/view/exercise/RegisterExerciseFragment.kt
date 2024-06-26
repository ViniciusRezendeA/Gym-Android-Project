package com.viniciusRezende.gym.view.exercise


import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
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
import kotlin.math.log


class RegisterExerciseFragment : Fragment() {


    private val viewModel: ExerciseViewModel by viewModels()

    private var _binding: FragmentRegisterExerciseBinding? = null
    private var training: TrainingModel? = null
    private var exercise: ExerciseModel? = null

    private val binding get() = _binding!!

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val galleryUri = it
        try {
            binding.imageView.setImageURI(galleryUri)
        } catch (e: Exception) {
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
        setupTextField()
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
    private fun setupTextField() {
        binding.nameEditText.setOnEditorActionListener { _, _, _ ->
            binding.observationEditText.requestFocus()
        }
    }
    private fun setupButton() {
        binding.sendButton.setOnClickListener {

            var id: String? = exercise?.id
            if(id == null){
                id = ""
            }
            Log.d("tap",id)
            if (binding.nameEditText.text.toString()
                    .isNotEmpty() && binding.observationEditText.text.toString()
                    .isNotEmpty()
            ) {
                activity?.findViewById<View>(R.id.shadowView)?.visibility= View.VISIBLE
                activity?.findViewById<ProgressBar>(R.id.progressBar)?.visibility= View.VISIBLE
                val newExercise = ExerciseModel(
                    id,
                    binding.nameEditText.text.toString(),
                    binding.observationEditText.text.toString(),
                    null
                )

                if (!exercise?.id.isNullOrEmpty()) {

                    viewModel.update(newExercise, drawableConverter()) {
                        returnToLastScreen()
                    }
                } else {

                    viewModel.save(newExercise, drawableConverter()) {
                        returnToLastScreen()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Complete os campos", Toast.LENGTH_LONG).show()
            }
        }
        binding.imageView.setOnClickListener {
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
        activity?.findViewById<View>(R.id.shadowView)?.visibility= View.GONE
        activity?.findViewById<ProgressBar>(R.id.progressBar)?.visibility= View.GONE
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
