package com.example.trainlocator.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.trainlocator.R
import com.example.trainlocator.models.Station
import com.example.trainlocator.utils.DateTimeUtils
import com.example.trainlocator.utils.VectorDrawableUtils
import com.github.vipulasri.timelineview.TimelineView
import kotlinx.android.synthetic.main.item_layout.view.*

class TimelineAdapter (private val mFeedList: List<Station>, private val context: Context) : RecyclerView.Adapter<TimelineAdapter.TimeLineViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        val  layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        view = layoutInflater.inflate(R.layout.item_layout, parent, false)
        return TimeLineViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {

        val station = mFeedList[position]

        when {
            station.status == "inactive" -> {
                holder.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.inactive_marker, ContextCompat.getColor(context, R.color.colorGrey500))
            }
            station.status == "active" -> {
                holder.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.active_marker,  ContextCompat.getColor(context, R.color.colorGrey500))
            }
            else -> {
                holder.timeline.setMarker(ContextCompat.getDrawable(holder.itemView.context, R.drawable.complete_marker), ContextCompat.getColor(context, R.color.colorGreen))
            }
        }

        if (station.date.isNotEmpty()) {
            holder.date.visibility = View.VISIBLE
            holder.date.text = DateTimeUtils.parseDateTime(station.date, "yyyy-MM-dd HH:mm", "hh:mm a, dd-MMM-yyyy")
        } else
            holder.date.visibility = View.GONE

        holder.name.text = station.name
    }

    override fun getItemCount() = mFeedList.size

    inner class TimeLineViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {

        val date = itemView.text_timeline_date
        val name = itemView.text_timeline_title
        val timeline = itemView.timeline

        init {
            timeline.initLine(viewType)
        }
    }

}