package xyz.podd.piholecontrol.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.podd.piholecontrol.R
import xyz.podd.piholecontrol.model.Client
import xyz.podd.piholecontrol.model.ClientStats

class ClientAdapter: ListAdapter<Map.Entry<Client, ClientStats>, ClientViewHolder>(DiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_client, parent, false)
        return ClientViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object DiffUtilCallback: DiffUtil.ItemCallback<Map.Entry<Client, ClientStats>>() {
        override fun areItemsTheSame(oldItem: Map.Entry<Client, ClientStats>, newItem: Map.Entry<Client, ClientStats>): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: Map.Entry<Client, ClientStats>, newItem: Map.Entry<Client, ClientStats>): Boolean {
            return oldItem.value == newItem.value
        }
    }
}

class ClientViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val textClient: TextView = view.findViewById(R.id.text_name)
    private val textQueries: TextView = view.findViewById(R.id.text_queries)
    private val textBlocked: TextView = view.findViewById(R.id.text_blocked)

    fun bind(item: Map.Entry<Client, ClientStats>) {
        textClient.text = item.key.toString()
        textQueries.text = "%,d".format(item.value.queries)
        textBlocked.text = "%,d".format(item.value.blocked)
    }
}