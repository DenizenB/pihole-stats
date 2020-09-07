package xyz.podd.piholecontrol.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import xyz.podd.piholecontrol.R

class AddDeviceFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_add_device, container, false)

        val txtName: TextView = root.findViewById(R.id.text_name)
        val txtUrl: TextView = root.findViewById(R.id.text_url)
        val txtPassword: TextView = root.findViewById(R.id.text_password)

        txtName.onFocusChangeListener = HintOnFocus("Pi-hole 1")
        txtUrl.onFocusChangeListener = HintOnFocus("http://192.168.1.50/admin/")

        return root
    }
}

class HintOnFocus(private val _hint: String): View.OnFocusChangeListener {
    override fun onFocusChange(view: View?, focused: Boolean) {
        val hint = when (focused) {
            true -> _hint
            else -> ""
        }

        if (view is TextView) {
            view.post {
                view.hint = hint
            }
        }
    }
}