package com.example.sensordata

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.lang.Exception

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var mSensorManager: SensorManager

    private lateinit var mButton: Button

    private var isSensorReading = false

    private lateinit var mTextView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when(event?.action!!) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("ASDF", "Action Started")
                x = 0f
                y = 0f
                z = 0f
                cnt = 0
                mSensorManager.registerListener (
                    this,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL
                )
            }
            MotionEvent.ACTION_UP -> {
                mSensorManager.unregisterListener(this)
                x /= cnt
                y /= cnt
                z /= cnt
                writeToFile("$x $y $z")
                Log.d("ASDF", "Action Ended")
            }
        }

        return super.onTouchEvent(event)
    }

    private fun writeToFile(data: String) {
        try {
            val mPath = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString() + "/mySensorData.txt"
            val outputStream = FileOutputStream(mPath, true)
            outputStream.write((data + "\n").toByteArray())
            Log.d("ASDF", "$data $cnt")
            outputStream.close()
        } catch (e: Exception) {
            Log.d("ASDF", "Exception: $e")
            e.printStackTrace()
        }
    }

    private var x: Float = 0f
    private var y: Float = 0f
    private var z: Float = 0f
    private var cnt: Int = 0


    override fun onSensorChanged(event: SensorEvent?) {
        Log.d("ASDF", "Sensor changed")
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            x += event.values[0]
            y += event.values[1]
            z += event.values[2]
            cnt++
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onDestroy() {
        super.onDestroy()
        mSensorManager.unregisterListener(this)
    }
}