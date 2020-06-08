package com.example.projetmultidisciplinaire_applicovid.controler.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.example.projetmultidisciplinaire_applicovid.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val REQUEST_CHECK_SETTINGS = 920
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var location:Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        location = Location("")
        // Create an instance of the Fused Location Provider Client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onResume() {
        super.onResume()
        getLocationSettings()
    }

    private fun getUserCurrentLocation() {
        // Manage permissions at runtime for Android M
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(object : PermissionListener {
                @SuppressLint("MissingPermission")
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    getLastKnownLocation()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {}
                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                }
            })
            .check()
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {
        // call getLastLocation
        fusedLocationProviderClient!!.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    this.location = location
                    val position = LatLng(location.latitude, location.longitude)
                    if (mMap != null) {
                        mMap.addMarker(
                            MarkerOptions()
                                .position(position)
                                .title("Marker in Sydney")
                        )
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(position))
                    }
                    // Display the current location on the device screen
                    Toast.makeText(
                        this@MapsActivity,
                        "Latitude is :" + location.latitude + " and Longitude is: " + location.longitude,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@MapsActivity,
                        "Cannot get user current location at the moment",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    fun getLocationSettings() {

        // Create a location request
        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 9000
        mLocationRequest.fastestInterval = 4000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        // Get current location settings
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)

        // Check if current location settings are convenient
        val client = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        // Add callback to location request task
        task.addOnFailureListener(this, object : OnFailureListener {
            override fun onFailure(@NonNull e: Exception) {
                val statusCode = (e as ApiException).statusCode
                when (statusCode) {
                    CommonStatusCodes.RESOLUTION_REQUIRED ->
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            val resolvable = e as ResolvableApiException
                            resolvable.startResolutionForResult(
                                this@MapsActivity,
                                REQUEST_CHECK_SETTINGS
                            )
                        } catch (sendEx: SendIntentException) {
                            // Ignore the error.
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        })
        task.addOnSuccessListener(
            this,
            OnSuccessListener<LocationSettingsResponse?> { // All location settings are satisfied. The client can initialize
                // location requests here.
                getUserCurrentLocation()
            })
    }

    protected override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                getUserCurrentLocation()
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        getUserCurrentLocation()
    }
}
