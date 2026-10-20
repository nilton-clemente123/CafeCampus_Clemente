package com.clemente.cafecampusclemente.data.local


import com.clemente.cafecampusclemente.R
import com.clemente.cafecampusclemente.data.local.entity.Producto
import com.clemente.cafecampusclemente.data.local.entity.Usuario

object DatosIniciales {

    suspend fun sembrar(db: CafeDatabase) {
        val usuarioDao = db.usuarioDao()
        val productoDao = db.productoDao()

        // Si ya existen usuarios, no volver a sembrar.
        if (usuarioDao.contar() != 0) return

        val productos = listOf(
            Producto(
                nombre = "Café americano",
                descripcion = "Café americano recién preparado",
                categoria = "Bebidas calientes",
                precio = 4.00,
                icono = "cafe",
                imagenRes = R.drawable.cappucino
            ),
            Producto(
                nombre = "Sándwich de pollo",
                descripcion = "Sándwich de pollo",
                categoria = "Comidas",
                precio = 9.00,
                icono = "pan",
                imagenRes = R.drawable.sandwichpollo
            ),
            Producto(
                nombre = "Menú del día",
                descripcion = "Menú preparado del día",
                categoria = "Menús",
                precio = 12.00,
                icono = "plato",
                imagenRes = R.drawable.ic_producto_menu
            ),
            Producto(
                nombre = "Jugo de naranja",
                descripcion = "Jugo de naranja",
                categoria = "Jugos",
                precio = 5.00,
                icono = "jugo",
                imagenRes = R.drawable.jugonaranja
            ),
            Producto(
                nombre = "Empanada",
                descripcion = "Empanada recién horneada",
                categoria = "Snacks",
                precio = 4.50,
                icono = "pan",
                imagenRes = R.drawable.empanada
            ),
            Producto(
                nombre = "Chicha morada",
                descripcion = "Chicha morada",
                categoria = "Bebidas frías",
                precio = 3.50,
                icono = "botella",
                imagenRes = R.drawable.chichamorada
            )
        )

        productos.forEach { producto ->
            productoDao.insertar(producto)
        }

        usuarioDao.insertar(
            Usuario(
                nombre = "Juan",
                correo = "juan@campus.test",
                clave = "1234",
                rol = "CLIENTE"
            )
        )

        usuarioDao.insertar(
            Usuario(
                nombre = "Administrador",
                correo = "admin@cafe.test",
                clave = "admin",
                rol = "ADMIN"
            )
        )
    }
}
