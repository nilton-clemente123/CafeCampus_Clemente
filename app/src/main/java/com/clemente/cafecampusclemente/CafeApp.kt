package com.clemente.cafecampusclemente


import android.app.Application
import com.clemente.cafecampusclemente.data.local.CafeDatabase
import com.clemente.cafecampusclemente.data.local.DatosIniciales
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CafeApp : Application() {

    private val applicationScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        val db = CafeDatabase.obtener(this)

        applicationScope.launch {
            DatosIniciales.sembrar(db)
        }
    }
}
