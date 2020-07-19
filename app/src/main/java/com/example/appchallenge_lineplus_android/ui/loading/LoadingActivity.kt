package com.example.appchallenge_lineplus_android.ui.loading

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appchallenge_lineplus_android.R
import com.example.appchallenge_lineplus_android.ui.main.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        GlobalScope.launch {
            delay(1000)
            loadMainActivity()
        }
    }

    private fun loadMainActivity(){
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(mainActivityIntent)
        finish()
    }
}