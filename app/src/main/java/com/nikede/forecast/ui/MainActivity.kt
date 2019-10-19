package com.nikede.forecast.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.nikede.forecast.R
import com.nikede.forecast.data.provider.USE_DEVICE_LOCATION
import com.nikede.forecast.data.repository.ForecastRepository
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

private const val MY_PERMISSION_COARSE_LOCATION = 1

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by closestKodein()

    private val forecastRepository: ForecastRepository by instance()

    private val fusedLocationProviderClient: FusedLocationProviderClient by instance()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
        }
    }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host_fragment)

        bottom_nav.setupWithNavController(navController)

        setupActionBarWithNavController(navController)

        if (hasLocationPermission())
            bindLocationManager()
        else
            requestLocationPermission()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    private fun requestLocationPermission() {
        val editor = PreferenceManager.getDefaultSharedPreferences(this).edit()
        editor.putBoolean(USE_DEVICE_LOCATION, false)
        editor.apply()
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSION_COARSE_LOCATION
        )
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSION_COARSE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bindLocationManager()

            }
            else {
                Toast.makeText(
                    this,
                    getString(R.string.please_set_location_manually_in_settings),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun bindLocationManager() {
        val editor = PreferenceManager.getDefaultSharedPreferences(this).edit()
        editor.putBoolean(USE_DEVICE_LOCATION, true)
        editor.apply()
        LifecycleBoundLocationManager(
            this,
            fusedLocationProviderClient,
            locationCallback
        )
    }
}
