package com.example.weatherapp.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.adaptors.HistoryRecyclerAdaptor
import com.example.weatherapp.databinding.FragmentHistoryBinding
import com.example.weatherapp.utils.beGone
import com.example.weatherapp.utils.beVisible


class HistoryFragment : BaseFragment() {
    private lateinit var binding: FragmentHistoryBinding
    private var mAdaptor: HistoryRecyclerAdaptor? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            mViewModel.loadHistory()
        }
        initRecycler()
        mViewModel.history.observe(viewLifecycleOwner) {
            binding.apply {
                if (it.isEmpty()) {
                    tvEmpty.beVisible()
                    dataRecycler.beGone()
                } else {
                    dataRecycler.beVisible()
                    tvEmpty.beGone()
                    mAdaptor?.submitList(it)
                }
            }
        }
    }

    private fun initRecycler() {
        mAdaptor = HistoryRecyclerAdaptor(requireContext())
        binding.dataRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = mAdaptor
        }
        mAdaptor?.onDeleteClick = {
            mViewModel.deleteHistory(it)
            Unit
        }
    }

}