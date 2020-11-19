package xyz.podd.piholestats.ui.queries

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.podd.piholestats.R
import xyz.podd.piholestats.model.network.QueryData
import java.util.*

class QueriesFragment : Fragment() {

    private lateinit var viewModel: QueriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, QueriesViewModelFactory(requireContext()))
            .get(QueriesViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_queries, container, false)

        val queriesView: RecyclerView = root.findViewById(R.id.recycler_queries)

        val queriesLayoutManager = LinearLayoutManager(requireContext())
        queriesView.layoutManager = queriesLayoutManager

        val queryAdapter = QueryAdapter()
        queriesView.adapter = queryAdapter

        viewModel.queries.observe(viewLifecycleOwner) { newQueries ->
            val shouldScrollToBottom =
                queriesLayoutManager.findLastCompletelyVisibleItemPosition() == queryAdapter.itemCount - 1

            // Merge old and new queries in a sorted set
            val queries: SortedSet<QueryData> = queryAdapter.currentList.toSortedSet(QueryComparator)
            queries.addAll(newQueries)

            // Submit queries (at most QueryAdapter.MAX_COUNT)
            val mostRecentQueries = queries.toList().takeLast(QueryAdapter.MAX_COUNT)
            queryAdapter.submitList(mostRecentQueries)

            if (shouldScrollToBottom) {
                queriesView.post { queriesView.smoothScrollToPosition(queryAdapter.itemCount - 1) }
            }
        }

        return root
    }

    override fun onResume() {
        super.onResume()

        viewModel.subscribe()
    }
}

@Suppress("UNCHECKED_CAST")
class QueriesViewModelFactory(private val context: Context): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = QueriesViewModel(context) as T
}

object QueryComparator: Comparator<QueryData> {
    override fun compare(lhs: QueryData, rhs: QueryData) = when {
        lhs.time != rhs.time -> lhs.time.compareTo(rhs.time)
        else -> lhs.domain.compareTo(rhs.domain)
    }
}