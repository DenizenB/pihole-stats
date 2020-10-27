package xyz.podd.piholestats.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import xyz.podd.piholestats.R
import xyz.podd.piholestats.Storage
import xyz.podd.piholestats.model.Device
import javax.net.ssl.SSLHandshakeException

class AddDeviceFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_add_device, container, false)

        val txtName: TextInputLayout = root.findViewById(R.id.layout_name)
        val txtUrl: TextInputLayout = root.findViewById(R.id.layout_url)
        val txtPassword: TextInputLayout = root.findViewById(R.id.layout_password)
        val swVerifySsl: Switch = root.findViewById(R.id.switch_verify_ssl)
        val btnSave: Button = root.findViewById(R.id.button_save)

        txtName.editText!!.onFocusChangeListener = HintOnFocus("Pi-hole 1")
        txtUrl.editText!!.onFocusChangeListener = HintOnFocus("http://192.168.1.50/admin/")

        btnSave.setOnClickListener {
            var error: String?

            // Verify name
            error = when(txtName.editText!!.text.isEmpty()) {
                true -> "Please name your Pi-hole"
                else -> null
            }

            txtName.error = error
            if (error != null) {
                return@setOnClickListener
            }

            // Verify url/password asynchronously
            lifecycleScope.launch(Dispatchers.IO) {
                val device = Device(
                    txtName.editText!!.text.toString(),
                    txtUrl.editText!!.text.toString(),
                    txtPassword.editText!!.text.toString(),
                    swVerifySsl.isChecked
                )

                // Verify URL
                try {
                    device.service.getStatus()
                } catch (e: SSLHandshakeException) {
                    error = "Failed to verify SSL certificate"
                } catch (e: JSONException) {
                    error = "This URL does not behave like a Pi-hole"
                } catch (e: Exception) {
                    e.printStackTrace()
                    error = e.message
                }

                txtUrl.post{ txtUrl.error = error }
                if (error != null) {
                    return@launch
                }

                // Verify password
                try {
                    device.service.getTopLists()
                }catch (e: Exception) {
                    e.printStackTrace()
                    error = "Invalid password"
                }

                txtPassword.post { txtPassword.error = error }
                if (error != null) {
                    return@launch
                }

                // Save to storage
                val storage = Storage(requireContext())
                val devices = storage.devices
                devices.add(device)
                storage.devices = devices

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Your Pi-hole has been added", Toast.LENGTH_SHORT).show()

                    // Return to Home fragment
                    findNavController().popBackStack(R.id.navigation_home, false)
                }
            }
        }

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