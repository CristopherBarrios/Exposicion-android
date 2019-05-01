package com.example.myalarma

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import android.widget.ToggleButton
import java.util.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    var seleccionarTiempo: TimePicker? = null
    var intentPendiente: PendingIntent? = null
    var Alarmas: AlarmManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seleccionarTiempo = findViewById<TimePicker>(R.id.timePicker)
        Alarmas = getSystemService(ALARM_SERVICE) as AlarmManager

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun onToggleClicked(view: View) {

        startAlarm(view)
    }

    private fun startAlarm(view: View) {
        if ((view as ToggleButton).isChecked) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, seleccionarTiempo!!.currentHour)
            calendar.set(Calendar.MINUTE, seleccionarTiempo!!.currentMinute)
            val intent = Intent(this, AlarmReceiver::class.java)
            intentPendiente = PendingIntent.getBroadcast(this, 0, intent, 0)

            var time = calendar.timeInMillis - calendar.timeInMillis % 60000

            if (System.currentTimeMillis() > time) {
                if (Calendar.AM_PM === 0)
                    time += 1000 * 60 * 60 * 12
                else
                    time += time + 1000 * 60 * 60 * 24
            }
            Alarmas!!.set(AlarmManager.RTC, time, intentPendiente);
            Toast.makeText(this, "Encendido", Toast.LENGTH_SHORT).show()
        } else {
            Alarmas!!.cancel(intentPendiente)
            Toast.makeText(this, "Apagado", Toast.LENGTH_SHORT).show()
        }
    }
}