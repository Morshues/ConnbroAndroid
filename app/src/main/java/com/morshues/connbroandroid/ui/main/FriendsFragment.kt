package com.morshues.connbroandroid.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import com.morshues.connbroandroid.databinding.FragmentFriendsBinding
import com.morshues.connbroandroid.util.InjectorUtils

class FriendsFragment : Fragment() {

    private lateinit var binding: FragmentFriendsBinding
    private val viewModel: FriendsViewModel by viewModels {
        InjectorUtils.provideFriendListViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendsBinding.inflate(inflater, container, false)

        val adapter = FriendsAdapter()
        binding.rvFriends.adapter = adapter
        subscribeUi(adapter)

        binding.btnAdd.setOnClickListener {
            val direction =
                FriendsFragmentDirections.actionMainFragmentToFriendCreateFragment()
            it.findNavController().navigate(direction)
        }

        return binding.root
    }

    private fun subscribeUi(adapter: FriendsAdapter) {
        viewModel.allFriends.observe(viewLifecycleOwner) { result ->
            adapter.submitList(result)
        }
    }

}
