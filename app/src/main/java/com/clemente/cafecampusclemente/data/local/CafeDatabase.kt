package com.clemente.cafecampusclemente.data.local


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.clemente.cafecampusclemente.R
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
    version = 3,
    exportSchema = false
)
abstract class CafeDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun pedidoDao(): PedidoDao

    companion object {

        private fun asignarImagenesDefault(db: SupportSQLiteDatabase) {
            db.execSQL(
                "UPDATE productos SET imagenRes = ${R.drawable.cappucino} " +
                    "WHERE nombre = 'Café americano'"
            )
            db.execSQL(
                "UPDATE productos SET imagenRes = ${R.drawable.sandwichpollo} " +
                    "WHERE nombre = 'Sándwich de pollo'"
            )
            db.execSQL(
                "UPDATE productos SET imagenRes = ${R.drawable.ic_producto_menu} " +
                    "WHERE nombre = 'Menú del día'"
            )
            db.execSQL(
                "UPDATE productos SET imagenRes = ${R.drawable.jugonaranja} " +
                    "WHERE nombre = 'Jugo de naranja'"
            )
            db.execSQL(
                "UPDATE productos SET imagenRes = ${R.drawable.empanada} " +
                    "WHERE nombre = 'Empanada'"
            )
            db.execSQL(
                "UPDATE productos SET imagenRes = ${R.drawable.chichamorada} " +
                    "WHERE nombre = 'Chicha morada'"
            )
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE productos ADD COLUMN imagenRes INTEGER NOT NULL DEFAULT 0"
                )
                asignarImagenesDefault(db)
            }
        }

        // Corrige los IDs obsoletos que quedaron de una versión intermedia
        // que usaba otros drawables (los vectores temporales).
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                asignarImagenesDefault(db)
            }
        }

        @Volatile
        private var INSTANCE: CafeDatabase? = null

        fun obtener(context: Context): CafeDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CafeDatabase::class.java,
                    "cafecampus.db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    // Permite recrear la BD cuando no hay migración disponible,
                    // incluyendo downgrades (p. ej. una BD v5 en el dispositivo
                    // frente a una versión menor en el código). La app vuelve a
                    // sembrar los datos en una BD vacía, por lo que es seguro.
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
