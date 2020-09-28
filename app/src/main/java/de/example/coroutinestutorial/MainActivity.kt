package de.example.coroutinestutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    private val RESULT_1 = "Result 1"
    private val RESULT_2 = "Result 2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_btn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                fakeApiRequest()
            }
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

    private suspend fun fakeApiRequest() {
        val result1 = getResult1fromApi()
        println("debug: $result1.await")
        setTextOnMainThread(result1)

        val result2 = getResult2fromApi()
        println("debug: $result2")
        setTextOnMainThread(result2)
    }

    private suspend fun getResult1fromApi(): String {
        logThread("getResult1fromApi")
        delay(1000)
        return RESULT_1
    }

    private suspend fun getResult2fromApi(): String {
        logThread("getResult2fromApi")
        delay(1000)
        return RESULT_2
    }

    private fun logThread(methodName: String) {
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }

}