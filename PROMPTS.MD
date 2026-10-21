

# Prompts utilizados
## Prompt 1
### **Texto enviado a Gemini:**
Agrega una barra de búsqueda en MenuScreen que filtre productos por nombre
en tiempo real mientras el usuario escribe. La lógica del filtro debe ir en MenuViewModel
usando combine de StateFlow, NO en el Composable. La búsqueda debe ignorar mayúsculas y minúsculas
y combinarse correctamente con el filtro de categoría existente, sin reemplazarlo. Si no hay coincidencias,
muestra un mensaje apropiado. No uses XML. Muéstrame solo los archivos que cambian.

### **Que genero la IA:**
En `MenuViewModel.kt` agregó dos `MutableStateFlow` (`_busqueda`, `_categoriaSeleccionada`) con sus `StateFlow` públicos, y calculó `productosFiltrados` con `combine(productos, _busqueda, _categoriaSeleccionada)`. El filtro usa `producto.nombre.contains(texto, ignoreCase = true)` y lo combina con el filtro de categoría (`categoria == "Todo" || producto.categoria.equals(categoria, ignoreCase = true)`). Añadió `cambiarBusqueda()` y `cambiarCategoria()`. En `MenuScreen.kt` eliminó el estado local y el filtrado del Composable; ahora observa `productosFiltrados` y conecta el `BasicTextField` a `vm::cambiarBusqueda` y los `FilterChip` a `vm.cambiarCategoria(categoria)`.

### **Que tuve que corregir yo:**
No hizo falta corregir lógica: el código funciona sin problemas.

### **Respeta MVVM?:** 
Si (el estado y la lógica de búsqueda/filtrado viven en el ViewModel usando StateFlow + combine; la UI solo observa el estado y emite eventos, y los datos siguen saliendo de Room a través de CafeRepository)


## Prompt 2

### **Texto enviado a Gemini:**

Agrega una pantalla de registro de estudiante que permita crear una cuenta nueva y valide que el correo electrónico sea único para evitar registros duplicados. La lógica de validación debe implementarse en ViewModel, las operaciones de base de datos en DAO y Repository, y la interfaz en Compose, respetando la arquitectura MVVM existente. Si es necesario agregar funciones al Repository, decláralas primero en la interfaz y luego impleméntalas en su clase correspondiente. Integra la pantalla con la navegación existente sin romper las funcionalidades actuales. Inspecciona primero el código del proyecto para reutilizar las entidades, rutas y componentes existentes. No uses XML. Muéstrame solo los archivos que cambian.

### **Que genero la IA:**
En `UsuarioDao.kt` agregó `buscarPorCorreo(correo)`. En `CafeRepository.kt` declaró `usuarioPorCorreo()` y `registrarUsuario()`, y en `CafeRepositoryRoom.kt` las implementó. En `SesionViewModel.kt` añadió `registroExitoso`, `registrarEstudiante()` (valida campos vacíos, formato de correo y unicidad del correo; crea un `Usuario` con `rol = CLIENTE` y lo inserta) y `resetRegistro()`. Creó `RegistroScreen.kt` (campos nombre/correo/contraseña, muestra errores y botón "Crear cuenta"). Modificó `LoginScreen.kt` para agregar `onRegistrarse` y enlazar "Regístrate", y `NavCafe.kt` para añadir la ruta `REGISTRO`, el import y el composable.

### **Que tuve que corregir yo:**
No hizo falta corregir lógica: compiló correctmente. Solo verifiqué que la pantalla quedara integrada en la navegación y que el nuevo usuario se creara con `rol = CLIENTE`.

### **Respeta MVVM?:** 
Si (la validación y el estado viven en el ViewModel, las operaciones de base de datos en el DAO/Repository, y la interfaz es Compose; la pantalla se integra con la navegación existente)


## Prompt 3

Agrega una pantalla de Reporte del día que muestre el producto más vendido y el total de ventas por forma de pago, utilizando datos reales de los pedidos almacenados en Room correspondientes al día actual. La lógica de consulta debe implementarse en DAO y Repository, el ViewModel debe procesar y exponer los resultados mediante Flow o StateFlow, y la interfaz Compose debe mostrar la información de forma clara. Respeta la arquitectura MVVM existente: la View no debe acceder directamente al DAO ni al Repository, y si agregas funciones al Repository, decláralas primero en la interfaz y luego impleméntalas en su clase correspondiente. Integra la pantalla con la navegación existente sin romper las funcionalidades actuales. Inspecciona primero el código del proyecto para reutilizar las entidades, relaciones, consultas y componentes existentes. No uses XML. Muéstrame solo los archivos que cambian.

### **Texto enviado a Gemini:**

### **Que genero la IA:**

### **Que tuve que corregir yo:**

### **Respeta MVVM?:** 