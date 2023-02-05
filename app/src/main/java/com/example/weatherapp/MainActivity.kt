package com.example.weatherapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.utils.LocationPermission
import com.google.android.gms.location.*

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        LocationPermission(this, mFusedLocationClient).handleLocation()
//        HandleAPI(this, "Panjutorials", "123456").execute()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                LocationPermission(this, mFusedLocationClient).requestLocationData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}