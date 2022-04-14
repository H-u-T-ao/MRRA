package com.mrra.fragment.mrra

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mrra.R
import com.mrra.base.BaseFragment

class ContactFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact, container, false).apply {
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ContactFragment()
    }
}