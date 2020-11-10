package xyz.podd.piholestats.ui.queries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.podd.piholestats.R
import xyz.podd.piholestats.dpToPx
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

class QueryViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val root: LinearLayout = view.findViewById(R.id.layout_query)
    private val card: CardView = view.findViewById(R.id.card_query)

    private val imageStatus: ImageView = view.findViewById(R.id.image_status)
    private val textDomain: TextView = view.findViewById(R.id.text_domain)

    private var isExpanded = false
    private val layoutExpansion: View = view.findViewById(R.id.layout_expansion)
    private val textTime: TextView = view.findViewById(R.id.text_time)

    init {
        view.setOnClickListener { setCardExpanded(!isExpanded) }
    }

    fun bind(item: QueryData) {
        val statusIcon = when (item.blocked) {
            true -> R.drawable.ic_blocked_24
            else -> R.drawable.ic_allowed_24
        }

        imageStatus.setImageResource(statusIcon)
        textTime.text = item.timeString
        textDomain.text = item.domain

        setCardExpanded(false)
    }

    private fun setCardExpanded(expand: Boolean) {
        if (expand) {
            root.updatePadding(left = 0, right = 0)
            card.cardElevation = 4.dpToPx(view.context)
            layoutExpansion.visibility = View.VISIBLE
        } else {
            val paddingHorizontal = 12.dpToPx(view.context).toInt()
            root.updatePadding(left = paddingHorizontal, right = paddingHorizontal)
            card.cardElevation = 2.dpToPx(view.context)
            layoutExpansion.visibility = View.GONE
        }

        this.isExpanded = expand
    }
}