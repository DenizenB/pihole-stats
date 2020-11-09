package xyz.podd.piholestats.ui.queries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.podd.piholestats.R
import xyz.podd.piholestats.model.network.QueryData

class QueryAdapter: ListAdapter<QueryData, QueryViewHolder>(DiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_query, parent, false)
        return QueryViewHolder(view)
    }

    override fun onBindViewHolder(holder: QueryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object DiffUtilCallback: DiffUtil.ItemCallback<QueryData>() {
        override fun areItemsTheSame(oldItem: QueryData, newItem: QueryData) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: QueryData, newItem: QueryData) =
            oldItem == newItem
    }

    companion object {
        const val MAX_COUNT = 12
    }
}

class QueryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private val imageStatus: ImageView = view.findViewById(R.id.image_status)
    private val textDomain: TextView = view.findViewById(R.id.text_domain)
    private val textTime: TextView = view.findViewById(R.id.text_time)

    fun bind(item: QueryData) {
        val statusIcon = when (item.blocked) {
            true -> R.drawable.ic_blocked_24
            else -> R.drawable.ic_allowed_24
        }

        imageStatus.setImageResource(statusIcon)
        textTime.text = item.timeString
        textDomain.text = item.domain
    }
}