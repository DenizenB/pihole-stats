package xyz.podd.piholestats.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.podd.piholestats.R

class DomainAdapter(private val blockedDomains: Boolean): ListAdapter<Map.Entry<String, Int>, DomainViewHolder>(DiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DomainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_domain, parent, false)

        return DomainViewHolder(view, blockedDomains)
    }

    override fun onBindViewHolder(holder: DomainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object DiffUtilCallback: DiffUtil.ItemCallback<Map.Entry<String, Int>>() {
        override fun areItemsTheSame(oldItem: Map.Entry<String, Int>, newItem: Map.Entry<String, Int>): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: Map.Entry<String, Int>, newItem: Map.Entry<String, Int>): Boolean {
            return oldItem.value == newItem.value
        }
    }
}

class DomainViewHolder(view: View, blockedDomain: Boolean): RecyclerView.ViewHolder(view) {
    private val textDomain: TextView = view.findViewById(R.id.text_name)
    private val textCount: TextView = view.findViewById(R.id.text_count)

    init {
        if (blockedDomain) {
            val color = textCount.context.getColor(android.R.color.holo_red_light)
            textCount.setTextColor(color)
        }
    }

    fun bind(item: Map.Entry<String, Int>) {
        textCount.text = "%,d".format(item.value)
        textDomain.text = item.key
    }
}