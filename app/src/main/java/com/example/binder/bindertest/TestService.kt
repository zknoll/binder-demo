package com.example.binder.bindertest

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Process
import android.util.Log
import java.util.*


class TestService : Service()  {

    var timeListener: ISystemTimeListener? = null

    override fun onCreate() {
        super.onCreate()
        Log.d("TESTSERVICE", "onCreate: called")
    }

    override fun onBind(intent: Intent): IBinder? {
        // Return the interface
        Log.d("TESTSERVICE", "onBind: called")
        return mBinder
    }

    private val mBinder = object : ITestService.Stub() {
        override fun getPid(): Int {
            return Process.myPid()
        }

        override fun basicTypes(anInt: Int, aLong: Long, aBoolean: Boolean, aFloat: Float, aDouble: Double, aString: String?) {
            // do nothing
        }

        override fun registerForTimeUpdates(listener: ISystemTimeListener?) {
            Log.d("TESTSERVICE", "registerForTimeUpdates: called, listener = $listener")
            listener?.let {
                timeListener = it
                startTimeLoop()
            }
        }
    }

    private val runnable: Runnable = Runnable {
        Log.d("TESTSERVICE", "runnable running: responding to listener = $timeListener")
        timeListener?.onResponse(Date().time)
        rerunLoop()
    }

    private fun startTimeLoop() {
        Log.d("TESTSERVICE", "startTimeLoop: called")
        Handler(Looper.getMainLooper()).postDelayed(runnable, 10000)
    }

    private fun rerunLoop() {
        Handler(Looper.getMainLooper()).postDelayed(runnable, 10000)
    }
}