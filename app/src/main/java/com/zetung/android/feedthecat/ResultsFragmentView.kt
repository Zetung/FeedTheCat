package com.zetung.android.feedthecat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.zetung.android.feedthecat.databinding.FragmentResultsViewBinding


class ResultsFragmentView : Fragment() {

    private var arrOfResults: ArrayList<String> = arrayListOf()
    private lateinit var adapter: ArrayAdapter<String>
    private var _binding: FragmentResultsViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultsViewBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        arrOfResults = arguments!!.getStringArrayList("resultsSession")!!
        val con = context!!
        adapter = ArrayAdapter(con, android.R.layout.simple_list_item_1, arrOfResults)
        binding.listResults.adapter = adapter
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}