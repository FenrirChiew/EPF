package my.edu.tarc.epf.ui.dividend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import my.edu.tarc.epf.R

/**
 * A simple [Fragment] subclass.
 * Use the [DividendFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DividendFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dividend, container, false)
    }
}