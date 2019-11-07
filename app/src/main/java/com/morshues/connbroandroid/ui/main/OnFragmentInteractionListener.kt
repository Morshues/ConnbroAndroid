package com.morshues.connbroandroid.ui.main

import com.morshues.connbroandroid.Page
import com.morshues.connbroandroid.repo.ConnbroRepository

interface OnFragmentInteractionListener {
    fun onFragmentChange(page: Page)
    fun getRepository(): ConnbroRepository
}