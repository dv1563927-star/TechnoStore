# TechnoStore

Sistema de consola en **Java** para gestionar el inventario, los clientes y las ventas de una tienda de teléfonos celulares.

Toda la información se guarda en una base de datos **MySQL** a través de **JDBC**, y el acceso a los datos se hace con **procedimientos almacenados**: el programa Java no escribe las consultas SQL directamente, sino que llama a procedimientos que ya están creados dentro de la base de datos.

El proyecto está construido por capas (modelo, acceso a datos, control, vista) siguiendo los principios de la programación orientada a objetos.

---

## Índice

1. [Tecnologías y conceptos aplicados](#1-tecnologías-y-conceptos-aplicados)
2. [Estructura del proyecto](#2-estructura-del-proyecto)
3. [Base de datos](#3-base-de-datos)
4. [Requisitos previos](#4-requisitos-previos)
5. [Instalación](#5-instalación)
6. [Ejecución desde una terminal Linux](#6-ejecución-desde-una-terminal-linux)
7. [Orden de uso: guía paso a paso](#7-orden-de-uso-guía-paso-a-paso)
8. [Detalle de cada menú](#8-detalle-de-cada-menú)
9. [Reglas de negocio y validaciones](#9-reglas-de-negocio-y-validaciones)
10. [Notas y recomendaciones](#10-notas-y-recomendaciones)

---

## 1. Tecnologías y conceptos aplicados

| Concepto | Dónde se aplica |
|---|---|
| **Programación orientada a objetos** | 6 clases de modelo: `Celular`, `Marca`, `Cliente`, `Usuarios`, `Venta`, `DetalleVenta` |
| **Encapsulamiento** | Todos los atributos son `private`, con sus `get` y `set` |
| **Composición** | `Celular` contiene una `Marca`, `Venta` contiene un `Cliente`, `DetalleVenta` contiene un `Celular` y una `Venta` |
| **Tipos enumerados (`enum`)** | `Usuarios.Rol` (`ADMIN`, `CLIENTE`) y `Celular.gama` (`ALTA`, `MEDIA`, `BAJA`) |
| **Colecciones** | `List` y `ArrayList` en los listados de celulares, clientes, marcas y ventas |
| **Stream API** | Filtros y recorridos en `ReporteUtils` (`filter`, `limit`, `forEach`) |
| **Manejo de excepciones** | Bloques `try / catch` con `SQLException` en toda la capa de datos |
| **try-with-resources** | Cierre automático de `Connection` y `CallableStatement` en cada DAO |
| **JDBC** | Conexión y llamadas a procedimientos con `CallableStatement` |
| **Patrón Singleton** | Clase `Conexion`, que expone una única instancia con `getInstancia()` |
| **Arquitectura por capas** | Paquetes `modelo`, `dao`, `controlador`, `vista`, `utilidades` |
| **Procedimientos almacenados** | 25 procedimientos en MySQL que contienen las reglas de la base de datos |

---

## 2. Estructura del proyecto

```
TechnoStore/
├── db/
│   └── technostore.sql              Script de creación de la base de datos
├── src/
│   └── main/
│       └── java/
│           ├── com/s2/technostore/
│           │   └── TechnoStore.java      Clase principal (tiene el main)
│           ├── modelo/                   Las entidades del negocio
│           │   ├── Celular.java
│           │   ├── Marca.java
│           │   ├── Cliente.java
│           │   ├── Usuarios.java
│           │   ├── Venta.java
│           │   └── DetalleVenta.java
│           ├── dao/                      Acceso a la base de datos
│           │   ├── Conexion.java         Patrón Singleton
│           │   ├── CelularDAO.java
│           │   ├── MarcaDAO.java
│           │   ├── ClienteDAO.java
│           │   ├── UsuarioDAO.java
│           │   └── VentaDAO.java
│           ├── controlador/              Lógica y navegación entre menús
│           │   ├── GestionSesion.java    Login y registro
│           │   ├── GestionAdmin.java     Menú del administrador
│           │   ├── GestionCelulares.java
│           │   ├── GestionMarcas.java
│           │   ├── GestionClientes.java
│           │   ├── GestionCliente.java   Menú del cliente
│           │   ├── GestionVentas.java
│           │   └── GestionReportes.java
│           ├── vista/                    Todo lo que se muestra en pantalla
│           │   ├── MenuLogin.java
│           │   ├── MenuAdmin.java
│           │   ├── MenuCliente.java
│           │   ├── IngresoDatosCelular.java
│           │   ├── IngresoDatosMarca.java
│           │   ├── IngresoDatosCliente.java
│           │   └── Validaciones.java
│           └── utilidades/
│               ├── ReporteUtils.java     Los reportes con Stream API
│               └── ArchivoUtils.java     Genera el archivo reporte_ventas.dat
├── .gitignore
└── pom.xml                          Configuración de Maven
```

**Total: 30 archivos `.java`.**

### Responsabilidad de cada capa

| Paquete | Responsabilidad |
|---|---|
| `modelo` | Representa las entidades del negocio. No sabe nada de la base de datos ni de menús |
| `dao` | Únicamente habla con la base de datos. Cada clase corresponde a una tabla |
| `controlador` | Contiene la lógica: valida, decide y navega entre menús |
| `vista` | Todo lo que el usuario ve y escribe. No contiene lógica de negocio |
| `utilidades` | Funciones de apoyo: reportes y generación del archivo |

---

## 3. Base de datos

Nombre de la base de datos: **`technostore`**

### Tablas (6)

| Tabla | Campos principales |
|---|---|
| `marcas` | `id`, `nombre` (único) |
| `usuarios` | `id`, `correo` (único), `password`, `rol` (`ADMIN` / `CLIENTE`) |
| `clientes` | `id`, `usuario_fk`, `nombre`, `identificacion` (única), `telefono` |
| `celulares` | `id`, `marca_fk`, `modelo`, `sistema_operativo`, `gama`, `precio`, `stock` |
| `ventas` | `id`, `cliente_fk`, `fecha`, `total` |
| `detalle_ventas` | `id`, `venta_fk`, `celular_fk`, `cantidad`, `subtotal` |

### Relaciones entre las tablas

```
marcas 1 ──── N celulares
usuarios 1 ── N clientes
clientes 1 ── N ventas
ventas 1 ──── N detalle_ventas
celulares 1 ─ N detalle_ventas
```

Un cliente no puede existir sin un usuario, un celular no puede existir sin una marca, y una venta no puede existir sin un cliente.

### Procedimientos almacenados (25)

El programa Java **no escribe consultas SQL**. Todo se hace llamando a procedimientos:

| Grupo | Procedimientos |
|---|---|
| Usuarios | `registrar_usuario`, `buscar_usuario_por_correo`, `buscar_usuario_por_id` |
| Marcas | `registrar_marcas`, `actualizar_marca`, `eliminar_marca`, `listar_marcas` |
| Celulares | `registrar_celular`, `actualizar_celular`, `eliminar_celular`, `listar_celulares`, `buscar_celular` |
| Clientes | `registrar_cliente`, `actualizar_cliente`, `eliminar_cliente`, `listar_clientes`, `buscar_cliente` |
| Ventas | `crear_venta`, `agregar_item_venta`, `listar_ventas`, `listar_ventas_por_cliente`, `listar_detalle_venta` |
| Reportes | `reporte_stock_bajo`, `reporte_top3_vendidos`, `reporte_ventas_por_mes` |

---

## 4. Requisitos previos

| Requisito | Versión | Para qué |
|---|---|---|
| **JDK** (Java Development Kit) | **17 o superior** | Compilar y ejecutar el programa |
| **MySQL Server** | **8.0 o superior** | Guardar los datos |
| **Driver JDBC de MySQL** | `mysql-connector-j` **9.7.0** | Permitir que Java se conecte a MySQL |
| **Maven** (opcional) | Cualquiera reciente | Compilar y descargar dependencias automáticamente |
| **NetBeans** (opcional) | Cualquiera reciente | Abrir el proyecto con interfaz gráfica |

Para comprobar si tienes Java instalado:

```bash
java -version
javac -version
```

Deben responder algo como `java version "17"` o superior.

### Cómo obtener el driver JDBC

**Si usas Maven**, no tienes que hacer nada: el `pom.xml` ya lo declara y Maven lo descarga solo.

**Si compilas a mano**, descarga el archivo desde Maven Central:

```
https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/9.7.0/mysql-connector-j-9.7.0.jar
```

Guárdalo, por ejemplo, en una carpeta `lib/` dentro del proyecto.

---

## 5. Instalación

### Paso 1: crear la base de datos

El script `db/technostore.sql` crea **todo**: la base de datos, las 6 tablas con sus llaves foráneas y restricciones, los 25 procedimientos almacenados, y el usuario administrador inicial.

```bash
mysql -u root -p < db/technostore.sql
```

> **Importante:** el script está en UTF-8 porque los procedimientos llevan tildes y la letra eñe. Si lo importas con otra herramienta, asegúrate de que use `utf8mb4`. Desde la consola se puede forzar así:
>
> ```bash
> mysql -u root -p --default-character-set=utf8mb4 < db/technostore.sql
> ```

Al terminar tendrás la base creada con **un solo usuario**:

| Correo | Contraseña | Rol |
|---|---|---|
| `admin@gmail.com` | `admin123` | `ADMIN` |

> **Nota:** el script crea la estructura y el administrador, pero **no incluye datos de prueba**. La tienda empieza sin marcas ni celulares, así que el administrador tiene que crearlos antes de poder vender (ver el [orden de uso](#7-orden-de-uso-guía-paso-a-paso)).

### Paso 2: configurar la conexión

Abre el archivo `src/main/java/dao/Conexion.java` y ajusta los datos de tu MySQL:

```java
private final String url     = "jdbc:mysql://localhost:3306/technostore";
private final String usuario = "root";
private final String password = "TU_CONTRASEÑA_DE_MYSQL";
```

| Campo | Qué poner |
|---|---|
| `url` | `localhost` si MySQL está en tu misma máquina; `3306` es el puerto por defecto |
| `usuario` | El usuario de MySQL (normalmente `root`) |
| `password` | La contraseña de ese usuario |

### Paso 3: compilar y ejecutar

**Con NetBeans:** abre el proyecto y pulsa *Run* (F6). NetBeans descarga el driver y compila solo.

**Con Maven desde la consola:**

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass=com.s2.technostore.TechnoStore
```

---

## 6. Ejecución desde una terminal Linux

El programa es **100 % de consola**, así que se puede ejecutar sin NetBeans, solo con el JDK. Esto es útil para revisarlo en cualquier sistema operativo.

### Con Maven

```bash
# 1) Compilar
mvn clean compile

# 2) Obtener la ruta del driver JDBC
mvn -q dependency:build-classpath -Dmdep.outputFile=classpath.txt

# 3) Ejecutar
java -cp "target/classes:$(cat classpath.txt)" com.s2.technostore.TechnoStore
```

### Sin Maven (solo con el JDK)

Suponiendo que guardaste el driver en `lib/mysql-connector-j-9.7.0.jar`:

```bash
# 1) Compilar todos los archivos .java
mkdir -p target/classes
find src/main/java -name "*.java" > fuentes.txt
javac -encoding UTF-8 -d target/classes \
      -cp "lib/mysql-connector-j-9.7.0.jar" @fuentes.txt

# 2) Ejecutar
java -cp "target/classes:lib/mysql-connector-j-9.7.0.jar" \
     com.s2.technostore.TechnoStore
```

### Detalles importantes para Linux

| Detalle | Explicación |
|---|---|
| **El separador del classpath es `:`** | En Windows es `;` y en Linux es `:`. Por eso en los comandos de arriba se usa `target/classes:lib/...` |
| **Ejecuta desde la raíz del proyecto** | El programa necesita encontrar el archivo de conexión y, además, escribe `reporte_ventas.dat` en la carpeta donde se ejecute. Si lo ejecutas desde otra carpeta, el reporte aparecerá allí |
| **Hay que indicar la clase principal** | El proyecto genera un `.jar` normal, no un `.jar` ejecutable, así que se arranca con `-cp` y el nombre completo de la clase: `com.s2.technostore.TechnoStore` |
| **No se necesita interfaz gráfica** | Nada de Swing ni JavaFX: es solo entrada y salida por consola |
| **El script SQL es independiente del sistema** | Las tablas usan solo tipos estándar de MySQL, así que el mismo script funciona en Linux, Windows o macOS |

---

## 7. Orden de uso: guía paso a paso

El orden importa, porque hay dependencias entre los datos: **una marca debe existir antes que un celular**, y **un celular debe existir antes que una venta**.

### Fase 1: preparar el catálogo (como administrador)

**1. Iniciar el programa y entrar como administrador**

En el menú principal elige la opción `1. Iniciar sesión` y escribe:

```
Correo: admin@gmail.com
Contraseña: admin123
```

Aparecerá el **menú de administrador**:

```
==============================
     TECHNOSTORE - ADMIN
==============================
 1. Gestión de celulares
 2. Gestión de marcas
 3. Gestión de clientes
 4. Ventas
 5. Reportes
 0. Cerrar sesión
==============================
```

**2. Crear una marca** → opción `2`, después `1. Registrar marca`

```
Nombre de la marca: Samsung
```

Guarda el **id** que se muestra (normalmente `1`), porque lo vas a necesitar en el paso siguiente.

**3. Crear un celular** → vuelve con `0` y entra en la opción `1. Gestión de celulares`, luego `1. Registrar celular`

El programa pide, en este orden:

| Dato | Ejemplo |
|---|---|
| Id de la marca | `1` (el que te dio al crear la marca) |
| Modelo | `Galaxy A55` |
| Sistema operativo | `Android 14` |
| Gama | `1` para ALTA, `2` para MEDIA, `3` para BAJA |
| Precio | `1500000` (debe ser mayor que 0) |
| Stock inicial | `10` (debe ser mayor que 0) |

> Si no hay ninguna marca registrada, el programa avisa: *"No hay marcas registradas, registre una marca primero"*. Por eso este paso va después del anterior.

**4. Repite si quieres** varios celulares y varias marcas. Puedes ver la lista con `4. Listar celulares` o `4. Listar marcas`.

**5. Cerrar sesión** → `0` para volver y `0` para cerrar sesión.

### Fase 2: comprar (como cliente)

**6. Registrarse como cliente**

En el menú principal elige `2. Registrarse` y llena los datos:

| Dato | Regla |
|---|---|
| Correo | Debe tener formato de correo (`algo@algo.com`) |
| Contraseña | Mínimo 6 caracteres |
| Nombre | No puede estar vacío |
| Identificación | **No puede repetirse** con otro cliente |
| Teléfono | No puede estar vacío |

Al terminar aparece *"Registro exitoso! Ya puede iniciar sesion"*.

> El programa **siempre** registra con rol de cliente. Los administradores no se crean desde aquí.

**7. Iniciar sesión como cliente** → opción `1` con el correo y la contraseña que acabas de registrar.

Aparece el **menú del cliente**:

```
==============================
  TECHNOSTORE - BIENVENIDO
==============================
 1. Ver catálogo
 2. Realizar compra
 3. Ver mis compras
 0. Cerrar sesión
==============================
```

**8. Ver el catálogo** → opción `1`. Aquí ves los **id** de los celulares disponibles y su stock.

**9. Realizar la compra** → opción `2`

| Paso | Qué escribir |
|---|---|
| Id del celular | El id que viste en el catálogo |
| Cantidad | Un número mayor que 0, y que no supere el stock disponible |
| ¿Desea agregar otro celular? | `s` para agregar otro, o **Enter** para terminar |

> **Importante: pulsa Enter para terminar la compra.** No escribas nada más, solo Enter.

**10. Ver el resultado** → opción `3. Ver mis compras` para confirmar que la venta quedó registrada.

**11. Cerrar sesión** → `0`.

### Fase 3: revisar los resultados (como administrador)

**12. Entrar de nuevo como administrador** → opción `1`, con `admin@gmail.com` / `admin123`.

**13. Ver todas las ventas** → opción `4. Ventas`, luego `1. Ver todas las ventas`.

**14. Ver los reportes** → opción `5. Reportes`:

| Opción | Qué muestra |
|---|---|
| `1` | Celulares con stock bajo: los que tienen **menos de 5 unidades** |
| `2` | Top 3 de celulares más vendidos |
| `3` | Ventas totales agrupadas por mes |
| `4` | Genera el archivo `reporte_ventas.dat` con el resumen de todas las ventas |

**15. Comprobar el stock** → con `1. Gestión de celulares`, `4. Listar celulares` puedes verificar que el stock del celular vendido bajó.

---

## 8. Detalle de cada menú

### Menú principal

| Opción | Acción |
|---|---|
| `1` | Iniciar sesión (lleva al menú de admin o de cliente, según el rol) |
| `2` | Registrarse como cliente |
| `0` | Salir del programa |

### Menú de administrador

| Opción | Submenú |
|---|---|
| `1` | Gestión de celulares |
| `2` | Gestión de marcas |
| `3` | Gestión de clientes |
| `4` | Ventas |
| `5` | Reportes |
| `0` | Cerrar sesión |

### Gestión de celulares

| Opción | Acción | Datos que pide |
|---|---|---|
| `1` | Registrar | marca, modelo, sistema operativo, gama, precio, stock |
| `2` | Actualizar | id del celular y todos los datos nuevos |
| `3` | Eliminar | id del celular |
| `4` | Listar | — |
| `0` | Volver | — |

### Gestión de marcas

| Opción | Acción |
|---|---|
| `1` | Registrar marca (solo pide el nombre) |
| `2` | Actualizar marca (id y nombre nuevo) |
| `3` | Eliminar marca (id) |
| `4` | Listar marcas |
| `0` | Volver |

### Gestión de clientes

| Opción | Acción |
|---|---|
| `1` | Actualizar cliente (id, nombre, identificación, teléfono) |
| `2` | Eliminar cliente (id) |
| `3` | Listar clientes |
| `0` | Volver |

> Los clientes no se crean desde el menú de administrador: se crean con la opción `2. Registrarse` del menú principal.

### Menú del cliente

| Opción | Acción |
|---|---|
| `1` | Ver el catálogo de celulares con su id, precio y stock |
| `2` | Realizar una compra |
| `3` | Ver las compras propias (no las de los demás clientes) |
| `0` | Cerrar sesión |

---

## 9. Reglas de negocio y validaciones

### Validaciones en Java (antes de llegar a la base de datos)

| Regla | Dónde |
|---|---|
| El precio debe ser mayor que 0 | `Validaciones.validarDecimalPositivo` |
| El stock debe ser mayor que 0 al registrarlo | `Validaciones.validarEnteroPositivo` |
| El formato del correo se valida al registrarse | `Validaciones.validarCorreo` |
| La contraseña debe tener mínimo 6 caracteres al registrarse | `Validaciones.validarPassword` |
| No se aceptan campos vacíos | `Validaciones.validarTexto` |
| No se permiten cantidades negativas en una compra | `Validaciones.validarEnteroPositivo` |

> En el **inicio de sesión** no se aplican reglas de formato: ahí solo se comprueba que el correo y la contraseña coincidan. Las reglas estrictas son solo para el registro.

### Validaciones en la base de datos

| Regla | Cómo se aplica |
|---|---|
| El correo debe tener formato válido | El procedimiento `registrar_usuario` lo comprueba con una expresión regular |
| No se pueden repetir correos | Restricción `UNIQUE` en `usuarios.correo` |
| No se pueden repetir identificaciones | Restricción `UNIQUE` en `clientes.identificacion` |
| No se pueden repetir nombres de marca | Restricción `UNIQUE` en `marcas.nombre` |
| El precio siempre es positivo | Restricción `CHECK` en `celulares` |
| El stock nunca es negativo | Restricción `CHECK` en `celulares` |
| La cantidad de una venta es positiva | Restricción `CHECK` en `detalle_ventas` |
| No se puede vender más de lo que hay | El procedimiento `agregar_item_venta` comprueba el stock y lanza un error si no alcanza |

### Reglas de las ventas

- Al agregar un celular a una venta, **el stock se descuenta automáticamente**.
- El **IVA del 19 %** se calcula dentro de la base de datos, en el procedimiento `agregar_item_venta`. El valor que se ve en cada línea de la venta ya lo incluye.
- El **total de la venta** se va acumulando a medida que se agregan artículos.
- Si un celular no se puede agregar (por ejemplo, porque no hay stock suficiente), el programa lo avisa con el mensaje **"Algunos celulares no se pudieron agregar a la venta"**.
- El **registro de un cliente es atómico**: el usuario y su ficha de cliente se crean dentro de la misma transacción. Si una de las dos partes falla, se deshace todo y no queda un usuario a medias.

### Reportes

| Reporte | Criterio |
|---|---|
| Celulares con stock bajo | `stock` menor a **5 unidades** |
| Top 3 más vendidos | Los 3 celulares con más unidades vendidas |
| Ventas por mes | Agrupa las ventas por año y mes, con el número de ventas y el total |
| Archivo de reporte | Escribe `reporte_ventas.dat` con el detalle de todas las ventas |

---

## 10. Notas y recomendaciones

| Nota | Detalle |
|---|---|
| **Escribe con el teclado, no pegues texto** | Cada menú usa su propio lector de teclado. Si pegas varias líneas de golpe, el programa puede perder la entrada y quedarse esperando. Escríbelo línea por línea |
| **El reporte se genera en la carpeta actual** | El archivo `reporte_ventas.dat` se crea en la carpeta desde donde ejecutes el programa. El menú lo menciona como `.txt`, pero el archivo que se genera es `.dat` |
| **Las credenciales están en el código** | Por simplicidad del proyecto, los datos de conexión están escritos en `Conexion.java`. En un sistema real se leerían de un archivo de configuración o de variables de entorno |
| **Las contraseñas se guardan sin cifrar** | El proyecto guarda las contraseñas en texto plano, porque el objetivo era practicar JDBC y no seguridad |
| **Un solo administrador** | El sistema tiene un único usuario con rol `ADMIN`, creado por el script de la base de datos. Todos los demás usuarios que se registran son clientes |
| **No hay interfaz gráfica** | Todo el programa es de consola, tal como pedía el enunciado |

---

## Autor

**Daniel Fernando Vargas Sarmiento**

Proyecto desarrollado en Java con MySQL y JDBC.
