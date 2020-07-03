package com.dinube.bonpreu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


class SelectionActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var listView = ListView(this@SelectionActivity)
        var adapter = ArrayAdapter<String>(this@SelectionActivity, android.R.layout.simple_list_item_1, android.R.id.text1, intent.getStringArrayListExtra("connections"))

        listView.setOnItemClickListener { adapter, _, position, _ ->

            var intent = Intent()
            intent.putExtra("connection_position", position)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        listView.adapter = adapter

        setContentView(listView)
    }
}