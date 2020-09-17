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
import androidx.lifecycle.observe

import xyz.podd.piholecontrol.R
import xyz.podd.piholecontrol.model.Device

private const val ARG_DEVICE = "device"

class DeviceFragment : Fragment() {
    private lateinit var device: Device

    companion object {
        fun newInstance(device: Device): DeviceFragment {
            val fragment = DeviceFragment()

            val args = Bundle()
            args.putParcelable(ARG_DEVICE, device)
            fragment.arguments = args

            return fragment
        }
    }

    private lateinit var viewModel: DeviceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = requireArguments()
        device = args.getParcelable(ARG_DEVICE)!!

        viewModel = ViewModelProvider(this, DeviceViewModelFactory(device)).get(DeviceViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_device, container, false)
        val lblName: TextView = root.findViewById(R.id.text_name)
        val imgStatus: ImageView = root.findViewById(R.id.image_status)
        val lblToday: TextView = root.findViewById(R.id.text_today)
        val lblQueries: TextView = root.findViewById(R.id.text_queries)
        val lblBlocked: TextView = root.findViewById(R.id.text_blocked)

        lblName.text = device.name
        lblQueries.text = ""
        lblBlocked.text = ""
        lblToday.visibility = View.INVISIBLE

        viewModel.summary.observe(viewLifecycleOwner) {
            imgStatus.setImageDrawable(resources.getDrawable(it.drawable))
            lblQueries.text = it.queries
            lblBlocked.text = it.blocked
            lblToday.visibility = View.VISIBLE
        }

        return root
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchStatus()
    }
}

@Suppress("UNCHECKED_CAST")
class DeviceViewModelFactory(private val device: Device): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = DeviceViewModel(device) as T
}