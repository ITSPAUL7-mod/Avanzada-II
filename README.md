# 📚 GUÍA COMPLETA DE ESTUDIO - PROGRAMACIÓN AVANZADA 2

> **Inyección de Dependencias, Patrones de Diseño e Interceptores con Quarkus & CDI**

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Quarkus](https://img.shields.io/badge/Quarkus-3.34.3-red?style=flat-square&logo=quarkus)
![CDI](https://img.shields.io/badge/CDI-Jakarta%20EE-blue?style=flat-square)
![Status](https://img.shields.io/badge/Status-Ready%20for%20Exam-brightgreen?style=flat-square)

**Última actualización:** 2026-05-08 | **Rama:** taller12 | **Estudiante:** ITSPAUL7-mod

</div>

---

## 📖 ÍNDICE DE CONTENIDOS

1. [Inyección de Dependencias (CDI)](#1-inyección-de-dependencias-cdi)
2. [Scopes de CDI](#2-scopes-de-cdi)
3. [Patrón Strategy](#3-patrón-strategy-estrategia)
4. [Patrón Selector/Router](#4-patrón-selectorreador)
5. [Interceptores (AOP)](#5-interceptores-aop)
6. [@Priority (Orden de Ejecución)](#6-priority-orden-de-ejecución)
7. [Extensiones](#7-extensiones)
8. [Anotaciones Personalizadas](#8-anotaciones-personalizadas)
9. [Anotaciones de Ciclo de Vida](#9-anotaciones-de-ciclo-de-vida)
10. [Quarkus Framework](#10-quarkus-framework)
11. [Checklist para Interceptores (PIAC)](#11-checklist-para-no-olvidar-interceptores)
12. [Tipos de Lógica de Negocio](#12-tipos-de-lógica-de-negocio-en-pruebas)
13. [Casos de Uso](#13-casos-de-uso---cuándo-usar-qué)
14. [Pasos para Hacer un Interceptor](#14-pasos-para-hacer-un-interceptor)
15. [Template Seguro](#15-template-seguro)
16. [Tabla Resumen Rápida](#16-tabla-resumen-rápida)
17. [Preguntas Probables](#17-preguntas-probables-en-prueba)
18. [Flujo Completo Típico](#18-flujo-completo-típico)
19. [Ejemplo Práctico Completo](#19-ejemplo-práctico-completo)
20. [Consejos Finales](#20-consejos-finales-para-la-prueba)

---

## 1️⃣ INYECCIÓN DE DEPENDENCIAS (CDI)

### ¿Qué es CDI?

| Concepto | Descripción |
|----------|-------------|
| **Container and Dependency Injection** | Sistema que crea y gestiona instancias automáticamente |
| **Automatización** | No usas `new`, el contenedor inyecta las instancias |
| **Desacoplamiento** | Las clases no dependen directamente unas de otras |
| **Testabilidad** | Facilita hacer pruebas unitarias |

### 3 Formas de Inyectar

<details open>
<summary><b>A) POR ATRIBUTO (✅ RECOMENDADA EN PRUEBAS)</b></summary>

```java
@Inject
private NotificadorMail notificador;
```

| Aspecto | Detalle |
|---------|---------|
| ✅ Ventajas | Fácil de leer, simple, directa |
| ❌ Desventajas | Más acoplamiento teórico |
| 📌 Uso | **Más usada en pruebas prácticas** |

</details>

<details>
<summary><b>B) POR CONSTRUCTOR (MENOS ACOPLAMIENTO)</b></summary>

```java
@Inject
public PedidoService(NotificadorMail notificador) {
    this.notificador = notificador;
}
```

| Aspecto | Detalle |
|---------|---------|
| ✅ Ventajas | Explícito, desacoplado, inmutable |
| ❌ Desventajas | Más código |
| 📌 Uso | Proyectos grandes |

</details>

<details>
<summary><b>C) POR SETTER/MÉTODO</b></summary>

```java
@Inject
public void setNotificador(NotificadorMail notificador) {
    this.notificador = notificador;
}
```

| Aspecto | Detalle |
|---------|---------|
| ✅ Ventajas | Flexible, opcional |
| ❌ Desventajas | Menos usado |
| 📌 Uso | Métodos públicos |

</details>

### ✨ Ventajas de CDI

- ✅ **Desacoplamiento** - Las clases no se conocen entre sí
- ✅ **Testabilidad** - Fácil hacer mock en tests
- ✅ **Flexibilidad** - Cambias implementación sin tocar código
- ✅ **Gestión automática** - El contenedor maneja el ciclo de vida
- ✅ **Singleton fácil** - Una única instancia cuando la necesitas

---

## 2️⃣ SCOPES DE CDI

### 📊 Comparativa Rápida

| Scope | Instancias | Duración | Comparte Estado | Uso Típico |
|-------|-----------|----------|-----------------|-----------|
| **@ApplicationScoped** | 1 para toda la app | Inicio a fin | ✅ SÍ | Servicios globales |
| **@Dependent** | N por inyección | Mientras viva inyector | ❌ NO | DTOs, datos |
| **@Singleton** | 1 para toda la app | Inicio a fin | ✅ SÍ | Parecido a ApplicationScoped |

### 🏢 @ApplicationScoped (MÁS USADO)

Una **ÚNICA instancia** para **TODA la aplicación**

```java
@ApplicationScoped
public class NotificadorMail implements Notificador {
    // Una instancia compartida entre todos
}
```

**Cuándo usar:**
- ✅ Servicios globales (NotificadorMail, NotificadorSMS)
- ✅ Repositorios/DAOs (Base de datos)
- ✅ **Estadísticas y contadores globales** ⭐
- ✅ Configuraciones de la app
- ✅ Cachés compartidas
- ✅ Auditoría y logging globales

**Ejemplo con Estadísticas:**
```java
@ApplicationScoped
public class EstadisticasVentasGobales {
    private int totalVentas = 0;        // Se ACUMULA entre operaciones
    private double montoTotal = 0;       // Se ACUMULA
    
    public void registrarVenta(double monto) {
        totalVentas++;                   // Incrementa
        montoTotal += monto;             // Acumula
    }
}
```

### 👤 @Dependent (DEFAULT)

Una **NUEVA instancia** para cada inyección

```java
public class Compra {  // Sin anotación = Dependent implícito
    private String cliente;
    private double total;
}
```

**Cuándo usar:**
- ✅ Objetos de datos (DTOs, Models)
- ✅ Objetos de una solicitud específica
- ✅ Cálculos temporales
- ✅ Objetos que NO necesitan mantener estado

### 🎯 REGLA DE ORO

```
┌─────────────────────────────────────────────────────────┐
│ @ApplicationScoped = Datos/Funcionalidad COMPARTIDA     │
│ @Dependent = Datos ESPECÍFICOS de una operación         │
│                                                          │
│ Si algo debe "recordarse" entre operaciones            │
│ → @ApplicationScoped                                    │
│                                                          │
│ Si algo es temporal/único por operación                │
│ → @Dependent o sin anotación                           │
└─────────────────────────────────────────────────────────┘
```

---

## 3️⃣ PATRÓN STRATEGY (ESTRATEGIA)

### 📋 Definición

> **Define una familia de algoritmos, encapsulalos, y hazlos intercambiables**

### 🔄 Estructura

| Componente | Función |
|-----------|---------|
| **Interface** | Define el contrato común |
| **Implementaciones** | Diferentes estrategias (algoritmos) |
| **Contexto** | Usa una estrategia sin conocerla |

### 💳 Ejemplo: PAGOS

```java
// 1. Interface
public interface PagoEstrategia {
    void realizar(double valor);
}

// 2. Estrategia 1
@ApplicationScoped
public class PagoEfectivo implements PagoEstrategia {
    public void realizar(double valor) {
        System.out.println("💵 Pago en efectivo: $" + valor);
    }
}

// 3. Estrategia 2
@ApplicationScoped
public class PagoTarjetaCredito implements PagoEstrategia {
    public void realizar(double valor) {
        System.out.println("💳 Pago con tarjeta: $" + valor);
    }
}

// 4. Uso (no conoce cuál estrategia es)
public void procesar(PagoEstrategia pago) {
    pago.realizar(100.0);  // Funciona con CUALQUIERA
}
```

### 🎯 3 STRATEGIES EN TU PROYECTO

| Strategy | Implementaciones |
|----------|-----------------|
| **PagoEstrategia** | PagoEfectivo, PagoTarjetaCredito |
| **ComprobanteEstrategia** | ComprobantePDF, Factura |
| **Notificador** | NotificadorMail, NotificadorSMS, NotificadorWhatsapp |

### ✨ Ventajas

- ✅ Fácil agregar nuevas estrategias
- ✅ No modificas código existente (Open/Closed)
- ✅ Cambias comportamiento en runtime
- ✅ Código limpio y mantenible

---

## 4️⃣ PATRÓN SELECTOR/ROUTER

### 💡 Concepto

**Elige dinámicamente CUÁL estrategia usar basado en criterios**

### 📌 Ejemplo en Taller

```java
@ApplicationScoped
public class NotificadorSelector {
    
    @Inject private NotificadorMail mail;
    @Inject private NotificadorSMS sms;
    @Inject private NotificadorWhatsapp whats;
    
    public Notificador seleccionar(double total) {
        if(total > 120)      return mail;       // 📧 Email
        else if(total < 50)  return whats;      // 📱 WhatsApp
        else                 return sms;        // 💬 SMS
    }
}
```

### 🎬 Cómo se USA

```java
@ApplicationScoped
public class PedidoService1 {
    
    @Inject
    private NotificadorSelector selector;
    
    public void registrar(Pedido pedido) {
        // Elige automáticamente qué notificador usar
        Notificador notificador = selector.seleccionar(pedido.getTotal());
        notificador.enviar(pedido.getDestino(), "Pedido registrado");
    }
}
```

### ✨ Ventajas

- ✅ Decisión centralizada
- ✅ Fácil de cambiar criterios
- ✅ Mantiene código limpio

---

## 5️⃣ INTERCEPTORES (AOP)

### 🎯 ¿Qué es un Interceptor?

| Aspecto | Detalle |
|---------|---------|
| **Definición** | Ejecuta código **ANTES y DESPUÉS** de un método |
| **Modificación** | Sin modificar el método original |
| **Patrón** | Aspect-Oriented Programming (AOP) |
| **Ventaja** | No ensucia la lógica de negocio |

### 🔑 Conceptos Clave

```java
@AroundInvoke              // Se ejecuta alrededor del método
context.proceed()          // Ejecuta el método REAL (⚠️ CRÍTICO)
context.getParameters()    // Obtiene los parámetros
context.getMethod().getName()  // Obtiene nombre del método
```

### 🏗️ ESTRUCTURA COMPLETA

#### 1️⃣ CREAR ANOTACIÓN PERSONALIZADA

```java
@InterceptorBinding  // ← CRÍTICA
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MedirTiempo {
}
```

#### 2️⃣ CREAR LA CLASE INTERCEPTOR

```java
@MedirTiempo           // ← Usa la anotación
@Interceptor           // ← OBLIGATORIO ⚠️
@Priority(1)           // ← OBLIGATORIO ⚠️
public class MedirTiempoInterceptor {
    
    @AroundInvoke      // ← OBLIGATORIO ⚠️
    public Object medir(InvocationContext context) throws Exception {
        
        long inicio = System.currentTimeMillis();
        Object resultado = context.proceed();  // ← OBLIGATORIO ⚠️
        long fin = System.currentTimeMillis();
        
        System.out.println("Tiempo: " + (fin - inicio) + "ms");
        return resultado;
    }
}
```

#### 3️⃣ USAR EN TUS MÉTODOS

```java
@ApplicationScoped
public class MiServicio {
    
    @MedirTiempo  // ← Anotación personalizada
    public void procesar(Pedido pedido) {
        System.out.println("Procesando...");
    }
}
```

### 📡 INTERCEPTORES EN TU PROYECTO

<details open>
<summary><b>A) MedirTiempoInterceptor</b></summary>

| Propiedad | Valor |
|-----------|-------|
| **Anotación** | @MedirTiempo |
| **Priority** | 2 (se ejecuta segundo) |
| **Función** | Mide tiempo de ejecución |
| **Uso** | @MedirTiempo en métodos |

```java
@MedirTiempo
@Interceptor
@Priority(2)
public class MedirTiempoInterceptor {
    @AroundInvoke
    public Object medir(InvocationContext context) throws Exception {
        long inicio = System.currentTimeMillis();
        Object resultado = context.proceed();
        long fin = System.currentTimeMillis();
        
        System.out.println("⏱️  Tiempo: " + (fin - inicio) + "ms");
        return resultado;
    }
}
```

</details>

<details>
<summary><b>B) logInterceptor</b></summary>

| Propiedad | Valor |
|-----------|-------|
| **Anotación** | @log |
| **Priority** | 1 (se ejecuta primero) |
| **Función** | Registra logs y parámetros |
| **Uso** | @log en métodos |

```java
@log
@Interceptor
@Priority(1)
public class logInterceptor {
    @AroundInvoke
    public Object medir(InvocationContext context) throws Exception {
        
        System.out.println("📋 LOG: " + context.getMethod().getName());
        Object[] args = context.getParameters();
        
        for(Object arg : args) {
            Venta venta = (Venta) arg;
            System.out.println("Cliente: " + venta.getCliente());
        }
        
        Object resultado = context.proceed();
        return resultado;
    }
}
```

</details>

### 🔄 Orden de Ejecución

```
Método @log @MedirTiempo
    ↓
logInterceptor (@Priority(1)) ★ INICIA
    ↓
MedirTiempoInterceptor (@Priority(2)) ★ INICIA
    ↓
→ MÉTODO REAL se ejecuta ←
    ↓
MedirTiempoInterceptor (@Priority(2)) ★ TERMINA
    ↓
logInterceptor (@Priority(1)) ★ TERMINA
    ↓
Retorna resultado
```

---

## 6️⃣ @PRIORITY (ORDEN DE EJECUCIÓN)

### 📊 Cómo Funciona

| Priority | Ejecución |
|----------|-----------|
| **@Priority(1)** | Se ejecuta **PRIMERO** |
| **@Priority(2)** | Se ejecuta **SEGUNDO** |
| **@Priority(3)** | Se ejecuta **TERCERO** |
| **@Priority(n)** | Orden **ASCENDENTE** |

### 🎯 EN INTERCEPTORES

```java
@log
@Interceptor
@Priority(1)  // ← Se ejecuta PRIMERO
public class logInterceptor { }

@MedirTiempo
@Interceptor
@Priority(2)  // ← Se ejecuta SEGUNDO
public class MedirTiempoInterceptor { }
```

### 🛍️ EN EXTENSIONES (Descuentos)

```java
@ApplicationScoped
@Priority(1)
public class DescuentoIVA implements Descuento {
    // Se aplica PRIMERO
}

@ApplicationScoped
@Priority(4)
public class DescuentoBlackFriday implements Descuento {
    // Se aplica ÚLTIMO
}
```

### ✨ Cómo se Usa

```java
@ApplicationScoped
public class ProcesadorCompraService {
    
    @Inject
    private Instance<Descuento> descuentos;
    
    public void procesar(Compra compra) {
        double total = compra.getSubTotal();
        // Se aplican EN ORDEN DE @Priority
        for(Descuento des: descuentos) {
            total = des.aplicar(total);
        }
        compra.setTotal(total);
    }
}
```

---

## 7️⃣ EXTENSIONES

### 💡 ¿Qué son?

**Múltiples clases implementan la misma interfaz, se seleccionan según criterios y se ordenan con @Priority**

### 🎯 Ejemplos en Taller

<details open>
<summary><b>A) Descuentos</b></summary>

```java
public interface Descuento {
    double aplicar(double valor);
}

@ApplicationScoped
@Priority(1)
public class DescuentoIVA implements Descuento {
    public double aplicar(double valor) {
        return valor * 0.85;  // 15% descuento
    }
}

@ApplicationScoped
@Priority(4)
public class DescuentoBlackFriday implements Descuento {
    public double aplicar(double valor) {
        return valor * 0.60;  // 40% descuento
    }
}
```

</details>

<details>
<summary><b>B) Notificadores</b></summary>

```java
public interface Notificador {
    void enviar(String destino, String mensaje);
}

@ApplicationScoped
public class NotificadorMail implements Notificador { }

@ApplicationScoped
public class NotificadorSMS implements Notificador { }

@ApplicationScoped
public class NotificadorWhatsapp implements Notificador { }
```

</details>

<details>
<summary><b>C) Pagos</b></summary>

```java
public interface PagoEstrategia {
    void realizar(double valor);
}

@ApplicationScoped
public class PagoEfectivo implements PagoEstrategia { }

@ApplicationScoped
public class PagoTarjetaCredito implements PagoEstrategia { }
```

</details>

---

## 8️⃣ ANOTACIONES PERSONALIZADAS

### 🏗️ Crear una Anotación Personalizada

```java
@InterceptorBinding              // ← CRÍTICA
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MiAnotacion {
}
```

### 📋 Anotaciones Clave

| Anotación | Significado | Obligatoria |
|-----------|-----------|-----------|
| **@InterceptorBinding** | Marca como interceptor | ✅ SÍ (en interceptores) |
| **@Target** | Dónde se aplica (TYPE, METHOD) | ✅ SÍ |
| **@Retention** | Cuándo se procesa (RUNTIME) | ✅ SÍ |

### 📌 Anotaciones en Taller

```java
// A) @MedirTiempo
@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MedirTiempo { }

// B) @log
@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface log { }
```

---

## 9️⃣ ANOTACIONES DE CICLO DE VIDA

### 📚 Anotaciones Usadas en CDI

| Anotación | Función | Ejemplo |
|-----------|---------|---------|
| **@ApplicationScoped** | Scope: 1 instancia para toda app | Servicios globales |
| **@Dependent** | Scope: Nueva instancia por inyección | DTOs |
| **@Inject** | Inyecta dependencia | `@Inject private Service s;` |
| **@InterceptorBinding** | Marca anotación como interceptor | Anotaciones personalizadas |
| **@Interceptor** | Marca clase como interceptor | Clases de interceptor |
| **@AroundInvoke** | Método alrededor del otro | Métodos interceptores |
| **@Priority(n)** | Orden de ejecución | Interceptores/extensiones |
| **@Target** | Dónde se aplica | Anotaciones |
| **@Retention** | Cuándo se procesa | Anotaciones |
| **@Unremovable** | Mantiene bean si no se usa | Beans especiales |

---

## 🔟 QUARKUS FRAMEWORK

### 🚀 ¿Qué es Quarkus?

| Aspecto | Detalle |
|--------|--------|
| **Tipo** | Framework moderno de Java |
| **Base** | CDI integrado |
| **Optimización** | Para microservicios y cloud |
| **Ventaja** | Inicio muy rápido |

### 📦 Estructura Básica

```java
@QuarkusMain
public class Main {
    
    public static void main(String... args) {
        Quarkus.run(App.class, args);
    }
    
    public static class App implements QuarkusApplication {
        
        @Inject
        private MiServicio miServicio;
        
        @Override
        public int run(String... args) throws Exception {
            miServicio.procesar();
            return 0;
        }
    }
}
```

### 📋 Tu Proyecto

| Propiedad | Valor |
|-----------|-------|
| **Framework** | Quarkus 3.34.3 |
| **Java** | 21 |
| **Build Tool** | Maven |
| **CDI** | Integrado automáticamente |
| **Rama Taller** | taller12 |

---

## 1️⃣1️⃣ CHECKLIST PARA NO OLVIDAR INTERCEPTORES

### 🔤 MNEMOTECNIA: **"PIAC"**

```
P = Personalizada (Crear @interface con @InterceptorBinding)
I = Interceptor (Poner @Interceptor en la clase)
A = AroundInvoke (Poner @AroundInvoke en el método)
C = Call proceed() (context.proceed() adentro)

FRASE: "Para Implementar Aspectos Críticos, usa PIAC"
```

### ✅ CHECKLIST ANTES DE ESCRIBIR

```
☐ 1. ¿Creé la ANOTACIÓN PERSONALIZADA?
   └─ @InterceptorBinding
   └─ @Target({ElementType.TYPE, ElementType.METHOD})
   └─ @Retention(RetentionPolicy.RUNTIME)

☐ 2. ¿Anotés LA CLASE con @Interceptor?
   └─ @MiAnotacion
   └─ @Interceptor  ← CRÍTICO
   └─ @Priority(n)  ← CRÍTICO

☐ 3. ¿Anotés EL MÉTODO con @AroundInvoke?
   └─ @AroundInvoke  ← CRÍTICO

☐ 4. ¿Llamaste context.proceed()?
   └─ Object resultado = context.proceed();  ← CRÍTICO

☐ 5. ¿Anotaste EL MÉTODO DE NEGOCIO?
   └─ @MiAnotacion en el método que quiero interceptar

SI TODAS SON ✅ → Tu interceptor funcionará ✅
```

### ❌ ERRORES COMUNES A EVITAR

```
❌ Olvidar @Interceptor en la clase
❌ Olvidar @AroundInvoke en el método
❌ Olvidar context.proceed() adentro
❌ Olvidar @Priority(n)
❌ No anotar el método con @MiAnotacion
❌ Olvidar @InterceptorBinding en la anotación
❌ Olvidar @Target y @Retention en la anotación
```

---

## 1️⃣2️⃣ TIPOS DE LÓGICA DE NEGOCIO EN PRUEBAS

### 📊 Tabla de Escenarios

| Tipo | Descripción | Elementos | Dificultad |
|------|-----------|-----------|-----------|
| **Compras/Ventas** | Sistema con descuentos y estadísticas | Strategy + @Priority + Selector | ⭐⭐ |
| **Notificaciones** | Envío multicanal según criterios | Strategy + Selector + Interceptor | ⭐⭐⭐ |
| **Rendimiento** | Monitoreo de tiempo de ejecución | Interceptor + Medición | ⭐⭐ |
| **Estadísticas** | Contadores globales | @ApplicationScoped | ⭐ |
| **Auditoría** | Registro de operaciones | Interceptor + Logs | ⭐⭐ |
| **Validación** | Verificar datos antes de procesar | Interceptor + Excepciones | ⭐⭐ |
| **Caché** | Almacenamiento temporal | @ApplicationScoped + HashMap | ⭐⭐ |
| **Cálculos** | Algoritmos complejos | Strategy | ⭐⭐⭐ |

---

## 1️⃣3️⃣ CASOS DE USO - CUÁNDO USAR QUÉ

### 🎯 APPLICATIONSCOPED

```java
✅ Servicios globales (NotificadorMail, NotificadorSMS)
✅ Repositorios/DAOs (Base de datos)
✅ Estadísticas y contadores globales
✅ Configuraciones de la app
✅ Cachés compartidas
✅ Auditoría y logging globales

EJEMPLO:
@ApplicationScoped
public class EstadisticasVentasGobales {
    private int totalVentas = 0;      // Se ACUMULA
    private double montoTotal = 0;     // Se ACUMULA
}
```

### 👤 DEPENDENT

```java
✅ Objetos de datos (DTOs, Models)
✅ Objetos de una solicitud específica
✅ Cálculos temporales
✅ Objetos que NO necesitan mantener estado

EJEMPLO:
public class Compra {  // Sin anotación = Dependent
    private String cliente;
    private double total;
}
```

### 🔍 INTERCEPTORES

```java
✅ Medir tiempo de ejecución
✅ Registrar logs
✅ Auditoría de operaciones
✅ Validación antes de ejecutar
✅ Manejo de excepciones
✅ Cache de resultados
```

### 📦 STRATEGY PATTERN

```java
✅ Diferentes algoritmos intercambiables
✅ Múltiples formas de hacer algo
✅ Selección en runtime
✅ Fácil agregar nuevas formas

EJEMPLOS:
- Diferentes formas de PAGO (efectivo, tarjeta)
- Diferentes COMPROBANTES (PDF, papel, XML)
- Diferentes NOTIFICACIONES (email, SMS, WhatsApp)
```

### 🎚️ SELECTOR

```java
✅ Elegir Strategy según criterios
✅ Lógica de decisión centralizada
✅ Mantener código limpio

EJEMPLO:
public Notificador seleccionar(double total) {
    if(total > 120)      return mail;
    else if(total < 50)  return whats;
    else                 return sms;
}
```

---

## 1️⃣4️⃣ PASOS PARA HACER UN INTERCEPTOR

### 📋 ORDEN EXACTO EN PRUEBA

#### **PASO 1: CREAR LA ANOTACIÓN PERSONALIZADA**

```java
@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MiInterceptor {
}
```

#### **PASO 2: CREAR LA CLASE INTERCEPTOR**

```java
@MiInterceptor              // Anotación personalizada
@Interceptor                // OBLIGATORIO
@Priority(1)                // OBLIGATORIO
public class MiInterceptorImpl {
    
    @AroundInvoke           // OBLIGATORIO
    public Object interceptar(InvocationContext context) 
        throws Exception {
        
        System.out.println("ANTES");
        Object resultado = context.proceed();  // OBLIGATORIO
        System.out.println("DESPUÉS");
        
        return resultado;
    }
}
```

#### **PASO 3: USAR EN TUS MÉTODOS**

```java
@ApplicationScoped
public class MiServicio {
    
    @MiInterceptor      // Anotación personalizada
    public void procesar(Pedido pedido) {
        System.out.println("Método real");
    }
}
```

#### **PASO 4: VERIFICAR CHECKLIST**

```
☐ ¿Anotación tiene @InterceptorBinding?
☐ ¿Clase tiene @Interceptor?
☐ ¿Clase tiene @Priority(n)?
☐ ¿Método tiene @AroundInvoke?
☐ ¿Llama a context.proceed()?
☐ ¿Método de negocio está anotado?
```

---

## 1️⃣5️⃣ TEMPLATE SEGURO

### 📋 COPIA Y PEGA - SIEMPRE FUNCIONA

```java
// 1️⃣ ANOTACIÓN (siempre igual)
@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MiNombre {
}

// 2️⃣ INTERCEPTOR
@MiNombre
@Interceptor
@Priority(1)
public class MiNombreInterceptor {
    
    @AroundInvoke
    public Object interceptar(InvocationContext context) throws Exception {
        
        System.out.println("ANTES: " + context.getMethod().getName());
        
        Object resultado = context.proceed();
        
        System.out.println("DESPUÉS: " + context.getMethod().getName());
        
        return resultado;
    }
}

// 3️⃣ USAR
@ApplicationScoped
public class MiServicio {
    
    @MiNombre
    public void miMetodo() {
        System.out.println("Método real");
    }
}
```

---

## 1️⃣6️⃣ TABLA RESUMEN RÁPIDA

### 📊 Anotaciones

| Anotación | Dónde | Obligatoria | Función |
|-----------|-------|-----------|---------|
| @Inject | Atributo/param | ✅ Inyecta dependencia | Inyecta instancia |
| @ApplicationScoped | Clase | ✅ CDI | Scope: 1 instancia |
| @Dependent | Clase | ❌ DEFAULT | Scope: N instancias |
| @Interceptor | Clase interceptor | ✅ CRÍTICA | Marca como interceptor |
| @AroundInvoke | Método interceptor | ✅ CRÍTICA | Se ejecuta alrededor |
| @Priority(n) | Clase interceptor | ✅ CRÍTICA | Orden de ejecución |
| @InterceptorBinding | Anotación | ✅ CRÍTICA | Marca como interceptor |
| @Target | Anotación | ✅ CRÍTICA | Dónde se aplica |
| @Retention | Anotación | ✅ CRÍTICA | Cuándo se procesa |

### 🏘️ Scopes

| Scope | Instancias | Cuándo Usar |
|-------|-----------|-----------|
| @ApplicationScoped | 1 para app | Servicios, estadísticas |
| @Dependent | N nuevas | DTOs, datos temporales |
| @Singleton | 1 para app | Parecido a ApplicationScoped |

### 🎯 Patrones

| Patrón | Función |
|--------|---------|
| **Strategy** | Múltiples algoritmos intercambiables |
| **Selector/Router** | Elegir qué Strategy usar |
| **Interceptor (AOP)** | Código antes/después sin modificar |
| **Extension** | Múltiples implementaciones |

---

## 1️⃣7️⃣ PREGUNTAS PROBABLES EN PRUEBA

### 🎓 TEÓRICAS

<details open>
<summary><b>Expande para ver preguntas teóricas</b></summary>

| Pregunta | Respuesta |
|----------|-----------|
| **¿Qué es CDI?** | Container que gestiona el ciclo de vida de beans y sus dependencias |
| **¿Diferencia entre @ApplicationScoped y @Dependent?** | ApplicationScoped = 1 instancia para toda app, Dependent = N instancias |
| **¿Qué es Strategy Pattern?** | Define algoritmos intercambiables |
| **¿Para qué sirve context.proceed()?** | Ejecuta el método real dentro de un interceptor |
| **¿Cómo ordenas interceptores?** | Con @Priority(n) - menor número = se ejecuta primero |
| **¿Qué es un Interceptor?** | Ejecuta código antes y después de un método sin modificarlo |
| **¿Cómo se crea un Interceptor?** | @InterceptorBinding + @Interceptor + @AroundInvoke |
| **¿Qué pasa sin context.proceed()?** | El método real no se ejecuta |
| **¿Para qué sirve @Priority?** | Define el orden de ejecución de interceptores |
| **¿Qué es una extensión?** | Múltiples implementaciones de la misma interfaz |

</details>

### 💻 PRÁCTICAS (ESPERADAS)

```
"Crea un sistema de _____ que _____ con _____"

Posibles espacios:
├─ "sistema de compras que aplique descuentos con interceptores"
├─ "sistema de notificaciones que envíe por múltiples canales"
├─ "sistema de auditoría que registre todas las operaciones"
├─ "sistema de inventario que mida tiempo de procesamiento"
└─ "sistema de pagos que valide transacciones"
```

---

## 1️⃣8️⃣ FLUJO COMPLETO TÍPICO

### 🔄 Ejecución Step-by-Step

```
1️⃣ MAIN INICIA
   └─ Quarkus.run(App.class)

2️⃣ CONTENEDOR CDI INICIA
   └─ Crea todas las instancias @ApplicationScoped

3️⃣ INYECTA DEPENDENCIAS
   └─ @Inject carga las instancias

4️⃣ LLAMA MÉTODO ANOTADO
   └─ @MiInterceptor en el método

5️⃣ INTERCEPTOR(es) SE EJECUTAN (en orden @Priority)
   └─ Priority(1) primero
   └─ Priority(2) segundo

6️⃣ MÉTODO REAL SE EJECUTA
   └─ context.proceed() en el interceptor

7️⃣ INTERCEPTOR TERMINA
   └─ Retorna resultado

8️⃣ RESULTADO SE RETORNA
   └─ Puede haber más procesamiento
```

### 📌 Ejemplo Práctico

```
Método anotado: @log @MedirTiempo

logInterceptor (@Priority(1))
├─ Registra entrada: "LOG: procesarVenta"
├─ Llama context.proceed()
│
MedirTiempoInterceptor (@Priority(2))
├─ Registra tiempo inicio
├─ Llama context.proceed()
│
→ MÉTODO REAL
  ├─ Ejecuta lógica de negocio
  ├─ Procesa compra
  
MedirTiempoInterceptor retorna
├─ Calcula tiempo: "Tiempo: 152ms"
│
logInterceptor retorna
├─ Registra salida: "LOG: fin"

Resultado retorna al Main
```

---

## 1️⃣9️⃣ EJEMPLO PRÁCTICO COMPLETO

### 🎯 Escenario: Sistema de Tienda Online

Un sistema completo que integra **TODOS** los conceptos:

<details open>
<summary><b>Ver código completo</b></summary>

```java
// ════════════════════════════════════════════════════════════════
// 1️⃣ ANOTACIONES PERSONALIZADAS
// ════════════════════════════════════════════════════════════════

@InterceptorBinding
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidarCompra { }

@InterceptorBinding
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditoriaVenta { }

// ════════════════════════════════════════════════════════════════
// 2️⃣ INTERCEPTORES
// ════════════════════════════════════════════════════════════════

@ValidarCompra
@Interceptor
@Priority(1)
public class ValidacionInterceptor {
    @AroundInvoke
    public Object validar(InvocationContext context) throws Exception {
        System.out.println("🔍 Validando compra...");
        Object resultado = context.proceed();
        System.out.println("✅ Compra válida\n");
        return resultado;
    }
}

@AuditoriaVenta
@Interceptor
@Priority(2)
public class AuditoriaInterceptor {
    @AroundInvoke
    public Object auditar(InvocationContext context) throws Exception {
        System.out.println("📋 Registrando en auditoría...");
        long inicio = System.currentTimeMillis();
        
        Object resultado = context.proceed();
        
        long duracion = System.currentTimeMillis() - inicio;
        System.out.println("Auditoría: Operación completada en " + duracion + "ms\n");
        return resultado;
    }
}

// ════════════════════════════════════════════════════════════════
// 3️⃣ INTERFACES (STRATEGY)
// ════════════════════════════════════════════════════════════════

public interface Pago {
    void ejecutar(double monto);
}

public interface Notificacion {
    void enviar(String cliente, String mensaje);
}

public interface Descuento {
    double aplicar(double valor);
}

// ════════════════════════════════════════════════════════════════
// 4️⃣ IMPLEMENTACIONES - PAGOS
// ════════════════════════════════════════════════════════════════

@ApplicationScoped
public class PagoEfectivo implements Pago {
    @Override
    public void ejecutar(double monto) {
        System.out.println("💵 Pago en efectivo: $" + monto);
    }
}

@ApplicationScoped
public class PagoTarjeta implements Pago {
    @Override
    public void ejecutar(double monto) {
        System.out.println("💳 Pago con tarjeta: $" + monto);
    }
}

// ════════════════════════════════════════════════════════════════
// 5️⃣ IMPLEMENTACIONES - NOTIFICACIONES
// ════════════════════════════════════════════════════════════════

@ApplicationScoped
public class NotificacionEmail implements Notificacion {
    @Override
    public void enviar(String cliente, String mensaje) {
        System.out.println("📧 Email a " + cliente + ": " + mensaje);
    }
}

@ApplicationScoped
public class NotificacionSMS implements Notificacion {
    @Override
    public void enviar(String cliente, String mensaje) {
        System.out.println("📱 SMS a " + cliente + ": " + mensaje);
    }
}

// ════════════════════════════════════════════════════════════════
// 6️⃣ IMPLEMENTACIONES - DESCUENTOS (@Priority)
// ════════════════════════════════════════════════════════════════

@ApplicationScoped
@Priority(1)
public class DescuentoIVA implements Descuento {
    @Override
    public double aplicar(double valor) {
        System.out.println("Aplicando descuento IVA (15%)");
        return valor * 0.85;
    }
}

@ApplicationScoped
@Priority(2)
public class DescuentoPremium implements Descuento {
    @Override
    public double aplicar(double valor) {
        System.out.println("Aplicando descuento Premium (10%)");
        return valor * 0.90;
    }
}

// ════════════════════════════════════════════════════════════════
// 7️⃣ SELECTOR
// ════════════════════════════════════════════════════════════════

@ApplicationScoped
public class VentaSelector {
    
    @Inject private PagoEfectivo pagoEfectivo;
    @Inject private PagoTarjeta pagoTarjeta;
    @Inject private NotificacionEmail email;
    @Inject private NotificacionSMS sms;
    
    public Pago seleccionarPago(String metodo) {
        return metodo.equals("tarjeta") ? pagoTarjeta : pagoEfectivo;
    }
    
    public Notificacion seleccionarNotificacion(String tipo) {
        return tipo.equals("sms") ? sms : email;
    }
}

// ════════════════════════════════════════════════════════════════
// 8️⃣ ESTADÍSTICAS (@ApplicationScoped)
// ════════════════════════════════════════════════════════════════

@ApplicationScoped
public class EstadisticasVentas {
    
    private int totalCompras = 0;
    private double montoTotal = 0.0;
    
    public void registrar(double monto) {
        totalCompras++;
        montoTotal += monto;
    }
    
    public void mostrar() {
        System.out.println("\n═══════════════════════════════");
        System.out.println("📊 ESTADÍSTICAS GLOBALES");
        System.out.println("═══════════════════════════════");
        System.out.println("Total Compras: " + totalCompras);
        System.out.println("Monto Total: $" + montoTotal);
        System.out.println("═══════════════════════════════\n");
    }
}

// ════════════════════════════════════════════════════════════════
// 9️⃣ SERVICIO DE NEGOCIO
// ════════════════════════════════════════════════════════════════

@ApplicationScoped
public class VentaService {
    
    @Inject private VentaSelector selector;
    @Inject private EstadisticasVentas estadisticas;
    @Inject private Instance<Descuento> descuentos;
    
    @ValidarCompra
    @AuditoriaVenta
    public void procesarVenta(String cliente, double monto, 
                              String metodoPago, String tipoNotif) {
        
        System.out.println("╔══════════════════════════════╗");
        System.out.println("║  PROCESANDO VENTA            ║");
        System.out.println("╚══════════════════════════════╝");
        System.out.println("Cliente: " + cliente);
        System.out.println("Monto Original: $" + monto + "\n");
        
        // Aplica descuentos EN ORDEN
        double montoFinal = monto;
        for(Descuento desc : descuentos) {
            montoFinal = desc.aplicar(montoFinal);
        }
        
        System.out.println("Monto Final: $" + montoFinal + "\n");
        
        // Selecciona e ejecuta pago
        Pago pago = selector.seleccionarPago(metodoPago);
        pago.ejecutar(montoFinal);
        
        // Selecciona e envía notificación
        Notificacion notif = selector.seleccionarNotificacion(tipoNotif);
        notif.enviar(cliente, "Tu compra de $" + montoFinal + " fue procesada");
        
        // Registra estadística
        estadisticas.registrar(montoFinal);
        
        System.out.println("══════════════════════════════\n");
    }
}

// ════════════════════════════════════════════════════════════════
// 🔟 CLASE PRINCIPAL
// ════════════════════════════════════════════════════════════════

@QuarkusMain
public class Main {
    
    public static void main(String[] args) {
        Quarkus.run(App.class, args);
    }
    
    public static class App implements QuarkusApplication {
        
        @Inject private VentaService ventaService;
        @Inject private EstadisticasVentas estadisticas;
        
        @Override
        public int run(String... args) throws Exception {
            
            System.out.println("\n╔════════════════════════════════╗");
            System.out.println("║  SISTEMA DE TIENDA ONLINE     ║");
            System.out.println("╚════════════════════════════════╝\n");
            
            // Venta 1: Efectivo + Email
            ventaService.procesarVenta("Juan", 150.0, "efectivo", "email");
            
            // Venta 2: Tarjeta + SMS
            ventaService.procesarVenta("María", 200.0, "tarjeta", "sms");
            
            // Venta 3: Efectivo + Email
            ventaService.procesarVenta("Carlos", 100.0, "efectivo", "email");
            
            // Muestra estadísticas finales
            estadisticas.mostrar();
            
            return 0;
        }
    }
}
```

### 📤 SALIDA ESPERADA

```
╔════════════════════════════════╗
║  SISTEMA DE TIENDA ONLINE     ║
╚════════════════════════════════╝

🔍 Validando compra...
✅ Compra válida

📋 Registrando en auditoría...
╔══════════════════════════════╗
║  PROCESANDO VENTA            ║
╚══════════════════════════════╝
Cliente: Juan
Monto Original: $150.0

Aplicando descuento IVA (15%)
Aplicando descuento Premium (10%)
Monto Final: $114.75

💵 Pago en efectivo: $114.75
📧 Email a Juan: Tu compra de $114.75 fue procesada
══════════════════════════════

Auditoría: Operación completada en 5ms

[... más compras ...]

═══════════════════════════════
📊 ESTADÍSTICAS GLOBALES
═══════════════════════════════
Total Compras: 3
Monto Total: $321.0
═══════════════════════════════
```

</details>

---

## 2️⃣0️⃣ CONSEJOS FINALES PARA LA PRUEBA

### 🎯 ANTES DE LA PRUEBA

- ✅ Lee esta guía 2-3 veces completa
- ✅ Memoriza la mnemotecnia "PIAC"
- ✅ Practica creando 3-4 interceptores
- ✅ Copia el template seguro a tu memoria
- ✅ Ten el checklist a mano
- ✅ Duerme bien la noche anterior

### 💡 DURANTE LA PRUEBA

```
1. Lee la prueba COMPLETA primero
2. Identifica qué tipo de lógica es (compras, notificaciones, etc.)
3. PRIMERO: Crea las interfaces
4. SEGUNDO: Crea las implementaciones (@ApplicationScoped)
5. TERCERO: Si hay interceptores, crea anotaciones
6. CUARTO: Crea los interceptores (usa template)
7. QUINTO: Crea los selectores si necesita
8. SEXTO: Crea el servicio principal
9. SÉPTIMO: Anotaciones en los métodos
10. OCTAVO: Verificar checklist
```

### 🚨 ERRORES QUE NO DEBES COMETER

```
❌ Olvidar @ApplicationScoped en servicios compartidos
❌ Olvidar @Interceptor en clase interceptor
❌ Olvidar context.proceed() en interceptor
❌ Olvidar @Priority en interceptores
❌ No anotar métodos con @MiInterceptor
❌ Olvidar @InterceptorBinding en anotación
❌ Mezclar logic en el método y en el interceptor
❌ Crear sin interfaces (strategy)
```

### 🎓 SI NO RECUERDAS ALGO

```
Usa el acrónimo "PIAC":
├─ P = Personalizada (anotación)
├─ I = Interceptor (clase)
├─ A = AroundInvoke (método)
└─ C = Call proceed() (en el método)

O revisa el TEMPLATE SEGURO (sección 15)
```

### ⚡ TRUCOS RÁPIDOS

```
¿No recuerdas dónde va @Priority?
→ Va en la clase del Interceptor: @Priority(1)

¿No recuerdas si es @proceed() o context.execute()?
→ Es context.proceed() - mnemotecnia: "¡PROCEED a ejecutar!"

¿No recuerdas scopes?
→ ApplicationScoped = comparte, Dependent = no comparte

¿No recuerdas orden de Priority?
→ MENOR número = se ejecuta PRIMERO (1 antes que 2)
```

---

## 📚 REFERENCIAS RÁPIDAS

### 📖 En tu Taller

- `PedidoService1.java` - Inyección + Selector
- `NotificadorSelector.java` - Selector Pattern
- `MedirTiempoInterceptor.java` - Interceptor ejemplo
- `logInterceptor.java` - Interceptor con logs
- `ProcesadorCompraService.java` - @Priority en extensiones
- `EstadisticasVentasGobales.java` - @ApplicationScoped ejemplo

### 🔗 Links

- [Jakarta CDI Docs](https://jakarta.ee/specifications/cdi/)
- [Quarkus CDI](https://quarkus.io/guides/cdi)
- [Interceptors](https://quarkus.io/guides/interceptors)

---

## ✨ RESUMEN FINAL

### 🎯 LOS 10 CONCEPTOS MÁS IMPORTANTES

1. **CDI** - Contenedor que gestiona beans
2. **@ApplicationScoped** - Una instancia compartida
3. **@Dependent** - Nueva instancia por inyección
4. **Strategy Pattern** - Algoritmos intercambiables
5. **Selector** - Elige qué Strategy usar
6. **Interceptor** - Código alrededor de métodos
7. **@AroundInvoke** - Se ejecuta alrededor
8. **context.proceed()** - Ejecuta método real
9. **@Priority** - Orden de ejecución
10. **PIAC** - Mnemotecnia para no olvidar

### ⚠️ LOS 5 ERRORES MÁS COMUNES

1. ❌ Olvidar `@Interceptor` en clase
2. ❌ Olvidar `context.proceed()` en método
3. ❌ Olvidar `@Priority` en interceptor
4. ❌ No anotar método de negocio
5. ❌ Confundir scopes (ApplicationScoped vs Dependent)

### ✅ LOS 5 PASOS SIEMPRE

1. ✅ Crear interfaces
2. ✅ Crear implementaciones (@ApplicationScoped)
3. ✅ Crear anotaciones (si hay interceptores)
4. ✅ Crear interceptores
5. ✅ Usar anotaciones en métodos

---

<div align="center">

### 🎓 ¡LISTO PARA LA PRUEBA!

**Memoriza PIAC, practica los ejemplos, y tendrás éxito.**

---

*Última actualización: 2026-05-08*  
*Estudiante: ITSPAUL7-mod*  
*Rama: taller12*

</div>
