package com.clemente.cafecampusclemente.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "ajustes")

class PreferenciasStore(
    private val context: Context
) {
    companion object {
        private val PERFIL_ACTIVO = stringPreferencesKey("perfilActivo")
        private val MODO_OSCURO = booleanPreferencesKey("modoOscuro")
        private val RECIBIR_PEDIDOS = booleanPreferencesKey("recibirPedidos")
    }

    val perfilActivo: Flow<String> =
        context.dataStore.data.map { preferencias ->
            preferencias[PERFIL_ACTIVO] ?: ""
        }

    val modoOscuro: Flow<Boolean> =
        context.dataStore.data.map { preferencias ->
            preferencias[MODO_OSCURO] ?: false
        }

    val recibirPedidos: Flow<Boolean> =
        context.dataStore.data.map { preferencias ->
            preferencias[RECIBIR_PEDIDOS] ?: true
        }

    suspend fun guardarPerfil(rol: String) {
        context.dataStore.edit { preferencias ->
            preferencias[PERFIL_ACTIVO] = rol
        }
    }

    suspend fun guardarModoOscuro(activado: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[MODO_OSCURO] = activado
        }
    }

    suspend fun guardarRecibirPedidos(activado: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[RECIBIR_PEDIDOS] = activado
        }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit { preferencias ->
            preferencias.remove(PERFIL_ACTIVO)
        }
    }
}
