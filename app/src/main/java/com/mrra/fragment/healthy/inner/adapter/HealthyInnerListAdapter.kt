package com.mrra.fragment.healthy.inner.adapter

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.mrra.R
import com.mrra.activity.web.WebActivity
import com.mrra.entity.News
import com.mrra.utils.startActivity
import com.mrra.weight.ViewPager2TouchListener
import kotlinx.coroutines.*

internal class HealthyInnerListAdapter(
    private val mContext: Context
) : RecyclerView.Adapter<HealthyInnerListAdapter.ViewHolder>(),
    CoroutineScope by MainScope() {

    private var job: Job? = null
    private var touching = false
    private var touchingTime = 0L

    companion object {
        private const val BANNER_TYPE = -1
        private const val INFO_TYPE = 0
        private val bannerList = mutableListOf(
            Pair(R.drawable.ic_banner_0, R.string.healthy_banner_0),
            Pair(R.drawable.ic_banner_1, R.string.healthy_banner_1),
            Pair(R.drawable.ic_banner_2, R.string.healthy_banner_2),
            Pair(R.drawable.ic_banner_3, R.string.healthy_banner_3)
        )
        private val newsList = mutableListOf(
            News(
                R.drawable.ic_news_0,
                R.string.healthy_news_title_0,
                R.string.healthy_news_url_0,
                R.string.healthy_news_update_0,
                R.string.healthy_news_from_0
            ),
            News(
                R.drawable.ic_news_1,
                R.string.healthy_news_title_1,
                R.string.healthy_news_url_1,
                R.string.healthy_news_update_1,
                R.string.healthy_news_from_1
            ),
            News(
                R.drawable.ic_news_2,
                R.string.healthy_news_title_2,
                R.string.healthy_news_url_2,
                R.string.healthy_news_update_2,
                R.string.healthy_news_from_2
            ),
            News(
                R.drawable.ic_news_3,
                R.string.healthy_news_title_3,
                R.string.healthy_news_url_3,
                R.string.healthy_news_update_3,
                R.string.healthy_news_from_3
            ),
            News(
                R.drawable.ic_news_4,
                R.string.healthy_news_title_4,
                R.string.healthy_news_url_4,
                R.string.healthy_news_update_4,
                R.string.healthy_news_from_4
            )
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_healthy_inner_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            if (position == 0) {
                vp.run {
                    isEnabled = true
                    setOnClickListener { }
                    adapter = HealthyInnerBannerAdapter(bannerList)
                    offscreenPageLimit = 3
                    currentItem = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 % bannerList.size)

                    vl.setTouchListener { event ->
                        if (event.actionMasked == MotionEvent.ACTION_MOVE) {
                            touching = true
                            touchingTime = SystemClock.uptimeMillis() + 3000L
                            launch(Dispatchers.Main) {
                                delay(3000L)
                                touching = SystemClock.uptimeMillis() < touchingTime
                            }
                        }
                    }

                }
                showBanner(this, true)
            } else {
                showBanner(this, false)
                newsList[position - 1].let {
                    image.setImageResource(it.image)
                    title.setText(it.title)
                    updateDate.setText(it.updateDate)
                    author.setText(it.from)
                    itemView.setOnClickListener { _ ->
                        mContext.startActivity<WebActivity> {
                            putExtra("url", mContext.resources.getString(it.url))
                        }
                    }
                }

            }
        }
    }

    override fun getItemCount() = newsList.size + 1

    override fun getItemViewType(position: Int) = if (position == 0) BANNER_TYPE else INFO_TYPE

    private fun showBanner(holder: ViewHolder, show: Boolean) {
        holder.run {
            if (show) {
                vl.visibility = View.VISIBLE
                cv.visibility = View.GONE
                title.visibility = View.GONE
                updateDate.visibility = View.GONE
                author.visibility = View.GONE
            } else {
                vl.visibility = View.GONE
                cv.visibility = View.VISIBLE
                title.visibility = View.VISIBLE
                updateDate.visibility = View.VISIBLE
                author.visibility = View.VISIBLE
            }
        }
    }

    internal class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var vl: ViewPager2TouchListener = view.findViewById(R.id.vl_info_touch)
        internal var vp: ViewPager2 = view.findViewById(R.id.vp_info_banner)
        internal var cv: CardView = view.findViewById(R.id.cv_info_image)
        internal var image: ImageView = view.findViewById(R.id.iv_info_image)
        internal var title: TextView = view.findViewById(R.id.tv_info_title)
        internal var updateDate: TextView = view.findViewById(R.id.tv_info_update_date)
        internal var author: TextView = view.findViewById(R.id.tv_info_author)
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        if (holder.adapterPosition == 0) {
            job?.cancel()
            job = launch(Dispatchers.Main) {
                while (isActive) {
                    delay(3000L)
                    if (!touching) {
                        holder.vp.currentItem = holder.vp.currentItem + 1
                    }
                }
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        if (holder.adapterPosition == 0) {
            job?.cancel()
        }
    }

}