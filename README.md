# 🚀 Guía de Repaso Rápido — Programación Avanzada II

Este repositorio contiene la arquitectura base para la gestión de proyectos e investigadores utilizando **JPA**, **PostgreSQL**, **Interceptores**, **`parallelStream()`** y **Endpoints REST (`List`)**.

---

## 📋 Tabla de Contenidos
1. [Estructura del Proyecto](#1-estructura-del-proyecto)
2. [Base de Datos (PostgreSQL)](#2-base-de-datos-postgresql)
3. [Entidades y Relaciones JPA (`@OneToOne` y `@ManyToMany`)](#3-entidades-y-relaciones-jpa)
4. [Consultas JPQL en el DAO (`LIKE` y `COUNT`)](#4-consultas-jpql-en-el-dao)
5. [Interceptor de Tiempo y Nombre de Método](#5-interceptor-de-tiempo-y-nombre-de-método)
6. [Procesamiento en Paralelo con `parallelStream()`](#6-procesamiento-en-paralelo-con-parallelstream)
7. [Endpoints REST para Postman (`List`)](#7-endpoints-rest-para-postman)

---

## 1. Estructura del Proyecto

Organiza el código siempre por capas/paquetes para asegurar la separación de responsabilidades:

```text
src/main/java/ec/edu/uce/
 ├── entity/          # Modelos de la Base de Datos
 │    ├── Proyecto.java
 │    ├── Investigador.java
 │    └── FichaMedica.java
 ├── dao/             # Consultas JPA / JPQL
 │    ├── ProyectoDAO.java
 │    └── InvestigadorDAO.java
 ├── interceptor/     # Medición de rendimiento
 │    └── TiempoInterceptor.java
 ├── service/         # Lógica de negocio y parallelStream
 │    └── ProyectoService.java
 └── rest/            # Endpoints exponiendo List<T> para Postman
      └── ProyectoResource.java

@Entity
public class Investigador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cedula;
    private String nombre;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ficha_medica_id")
    private FichaMedica fichaMedica;

    // Getters y Setters
}

@Entity
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private String nombre;
    private Double presupuesto;

@ManyToMany
    @JoinTable(
        name = "proyecto_investigador",
        joinColumns = @JoinColumn(name = "proyecto_id"),
        inverseJoinColumns = @JoinColumn(name = "investigador_id")
    )
    private List<Investigador> investigadores = new ArrayList<>();

    // Getters y Setters
}

@ApplicationScoped
public class ProyectoDAO {

    @Inject
    EntityManager em;

    // 1. Consulta LIKE (Buscar por coincidencia de texto)
    public List<Proyecto> buscarPorNombre(String texto) {
        String jpql = "SELECT p FROM Proyecto p WHERE LOWER(p.nombre) LIKE LOWER(:texto)";
        return em.createQuery(jpql, Proyecto.class)
                 .setParameter("texto", "%" + texto + "%")
                 .getResultList();
    }

    // 2. Consulta COUNT (Contar total de registros)
    public Long contarTotalProyectos() {
        String jpql = "SELECT COUNT(p) FROM Proyecto p";
        return em.createQuery(jpql, Long.class)
                 .getSingleResult();
    }
}

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
public class TiempoInterceptor {

    @AroundInvoke
    public Object medirTiempoYMetodo(InvocationContext context) throws Exception {
        String nombreMetodo = context.getMethod().getName();
        long inicio = System.currentTimeMillis();

        // Ejecutar el método real
        Object resultado = context.proceed();

        long duracion = System.currentTimeMillis() - inicio;

        System.out.println("==========================================");
        System.out.println("Método Interceptado : " + nombreMetodo);
        System.out.println("Tiempo de Ejecución : " + duracion + " ms");
        System.out.println("==========================================");

        return resultado;
    }
}


@ApplicationScoped
public class ProyectoService {

    @Inject
    ProyectoDAO proyectoDAO;

    @Interceptors(TiempoInterceptor.class) // <--- Activa el Interceptor
    public List<String> obtenerProyectosFiltradosEnParalelo(Double presupuestoMinimo) {
        List<Proyecto> lista = proyectoDAO.obtenerTodos();

        // Filtra y procesa en paralelo, retornando List<String>
        return lista.parallelStream()
                .filter(p -> p.getPresupuesto() > presupuestoMinimo)
                .map(p -> {
                    System.out.println("Procesando: " + p.getNombre() + " | Hilo: " + Thread.currentThread().getName());
                    return p.getNombre();
                })
                .toList(); // Retorna List<String> de forma limpia
    }
}

@Path("/proyectos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProyectoResource {

    @Inject
    ProyectoService proyectoService;

    // GET: http://localhost:8080/proyectos/buscar?nombre=ia
    @GET
    @Path("/buscar")
    public List<Proyecto> buscarPorNombre(@QueryParam("nombre") String nombre) {
        return proyectoService.buscarProyectos(nombre);
    }

    // GET: http://localhost:8080/proyectos/conteo
    @GET
    @Path("/conteo")
    public Long contarProyectos() {
        return proyectoService.obtenerConteoProyectos();
    }

    // GET: http://localhost:8080/proyectos/costosos?monto=5000
    @GET
    @Path("/costosos")
    public List<String> obtenerProyectosCostosos(@QueryParam("monto") Double monto) {
        return proyectoService.obtenerProyectosFiltradosEnParalelo(monto);
    }
}