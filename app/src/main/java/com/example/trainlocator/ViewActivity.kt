package com.example.trainlocator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainlocator.adapters.TimelineAdapter
import com.example.trainlocator.models.Station
import kotlinx.android.synthetic.main.activity_view.*

class ViewActivity : AppCompatActivity() {

    private lateinit var mAdapter: TimelineAdapter
    private val mDataList = ArrayList<Station>()
    private lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        val messageString = intent.extras?.getString("message")

        setDataListItems()
        initRecyclerView()

    }

    private fun setDataListItems() {
        mDataList.add(Station("Dodoma", "", "inactive"))
        mDataList.add(Station("Mpwapwa", "2017-02-12 08:00", "inactive"))
        mDataList.add(Station("Maghubike", "2017-02-11 21:00", "active"))
        mDataList.add(Station("Gairo", "2017-02-11 18:00", "completed"))
        mDataList.add(Station("Morogoro", "2017-02-11 09:30", "completed"))
        mDataList.add(Station("Chalinze", "2017-02-11 08:00", "completed"))
        mDataList.add(Station("Mlandizi", "2017-02-10 15:00", "completed"))
        mDataList.add(Station("Kibaha", "2017-02-10 14:30", "completed"))
        mDataList.add(Station("Dar es Salaam", "2017-02-10 14:00", "completed"))
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
