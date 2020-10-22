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
import xyz.podd.piholecontrol.ui.home.NoScrollLinearLayoutManager

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

        viewModel.queries.observe(viewLifecycleOwner) {
            queryAdapter.submitList(it)
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