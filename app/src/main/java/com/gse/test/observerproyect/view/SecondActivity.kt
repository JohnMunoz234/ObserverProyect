package com.gse.test.observerproyect.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gse.test.observerproyect.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    var binding: ActivitySecondBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }
}