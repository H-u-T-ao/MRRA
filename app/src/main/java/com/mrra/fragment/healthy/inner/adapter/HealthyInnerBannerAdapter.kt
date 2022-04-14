package com.mrra.fragment.healthy.inner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mrra.R

internal class HealthyInnerBannerAdapter(
    private val bannerList: List<Pair<Int, Int>>
) : RecyclerView.Adapter<HealthyInnerBannerAdapter.ViewHolder>() {

    companion object {
        private const val BANNER_TYPE = -1
        private const val NEWS_TYPE = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_healthy_banner, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            image.setImageResource(bannerList[position % bannerList.size].first)
            title.setText(bannerList[position % bannerList.size].second)
        }
    }

    override fun getItemCount() = Int.MAX_VALUE

    override fun getItemViewType(position: Int) = if (position == 0) BANNER_TYPE else NEWS_TYPE

    internal class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var image: ImageView = view.findViewById(R.id.iv_banner_image)
        internal var title: TextView = view.findViewById(R.id.tv_banner_title)
    }

}