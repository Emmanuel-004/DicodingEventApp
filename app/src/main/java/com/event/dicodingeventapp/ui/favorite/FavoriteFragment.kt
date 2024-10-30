package com.event.dicodingeventapp.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.event.dicodingeventapp.EventAdapter
import com.event.dicodingeventapp.ViewModelFactory
import com.event.dicodingeventapp.databinding.FragmentFavoriteBinding
import com.event.dicodingeventapp.ui.settings.SettingPreferences
import com.event.dicodingeventapp.ui.settings.dataStore

class FavoriteFragment : Fragment() {


    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: SettingPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = SettingPreferences.getInstance(requireContext().applicationContext.dataStore)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), preferences)
        val viewModel: FavoriteViewModel by viewModels {
            factory
        }

        val eventAdapter = EventAdapter { event ->
            if (event.isBookmarked) {
                viewModel.deleteBookmark(event)
            } else {
                viewModel.saveBookmark(event)
            }
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorite.layoutManager = layoutManager
        binding.rvFavorite.adapter = eventAdapter

        viewModel.getBookmarkedEvents().observe(viewLifecycleOwner) { events ->
            eventAdapter.submitList(events)
        }
    }
}