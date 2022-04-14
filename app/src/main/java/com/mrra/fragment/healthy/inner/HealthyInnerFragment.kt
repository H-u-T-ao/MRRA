package com.mrra.fragment.healthy.inner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mrra.R
import com.mrra.base.BaseFragment
import com.mrra.fragment.healthy.inner.adapter.HealthyInnerListAdapter

class HealthyInnerFragment : BaseFragment() {

    private lateinit var list: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_healthy_inner, container, false).apply {
            list = findViewById(R.id.rv_healthy_inner_list)

            list.setHasFixedSize(true)
            val linearLayoutManager = LinearLayoutManager(requireContext())
            linearLayoutManager.orientation = RecyclerView.VERTICAL
            list.layoutManager = linearLayoutManager
            list.adapter = HealthyInnerListAdapter(requireContext())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HealthyInnerFragment()
    }
}