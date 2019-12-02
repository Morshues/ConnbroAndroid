package com.morshues.connbroandroid.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.morshues.connbroandroid.R
import com.morshues.connbroandroid.db.model.Person
import android.app.DatePickerDialog
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.morshues.connbroandroid.databinding.FragmentFriendCreateBinding
import com.morshues.connbroandroid.util.DateTimeUtils
import com.morshues.connbroandroid.util.InjectorUtils
import java.util.*

class FriendCreateFragment : Fragment() {
    private lateinit var binding: FragmentFriendCreateBinding

    private val viewModel: FriendCreateViewModel by viewModels {
        InjectorUtils.provideFriendCreateViewModelFactory(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendCreateBinding.inflate(inflater, container, false)

        binding.setDateEditListener {
            val c = DateTimeUtils.dateToCalender(binding.tvBirthday.text) ?: Calendar.getInstance()
            val dlg = DatePickerDialog(requireActivity(),
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    binding.tvBirthday.text = DateTimeUtils.toDateString(year, month, dayOfMonth)
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
            dlg.show()
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.friend_create, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                createFriend()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createFriend() {
        view?.let { v ->
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }

        binding.apply {
            val newFriend = Person(
                firstName = etFirstName.text.toString().trim(),
                midName = etMidName.text.toString().trim(),
                lastName = etLastName.text.toString().trim(),
                nickName = etNickName.text.toString().trim(),
                birthday = DateTimeUtils.dateToCalender(tvBirthday.text),
                note = etNote.text.toString().trim()
            )
            if (newFriend.nickName.isNotBlank() || newFriend.fullName().isNotBlank()) {
                viewModel.insert(newFriend)
                binding.root.findNavController().popBackStack()
            } else {
                Snackbar.make(
                    binding.root,
                    R.string.msg_name_cannot_be_blank,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

}
