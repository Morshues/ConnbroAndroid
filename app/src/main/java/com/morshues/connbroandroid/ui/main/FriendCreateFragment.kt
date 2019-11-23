package com.morshues.connbroandroid.ui.main

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.morshues.connbroandroid.Page
import com.morshues.connbroandroid.R
import com.morshues.connbroandroid.db.model.Person
import com.morshues.connbroandroid.repo.ConnbroRepository
import kotlinx.android.synthetic.main.fragment_friend_create.view.*
import android.app.DatePickerDialog
import android.view.inputmethod.InputMethodManager
import com.morshues.connbroandroid.util.DateTimeUtils
import kotlinx.android.synthetic.main.fragment_friend_create.*
import java.util.*

class FriendCreateFragment : Fragment() {
    private lateinit var mRepository: ConnbroRepository

    private var mListener: OnFragmentInteractionListener? = null

    private lateinit var rootView: View
    private lateinit var viewModel: FriendCreateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
            mRepository = context.getRepository()
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_friend_create, container, false)
        return rootView
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_birthday.setOnClickListener {
            val activity = activity?: return@setOnClickListener
            val c = DateTimeUtils.dateToCalender(tv_birthday.text)
            val dlg = DatePickerDialog(activity,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    tv_birthday.text = DateTimeUtils.toDateString(year, month, dayOfMonth)
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
            dlg.show()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(
            this,
            viewModelFactory { FriendCreateViewModel(mRepository) }
        ).get(FriendCreateViewModel::class.java)
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

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    private fun createFriend() {
        view?.let { v ->
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }

        rootView.apply {
            val newFriend = Person(
                firstName = et_first_name.text.toString().trim(),
                midName = et_mid_name.text.toString().trim(),
                lastName = et_last_name.text.toString().trim(),
                nickName = et_nick_name.text.toString().trim(),
                birthday = DateTimeUtils.toSqlDate(tv_birthday.text),
                note = et_note.text.toString().trim()
            )
            if (newFriend.nickName.isNotBlank() || newFriend.fullName().isNotBlank()) {
                viewModel.insert(newFriend)
                mListener?.onFragmentChange(Page.MainPage)
            } else {
                Snackbar.make(
                    rootView,
                    R.string.msg_name_cannot_be_blank,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        fun newInstance() = FriendCreateFragment()
    }
}
