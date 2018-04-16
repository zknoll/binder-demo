package com.example.binder.bindertest

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileDescriptor
import java.io.PrintWriter

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MACT"
    }

    val binder = object : ISystemTimeListener.Stub() {
        override fun onResponse(time: Long) {
            Log.d(TAG, "onResponse called: time = $time")
        }
    }
    var testService: ITestService? = null
    val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.e(TAG, "Service has unexpectedly disconnected")
            testService = null
        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            // Following the example above for an AIDL interface,
            // this gets an instance of the IRemoteInterface, which we can use to call on the service
            Log.d(TAG, "Service was successfully bound with IBinder instance $p1")
            testService = ITestService.Stub.asInterface(p1)
            Log.d(TAG, "onServiceConnected: bound to service, pid = ${testService?.pid}")
            testService?.registerForTimeUpdates(binder)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: called")
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val intent = Intent(this, TestService::class.java)
        bindService(intent, serviceConnection,  Context.BIND_AUTO_CREATE)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
