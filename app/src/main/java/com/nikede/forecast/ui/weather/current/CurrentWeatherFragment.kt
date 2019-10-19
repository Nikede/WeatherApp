package com.nikede.forecast.ui.weather.current

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import com.nikede.forecast.R
import com.nikede.forecast.data.network.ConnectivityInterceptorImpl
import com.nikede.forecast.data.network.WeatherNetworkDataSourceImpl
import com.nikede.forecast.data.network.WeatherstackApiService
import com.nikede.forecast.internal.NoConnectivityException
import com.nikede.forecast.internal.glide.GlideApp
import com.nikede.forecast.ui.MainActivity
import com.nikede.forecast.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    companion object {
        fun newInstance() = CurrentWeatherFragment()
    }

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch {
        val currentWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(this@CurrentWeatherFragment, Observer { location ->
            if (location == null) return@Observer
            updateLocation(location.name)

        })

        currentWeather.observe(this@CurrentWeatherFragment, Observer {
            if (it == null) return@Observer

            group_loading.visibility = View.GONE
            updateDateToToday()
            updateTemperatures(it.temperature, it.feelsLike)
            updateCondition(it.weatherDescriptions[0])
            updatePrecipitation(it.precipitation)
            updateWind(it.windDirection, it.windSpeed)
            updateVisibility(it.visibility)

            GlideApp.with(this@CurrentWeatherFragment)
                .load(it.weatherIcons[0])
                .into(imageView_condition_icon)
        })
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToToday() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = getString(R.string.today)
    }

    @SuppressLint("SetTextI18n")
    private fun updateTemperatures(temperature: Double, feelsLike: Double) {
        textView_temperature.text = "$temperature°C"
        textView_feels_like_temperature.text = "${getString(R.string.feels_like)} $feelsLike°C"
    }

    private fun updateCondition(condition: String) {
        textView_condition.text = condition
    }

    @SuppressLint("SetTextI18n")
    private fun updatePrecipitation(precipitationVolume: Double) {
        textView_precipitation.text =
            "${getString(R.string.precipitation)}: $precipitationVolume mm"
    }

    @SuppressLint("SetTextI18n")
    private fun updateWind(windDirection: String, windSpeed: Double) {
        textView_wind.text = "${getString(R.string.wind)}: $windDirection, $windSpeed kph"
    }

    @SuppressLint("SetTextI18n")
    private fun updateVisibility(visibilityDistance: Double) {
        textView_visibility.text = "${getString(R.string.visibility)}: $visibilityDistance km"
    }

}
