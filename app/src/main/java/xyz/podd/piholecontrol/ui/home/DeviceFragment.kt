package xyz.podd.piholecontrol.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import xyz.podd.piholecontrol.R
import java.lang.IllegalStateException

class DeviceFragment : Fragment() {
    private lateinit var name: String
    private lateinit var url: String
    private lateinit var authToken: String

    companion object {
        const val ARG_NAME = "name"
        const val ARG_URL = "url"
        const val ARG_AUTH_TOKEN = "authToken"

        fun newInstance(name: String, url: String, authToken: String): DeviceFragment {
            val fragment = DeviceFragment()

            val args = Bundle()
            args.putString(ARG_NAME, name)
            args.putString(ARG_URL, url)
            args.putString(ARG_AUTH_TOKEN, authToken)
            fragment.arguments = args

            return fragment
        }
    }

    private lateinit var viewModel: DeviceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = arguments ?: throw IllegalStateException("No arguments provided to DeviceFragment")
        name = args.getString(ARG_NAME) ?: throw IllegalStateException("Missing name in DeviceFragment arguments")
        url = args.getString(ARG_URL) ?: throw IllegalStateException("Missing url in DeviceFragment arguments")
        authToken = args.getString(ARG_AUTH_TOKEN) ?: throw IllegalStateException("Missing authToken in DeviceFragment arguments")

        viewModel = ViewModelProvider(this, DeviceViewModelFactory(url, authToken)).get(DeviceViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_device, container, false)
        val lblName: TextView = root.findViewById(R.id.text_name)
        val imgStatus: ImageView = root.findViewById(R.id.image_status)
        val lblToday: TextView = root.findViewById(R.id.text_today)
        val lblQueries: TextView = root.findViewById(R.id.text_queries)
        val lblBlocked: TextView = root.findViewById(R.id.text_blocked)

        lblName.text = name
        lblQueries.text = ""
        lblBlocked.text = ""
        lblToday.visibility = View.INVISIBLE

        viewModel.summary.observe(viewLifecycleOwner, {
            imgStatus.setImageDrawable(resources.getDrawable(it.drawable))
            lblQueries.text = it.queries
            lblBlocked.text = it.blocked
            lblToday.visibility = View.VISIBLE
        })

        return root
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchStatus()
    }
}

class DeviceViewModelFactory(private val url: String, private val authToken: String): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = DeviceViewModel(url, authToken) as T
}