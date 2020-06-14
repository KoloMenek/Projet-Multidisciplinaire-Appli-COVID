package com.example.projetmultidisciplinaire_applicovid.controler.ui;

import android.Manifest
import android.content.*
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Color.argb
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.projetmultidisciplinaire_applicovid.BuildConfig
import com.example.projetmultidisciplinaire_applicovid.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import java.io.IOException


/**
 * The only activity in this sample.
 *
 * Note: Users have three options in "Q" regarding location:
 *
 *  * Allow all the time
 *  * Allow while app is in use, i.e., while app is in foreground
 *  * Not allow location at all
 *
 * Because this app creates a foreground service (tied to a Notification) when the user navigates
 * away from the app, it only needs location "while in use." That is, there is no need to ask for
 * location all the time (which requires additional permissions in the manifest).
 *
 * "Q" also now requires developers to specify foreground service type in the manifest (in this
 * case, "location").
 *
 * Note: For Foreground Services, "P" requires additional permission in manifest. Please check
 * project manifest for more information.
 *
 * Note: for apps running in the background on "O" devices (regardless of the targetSdkVersion),
 * location may be computed less frequently than requested when the app is not in the foreground.
 * Apps that use a foreground service -  which involves displaying a non-dismissable
 * notification -  can bypass the background location limits and request location updates as before.
 *
 * This sample uses a long-running bound and started service for location updates. The service is
 * aware of foreground status of this activity, which is the only bound client in
 * this sample. After requesting location updates, when the activity ceases to be in the foreground,
 * the service promotes itself to a foreground service and continues receiving location updates.
 * When the activity comes back to the foreground, the foreground service stops, and the
 * notification associated with that foreground service is removed.
 *
 * While the foreground service notification is displayed, the user has the option to launch the
 * activity from the notification. The user can also remove location updates directly from the
 * notification. This dismisses the notification and stops the service.
 */
class MapsActivity : AppCompatActivity(), OnSharedPreferenceChangeListener, OnMapReadyCallback {
    // The BroadcastReceiver used to listen from broadcasts from the service.
    private var myReceiver: MyReceiver? = null
    private var mMap: GoogleMap? = null
    private lateinit var loc: Location
    private lateinit var marker: Marker
    private lateinit var zoom: CameraUpdate
    private lateinit var center: CameraUpdate
    private lateinit var adresse: LatLng
    private lateinit var circle: Circle

    // A reference to the service used to get location updates.
    private var mService: LocationUpdatesService? = null

    // Tracks the bound state of the service.
    private var mBound = false

    // UI elements.
    private var mRequestLocationUpdatesButton: Button? = null
    private var mRemoveLocationUpdatesButton: Button? = null

    // Monitors the state of the connection to the service.
    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder: LocationUpdatesService.LocalBinder =
                service as LocationUpdatesService.LocalBinder
            mService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myReceiver = MyReceiver()
        setContentView(R.layout.activity_maps)
        loc = Location("")

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Check that the user hasn't revoked permissions by going to Settings.
        if (Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)
        mRequestLocationUpdatesButton =
            findViewById<View>(R.id.request_location_updates_button) as Button
        mRemoveLocationUpdatesButton =
            findViewById<View>(R.id.remove_location_updates_button) as Button
        mRequestLocationUpdatesButton!!.setOnClickListener {
            if (!checkPermissions()) {
                requestPermissions()
            } else {
                mService?.requestLocationUpdates()
            }
        }
        mRemoveLocationUpdatesButton!!.setOnClickListener { mService?.removeLocationUpdates() }

        // Restore the state of the buttons when the activity (re)launches.
        setButtonsState(Utils.requestingLocationUpdates(this))


        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(
            Intent(this, LocationUpdatesService::class.java), mServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            myReceiver!!,
            IntentFilter(LocationUpdatesService.ACTION_BROADCAST)
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver!!)
        super.onPause()
    }

    override fun onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection)
            mBound = false
        }
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
        super.onStop()
    }

    /**
     * Returns the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestPermissions() {
        val shouldProvideRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(
                TAG,
                "Displaying permission rationale to provide additional context."
            )
            Snackbar.make(
                findViewById<View>(R.id.activity_maps),
                R.string.permission_rationale,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.ok, View.OnClickListener { // Request permission
                    ActivityCompat.requestPermissions(
                        this@MapsActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_PERMISSIONS_REQUEST_CODE
                    )
                })
                .show()
        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(
                this@MapsActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                mService?.requestLocationUpdates()
            } else {
                // Permission denied.
                setButtonsState(false)
                Snackbar.make(
                    findViewById<View>(R.id.activity_maps),
                    R.string.permission_denied_explanation,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(
                        R.string.settings,
                        View.OnClickListener { // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                BuildConfig.APPLICATION_ID, null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        })
                    .show()
            }
        }
    }

    /**
     * Receiver for broadcasts sent by [LocationUpdatesService].
     */
    private inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            val location =
                intent.getParcelableExtra<Location>(LocationUpdatesService.EXTRA_LOCATION)
            if (location != null) {
                Toast.makeText(
                    this@MapsActivity, Utils.getLocationText(location),
                    Toast.LENGTH_SHORT
                ).show()
                loc = location
                marker.remove()
                updateUI()

            }
        }
    }

    override fun onSharedPreferenceChanged(
        sharedPreferences: SharedPreferences,
        s: String
    ) {
        // Update the buttons state depending on whether location updates are being requested.
        if (s == Utils.KEY_REQUESTING_LOCATION_UPDATES) {
            setButtonsState(
                sharedPreferences.getBoolean(
                    Utils.KEY_REQUESTING_LOCATION_UPDATES,
                    false
                )
            )
        }
    }

    private fun setButtonsState(requestingLocationUpdates: Boolean) {
        if (requestingLocationUpdates) {
            mRequestLocationUpdatesButton!!.isEnabled = false
            mRemoveLocationUpdatesButton!!.isEnabled = true
        } else {
            mRequestLocationUpdatesButton!!.isEnabled = true
            mRemoveLocationUpdatesButton!!.isEnabled = false
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName

        // Used in checking for runtime permissions.
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

    fun updateUI() {

        if (mMap != null) {
            val position = LatLng(loc.latitude, loc.longitude)
            marker = mMap!!.addMarker(
                MarkerOptions()
                    .position(position)
                    .title("Ma position")
            )
            center = CameraUpdateFactory.newLatLng(position)
            zoom = CameraUpdateFactory.zoomTo(16F)
            mMap!!.moveCamera(center)
            mMap!!.animateCamera(zoom)

        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        updateUI()

        var addresss: List<Address?>
        val coder = Geocoder(this)
        var p1: LatLng? = null

        var num =
            getSharedPreferences("UserData", Context.MODE_PRIVATE).getString("num", "errornum")
        var street = getSharedPreferences("UserData", Context.MODE_PRIVATE).getString(
            "street",
            "errorstreet"
        )
        var city =
            getSharedPreferences("UserData", Context.MODE_PRIVATE).getString("city", "errorcity")
        var zipCode =
            getSharedPreferences("UserData", Context.MODE_PRIVATE).getString("zipCode", "errorZIP")
        var adress: String = "$num,$street,$zipCode,$city"

        System.out.println(adress)

        // May throw an IOException
        addresss = coder.getFromLocationName(adress, 5);
        if (addresss == null) {
            System.out.println("Bruh adresss = null")
        }
        val location = addresss[0]
        p1 = LatLng(location!!.latitude, location!!.longitude)
        var latlngadresse = "$p1"
        System.out.println(latlngadresse)


        //Cercle de limitation des 3km
        adresse = LatLng(loc.latitude, loc.longitude)
        val circleOptions = CircleOptions()
            .center(adresse)
            .radius(3000.0) // In meters
            .strokeColor(Color.BLUE)
            .fillColor(argb(50, 0, 0, 200))

// Get back the mutable Circle
        circle = mMap!!.addCircle(circleOptions)


    }

}
