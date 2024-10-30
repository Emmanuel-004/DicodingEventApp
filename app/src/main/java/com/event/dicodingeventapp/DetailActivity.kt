package com.event.dicodingeventapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.event.dicodingeventapp.data.Result
import com.event.dicodingeventapp.data.response.Event
import com.event.dicodingeventapp.databinding.ActivityDetailBinding
import com.event.dicodingeventapp.ui.settings.SettingPreferences
import com.event.dicodingeventapp.ui.settings.dataStore

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(
            applicationContext,
            SettingPreferences.getInstance(applicationContext.dataStore)
        )
    }
    private lateinit var preferences: SettingPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getStringExtra("EVENT_ID")
        detailViewModel.getDetailEvents(eventId ?:"").observe(this){ detailResult ->
            if (detailResult != null) {
                when (detailResult) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val event = detailResult.data
                        setEventDetail(event.event)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setEventDetail(event: Event) {
        Glide.with(binding.root.context)
            .load(event.mediaCover)
            .into(binding.ivDetailImage)
        binding.tvEventName.text = event.name
        binding.tvEventOwner.text = event.ownerName
        binding.tvEventDate.text = event.beginTime

        val sisaQuota = event.quota - event.registrants

        binding.tvEventQuota.text = sisaQuota.toString()
        binding.tvEventDescription.text = HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_COMPACT)
        binding.btnRegister.setOnClickListener {
            val eventResponse = detailViewModel.getDetailEvents(event.id.toString()).value
            eventResponse?.let {
                register(event.link)
            }
        }
    }

    private fun register(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
