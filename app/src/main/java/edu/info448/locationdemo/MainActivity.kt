package edu.info448.locationdemo

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.google.android.gms.location.*

class MainActivity : AppCompatActivity() {

    private val TAG = "LOCATION"
    private val LAST_LOCATION_REQUEST_CODE = 1
    private val ONGOING_LOCATION_REQUEST_CODE = 2

    lateinit private var textLat: TextView
    lateinit private var textLng: TextView

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textLat = findViewById<TextView>(R.id.txt_lat)
        textLng = findViewById<TextView>(R.id.txt_lng)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation() //first time!
    }

    override fun onStart() {
        super.onStart()
        startLocationUpdates()

    }

    override fun onStop() {
        super.onStop()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun getLastLocation() {

        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

            //access last location, asynchronously!
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                Log.v(TAG, "$location")
                displayLocation(location)
            }

        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LAST_LOCATION_REQUEST_CODE)
        }
    }

    fun startLocationUpdates() {

        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

            val locationRequest = LocationRequest().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }


            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    displayLocation(locationResult.locations[0])
                }
            }

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ONGOING_LOCATION_REQUEST_CODE)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LAST_LOCATION_REQUEST_CODE -> { //if asked for last location
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation() //do whatever we'd do when first connecting (try again)
                }
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
            ONGOING_LOCATION_REQUEST_CODE -> { //if asked for last location
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates() //do whatever we'd do when first connecting (try again)
                }
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun displayLocation(location: Location?) {
        Log.v(TAG, "Received location: $location")
        if (location != null) {
            textLat.text = "" + location.latitude
            textLng.text = "" + location.longitude
        }
    }
}
