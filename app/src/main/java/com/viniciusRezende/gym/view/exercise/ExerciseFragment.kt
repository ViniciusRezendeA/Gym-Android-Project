package com.viniciusRezende.gym.view.exercise



import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.viniciusRezende.gym.R
import com.viniciusRezende.gym.adapter.ExerciseAdapter
import com.viniciusRezende.gym.databinding.RecyclerViewFragmentBinding
import com.viniciusRezende.gym.models.TrainingModel
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.net.URL


class ExerciseFragment : Fragment()  {


    private val viewModel: ExerciseViewModel by viewModels()
    private lateinit var exerciseAdapter: ExerciseAdapter

    private var _binding: RecyclerViewFragmentBinding? = null

    private lateinit var training: TrainingModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = RecyclerViewFragmentBinding.inflate(inflater, container, false)
        training = (arguments?.getSerializable("training") as? TrainingModel)!!
        viewModel.setTrainingId(training.id)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        sendToRegisterPage()

    }


    private  fun setupRecyclerView() {
        viewModel.getData{result ->
            this.exerciseAdapter = ExerciseAdapter( result)
            binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
            binding.recyclerView.adapter = exerciseAdapter
            enableSwipeToDeleteAndUpdate()
        }

    }

    private fun sendToRegisterPage() {
        binding.addButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("label", "Registrar Exercicio")
            bundle.putSerializable("training",training)
            findNavController().navigate(
                R.id.action_ExerciseFragment_to_registerExerciseFragment,
                bundle
            )
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun enableSwipeToDeleteAndUpdate() {
        val callback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeLeftBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                    .addSwipeLeftActionIcon(android.R.drawable.ic_delete)
                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green
                        )
                    )
                    .addSwipeRightActionIcon(android.R.drawable.ic_menu_edit)
                    .create()
                    .decorate()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (direction == ItemTouchHelper.RIGHT) {
                    //update

                    val bundle = Bundle()
                    bundle.putString("label", "Atualizar Exercicio")
                    bundle.putSerializable("training",training)
                    bundle.putSerializable("exercise",exerciseAdapter.getData()[position])
                    findNavController().navigate(
                        R.id.action_ExerciseFragment_to_registerExerciseFragment,
                        bundle
                    )
                } else {
                    //delete
                    viewModel.delete(exerciseAdapter.getData()[position])
                    viewModel.getData{result ->
                        this@ExerciseFragment.exerciseAdapter = ExerciseAdapter(result)
                        binding.recyclerView.layoutManager = LinearLayoutManager(this@ExerciseFragment.context)
                        binding.recyclerView.adapter = exerciseAdapter
                    }                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

}
