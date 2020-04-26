package com.steadbytes.crosslibexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.widget.*
import com.steadbytes.crosslib.IDGenerator
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 32 char seed value
 */
const val DEFAULT_SEED = "Welcome to the CrossLib example!"

/**
 * Avoid having to create a UI that can handle lots of IDs...
 */
const val MAX_IDS = 15

/**
 * Simple demonstration of [IDGenerator]. Displays list of generated IDs updating on a button click.
 * The list is cleared, generator reset and the user notified via a [Toast] upon generator
 * exhaustion.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var gen: IDGenerator
    private lateinit var currentIdsListAdapter: ArrayAdapter<String>
    private var currentIds = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up ListView to be backed by currentIds list
        currentIdsListAdapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, currentIds
        )
        listViewIds.adapter = currentIdsListAdapter

        initGen()

        buttonGenerate.setOnClickListener {
            if (!gen.hasNext()) {
                Toast.makeText(
                    this,
                    "Generator exhausted - restarting!", Toast.LENGTH_LONG
                ).show()
                initGen()
                currentIds.clear()
            }
            currentIds.add(gen.next().toString())
            currentIdsListAdapter.notifyDataSetChanged()
        }
    }

    private fun initGen() {
        gen = IDGenerator(DEFAULT_SEED.toByteArray(), MAX_IDS)
    }
}
