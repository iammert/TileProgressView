package com.iammert.tileprogressviewsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iammert.tileprogressview.TiledProgressView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TiledProgressView>(R.id.tileProgressView).setProgress(0.6f)
    }
}