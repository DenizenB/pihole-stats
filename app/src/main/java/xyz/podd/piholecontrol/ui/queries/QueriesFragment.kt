package xyz.podd.piholecontrol.ui.queries

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
import xyz.podd.piholecontrol.R
import xyz.podd.piholecontrol.model.QueryData

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
        queriesView.layoutManager = LinearLayoutManager(requireContext())

        val queryAdapter = QueryAdapter()
        queriesView.adapter = queryAdapter

        viewModel.queries.observe(viewLifecycleOwner) { newQueries ->
            val previousQueries = queryAdapter.currentList

            // If the list is empty, submit the new one in full
            if (previousQueries.isEmpty()) {
                queryAdapter.submitList(newQueries.toSortedSet(QueryComparator).toList())
                return@observe
            }

            // Merge old and new queries in a sorted set
            val mergedQueries = previousQueries.toSortedSet(QueryComparator)
            mergedQueries.addAll(newQueries)

            // Submit queries (at most QueryAdapter.MAX_COUNT)
            val mostRecentQueries = mergedQueries.toList().takeLast(QueryAdapter.MAX_COUNT)
            queryAdapter.submitList(mostRecentQueries)
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