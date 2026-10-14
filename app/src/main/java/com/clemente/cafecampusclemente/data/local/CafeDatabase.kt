package com.clemente.cafecampusclemente.data.local


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.clemente.cafecampusclemente.data.local.dao.PedidoDao
import com.clemente.cafecampusclemente.data.local.dao.ProductoDao
import com.clemente.cafecampusclemente.data.local.dao.UsuarioDao
import com.clemente.cafecampusclemente.data.local.entity.DetallePedido
import com.clemente.cafecampusclemente.data.local.entity.Pedido
import com.clemente.cafecampusclemente.data.local.entity.Producto
import com.clemente.cafecampusclemente.data.local.entity.Usuario

@Database(
    entities = [
        Producto::class,
        Usuario::class,
        Pedido::class,
        DetallePedido::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CafeDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun pedidoDao(): PedidoDao

    companion object {

        @Volatile
        private var INSTANCE: CafeDatabase? = null

        fun obtener(context: Context): CafeDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CafeDatabase::class.java,
                    "cafecampus.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
