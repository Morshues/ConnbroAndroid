package com.morshues.connbroandroid.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import com.morshues.connbroandroid.R
import com.morshues.connbroandroid.databinding.FragmentFriendsBinding
import com.morshues.connbroandroid.util.InjectorUtils

class FriendsFragment : Fragment() {

    private lateinit var binding: FragmentFriendsBinding
    private val viewModel: FriendsViewModel by viewModels {
        InjectorUtils.provideFriendListViewModelFactory(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.friend_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_event -> {
                val direction = FriendsFragmentDirections.actionMainFragmentToEventListFragment()
                view?.findNavController()?.navigate(direction)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun subscribeUi(adapter: FriendsAdapter) {
        viewModel.allFriends.observe(viewLifecycleOwner) { result ->
            adapter.submitList(result)
        }
    }

}
