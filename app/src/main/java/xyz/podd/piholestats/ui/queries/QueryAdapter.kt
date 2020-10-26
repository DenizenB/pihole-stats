package xyz.podd.piholestats.ui.queries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        const val MAX_COUNT = 14
    }
}

class QueryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val textDomain: TextView = view.findViewById(R.id.text_domain)
    private val textTime: TextView = view.findViewById(R.id.text_time)

    fun bind(item: QueryData) {
        val status = when (item.blocked) {
            true -> "Blocked"
            else -> "Allowed"
        }

        textTime.text = item.timeString
        textDomain.text = "$status ${item.domain}"
    }
}