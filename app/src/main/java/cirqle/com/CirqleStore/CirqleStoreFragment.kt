package cirqle.com.CirqleStore

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cirqle.com.R
import cirqle.com.WebViewActivity


class CirqleStoreFragment : Fragment() {
    private lateinit var  gform_link:TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view:View= inflater.inflate(R.layout.fragment_cirqle_store, container, false)
        gform_link=view.findViewById(R.id.gform_link)
        gform_link.setOnClickListener{
            val googleFormUrl = "https://forms.gle/JTEDZhwz8osVbFFv5"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(googleFormUrl))
            startActivity(intent)
        }
        return view;
    }

}