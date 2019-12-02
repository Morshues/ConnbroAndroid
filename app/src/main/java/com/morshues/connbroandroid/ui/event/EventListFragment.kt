package com.morshues.connbroandroid.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.morshues.connbroandroid.databinding.FragmentEventListBinding
import com.morshues.connbroandroid.util.InjectorUtils

class EventListFragment : Fragment() {

    private lateinit var binding: FragmentEventListBinding
    private val viewModel: EventListViewModel by viewModels {
        InjectorUtils.provideEventListViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventListBinding.inflate(inflater, container, false)

        val adapter = EventAdapter()
        binding.rvEvents.adapter = adapter
        subscribeUi(adapter)

        return binding.root
    }

    private fun subscribeUi(adapter: EventAdapter) {
        viewModel.events.observe(viewLifecycleOwner) { result ->
            adapter.submitList(result)
        }
    }
}