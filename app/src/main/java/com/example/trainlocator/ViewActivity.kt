package com.example.trainlocator

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainlocator.adapters.TimelineAdapter
import com.example.trainlocator.models.Station
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_view.*
import kotlinx.android.synthetic.main.activity_view.toolbar

class ViewActivity : AppCompatActivity() {

    private lateinit var mAdapter: TimelineAdapter
    private val mDataList = ArrayList<Station>()
    private lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = null

        val routeDetails = intent.extras?.getString("route_details")

        if(!routeDetails.isNullOrEmpty()) {
            var gson = Gson()
            mDataList.addAll(gson.fromJson(routeDetails,Array<Station>::class.java).asList())
        }
        initRecyclerView()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.view_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                startActivity(Intent(this@ViewActivity, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initRecyclerView() {
        initAdapter()
//        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
//            @SuppressLint("LongLogTag")
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                dropshadow.visibility = if(recyclerView.getChildAt(0).top < 0) View.VISIBLE else View.GONE
//            }
//        })
    }

    private fun initAdapter() {

        mLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        recyclerView.layoutManager = mLayoutManager

        mAdapter = TimelineAdapter(mDataList, this)
        recyclerView.adapter = mAdapter
    }

}
