package com.example.weatherui.views

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.weatherui.databinding.ActivityMainBinding
import com.example.weatherui.util.MainApplication
import com.example.weatherui.viewmodel.MainViewModel
import com.example.weatherui.viewmodel.MainViewModelFactory
import androidx.lifecycle.Observer
import com.example.weatherui.R
import com.example.weatherui.db.DBHelper
import com.example.weatherui.model.*
import com.example.weatherui.util.AuthState
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(),LocationListener {

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    lateinit var binding: ActivityMainBinding
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.txtCity.visibility = View.GONE
        binding.txtTemp.visibility = View.GONE
        binding.cardMin.visibility = View.GONE
        binding.cardMax.visibility = View.GONE

        checkPermissionLocation(Manifest.permission.ACCESS_FINE_LOCATION,"location",locationPermissionCode)

        binding.progressBar.visibility = View.VISIBLE
        binding.edtSearch.setOnEditorActionListener { textView, i, keyEvent ->
            if(i == EditorInfo.IME_ACTION_SEARCH){
                val intent = Intent(this,DetailActivity::class.java)
                intent.putExtra("city",binding.edtSearch.text.toString())
                startActivity(intent)
            }
            true
        }

    }

    private fun checkPermissionLocation(permission:String, name:String, requestCode:Int){
        when{
            ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED -> {
                binding.progressBar.visibility = View.VISIBLE
                getLocation()
            }shouldShowRequestPermissionRationale(permission) ->{
                showDialog(permission,name,requestCode)
            }
            else ->ActivityCompat.requestPermissions(this, arrayOf(permission),requestCode)
        }
    }

    private fun showDialog(permission:String,name:String,requestCode:Int) {
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setMessage("Permission needed to check current location")
            setTitle("Permission for location")
            setPositiveButton("OK"){
                    dialog,which -> ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission),requestCode)
            }
        }
        val dialog = builder.create()
        dialog.show()
    }
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5f,this)
    }

    override fun onLocationChanged(location: Location) {

        val geo:Geocoder = Geocoder(this, Locale.getDefault())
        val address:List<Address> = geo.getFromLocation(location.latitude,location.longitude,1)
        val city:String = address[0].locality
        Log.d("myLocation", city)

        val weather:List<Weather> = listOf(Weather("null","null",0,"null"))
        val db = DBHelper.getDatabase(this@MainActivity)

        val repo = (application as MainApplication).repo
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repo))[MainViewModel::class.java]
        mainViewModel.getWeather(city)
        mainViewModel.weatherResult.observe(this, Observer {
            when(it){
                is AuthState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is AuthState.Success -> {

                    binding.progressBar.visibility = View.GONE
                    binding.txtCity.visibility = View.VISIBLE
                    binding.txtTemp.visibility = View.VISIBLE
                    binding.cardMin.visibility = View.VISIBLE
                    binding.cardMax.visibility = View.VISIBLE

                    it.data?.let {
                        Log.d("list_weather", it.main.toString())

                        val temp: Double = it.main.temp
                        val temp_min: Double = it.main.temp_min
                        val temp_max: Double = it.main.temp_max

                        binding.txtCity.text = city
                        binding.txtTemp.text = temp.toString() + "\u2103"
                        binding.txtMaxTemp.text = temp_max.toString() + "\u2103"
                        binding.txtMinTemp.text = temp_min.toString() + "\u2103"

                        val c: Calendar = Calendar.getInstance()
                        val timeOfDay: Int = c.get(Calendar.HOUR_OF_DAY)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            binding.txtDay.text = LocalDate.now().dayOfWeek.name.toLowerCase().trim()
                        }
                        if (timeOfDay in 19..23) {
                            val window = window
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                            window.statusBarColor = getColor(R.color.blacklight)

                            binding.constraint.setBackgroundColor(getColor(R.color.blacklight))
                            binding.linlayMin.setBackgroundColor(getColor(R.color.black))
                            binding.linlayMax.setBackgroundColor(getColor(R.color.black))
                            binding.img.setImageResource(R.drawable.moon)
                        }else{
                            when (temp) {
                                in 35.00..50.00 -> {
                                    val window = window
                                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                                    window.statusBarColor = getColor(R.color.yellow_600)

                                    binding.constraint.background =
                                        ContextCompat.getDrawable(this, R.drawable.gradient_yellow)
                                    binding.linlayMin.setBackgroundColor(getColor(R.color.yellow_600))
                                    binding.linlayMax.setBackgroundColor(getColor(R.color.yellow_600))
                                    binding.img.setImageResource(R.drawable.sun)
                                }
                                in 5.00..34.00 -> {
                                    val window = window
                                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                                    window.statusBarColor = getColor(R.color.blue_100)

                                    binding.constraint.background =
                                        ContextCompat.getDrawable(this, R.drawable.gradient_blue)
                                    binding.linlayMin.setBackgroundColor(getColor(R.color.blue_100))
                                    binding.linlayMax.setBackgroundColor(getColor(R.color.blue_100))
                                    binding.img.setImageResource(R.drawable.cloud)
                                }
                                in 0.00..5.00 -> {
                                    val window = window
                                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                                    window.statusBarColor = getColor(R.color.blue_200)

                                    binding.constraint.background =
                                        ContextCompat.getDrawable(this, R.drawable.gradient_blue)
                                    binding.linlayMin.setBackgroundColor(getColor(R.color.blue_100))
                                    binding.linlayMax.setBackgroundColor(getColor(R.color.blue_100))
                                    binding.img.setImageResource(R.drawable.cloud)
                                }
                            }
                    }
                        db.dao().insert(WeatherAPI(0,city,
                            Clouds(0),
                            Coord( location.latitude,location.longitude),
                            Main(0.0,0,0,0,0,temp,temp_max,temp_min),
                            city,
                            Sys("",0,0),
                            0,0,
                            weather,Wind(0,0.0,0.0))
                        )
                    }
                }
                is AuthState.AuthError -> {
                    Toast.makeText(this@MainActivity,"Location not found",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == locationPermissionCode){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLocation()
                Toast.makeText(this@MainActivity,"Permission Granted",Toast.LENGTH_SHORT).show()
            }else{
                moveTaskToBack(true)
                exitProcess(-1)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
        exitProcess(-1)
    }
}