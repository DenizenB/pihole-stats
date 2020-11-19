package xyz.podd.piholestats.ui.queries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.podd.piholestats.R
import xyz.podd.piholestats.blendColors
import xyz.podd.piholestats.dp
import xyz.podd.piholestats.getValueAnimator
import xyz.podd.piholestats.model.network.QueryData

class QueryAdapter: ListAdapter<QueryData, QueryViewHolder>(DiffUtilCallback) {

    lateinit var recyclerView: RecyclerView
    var expandedItem: QueryData? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_query, parent, false)
        return QueryViewHolder(view)
    }

    override fun onBindViewHolder(holder: QueryViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item, expandedItem == item)
        holder.itemView.setOnClickListener {
            if (expandedItem == item) {
                // Collapse clicked item
                holder.setCardExpanded(false)

                expandedItem = null
            } else {
                // Expand clicked item
                holder.setCardExpanded(true)

                // Collapse previously expanded item
                expandedItem?.let(::collapseItem)

                expandedItem = item
            }
        }
    }

    fun collapseItem(expandedItem: QueryData) {
        val expandedIndex = currentList.indexOf(expandedItem)
        if (expandedIndex == -1)
            return

        val expandedHolder = recyclerView.findViewHolderForAdapterPosition(expandedIndex)
        if (expandedHolder is QueryViewHolder)
            expandedHolder.setCardExpanded(false)
    }

    object DiffUtilCallback: DiffUtil.ItemCallback<QueryData>() {
        override fun areItemsTheSame(oldItem: QueryData, newItem: QueryData) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: QueryData, newItem: QueryData) =
            oldItem == newItem
    }

    companion object {
        const val MAX_COUNT = 24
    }
}

class QueryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val root: LinearLayout = view.findViewById(R.id.layout_query)
    private val card: CardView = view.findViewById(R.id.card_query)
    private val layoutExpansion: View = view.findViewById(R.id.layout_expansion)

    private val imageStatus: ImageView = view.findViewById(R.id.image_status)
    private val textDomain: TextView = view.findViewById(R.id.text_domain)
    private val textTime: TextView = view.findViewById(R.id.text_time)
    private val textClient: TextView = view.findViewById(R.id.text_client)

    private val originalHeight = 48.dp
    private val expandedHeight = 122.dp
    private val originalPadding = 24.dp
    private val expandedPadding = 4.dp
    private val originalElevation = 2f.dp
    private val expandedElevation = 4f.dp
    private val originalBackground = view.resources.getColor(R.color.cardBackground, null)
    private val expandedBackground = view.resources.getColor(R.color.cardBackgroundFocused, null)

    fun bind(item: QueryData, expanded: Boolean) {
        val statusIcon = when (item.blocked) {
            true -> R.drawable.ic_blocked_24
            else -> R.drawable.ic_allowed_24
        }

        imageStatus.setImageResource(statusIcon)
        textTime.text = item.timeString
        textDomain.text = item.domain
        textClient.text = item.client

        setCardExpanded(expanded, animate = false)
    }

    fun setCardExpanded(expand: Boolean, animate: Boolean = true) {
        if (animate) {
            val animator = getValueAnimator(
                forward = expand,
                duration = 200,
                interpolator = AccelerateDecelerateInterpolator()
            ) { progress ->
                val padding = (originalPadding + (expandedPadding - originalPadding) * progress).toInt()
                val elevation = originalElevation + (expandedElevation - originalElevation) * progress
                val background = blendColors(originalBackground, expandedBackground, progress)

                root.updatePadding(left = padding, right = padding)
                card.cardElevation = elevation
                card.setCardBackgroundColor(background)
                card.layoutParams.height = (originalHeight + (expandedHeight - originalHeight) * progress).toInt()
                card.requestLayout()
            }

            if (expand) animator.doOnStart { layoutExpansion.isVisible = true }
            else animator.doOnEnd { layoutExpansion.isVisible = false }

            animator.start()
        } else {
            val padding = if (expand) expandedPadding else originalPadding
            val elevation = if (expand) expandedElevation else originalElevation
            val background = if (expand) expandedBackground else originalBackground

            root.updatePadding(left = padding, right = padding)
            card.cardElevation = elevation
            card.setCardBackgroundColor(background)

            layoutExpansion.isVisible = expand
            card.layoutParams.height = WRAP_CONTENT
            card.requestLayout()
        }
    }
}