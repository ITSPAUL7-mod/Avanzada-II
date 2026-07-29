# Sistema de Reserva de Vehículos — Quarkus

Proyecto backend en Java 25 + Quarkus (RESTEasy Reactive + Jackson, Hibernate ORM con Panache, JDBC PostgreSQL) para un sistema de reserva de vehículos con 7 entidades relacionadas.

## 1. Requisitos previos

- JDK 25
- Maven 3.9+
- PostgreSQL corriendo en `localhost:5432`

Crea la base de datos:

```sql
CREATE DATABASE reservas_vehiculos;
```

## 2. Configuración (`src/main/resources/application.properties`)

```properties
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=postgres
quarkus.datasource.password=postgres
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/reservas_vehiculos

quarkus.hibernate-orm.database.generation=update
quarkus.hibernate-orm.log.sql=true

quarkus.http.port=8080
quarkus.http.cors=true
```

Ajusta `username`, `password` y `jdbc.url` a tu entorno local.

## 3. Ejecutar el proyecto

```bash
mvn quarkus:dev
```

La API queda disponible en `http://localhost:8080`.

## 4. Estructura de paquetes

```
uce.edu.ec
 ├── domain.model              -> Entidades JPA (extienden PanacheEntityBase, id de tipo Integer con SequenceGenerator)
 ├── infraestructure.repository -> XxxxRepositoryImpl implementando PanacheRepositoryBase<Entidad, Integer>
 ├── application.service.interceptor  -> Interceptores CDI y anotaciones de auditoría
 ├── application.service        -> XxxxService con la lógica de negocio y validaciones
 └── web.resource                -> XxxxResources con los endpoints JAX-RS
```

No se usan clases DTO: los endpoints reciben y devuelven directamente las entidades. Para las entidades con relaciones (`Vehiculo`, `ReservaVehiculo`, `Factura`) el JSON de entrada envía el objeto relacionado anidado, indicando solo el dato necesario para ubicarlo (`id`, `cedula`, `cedulaVendedor` o `placa`); el servicio se encarga de buscar la entidad real en la base de datos antes de guardar.

## 5. Endpoints disponibles

Cada entidad expone las mismas 5 rutas, siguiendo el mismo patrón:

| Método | Ruta                              | Acción                          |
|--------|------------------------------------|----------------------------------|
| GET    | `/{entidad}/porId/{id}`            | Buscar por id                    |
| GET    | `/{entidad}/todos`                 | Listar todos                     |
| POST   | `/{entidad}/guardar`               | Crear                            |
| PUT    | `/{entidad}/actualizar/{id}`       | Actualizar                       |
| DELETE | `/{entidad}/eliminar/{id}`         | Eliminar                         |

Recursos:

- `/usuarios`
- `/vendedores`
- `/sucursales`
- `/vehiculos` (además `GET /vehiculos/porPlaca/{placa}`)
- `/reservas`
- `/auditoria`
- `/estado-disponibilidad`

## 6. Orden de creación en Postman (respeta las FK)

Crea los recursos en este orden exacto para no violar restricciones de clave foránea.

### 6.1 POST `http://localhost:8080/usuarios/guardar`
```json
{
  "cedula": "1712345678",
  "nombre": "Ana Torres",
  "correo": "ana.torres@uce.edu.ec"
}
```

### 6.2 POST `http://localhost:8080/vendedores/guardar`
```json
{
  "cedulaVendedor": "1798765432",
  "nombre": "Carlos Pazmiño",
  "telefono": "0991234567"
}
```

### 6.3 POST `http://localhost:8080/sucursales/guardar`
```json
{
  "nombre": "Sucursal Norte",
  "ciudad": "Quito",
  "direccion": "Av. 6 de Diciembre N34-120"
}
```

### 6.4 POST `http://localhost:8080/categorias-vehiculo/guardar`
```json
{
  "nombre": "SUV",
  "precioPorDia": 45.00
}
```

### 6.5 POST `http://localhost:8080/vehiculos/guardar`
Envía el `id` de la categoría (paso 6.4) y de la sucursal (paso 6.3) dentro del objeto anidado.
```json
{
  "placa": "PBX-1234",
  "marca": "Toyota",
  "modelo": "RAV4",
  "anio": 2023,
  "estadoDisponibilidad": "DISPONIBLE",
  "categoriaVehiculo": { "id": 1 },
  "sucursal": { "id": 1 }
}
```

### 6.6 POST `http://localhost:8080/reservas/guardar`
El servicio busca cada relación por su campo único (`cedula`, `cedulaVendedor`, `placa`), valida que el vehículo exista y esté `DISPONIBLE`, calcula el `total` según los días y el precio de la categoría, y lo cambia a `RESERVADO`.
```json
{
  "fechaReserva": "2026-07-19",
  "fechaInicio": "2026-07-20",
  "fechaFin": "2026-07-25",
  "usuario": { "cedula": "1712345678" },
  "vendedor": { "cedulaVendedor": "1798765432" },
  "vehiculo": { "placa": "PBX-1234" }
}
```

## 7. Validaciones de negocio implementadas

- `VehiculoService.crearVehiculo` valida que la categoría y la sucursal indicadas existan (`404` si no).
- `ReservaVehiculoService.crearReservaVehiculo` valida, en este orden: existencia del usuario por `cedula`, existencia del vendedor por `cedulaVendedor`, existencia del vehículo por `placa`, y que su `estadoDisponibilidad` sea `DISPONIBLE`. Si alguna entidad no existe responde `404`; si el vehículo no está disponible o las fechas son inválidas responde `400`.
- Al crear una reserva, el vehículo pasa automáticamente a `RESERVADO`; al eliminarla o marcarla como `CANCELADA`/`FINALIZADA` vuelve a `DISPONIBLE`.
- `FacturaService.crearFactura` valida que la reserva exista y que no tenga ya una factura asociada (relación `@OneToOne`).
