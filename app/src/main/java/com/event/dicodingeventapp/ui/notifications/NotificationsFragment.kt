package com.event.dicodingeventapp.ui.notifications

//import com.google.android.material.search.SearchView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.event.dicodingeventapp.EventAdapter
import com.event.dicodingeventapp.ViewModelFactory
import com.event.dicodingeventapp.data.Result
import com.event.dicodingeventapp.databinding.FragmentNotificationsBinding
import com.event.dicodingeventapp.ui.settings.SettingPreferences
import com.event.dicodingeventapp.ui.settings.dataStore


class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventAdapter: EventAdapter
    private lateinit var preferences: SettingPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = SettingPreferences.getInstance(requireContext().applicationContext.dataStore)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), preferences)
        val eventViewModel: NotificationsViewModel by viewModels { factory }

        eventAdapter = EventAdapter{ event ->
            if (event.isBookmarked) {
                eventViewModel.deleteBookmark(event)
            } else {
                eventViewModel.saveBookmark(event)
            }
        }

        binding.rvFinished.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinished.adapter = eventAdapter

        eventViewModel.getFinishedEvents().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        eventAdapter.submitList(result.data.filter {it.isFinished})
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                filterEvents(newText.orEmpty())
                return true
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })
    }

    private fun filterEvents(query: String) {
        val filteredList = if (query.isEmpty()) {
            eventAdapter.currentList
        } else {
            eventAdapter.currentList.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
        eventAdapter.submitList(filteredList)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}