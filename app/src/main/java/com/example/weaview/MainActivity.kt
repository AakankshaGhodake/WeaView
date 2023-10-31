package com.example.weaview

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import com.example.weaview.databinding.ActivityMainBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var openButton: Button
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root) // Use binding.root to set the content view

        fetchWeatherData("Mumbai")
        SearchCity()

        openButton = binding.buttonend // Use ViewBinding to access the button

        // Open new activity on button click
        openButton.setOnClickListener {
            val intent = Intent(this, LocationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun SearchCity() {
        val searchview = binding.searchView

        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        val searchPlateId = searchview.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val searchPlate = searchview.findViewById<EditText>(searchPlateId)
        searchPlate.setHintTextColor(resources.getColor(R.color.your_hint_color))
    }

    private fun fetchWeatherData(cityName: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build()
            .create(ApiInterface::class.java)

        val response = retrofit.getWeatherData(
            "$cityName",
            "5028f352c531bb47d86e8ea54a52217d",
            "metric"
        )

        response.enqueue(object : Callback<WeaView> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<WeaView>, response: Response<WeaView>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val temperature = responseBody.main.temp
                    val humidity = responseBody.main.humidity
                    val windSpeed = responseBody.wind.speed
                    val sunRise = responseBody.sys.sunrise.toLong()
                    val sunset = responseBody.sys.sunset.toLong()
                    val seaLevel = responseBody.main.pressure
                    val condition = responseBody.weather.firstOrNull()?.main ?: "unknown"
                    val maxTemp = responseBody.main.temp_max
                    val minTemp = responseBody.main.temp_min

                    runOnUiThread {
                        binding.temp.text = "$temperature °C"
                        binding.humidity.text = "$humidity %"
                        binding.wind.text = "$windSpeed m/s"
                        binding.sunrise.text = "${time(sunRise)} "
                        binding.sunset.text = "${time(sunset)} "
                        binding.sea.text = "$seaLevel hPa"
                        binding.weather.text = condition
                        binding.max.text = "Max Temp: $maxTemp °C"
                        binding.min.text = "Min Temp: $minTemp °C"
                        binding.condition.text = condition
                        binding.day.text = dayName(System.currentTimeMillis())
                        binding.date.text = date()
                        binding.cityname.text = "$cityName"

                        changingImage(condition)
                    }

//                    Log.d("TAG", "onResponse: $temperature")
//                    Log.d("TAG", "onResponse: $windSpeed")
                }
            }

            override fun onFailure(call: Call<WeaView>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun changingImage(conditions: String) {
        when (conditions) {
            "Clear Sky", "Sunny", "Clear", "Smoke" -> {
                binding.root.setBackgroundResource(R.drawable.sunnybg)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
            "Haze", "Partly Clouds", "Clouds", "Overcast", "Mist", "Foggy" -> {
                binding.root.setBackgroundResource(R.drawable.cloudy)
                binding.lottieAnimationView.setAnimation(R.raw.clouds)
            }
            "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain","Rain" -> {
                binding.root.setBackgroundResource(R.drawable.rainy)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }
            "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzard" -> {
                binding.root.setBackgroundResource(R.drawable.snowy)
                binding.lottieAnimationView.setAnimation(R.raw.snowy)
            }
            else -> {
                binding.root.setBackgroundResource(R.drawable.sunnybg)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimationView.playAnimation()
    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun time(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }

    fun dayName(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())
    }
}
