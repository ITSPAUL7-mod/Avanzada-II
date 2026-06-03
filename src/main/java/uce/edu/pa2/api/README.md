# 📚 Guía de Desarrollo - API REST con Quarkus

Esta guía documenta los conceptos fundamentales implementados en este proyecto: **Interceptadores**, **Modelado de Base de Datos en Java** y **Tipos de Consultas (Queries)**.

---

## 📋 Tabla de Contenidos

1. [Arquitectura del Proyecto](#arquitectura-del-proyecto)
2. [Interceptadores en Quarkus](#interceptadores-en-quarkus)
3. [Modelado de Base de Datos](#modelado-de-base-de-datos)
4. [Tipos de Consultas (Queries)](#tipos-de-consultas-queries)
5. [Flujo de Ejecución](#flujo-de-ejecución)

---

## Arquitectura del Proyecto

Este proyecto sigue una arquitectura **Arquitectura Hexagonal (Puertos y Adaptadores)** dividida en 3 capas:

```
src/main/java/uce/edu/pa2/api/
├── domain/                       # Dominio de negocio (independiente)
│   ├── model/                    # Entidades JPA
│   │   └── Productos.java        # Modelo de datos
│   └── repository/               # Interfaces del repositorio
│       └── ProductosRepository.java
├── infraestructure/              # Implementación técnica
│   ├── repository/               # Implementaciones del repositorio
│   │   └── ProductosRepositoryImpl.java
│   └── interceptor/              # Interceptadores y auditoría
│       ├── AlertaPrecioInterceptor.java
│       ├── Interceptora.java
│       ├── Auditable.java
│       └── VerificarPrecio.java
└── application/                  # Lógica de aplicación
    └── service/                  # Servicios de negocio
        └── ProductoService.java
```

---

## Interceptadores en Quarkus

### ¿Qué es un Interceptador?

Un **interceptador** es un mecanismo que intercepta llamadas a métodos antes y después de su ejecución, permitiendo:

- ✅ **Auditoría**: Registrar quién, cuándo y qué operaciones se realizan
- ✅ **Validación**: Verificar datos antes de procesarlos
- ✅ **Alertas**: Notificar sobre eventos críticos
- ✅ **Timing**: Medir el tiempo de ejecución
- ✅ **Seguridad**: Validar permisos antes de ejecutar métodos

### Componentes Clave

#### 1. **Anotación Marcadora** (`@Auditable`, `@VerificarPrecio`)

```java
package uce.edu.pa2.api.infraestructure.interceptor;

import jakarta.interceptor.InterceptorBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@InterceptorBinding
@Target({TYPE, METHOD})
@Retention(RUNTIME)
public @interface Auditable {
}
```

**Uso**: Esta anotación marca qué clases o métodos deben ser interceptados.

#### 2. **El Interceptador de Auditoría** (`Interceptora.java`)

```java
package uce.edu.pa2.api.infraestructure.interceptor;

import java.util.Arrays;
import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Auditable
@Interceptor
@Priority(1) // Prioridad 1 (se ejecuta primero)
public class Interceptora {

    @AroundInvoke
    public Object interceptarMetodo(InvocationContext context) throws Exception {
        
        // 1️⃣ ANTES de la ejecución
        String nombreMetodo = context.getMethod().getName();
        String parametros = Arrays.toString(context.getParameters());
        System.out.println("====== [AUDITORÍA INTERCEPTOR] ======");
        System.out.println("-> Ejecutando método: " + nombreMetodo);
        System.out.println("-> Parámetros enviados: " + parametros);
        
        long tiempoInicio = System.currentTimeMillis();

        try {
            // 2️⃣ EJECUTA el método original
            Object resultado = context.proceed(); 
            
            // 3️⃣ DESPUÉS de la ejecución exitosa
            long tiempoFin = System.currentTimeMillis();
            System.out.println("-> Método '" + nombreMetodo + "' finalizó con éxito en " + 
                             (tiempoFin - tiempoInicio) + " ms");
            return resultado;

        } catch (Exception e) {
            // ❌ Si ocurre un error
            System.out.println("❌ ERROR detectado en método '" + nombreMetodo + "': " + 
                             e.getMessage());
            throw e; // Re-lanzar la excepción
        } finally {
            System.out.println("=====================================");
        }
    }
}
```

**¿Cómo funciona?**
- `@AroundInvoke`: Intercepta la llamada al método
- `context.proceed()`: Ejecuta el método original
- Se registra el tiempo de inicio y fin para calcular duración

#### 3. **Interceptador de Validación de Precios** (`AlertaPrecioInterceptor.java`)

```java
package uce.edu.pa2.api.infraestructure.interceptor;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import uce.edu.pa2.api.domain.model.Productos;

@VerificarPrecio
@Interceptor
@Priority(2) // Prioridad 2 (se ejecuta después del de auditoría)
public class AlertaPrecioInterceptor {

    @AroundInvoke
    public Object verificar(InvocationContext context) throws Exception {
        
        // Obtener los parámetros del método
        Object[] parametros = context.getParameters();

        for (Object param : parametros) {
            // Verificar si es un Producto
            if (param instanceof Productos) {
                Productos prod = (Productos) param;
                
                // VALIDACIÓN: Si el precio es mayor a 1000
                if (prod.getPrecio() != null && prod.getPrecio() > 1000.0) {
                    System.out.println("\n🚨 [ALERTA DE SEGURIDAD/NEGOCIO] 🚨");
                    System.out.println("⚠️ NOTIFICACIÓN: Se intenta registrar un producto de ALTO VALOR.");
                    System.out.println("⚠️ Detalle: " + prod.getNombre() + " - Precio: $" + 
                                     prod.getPrecio());
                    System.out.println("📨 [Mensaje enviado al Administrador de Finanzas]\n");
                    
                    // Opcional: Denegar la operación
                    // throw new IllegalArgumentException("Precio demasiado alto sin aprobación");
                }
            }
        }

        // Continuar con la ejecución
        return context.proceed();
    }
}
```

**¿Cómo funciona?**
- Verifica cada parámetro del método
- Si es un `Productos`, examina el precio
- Si el precio > 1000, genera una alerta
- Usa `@Priority(2)` para ejecutarse después del interceptor de auditoría

#### 4. **Activar Interceptadores en la Implementación**

Para que los interceptadores funcionen, deben aplicarse a la clase que los necesita:

```java
@ApplicationScoped
@Transactional
@Auditable  // ← Activa el interceptor de auditoría
public class ProductosRepositoryImpl implements ProductosRepository {
    
    @Inject
    private EntityManager em;

    @Override
    @VerificarPrecio  // ← Activa el interceptor de precios (opcional por método)
    public void crearProducto(Productos productos) {
        this.em.merge(productos);
    }
    
    // Resto de métodos...
}
```

---

## Modelado de Base de Datos

### Configuración de Base de Datos

#### `application.properties`

```properties
# PostgreSQL Configuration
quarkus.datasource.db-kind=postgresql
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/nombre_base_datos
quarkus.datasource.username=usuario
quarkus.datasource.password=contraseña

# JPA/Hibernate Configuration
quarkus.jpa.dialect=org.hibernate.dialect.PostgreSQL10Dialect
quarkus.hibernate-orm.database.generation=update
quarkus.hibernate-orm.log.sql=true
quarkus.hibernate-orm.sql-load-script=no-file
```

### Entidad JPA: Productos

```java
package uce.edu.pa2.api.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "producto")
@NamedQuery(
    name = "Productos.buscarPorCategoria", 
    query = "SELECT p FROM Productos p WHERE p.categoria = :categoria1"
)
@NamedQuery(
    name = "Productos.contar", 
    query = "SELECT COUNT(p) FROM Productos p"
)
public class Productos {

    @Id
    @SequenceGenerator(
        name = "seq_producto_generador", 
        sequenceName = "seq_producto", 
        allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_producto_generador")
    @Column(name = "id_producto")
    private Integer id;

    @Column(name = "nombre_producto", nullable = false)
    private String nombre;
    
    @Column(name = "categoria_producto")
    private String categoria;

    @Column(name = "precio_producto")
    private Double precio;

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    @Override
    public String toString() {
        return "Productos [id=" + id + ", nombre=" + nombre + ", categoria=" + 
               categoria + ", precio=" + precio + "]";
    }
}
```

**Anotaciones principales:**
- `@Entity`: Marca la clase como una tabla persistente
- `@Table`: Define el nombre de la tabla en BD
- `@Id`: Clave primaria
- `@SequenceGenerator`: Genera IDs automáticamente usando secuencias
- `@Column`: Mapea a columnas específicas
- `@NamedQuery`: Define consultas reutilizables

---

## Tipos de Consultas (Queries)

### 1. **TypedQuery** (Consultas simples con JPQL)

```java
// En ProductosRepositoryImpl.java

@Override
public List<Productos> seleccionarProductos() {
    TypedQuery<Productos> query = this.em.createQuery(
        "SELECT p FROM Productos p",
        Productos.class
    );
    return query.getResultList();
}

@Override
public List<Productos> selecionarPorPrecio(Double precio) {
    TypedQuery<Productos> query = this.em.createQuery(
        "SELECT p FROM Productos p WHERE p.precio > :precio1",
        Productos.class
    );
    query.setParameter("precio1", precio);
    return query.getResultList();
}
```

**Características:**
- Seguridad de tipos (genera excepciones en tiempo de compilación)
- Fácil de leer
- Parámetros nombrados con `:nombreParam`

### 2. **NamedQuery** (Consultas predefinidas en la entidad)

```java
// Definición en Productos.java
@NamedQuery(
    name = "Productos.buscarPorCategoria", 
    query = "SELECT p FROM Productos p WHERE p.categoria = :categoria1"
)
@NamedQuery(
    name = "Productos.contar", 
    query = "SELECT COUNT(p) FROM Productos p"
)

// Uso en ProductosRepositoryImpl.java
@Override
public List<Productos> selecionarProductosPorCategoria(String categoria) {
    TypedQuery<Productos> query = this.em.createNamedQuery(
        "Productos.buscarPorCategoria", 
        Productos.class
    );
    query.setParameter("categoria1", categoria);
    return query.getResultList();
}

@Override
public Long contarProductos() {
    TypedQuery<Long> query = this.em.createNamedQuery(
        "Productos.contar", 
        Long.class
    );
    return query.getSingleResult();
}
```

**Ventajas:**
- Consultas centralizadas en la entidad
- Mejor rendimiento (compiladas al inicializar)
- Fácil mantenimiento

### 3. **Native Query** (SQL directo)

```java
@Override
public List<Productos> seleccionarPorCategoriaNative(String categoria) {
    Query query = this.em.createNativeQuery(
        "SELECT * FROM producto WHERE categoria_producto LIKE :categoria", 
        Productos.class
    );
    query.setParameter("categoria", "%" + categoria + "%");
    return query.getResultList();
}
```

**Casos de uso:**
- Consultas complejas específicas de PostgreSQL
- Funciones nativas de la BD
- Performance optimizada para casos específicos

⚠️ **Riesgo:** SQL Injection si no usas parámetros

### 4. **Criteria API Query** (Consultas dinámicas)

```java
@Override
public List<Productos> seleccionarDinamica(String nombre, Double preciomax, Double preciomin) {
    
    // 1. Crear CriteriaBuilder y CriteriaQuery
    CriteriaBuilder cb = this.em.getCriteriaBuilder();
    CriteriaQuery<Productos> query = cb.createQuery(Productos.class);
    Root<Productos> root = query.from(Productos.class);

    // 2. Construir condiciones dinámicas
    List<Predicate> condiciones = new ArrayList<>();

    if (nombre != null) {
        Predicate p1 = cb.like(root.get("nombre"), "%" + nombre + "%");
        condiciones.add(p1);
    }

    if (preciomax != null && preciomin != null) {
        Predicate p2 = cb.between(root.get("precio"), preciomin, preciomax);
        condiciones.add(p2);
    }

    // 3. Aplicar todas las condiciones con AND
    query.select(root).where(condiciones.toArray(new Predicate[0]));

    // 4. Ejecutar
    TypedQuery<Productos> query1 = this.em.createQuery(query);
    return query1.getResultList();
}
```

**Ventajas:**
- Consultas 100% dinámicas
- Sin SQL Injection
- Condiciones opcionales

**Ejemplo de uso:**
```java
// Encuentra productos que:
// - Contengan "P" en el nombre
// - Cuyo precio esté entre 1000 y 10000
List<Productos> resultado = productosRepository.seleccionarDinamica("P", 1000.0, 10000.0);
```

---

## Comparativa de Tipos de Queries

| Tipo | Seguridad | Flexibilidad | Complejidad | Caso de Uso |
|------|-----------|--------------|-------------|------------|
| **TypedQuery** | Alta | Media | Baja | Consultas simples y directas |
| **NamedQuery** | Alta | Baja | Baja | Consultas reutilizables |
| **Native Query** | Media | Muy Alta | Alta | Queries muy específicas de BD |
| **Criteria API** | Alta | Muy Alta | Media | Búsquedas dinámicas con múltiples filtros |

---

## Flujo de Ejecución

### Ejemplo: Crear un Producto

```
1. Cliente llama a ProductoService.guardar(producto)
   ↓
2. [INTERCEPTOR AUDITORÍA] Registra inicio
   ├─ Método: crearProducto
   ├─ Parámetros: [Productos{...}]
   ├─ Hora inicio: XX:XX:XX
   
3. [INTERCEPTOR ALERTA] Verifica el precio
   ├─ ¿Precio > 1000? 
   │  ├─ SÍ: 🚨 Envía alerta
   │  └─ NO: Continúa silenciosamente
   
4. ProductosRepositoryImpl.crearProducto(producto)
   ├─ em.merge(producto)  → Guarda en BD
   
5. [INTERCEPTOR AUDITORÍA] Registra fin
   ├─ Duración: 45 ms
   ├─ Estado: ÉXITO ✅
   
6. Retorna al servicio
```

### Salida en Consola

```
====== [AUDITORÍA INTERCEPTOR] ======
-> Ejecutando método: crearProducto
-> Parámetros enviados: [Productos[id=null, nombre=Teclado Mecánico, categoria=Electronica, precio=75.50]]

🚨 [ALERTA DE SEGURIDAD/NEGOCIO] 🚨
⚠️ NOTIFICACIÓN: Se intenta registrar un producto de ALTO VALOR.
⚠️ Detalle: Teclado Mecánico - Precio: $75.50
📨 [Mensaje enviado al Administrador de Finanzas]

-> Método 'crearProducto' finalizó con éxito en 23 ms
=====================================
```

---

## Archivos Importantes

| Archivo | Propósito |
|---------|-----------|
| `Productos.java` | Entidad JPA, define la tabla y NamedQueries |
| `ProductosRepository.java` | Interfaz del repositorio (contrato) |
| `ProductosRepositoryImpl.java` | Implementación con queries |
| `Interceptora.java` | Interceptor de auditoría |
| `AlertaPrecioInterceptor.java` | Interceptor de validación |
| `Auditable.java` | Anotación para marcar métodos auditables |
| `VerificarPrecio.java` | Anotación para marcar validación de precios |
| `ProductoService.java` | Servicio de lógica de negocio |

---

## Mejores Prácticas

### ✅ Recomendaciones

1. **Interceptadores**: Úsalos para auditoría, no para lógica de negocio
2. **Queries**: 
   - TypedQuery → Consultas simples
   - NamedQuery → Consultas frecuentes
   - Criteria API → Filtros dinámicos
   - Native Query → Última opción (solo si es necesario)
3. **Parámetros**: Siempre usa parámetros nombrados, nunca concatenes strings
4. **Transacciones**: Marca con `@Transactional` las operaciones de escritura
5. **Inyección**: Usa `@Inject` para inyectar `EntityManager`

### ❌ Evitar

- No usar `em.remove(id)` directamente - debes obtener la entidad primero
- No mezclar SQL nativo con JPQL en la misma consulta
- No olvidar los parámetros de paginación en consultas grandes
- No usar interceptadores para validaciones complejas (mejor en servicios)

---

## Testing

### Ejemplo de prueba unitaria

```java
@QuarkusTest
public class ProductoServiceTest {
    
    @Inject
    ProductoService productoService;
    
    @Test
    public void testGuardarProducto() {
        Productos p = new Productos();
        p.setNombre("Laptop");
        p.setCategoria("Electronica");
        p.setPrecio(1500.0);
        
        // Ejecuta y verifica
        productoService.guardar(p);
        
        // Observa los logs del interceptor
    }
}
```

---

## Recursos Útiles

- [Quarkus Documentation](https://quarkus.io/guides/)
- [JPA/Hibernate Guide](https://quarkus.io/guides/hibernate-orm)
- [Interceptores en Quarkus](https://quarkus.io/guides/cdi-reference#interceptors)
- [Criteria API](https://www.baeldung.com/hibernate-criteria-queries)

---

## 📞 Preguntas Frecuentes

**P: ¿Por qué los interceptadores tienen @Priority?**
R: Determina el orden de ejecución. Mayor número = se ejecuta después.

**P: ¿Qué es context.proceed()?**
R: Ejecuta el método original. Si no lo llamas, el método nunca se ejecuta.

**P: ¿Puedo denegar una operación en un interceptador?**
R: Sí, lanzando una excepción: `throw new IllegalArgumentException(...)`

**P: ¿Cuándo usar Criteria API?**
R: Cuando tienes múltiples filtros opcionales que se combinan dinámicamente.

