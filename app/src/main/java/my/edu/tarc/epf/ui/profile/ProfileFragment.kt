package my.edu.tarc.epf.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import my.edu.tarc.epf.R
import my.edu.tarc.epf.databinding.FragmentProfileBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream

class ProfileFragment : Fragment(), MenuProvider {
    var _binding: FragmentProfileBinding? = null
    val binding get() = _binding!!

    // Implicit Intent
    private val getPhoto = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.imageViewProfile.setImageURI(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(
            inflater,
            container,
            false
        )

        // Support Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        //Read shared pref file
        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val name =
            sharedPref.getString(getString(R.string.name), getString(R.string.nav_header_title))
        val email =
            sharedPref.getString(getString(R.string.email), getString(R.string.nav_header_subtitle))
        binding.editTextName.setText(name)
        binding.editTextEmailAddress.setText(email)

        val imageProfile = readProfilePicture()
        if (imageProfile != null) {
            binding.imageViewProfile.setImageBitmap(imageProfile)
        } else {
            binding.imageViewProfile.setImageResource(R.drawable.baseline_account_box_24)
        }

        return binding.root
    }

    private fun saveProfilePicture(view: View) {
        val filename = "profile.png"
        val file = File(this.context?.filesDir, filename)
        val image = view as ImageView

        val bd = image.drawable as BitmapDrawable
        val bitmap = bd.bitmap
        val outputStream: OutputStream

        try {
            outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun readProfilePicture(): Bitmap? {
        val filename = "profile.png"
        val file = File(this.context?.filesDir, filename)

        if (file.isFile) {
            try {
                return BitmapFactory.decodeFile(file.absolutePath)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
        return null
    }

    private lateinit var sharedPref: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewProfile.setOnClickListener {
            getPhoto.launch("image/*")
        }
        // TODO: Input validation here
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.findItem(R.id.action_settings).isVisible = false
        menu.findItem(R.id.action_about).isVisible = false
        menu.findItem(R.id.action_save).isVisible = true
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.action_save) {
            saveProfilePicture(binding.imageViewProfile)

            // Save Nam and Email using the SharedPref
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmailAddress.text.toString()
            with(sharedPref.edit()) {
                putString(getString(R.string.name), name)
                putString(getString(R.string.email), email)
                apply()
            }
        } else if (menuItem.itemId == android.R.id.home) {
            findNavController().navigateUp()
        }

        return true
    }
}