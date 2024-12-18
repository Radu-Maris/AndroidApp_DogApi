package com.example.assignment2_maris_radu

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment2_maris_radu.adapters.DogImagesAdapter
import com.example.assignment2_maris_radu.data.RetrofitInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import kotlinx.coroutines.withContext
import okhttp3.internal.notifyAll

class ImageNavigationActivity : ComponentActivity() {
    private lateinit var progressBarImages: ProgressBar
    private lateinit var loadingTextImages: TextView
    private lateinit var recyclerViewImages: RecyclerView
    private lateinit var loadMoreButton: Button
    private lateinit var imageAdapter: DogImagesAdapter
    private val activityScope = CoroutineScope(Dispatchers.Main + Job())

    private var allImagesList = emptyList<String>()
    private var currentImagesList = mutableListOf<String>()
    private val nrOfImages = 6
    private var currentImage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.dog_images)

        progressBarImages = findViewById(R.id.progressBarImages)
        loadingTextImages = findViewById(R.id.loadingTextImages)
        recyclerViewImages = findViewById(R.id.recyclerViewImages)
        loadMoreButton = findViewById(R.id.button)

        val breedName = intent.getStringExtra("breed_name") ?: "Unknown Breed"

        setupRecyclerView()
        loadImages(breedName)

        loadMoreButton.setOnClickListener{
            loadMoreImages()
        }

    }

    private fun setupRecyclerView() {
        recyclerViewImages.layoutManager = LinearLayoutManager(this)
        imageAdapter = DogImagesAdapter(currentImagesList)
        recyclerViewImages.adapter = imageAdapter
    }

    private fun loadImages(breedName: String) {
        progressBarImages.visibility = View.VISIBLE
        loadingTextImages.visibility = View.VISIBLE
        loadMoreButton.visibility = View.GONE

        activityScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInterface.dogImagesApi.getBreedsImages(breedName)
                allImagesList = response.message ?: emptyList()

                kotlinx.coroutines.delay(2000)

                withContext(Dispatchers.Main) {
                    progressBarImages.visibility = View.GONE
                    loadingTextImages.visibility = View.GONE

                    if(allImagesList.isNotEmpty()){
                        loadMoreButton.visibility = View.VISIBLE
                        loadMoreImages()
                    }
                    else{
                        loadingTextImages.text = "You have seen all pictures"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBarImages.visibility = View.GONE
                    loadingTextImages.text = "Failed to load images"
                }
            }
        }
    }

    private fun loadMoreImages() {
        val nextPosition = (currentImage + nrOfImages).coerceAtMost(allImagesList.size)
        val newImages = allImagesList.subList(currentImage, nextPosition)

        currentImagesList.addAll(newImages)
        imageAdapter.notifyItemRangeInserted(currentImage, newImages.size)

        currentImage = nextPosition

        if (currentImage >= allImagesList.size) {
            loadMoreButton.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancel()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage("You would like to navigate back to the breeds list?")
            .setPositiveButton("Yes") { _, _ ->
                super.onBackPressed()
            }
            .setNegativeButton("No", null)
            .show()
    }

}
