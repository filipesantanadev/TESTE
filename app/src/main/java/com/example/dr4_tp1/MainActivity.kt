package com.example.dr4_tp1

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : AppCompatActivity(), LocationListener {

    val COARSE_REQUEST = 12345
    val FINE_REQUEST = 54321

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRegistrar = this.findViewById<Button>(R.id.btnRegistrar)
        btnRegistrar.setOnClickListener{

            getLocalByNetwork()
            getLocalByGps()

            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                val location = getLocalByGps().toString()
                val arquivo = File(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "myLocation.txt")
                val saida = FileOutputStream(arquivo)
                val txtTexto = "Localização: $location"
                saida.write(txtTexto.toByteArray())
                saida.close()
            }
        }

        val btnLer = this.findViewById<Button>(R.id.btnLer)
        btnLer.setOnClickListener{

            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                val arquivo = File(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "myLocation.txt")
                val entrada = FileInputStream(arquivo)
                val texto = String(entrada.readBytes())
                val lblTexto = this.findViewById<TextView>(R.id.listViewArquivos)
                lblTexto.setText(texto)
            }
        }


    }

    private fun getLocalByNetwork() {
        var location: Location? = null
        val locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        val isNetEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (isNetEnabled) {

            Log.i("DR4", "Indo pela Rede")
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    2000L,
                    0f,
                    this)
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                val lblValorLatitude = this.findViewById<TextView>(R.id.lblValorLatitude)
                if (location != null) {
                    lblValorLatitude.setText(location.latitude.toString())
                }
                val lblValorLongitude = this.findViewById<TextView>(R.id.lblValorLongitude)
                if (location != null) {
                    lblValorLongitude.setText(location.longitude.toString())
                }
            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), COARSE_REQUEST)
            }
            if (location != null) {
                Log.i("DR4_NET", "${location.latitude}  ${location.longitude}")
            }
            else {
                Log.i("DR4_NET", "Erro na REDE")
            }
        }
    }

    private fun getLocalByGps() {
        var location: Location? = null
        val locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsEnabled) {

            Log.i("DR4", "Indo pelo GPS")
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    2000L,
                    0f,
                    this)
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                val lblValorLatitude = this.findViewById<TextView>(R.id.lblValorLatitude)
                if (location != null) {
                    lblValorLatitude.setText(location.latitude.toString())
                }
                val lblValorLongitude = this.findViewById<TextView>(R.id.lblValorLongitude)
                if (location != null) {
                    lblValorLongitude.setText(location.longitude.toString())
                }
            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_REQUEST)
            }
            if (location != null) {
                Log.i("DR4_GPS", "${location.latitude}  ${location.longitude}")
            }
            else {
                Log.i("DR4_GPS", "Erro no GPS")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == COARSE_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            getLocalByNetwork()
        }
        if (requestCode == FINE_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            getLocalByGps()
        }
    }

    override fun onLocationChanged(location: Location) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}


}