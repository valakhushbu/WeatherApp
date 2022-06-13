package com.example.weatherui.views

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherui.R
import com.example.weatherui.databinding.ActivityDetailBinding
import com.example.weatherui.databinding.ActivityMainBinding
import com.example.weatherui.util.AuthState
import com.example.weatherui.util.MainApplication
import com.example.weatherui.viewmodel.MainViewModel
import com.example.weatherui.viewmodel.MainViewModelFactory
import java.time.LocalDate
import java.util.*

class DetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailBinding
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_detail)
        binding.btnback.setOnClickListener{
            val intent =  Intent(this@DetailActivity,MainActivity::class.java)
            startActivity(intent)
        }

        val city = intent.getStringExtra("city")

        val repo = (application as MainApplication).repo
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repo))[MainViewModel::class.java]
        if (city != null) {
            mainViewModel.getWeather(city)
        }
        mainViewModel.weatherResult.observe(this, Observer {
            when (it) {
                is AuthState.Loading -> {
                  binding.progressBar.visibility = View.VISIBLE
                }
                is AuthState.AuthError -> {


                }
                is AuthState.Success -> {
                    it.data?.let {
                        Log.d("list_weather", it.main.toString())
                        val temp: Double = it.main.temp
                        val temp_min: Double = it.main.temp_min
                        val temp_max: Double = it.main.temp_max
                        val real_feel: Double = it.main.feels_like
                        val pressure: Int = it.main.pressure
                        val humidity : Int = it.main.humidity

                        binding.progressBar.visibility = View.GONE
                        binding.txtCity.visibility = View.VISIBLE
                        binding.txtTemp.visibility = View.VISIBLE
                        binding.txtDay.visibility = View.VISIBLE
                        binding.lineone.visibility = View.VISIBLE
                        binding.linetwo.visibility = View.VISIBLE
                        binding.linlaytemp.visibility = View.VISIBLE
                        binding.linlay.visibility = View.VISIBLE

                        val c: Calendar = Calendar.getInstance()
                        val timeOfDay: Int = c.get(Calendar.HOUR_OF_DAY)

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            binding.txtDay.text = LocalDate.now().dayOfWeek.name.toLowerCase().trim()
                        }
                        binding.txtMaxTemp.text = temp_max.toString()
                        binding.txtMinTemp.text = temp_min.toString()
                        binding.txtCity.text = city
                        binding.txtTemp.text = temp.toString() + "\u2103"

                        binding.txtPressureGet.text = pressure.toString()
                        binding.txtHumidityGet.text = humidity.toString()
                        binding.txtFeelGet.text = real_feel.toString()

                    }
                }
            }
        })
    }
}