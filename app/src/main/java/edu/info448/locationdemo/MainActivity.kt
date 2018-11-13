package edu.info448.locationdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val TAG = "LOCATION"

    lateinit private var textLat: TextView
    lateinit private var textLng: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textLat = findViewById<TextView>(R.id.txt_lat)
        textLng = findViewById<TextView>(R.id.txt_lng)

    }

}
