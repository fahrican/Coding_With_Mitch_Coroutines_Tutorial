package de.example.coroutinestutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis


class MainActivity : AppCompatActivity() {

    private val RESULT_1 = "Result 1"
    private val RESULT_2 = "Result 2"
    private val JOB_TIMEOUT = 1900L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_btn.setOnClickListener {
            setNewText("Click!")
            fakeApiRequest()
        }
    }

    private fun fakeApiRequest() {
        CoroutineScope(Dispatchers.IO).launch {
            val executionTime = measureTimeMillis {
                val result1: Deferred<String> = async {
                    println("debug: launching job1: ${Thread.currentThread().name}")
                    getResult1fromApi()
                }
                val result2: Deferred<String> = async {
                    println("debug: launching job2: ${Thread.currentThread().name}")
                    getResult2fromApi()
                }
                setTextOnMainThread("Got ${result1.await()}")
                setTextOnMainThread("Got ${result2.await()}")
            }
            println("debug: total elapsed time: $executionTime")
        }
    }

    private fun setNewText(input: String) {
        val newText = main_text.text.toString() + "\n$input"
        main_text.text = newText
    }

    private suspend fun setTextOnMainThread(input: String) {
        withContext(Dispatchers.Main) {
            setNewText(input)
        }
    }

    private suspend fun getResult1fromApi(): String {
        logThread("getResult1fromApi")
        delay(1000)
        return RESULT_1
    }

    private suspend fun getResult2fromApi(): String {
        logThread("getResult2fromApi")
        delay(1700)
        return RESULT_2
    }

    private fun logThread(methodName: String) {
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }

}