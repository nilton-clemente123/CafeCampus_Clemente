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
import com.clemente.cafecampusclemente.data.local.dao.VotoDao
import com.clemente.cafecampusclemente.data.local.entity.DetallePedido
import com.clemente.cafecampusclemente.data.local.entity.Pedido
import com.clemente.cafecampusclemente.data.local.entity.Producto
import com.clemente.cafecampusclemente.data.local.entity.Usuario
import com.clemente.cafecampusclemente.data.local.entity.Voto

@Database(
    entities = [
        Producto::class,
        Usuario::class,
        Pedido::class,
        DetallePedido::class,
        Voto::class
    ],
    version = 5,
    exportSchema = false
)
abstract class CafeDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun pedidoDao(): PedidoDao
    abstract fun votoDao(): VotoDao

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

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `votos` (" +
                        "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "`estudianteId` INTEGER NOT NULL, " +
                        "`productoId` INTEGER NOT NULL, " +
                        "`fecha` INTEGER NOT NULL, " +
                        "FOREIGN KEY(`estudianteId`) REFERENCES `usuarios`(`id`) " +
                        "ON UPDATE NO ACTION ON DELETE CASCADE, " +
                        "FOREIGN KEY(`productoId`) REFERENCES `productos`(`id`) " +
                        "ON UPDATE NO ACTION ON DELETE CASCADE)"
                )
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS `index_votos_estudianteId` " +
                        "ON `votos` (`estudianteId`)"
                )
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS `index_votos_productoId` " +
                        "ON `votos` (`productoId`)"
                )
            }
        }

        // Reasigna las imágenes por nombre para sincronizar los IDs de
        // drawable con el build actual. Los IDs de recursos de Android NO son
        // estables entre compilaciones y, si quedan obsoletos en la base de
        // datos, painterResource() puede lanzar un ClassCastException.
        private val MIGRATION_4_5 = object : Migration(4, 5) {
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
                ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5).build().also { INSTANCE = it }
            }
        }
    }
}
