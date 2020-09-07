package xyz.podd.piholecontrol.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import xyz.podd.piholecontrol.R
import xyz.podd.piholecontrol.model.Device

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        parentFragmentManager.beginTransaction()
            .add(R.id.layout_devices, DeviceFragment.newInstance(Device("pi3", "https://192.168.1.251:8080/admin/", "<password>")))
            .add(R.id.layout_devices, DeviceFragment.newInstance(Device("pi2", "https://192.168.1.250:8080/admin/", "<password>")))
            .commit()

        return root
    }
}