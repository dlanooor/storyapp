package com.example.storyapp.ui.activity

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.data.local.UserSession
import com.example.storyapp.databinding.ActivityMapsBinding
import com.example.storyapp.ui.viewmodel.MapsViewModel
import com.example.storyapp.ui.viewmodel.factory.ViewModelFactory

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserSession.getInstance(dataStore)

        mapsViewModel =
            ViewModelProvider(this, ViewModelFactory(pref, this))[MapsViewModel::class.java]

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mapsViewModel.userToken.observe(
            this
        ) { token: String ->
            if (token.isNotEmpty()) {
                mapsViewModel.getLocation(token)
            }
        }
        
        mapsViewModel.listStory.observe(this) {
            for (story in it) {
                if (story.lat != null && story.lon != null) {
                    val position = LatLng(story.lat, story.lon)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(position)
                            .title(story.name)
                            .snippet(story.description)
                    )
                }
            }
            val firstLocation = LatLng(it[0].lat as Double, it[0].lon as Double)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 15f))
        }
        
        getMyLocation()
        setMapStyle()
    }

    private fun setMapStyle() {
        try {
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style Parsing Failed.")
            }
        } catch (exeception: Resources.NotFoundException) {
            Log.e(TAG, "Can't Find Style. Error: ", exeception)
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}