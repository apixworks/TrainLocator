package com.example.trainlocator

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trainlocator.models.RouteResponse
import com.example.trainlocator.utils.ServerApi
import com.google.gson.Gson
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.android.material.snackbar.Snackbar
import com.jaredrummler.materialspinner.MaterialSpinner
import android.app.DatePickerDialog
import android.view.Menu
import android.view.MenuItem
import com.example.trainlocator.models.BookingResponse
import com.example.trainlocator.models.Station
import com.example.trainlocator.utils.SharedPreference
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.text.SimpleDateFormat


class MainActivity : AppCompatActivity() {

    lateinit var dialog: AlertDialog
    private lateinit var serverURL: String
    lateinit var myCalendar: Calendar
    var mDate: String = ""
    lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = null

        sharedPreference = SharedPreference(this@MainActivity)

        serverURL = getString(R.string.serverURL)

        dialog = SpotsDialog.Builder()
            .setContext(this@MainActivity)
            .setCancelable(false)
            .setTheme(R.style.CustomDialog)
            .build()
            .apply {
                show()
            }

        myCalendar = Calendar.getInstance()

        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateLabel()
        }

        dateChooser.setOnClickListener {
            val datePicker = DatePickerDialog(
                this@MainActivity, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.datePicker.minDate = Calendar.getInstance().timeInMillis
            datePicker.show()
        }

        var fromString = ""
        var toString = ""
        var classString = ""

        val gson = Gson()

        val retrofit = Retrofit.Builder()
            .baseUrl(serverURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ServerApi::class.java)

        val call = api.getRoutes()

        call.enqueue(object : Callback<RouteResponse> {

            override fun onResponse(call: Call<RouteResponse>, response: Response<RouteResponse>) {
                Log.i("ResponseString", gson.toJson(response.body()))
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", gson.toJson(response.body()))
                        val routes = response.body()?.routes
                        val fromList: MutableList<String> = mutableListOf()
                        val toList: MutableList<String> = mutableListOf()
                        fromList.add("--Select--")
                        toList.add("--Select--")
                        if (routes!!.isNotEmpty()) {
                            for (route in routes) {
                                if (route.from.toString() !in fromList)
                                    fromList.add(route.from.toString())
                                if (route.to.toString() !in fromList)
                                    toList.add(route.to.toString())
                            }

                            from_spinner.setItems(fromList)
                            from_spinner.setOnItemSelectedListener(MaterialSpinner.OnItemSelectedListener<String> { view, position, id, item ->
                                //                                Snackbar.make(
//                                    view,
//                                    "Clicked $item",
//                                    Snackbar.LENGTH_LONG
//                                ).show()
                                toList.clear()
                                toList.add("--Select--")
                                for (route in routes) {
                                    if (route.from.equals(item)) {
                                        toList.add(route.to.toString())
                                    }
                                }
                                fromString = item
                            })
                            to_spinner.setItems(toList)
                            to_spinner.setOnItemSelectedListener(MaterialSpinner.OnItemSelectedListener<String> { view, position, id, item ->
                                //                                Snackbar.make(
//                                    view,
//                                    "Clicked $item",
//                                    Snackbar.LENGTH_LONG
//                                ).show()
                                toString = item
                            })
                            class_spinner.setItems("--Select--", "Class A", "Class B", "Class C")
                            class_spinner.setOnItemSelectedListener(MaterialSpinner.OnItemSelectedListener<String> { view, position, id, item ->
                                //                                Snackbar.make(
//                                    view,
//                                    "Clicked $item",
//                                    Snackbar.LENGTH_LONG
//                                ).show()
                                classString = item
                            })
                        }
                        dialog.hide()
                    } else {
                        Log.i(
                            "onEmptyResponse",
                            "Returned empty response"
                        )
                        dialog.hide()
                        Toast.makeText(this@MainActivity, "Routes Unsuccessful!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.i(
                        "onEmptyResponse",
                        "Returned empty response"
                    )
                    Toast.makeText(this@MainActivity, "Routes Unsuccessful!", Toast.LENGTH_SHORT).show()
                    dialog.hide()
                }
            }

            override fun onFailure(call: Call<RouteResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Routes Unsuccessful!", Toast.LENGTH_SHORT).show()
                Log.d("Route error", t.message)
                dialog.hide()
            }
        })

        book_btn.setOnClickListener {
            if(fromString!="--Select--" && toString!="--Select--" && classString!="--Select--" && mDate!=""){
                dialog = SpotsDialog.Builder()
                    .setContext(this@MainActivity)
                    .setCancelable(false)
                    .setTheme(R.style.CustomDialog)
                    .build()
                    .apply {
                        show()
                    }

                val gson = Gson()

                val retrofit = Retrofit.Builder()
                    .baseUrl(serverURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val api = retrofit.create(ServerApi::class.java)

                val sharedPreference = SharedPreference(this@MainActivity)
                val call = api.userBooking(
                    fromString,
                    toString,
                    classString,
                    mDate,
                    sharedPreference.getValueString("name").split(" ")[0],
                    sharedPreference.getValueString("name").split(" ")[1],
                    sharedPreference.getValueString("phone")
                )

                call.enqueue(object : Callback<BookingResponse> {

                    override fun onResponse(call: Call<BookingResponse>, response: Response<BookingResponse>) {
                        Log.i("ResponseString", gson.toJson(response.body()))
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                Log.i("onSuccess", gson.toJson(response.body()))
                                parseRegisterData(response.body()!!)
                                dialog.hide()
                            } else {
                                Log.i(
                                    "onEmptyResponse1",
                                    "Returned empty response"
                                )
                                dialog.hide()
                                Toast.makeText(this@MainActivity, "Booking Unsuccessful!", Toast.LENGTH_SHORT).show()
                            }
                        } else {

                            Log.i(
                                "onEmptyResponse2",
                                "Returned empty response"
                            )
                            Toast.makeText(this@MainActivity, "Booking Unsuccessful!", Toast.LENGTH_SHORT).show()
                            dialog.hide()
                        }

                    }

                    override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Booking Unsuccessful!", Toast.LENGTH_SHORT).show()
                        Log.i("onFailure", t.message)
                        dialog.hide()
                    }
                })
            }else{
                Toast.makeText(this@MainActivity,"Please fill the fields!",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
                true
            }
            R.id.action_routes -> {
                if (sharedPreference.getValueString("booking").isNullOrEmpty())
                    Toast.makeText(this@MainActivity, "No bookings", Toast.LENGTH_SHORT).show()
                else {
                    val intent = Intent(this@MainActivity, ViewActivity::class.java)
                    intent.putExtra("route_details", sharedPreference.getValueString("booking"))
                    startActivity(intent)
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun parseRegisterData(response: BookingResponse) {
        val gson = Gson()
        try {
            if (response.status!!) {
                val TOPIC_GLOBAL = "global" + response.response!!.routeId
                FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_GLOBAL)
                    .addOnCompleteListener { task ->
                        var msg = "Succesful subscribed to " + TOPIC_GLOBAL
                        if (!task.isSuccessful) {
                            msg = "Failed to subscribe to " + TOPIC_GLOBAL
                        }
                        Log.d(MainActivity::class.java.simpleName, msg)
                    }
                val mDataList = ArrayList<Station>()
                val stations = response.response!!.stations
                for (i in 0 until stations!!.size) {
                    if ((i != stations.size - 1) and (i != stations.size - 2))
                        mDataList.add(Station(stations[i], "", "inactive"))
                    else if (i == stations.size - 2)
                        mDataList.add(Station(stations[i], "", "active"))
                    else
                        mDataList.add(Station(stations[i], "", "complete"))
                }
                sharedPreference.save("booking",gson.toJson(mDataList))
                val intent = Intent(this@MainActivity, ViewActivity::class.java)
                intent.putExtra("route_details", gson.toJson(mDataList))
                startActivity(intent)
            } else {
                Toast.makeText(this@MainActivity, "Login Unsuccessful!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun updateDateLabel() {
        val myFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        dateChooser.setText(sdf.format(myCalendar.getTime()))

        val mFormat = "dd/MM/yyyy" //In which you need put here
        val sdfM = SimpleDateFormat(mFormat, Locale.US)
        mDate = sdfM.format(myCalendar.getTime())
    }

}
