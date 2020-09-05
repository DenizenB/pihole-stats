package xyz.podd.piholecontrol.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import xyz.podd.piholecontrol.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val lblStatus: TextView = root.findViewById(R.id.text_status)
        val imgStatus: ImageView = root.findViewById(R.id.image_status)

        homeViewModel.status.observe(viewLifecycleOwner, Observer {
            imgStatus.setImageDrawable(resources.getDrawable(it.drawable))
            lblStatus.text = it.message
        })

        homeViewModel.toast.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        return root
    }

    override fun onResume() {
        super.onResume()

        homeViewModel.fetchStatus()
    }
}