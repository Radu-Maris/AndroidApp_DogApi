package com.example.assignment2_maris_radu

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment2_maris_radu.data.RetrofitInterface
import com.example.assignment2_maris_radu.data.dto.BreedResponse
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var loadingText: TextView
    private lateinit var recyclerView: RecyclerView
    private val activityScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        progressBar = findViewById(R.id.progressBar5)
        loadingText = findViewById(R.id.textView)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        loadData()
    }

    private fun loadData() {
        progressBar.visibility = View.VISIBLE

        activityScope.launch(Dispatchers.IO) {
            try {
                val response: BreedResponse = RetrofitInterface.api.getBreeds()
                val breedsList = response.message?.keys?.toList() ?: emptyList()

                delay(2000)

                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    loadingText.visibility = View.GONE
                    recyclerView.adapter = BreedsAdapter(breedsList) { selectedBreed ->
                        val intent = Intent(this@MainActivity, ImageNavigationActivity::class.java)
                        intent.putExtra("breed_name", selectedBreed)
                        startActivity(intent)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancel()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage("You would like to leave application?")
            .setPositiveButton("Yes") { _, _ ->
                super.onBackPressed()
            }
            .setNegativeButton("No", null)
            .show()
    }
}
