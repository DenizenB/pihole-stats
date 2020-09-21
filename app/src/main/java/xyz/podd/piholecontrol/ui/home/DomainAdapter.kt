package xyz.podd.piholecontrol.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.podd.piholecontrol.R

class DomainAdapter(blockedDomains: Boolean): ListAdapter<Map.Entry<String, Int>, DomainViewHolder>(DiffUtilCallback) {
    private val textAreaIdForValue = when (blockedDomains) {
        true -> R.id.text_blocked
        else -> R.id.text_queries
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DomainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_domain, parent, false)

        return DomainViewHolder(view, textAreaIdForValue)
    }

    override fun onBindViewHolder(holder: DomainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object DiffUtilCallback: DiffUtil.ItemCallback<Map.Entry<String, Int>>() {
        override fun areItemsTheSame(oldItem: Map.Entry<String, Int>, newItem: Map.Entry<String, Int>): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: Map.Entry<String, Int>, newItem: Map.Entry<String, Int>): Boolean {
            return oldItem == newItem // TODO test
        }
    }
}

class DomainViewHolder(view: View, textAreaIdForValue: Int): RecyclerView.ViewHolder(view) {
    private val textDomain: TextView = view.findViewById(R.id.text_name)
    private val textValue: TextView = view.findViewById(textAreaIdForValue)

    init {
        textValue.visibility = View.VISIBLE
    }

    fun bind(item: Map.Entry<String, Int>) {
        textDomain.text = item.key
        textValue.text = "%,d".format(item.value)
    }
}