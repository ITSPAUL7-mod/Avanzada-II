================================================================================
   GUÍA COMPLETA DE ESTUDIO - PROGRAMACIÓN AVANZADA 2
   Sistema de Inyección de Dependencias, Patrones de Diseño e Interceptores
================================================================================

FECHA: 2026-05-08
ESTUDIANTE: ITSPAUL7-mod
FRAMEWORK: Quarkus 3.34.3
JAVA: 21
RAMA: taller12

================================================================================
                    1. INYECCIÓN DE DEPENDENCIAS (CDI)
================================================================================

¿QUÉ ES CDI?
   • Container and Dependency Injection
   • Contenedor que crea y gestiona instancias de clases automáticamente
   • No usas "new", el contenedor inyecta las instancias
   • Desacopla el código
   • Hace más fácil hacer pruebas (testing)

3 FORMAS DE INYECTAR:

   A) POR ATRIBUTO (MÁS SIMPLE) ← RECOMENDADA EN PRUEBAS
   ─────────────────────────────
   @Inject
   private NotificadorMail notificador;
   
   ✓ Fácil de leer
   ✗ Más acoplamiento teórico
   • Más usada en pruebas prácticas

   B) POR CONSTRUCTOR (MENOS ACOPLAMIENTO)
   ──────────────────────────────────────
   @Inject
   public PedidoService(NotificadorMail notificador) {
       this.notificador = notificador;
   }
   
   ✓ Explícito y desacoplado
   ✗ Más código

   C) POR SETTER/MÉTODO
   ────────────────────
   @Inject
   public void setNotificador(NotificadorMail notificador) {
       this.notificador = notificador;
   }
   
   ✓ Flexible
   ✗ Menos usado

VENTAJAS DE CDI:
   ✓ Desacoplamiento
   ✓ Testabilidad
   ✓ Flexibilidad
   ✓ Gestión automática de ciclo de vida
   ✓ Una única instancia si es necesario

================================================================================
                         2. SCOPES DE CDI
================================================================================

@ApplicationScoped (MÁS USADO EN EL CURSO)
──────────────────────────────────────────
   • Una ÚNICA instancia para TODA la aplicación
   • Se crea cuando inicia la app
   • Se destruye cuando finaliza la app
   • La misma instancia para TODOS
   • COMPARTE ESTADO entre operaciones

   EJEMPLO EN TALLER:
   @ApplicationScoped
   public class NotificadorMail { }
   
   @ApplicationScoped
   public class EstadisticasVentasGobales {
       private int totalVentas = 0;  // Se ACUMULA
       private double montoTotal = 0;
   }

   CUÁNDO USAR:
   ✓ Servicios globales (NotificadorMail, NotificadorSMS)
   ✓ Repositorios/DAOs (Base de datos)
   ✓ Estadísticas y contadores globales
   ✓ Configuraciones de la app
   ✓ Cachés
   ✓ Auditoría y logging globales

@Dependent (DEFAULT)
─────────────────────
   • Nueva INSTANCIA para cada @Inject
   • Vive mientras viva quien lo inyectó
   • NO comparte estado
   • Cada objeto es independiente

   EJEMPLO:
   public class Compra {  // Sin anotación = Dependent implícito
       private String cliente;
       private double total;
   }

   CUÁNDO USAR:
   ✓ Objetos de datos (DTOs, Models)
   ✓ Objetos de una solicitud específica
   ✓ Cálculos temporales
   ✓ Objetos que no necesitan mantener estado

@Singleton
──────────
   • Parecido a @ApplicationScoped
   • Más explícito
   • Menos usado en pruebas

REGLA DE ORO:
┌────────────────────────────────────────────────────────┐
│ @ApplicationScoped = Datos/Funcionalidad COMPARTIDA    │
│ @Dependent = Datos ESPECÍFICOS de una operación        │
│                                                         │
│ Si algo debe "recordarse" entre operaciones            │
│ → @ApplicationScoped                                   │
│                                                         │
│ Si algo es temporal/único por operación                │
│ → @Dependent o sin anotación                           │
└────────────────────────────────────────────────────────┘

================================================================================
                     3. PATRÓN STRATEGY (ESTRATEGIA)
================================================================================

¿QUÉ ES?
   • Define una familia de algoritmos
   • Los encapsula
   • Los hace intercambiables
   • El cliente puede elegir cuál usar

ESTRUCTURA:
   1. Interface: Define el contrato
   2. Implementaciones: Diferentes estrategias
   3. Contexto: Usa una estrategia

EJEMPLO EN TALLER - PAGOS:
───────────────────────────
   // Interface
   public interface PagoEstrategia {
       void realizar(double valor);
   }
   
   // Estrategia 1
   @ApplicationScoped
   public class PagoEfectivo implements PagoEstrategia {
       public void realizar(double valor) {
           System.out.println("Pago en efectivo: $" + valor);
       }
   }
   
   // Estrategia 2
   @ApplicationScoped
   public class PagoTarjetaCredito implements PagoEstrategia {
       public void realizar(double valor) {
           System.out.println("Pago con tarjeta: $" + valor);
       }
   }
   
   // Uso
   public void procesar(PagoEstrategia pago) {
       pago.realizar(100.0);  // Usa CUALQUIER estrategia
   }

VENTAJAS:
   ✓ Fácil agregar nuevas estrategias
   ✓ No modificas código existente
   ✓ Cambias comportamiento en runtime
   ✓ Código limpio y mantenible

3 STRATEGIES EN TU PROYECTO:

   1) PagoEstrategia
      - PagoEfectivo
      - PagoTarjetaCredito
   
   2) ComprobanteEstrategia
      - ComprobantePDF
      - Factura
   
   3) Notificador
      - NotificadorMail
      - NotificadorSMS
      - NotificadorWhatsapp

================================================================================
                    4. PATRÓN SELECTOR/ROUTER
================================================================================

¿QUÉ ES?
   • Elige dinámicamente CUÁL estrategia usar
   • Basado en criterios (monto, tipo, cliente, etc.)
   • NO es un patrón oficial, es una implementación local

EJEMPLO EN TALLER:
──────────────────
   @ApplicationScoped
   public class NotificadorSelector {
       
       @Inject private NotificadorMail mail;
       @Inject private NotificadorSMS sms;
       @Inject private NotificadorWhatsapp whats;
       
       public Notificador seleccionar(double total) {
           if(total > 120)      return mail;       // Email
           else if(total < 50)  return whats;      // WhatsApp
           else                 return sms;        // SMS
       }
   }

CÓMO SE USA:
───────────
   @ApplicationScoped
   public class PedidoService1 {
       
       @Inject
       private NotificadorSelector selector;
       
       public void registrar(Pedido pedido) {
           Notificador notificador = selector.seleccionar(pedido.getTotal());
           notificador.enviar(pedido.getDestino(), "Pedido registrado");
       }
   }

VENTAJAS:
   ✓ Decisión centralizada
   ✓ Fácil de cambiar criterios
   ✓ Mantiene código limpio

================================================================================
                     5. INTERCEPTORES (AOP)
================================================================================

¿QUÉ ES UN INTERCEPTOR?
   • Ejecuta código ANTES y DESPUÉS de un método
   • Sin modificar el método original
   • Aspecto Oriented Programming (AOP)
   • Limpio, no ensucia la lógica de negocio

CONCEPTOS CLAVE:
   @AroundInvoke → Se ejecuta alrededor del método
   context.proceed() → Ejecuta el método REAL (CRÍTICO)
   context.getParameters() → Obtiene los parámetros
   context.getMethod().getName() → Obtiene nombre del método

ESTRUCTURA DE UN INTERCEPTOR:

   1️⃣ CREAR ANOTACIÓN PERSONALIZADA
   ─────────────────────────────────
   @InterceptorBinding
   @Target({ElementType.TYPE, ElementType.METHOD})
   @Retention(RetentionPolicy.RUNTIME)
   public @interface MiInterceptor {
   }

   2️⃣ CREAR LA CLASE INTERCEPTOR
   ──────────────────────────────
   @MiInterceptor              // ← Usa la anotación
   @Interceptor                // ← OBLIGATORIO
   @Priority(1)                // ← OBLIGATORIO (orden)
   public class MiInterceptorImpl {
       
       @AroundInvoke           // ← OBLIGATORIO
       public Object interceptar(InvocationContext context) 
           throws Exception {
           
           System.out.println("ANTES del método");
           Object resultado = context.proceed();  // ← OBLIGATORIO
           System.out.println("DESPUÉS del método");
           
           return resultado;
       }
   }

   3️⃣ USAR EN TUS MÉTODOS
   ──────────────────────
   @ApplicationScoped
   public class MiServicio {
       
       @MiInterceptor  // ← Anotación personalizada
       public void miMetodo() {
           System.out.println("Método real");
       }
   }

INTERCEPTORES EN TU PROYECTO:

   A) MedirTiempoInterceptor
      ├─ Anotación: @MedirTiempo
      ├─ Priority: 2 (se ejecuta segundo)
      ├─ Función: Mide tiempo de ejecución
      └─ Uso: @MedirTiempo en métodos
      
      @MedirTiempo
      @Interceptor
      @Priority(2)
      public class MedirTiempoInterceptor {
          @AroundInvoke
          public Object medir(InvocationContext context) throws Exception {
              long inicio = System.currentTimeMillis();
              Object resultado = context.proceed();
              long fin = System.currentTimeMillis();
              
              System.out.println("Tiempo: " + (fin - inicio) + "ms");
              return resultado;
          }
      }

   B) logInterceptor
      ├─ Anotación: @log
      ├─ Priority: 1 (se ejecuta primero)
      ├─ Función: Registra logs y parámetros
      └─ Uso: @log en métodos
      
      @log
      @Interceptor
      @Priority(1)
      public class logInterceptor {
          @AroundInvoke
          public Object medir(InvocationContext context) throws Exception {
              
              System.out.println("LOG: " + context.getMethod().getName());
              Object[] args = context.getParameters();
              
              for(Object arg : args) {
                  Venta venta = (Venta) arg;
                  System.out.println("Cliente: " + venta.getCliente());
                  System.out.println("Total: " + venta.getTotal());
              }
              
              Object resultado = context.proceed();
              return resultado;
          }
      }

ORDEN DE EJECUCIÓN CON @Priority:
──────────────────────────────────
   @Priority(1) ← Se ejecuta PRIMERO
   @Priority(2) ← Se ejecuta SEGUNDO
   @Priority(n) ← Orden ascendente

   Método @log @MedirTiempo
         ↓
   logInterceptor (@Priority(1)) INICIA
         ↓
   MedirTiempoInterceptor (@Priority(2)) INICIA
         ↓
   MÉTODO REAL se ejecuta
         ↓
   MedirTiempoInterceptor (@Priority(2)) TERMINA
         ↓
   logInterceptor (@Priority(1)) TERMINA
         ↓
   Retorna resultado

================================================================================
                      6. @PRIORITY (ORDEN)
================================================================================

¿QUÉ ES @Priority?
   • Define el orden de ejecución de interceptores
   • Número menor = se ejecuta primero
   • Número mayor = se ejecuta último
   • CRÍTICA en interceptores

EN INTERCEPTORES:
─────────────────
   @Priority(1)  → Se ejecuta PRIMERO
   @Priority(2)  → Se ejecuta SEGUNDO
   @Priority(3)  → Se ejecuta TERCERO

EN EXTENSIONES (Descuentos):
────────────────────────────
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

EJEMPLO EN TALLER - ProcesadorCompraService:
─────────────────────────────────────────────
   @ApplicationScoped
   public class ProcesadorCompraService {
       
       @Inject
       private Instance<Descuento> descuentos;
       
       public void procesar(Compra compra){
           double total = compra.getSubTotal();
           for(Descuento des: descuentos){  // Se aplican en orden
               total = des.aplicar(total);
           }
           compra.setTotal(total);
       }
   }

================================================================================
                      7. EXTENSIONES
================================================================================

¿QUÉ SON EXTENSIONES?
   • Múltiples clases implementan la misma interfaz
   • Se seleccionan según criterios
   • Ordenadas con @Priority
   • Parecido a Strategy pero con múltiples

EJEMPLOS EN TALLER:

   A) Descuentos
      ├─ Descuento interface
      ├─ DescuentoIVA (@Priority(1))
      ├─ DescuentoBlackFriday (@Priority(4))
      ├─ DescuentoISD (@Priority(2))
      └─ DescuentoSOLCA (@Priority(3))
      
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

   B) Notificadores
      ├─ Notificador interface
      ├─ NotificadorMail
      ├─ NotificadorSMS
      └─ NotificadorWhatsapp

   C) Pagos
      ├─ PagoEstrategia interface
      ├─ PagoEfectivo
      └─ PagoTarjetaCredito

================================================================================
                  8. ANOTACIONES PERSONALIZADAS
================================================================================

CREAR UNA ANOTACIÓN PERSONALIZADA:
──────────────────────────────────
   @InterceptorBinding              // ← CRÍTICA para interceptores
   @Target({ElementType.TYPE, ElementType.METHOD})
   @Retention(RetentionPolicy.RUNTIME)
   public @interface MiAnotacion {
   }

ANOTACIONES CLAVE:

   @InterceptorBinding
      ├─ Para interceptores
      ├─ Marca una anotación como interceptor
      └─ OBLIGATORIA en anotaciones de interceptores

   @Target({ElementType.TYPE, ElementType.METHOD})
      ├─ ElementType.TYPE = se aplica a clases
      ├─ ElementType.METHOD = se aplica a métodos
      └─ Puedes poner ambas

   @Retention(RetentionPolicy.RUNTIME)
      ├─ RUNTIME = se ejecuta en tiempo de ejecución
      ├─ CLASS = se compila pero no se ejecuta
      └─ SOURCE = solo en código fuente

ANOTACIONES EN TU PROYECTO:

   A) @MedirTiempo
      @InterceptorBinding
      @Target({ElementType.TYPE, ElementType.METHOD})
      @Retention(RetentionPolicy.RUNTIME)
      public @interface MedirTiempo { }

   B) @log
      @InterceptorBinding
      @Target({ElementType.TYPE, ElementType.METHOD})
      @Retention(RetentionPolicy.RUNTIME)
      public @interface log { }

================================================================================
                    9. ANOTACIONES DE CICLO DE VIDA
================================================================================

ANOTACIONES USADAS EN CDI:

   @ApplicationScoped
      └─ Scope: Una instancia para toda la app

   @Dependent
      └─ Scope: Nueva instancia por inyección

   @Inject
      └─ Inyecta dependencia automáticamente

   @InterceptorBinding
      └─ Marca anotación como interceptor

   @Interceptor
      └─ Marca clase como interceptor

   @AroundInvoke
      └─ Método se ejecuta alrededor de otros

   @Priority(n)
      └─ Define orden de ejecución (interceptores/extensiones)

   @Target
      └─ Dónde se aplica la anotación (TYPE, METHOD)

   @Retention
      └─ Cuándo se procesa (RUNTIME, CLASS, SOURCE)

   @Unremovable
      └─ Mantiene bean incluso si no se usa

   @Default
      └─ Candidato por defecto para inyección

================================================================================
                        10. QUARKUS
================================================================================

¿QUÉ ES QUARKUS?
   • Framework moderno de Java
   • Basado en CDI
   • Optimizado para microservicios y cloud
   • Inicio muy rápido

ESTRUCTURA BÁSICA:

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

EN TU PROYECTO:
───────────────
   • pom.xml: Maven, Quarkus 3.34.3
   • Java 21
   • CDI integrado
   • Interceptores funcionan automáticamente

================================================================================
                 11. CHECKLIST PARA NO OLVIDAR INTERCEPTORES
================================================================================

MNEMOTECNIA: "PIAC"

   P = Personalizada (Crear @interface con @InterceptorBinding)
   I = Interceptor (Poner @Interceptor en la clase)
   A = AroundInvoke (Poner @AroundInvoke en el método)
   C = Call proceed() (context.proceed() adentro)

ANTES DE ESCRIBIR UN INTERCEPTOR:

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

SI TODAS SON ✓ → Tu interceptor funcionará ✅

ERRORES COMUNES A EVITAR:

   ❌ Olvidar @Interceptor en la clase
   ❌ Olvidar @AroundInvoke en el método
   ❌ Olvidar context.proceed() adentro
   ❌ Olvidar @Priority(n)
   ❌ No anotar el método con @MiAnotacion
   ❌ Olvidar @InterceptorBinding en la anotación
   ❌ Olvidar @Target y @Retention en la anotación

================================================================================
               12. TIPOS DE LÓGICA DE NEGOCIO EN PRUEBAS
================================================================================

TIPO 1: PROCESAMIENTO DE COMPRAS/VENTAS (MÁS COMÚN)
─────────────────────────────────────────────────
Escenario: Sistema de ventas con descuentos, registre estadísticas

Elementos:
   ✓ Interface de Descuentos (Strategy)
   ✓ Múltiples implementaciones (@Priority)
   ✓ Servicio que aplica descuentos en orden
   ✓ Estadísticas globales (@ApplicationScoped)
   ✓ Interceptores para auditoría

Dificultad: ⭐⭐

TIPO 2: NOTIFICACIONES MULTICANAL (MÁS COMÚN)
──────────────────────────────────────────────
Escenario: Notificaciones por Email, SMS, WhatsApp según criterios

Elementos:
   ✓ Interface Notificador (Strategy)
   ✓ Múltiples implementaciones
   ✓ Selector que elige según criterios
   ✓ Interceptores para registrar intentos

Dificultad: ⭐⭐⭐

TIPO 3: MEDICIÓN DE RENDIMIENTO (COMÚN)
────────────────────────────────────────
Escenario: Monitorear tiempo de ejecución de operaciones críticas

Elementos:
   ✓ Interceptor @MedirTiempo
   ✓ Servicios que manejan datos
   ✓ Métodos anotados
   ✓ Logs de duración

Dificultad: ⭐⭐

TIPO 4: ESTADÍSTICAS GLOBALES (COMÚN)
──────────────────────────────────────
Escenario: Mantener contadores de transacciones, totales vendidos

Elementos:
   ✓ @ApplicationScoped con variables acumulativas
   ✓ Métodos para registrar
   ✓ Métodos para consultar
   ✓ Se inyecta en otros servicios

Dificultad: ⭐

TIPO 5: AUDITORÍA Y LOGGING (COMÚN)
───────────────────────────────────
Escenario: Registrar quién hizo qué, cuándo, con qué parámetros

Elementos:
   ✓ Interceptor @log
   ✓ context.getParameters()
   ✓ Registrar entrada y salida
   ✓ Puede loguear objetos complejos

Dificultad: ⭐⭐

TIPO 6: VALIDACIÓN Y FILTRADO
──────────────────────────────
Escenario: Validar datos, rechazar compras bajo criterios

Elementos:
   ✓ Interceptor de validación
   ✓ Verificación de condiciones
   ✓ Lanzar excepciones si no cumple
   ✓ Registrar intentos fallidos

Dificultad: ⭐⭐

TIPO 7: CACHÉ Y ALMACENAMIENTO
──────────────────────────────
Escenario: Guardar resultados costosos, evitar consultas repetidas

Elementos:
   ✓ @ApplicationScoped con HashMap
   ✓ Métodos get/put
   ✓ Lógica de invalidación

Dificultad: ⭐⭐

TIPO 8: CÁLCULOS COMPLEJOS
──────────────────────────
Escenario: Recomendaciones, cálculos financieros, análisis

Elementos:
   ✓ Múltiples estrategias de cálculo
   ✓ Strategy Pattern
   ✓ Comparación de métodos

Dificultad: ⭐⭐⭐

================================================================================
                    13. CASOS DE USO - CUÁNDO USAR QUÉ
================================================================================

¿CUÁNDO USAR @ApplicationScoped?

   ✓ Servicios globales (NotificadorMail, NotificadorSMS)
   ✓ Repositorios/DAOs (Base de datos)
   ✓ Estadísticas y contadores globales
   ✓ Configuraciones de la app
   ✓ Cachés compartidas
   ✓ Auditoría y logging globales
   ✓ Servicios que deben compartir estado

EJEMPLO:
   @ApplicationScoped
   public class EstadisticasVentasGobales {
       private int totalVentas = 0;      // Se ACUMULA
       private double montoTotal = 0;     // Se ACUMULA
   }

¿CUÁNDO USAR @Dependent?

   ✓ Objetos de datos (DTOs, Models)
   ✓ Objetos de una solicitud específica
   ✓ Cálculos temporales
   ✓ Objetos que NO necesitan mantener estado

EJEMPLO:
   public class Compra {  // Sin anotación = Dependent
       private String cliente;
       private double total;
   }

¿CUÁNDO USAR INTERCEPTORES?

   ✓ Medir tiempo de ejecución
   ✓ Registrar logs
   ✓ Auditoría de operaciones
   ✓ Validación antes de ejecutar
   ✓ Manejo de excepciones
   ✓ Cache de resultados
   ✓ Sincronización (threads)

¿CUÁNDO USAR STRATEGY PATTERN?

   ✓ Diferentes algoritmos intercambiables
   ✓ Múltiples formas de hacer algo
   ✓ Selección en runtime
   ✓ Fácil agregar nuevas formas

EJEMPLO:
   - Diferentes formas de PAGO (efectivo, tarjeta)
   - Diferentes COMPROBANTES (PDF, papel, XML)
   - Diferentes NOTIFICACIONES (email, SMS, WhatsApp)

¿CUÁNDO USAR SELECTOR?

   ✓ Elegir Strategy según criterios
   ✓ Lógica de decisión centralizada
   ✓ Mantener código limpio

EJEMPLO:
   public Notificador seleccionar(double total) {
       if(total > 120)      return mail;
       else if(total < 50)  return whats;
       else                 return sms;
   }

================================================================================
                    14. PASOS PARA HACER UN INTERCEPTOR
================================================================================

ORDEN EXACTO EN PRUEBA:

PASO 1: CREAR LA ANOTACIÓN PERSONALIZADA
─────────────────────────────────────────
   @InterceptorBinding
   @Target({ElementType.TYPE, ElementType.METHOD})
   @Retention(RetentionPolicy.RUNTIME)
   public @interface MiInterceptor {
   }

PASO 2: CREAR LA CLASE INTERCEPTOR
───────────────────────────────────
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

PASO 3: USAR EN TUS MÉTODOS
────────────────────────────
   @ApplicationScoped
   public class MiServicio {
       
       @MiInterceptor      // Anotación personalizada
       public void procesar(Pedido pedido) {
           System.out.println("Método real");
       }
   }

PASO 4: VERIFICAR CHECKLIST
────────────────────────────
   ☐ ¿Anotación tiene @InterceptorBinding?
   ☐ ¿Clase tiene @Interceptor?
   ☐ ¿Clase tiene @Priority(n)?
   ☐ ¿Método tiene @AroundInvoke?
   ☐ ¿Llama a context.proceed()?
   ☐ ¿Método de negocio está anotado?

================================================================================
                      15. TEMPLATE SEGURO
================================================================================

COPIA Y PEGA ESTE TEMPLATE CADA VEZ:

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

================================================================================
                    16. TABLA RESUMEN RÁPIDA
================================================================================

┌─────────────────┬──────────────────────┬─────────────────────┐
│ ANOTACIÓN       │ DÓNDE VA             │ OBLIGATORIA         │
├─────────────────┼──────────────────────┼─────────────────────┤
│ @Inject         │ En atributo/param    │ Inyecta dependencia │
│ @ApplicationScoped │ En clase         │ Scope: 1 instancia  │
│ @Dependent      │ En clase             │ Scope: N instancias │
│ @Interceptor    │ En clase interceptor │ ✅ SÍ - OBLIGATORIA │
│ @AroundInvoke   │ En método interceptor│ ✅ SÍ - OBLIGATORIA │
│ @Priority(n)    │ En clase interceptor │ ✅ SÍ - OBLIGATORIA │
│ @InterceptorBinding │ En anotación    │ ✅ SÍ - OBLIGATORIA │
│ @Target         │ En anotación         │ ✅ SÍ - OBLIGATORIA │
│ @Retention      │ En anotación         │ ✅ SÍ - OBLIGATORIA │
└─────────────────┴──────────────────────┴─────────────────────┘

┌────────────────────┬─────────────┬──────────────────────────────┐
│ SCOPE              │ INSTANCIAS  │ CUÁNDO USAR                  │
├────────────────────┼─────────────┼──────────────────────────────┤
│ @ApplicationScoped │ 1 para app  │ Servicios, estadísticas      │
│ @Dependent         │ N nuevas    │ DTOs, datos temporales       │
│ @Singleton         │ 1 para app  │ Parecido a ApplicationScoped │
└────────────────────┴─────────────┴──────────────────────────────┘

┌──────────────────────┬─────────────────────────────────────────┐
│ PATRÓN               │ CUÁNDO USAR                             │
├──────────────────────┼─────────────────────────────────────────┤
│ Strategy             │ Múltiples formas de hacer algo          │
│ Selector/Router      │ Elegir qué Strategy usar                │
│ Interceptor (AOP)    │ Ejecutar código alrededor de métodos   │
│ Extension            │ Múltiples implementaciones de interfaz  │
└──────────────────────┴─────────────────────────────────────────┘

================================================================================
                    17. PREGUNTAS PROBABLES EN PRUEBA
================================================================================

TEÓRICO:

   P: ¿Qué es CDI?
   R: Container que gestiona el ciclo de vida de beans y sus dependencias

   P: ¿Diferencia entre @ApplicationScoped y @Dependent?
   R: ApplicationScoped = 1 instancia para toda app, Dependent = N instancias

   P: ¿Qué es Strategy Pattern?
   R: Define algoritmos intercambiables

   P: ¿Para qué sirve context.proceed()?
   R: Ejecuta el método real dentro de un interceptor

   P: ¿Cómo ordenas interceptores?
   R: Con @Priority(n) - menor = primero

   P: ¿Qué es un Interceptor?
   R: Ejecuta código antes y después de un método sin modificarlo

   P: ¿Cómo se crea un Interceptor?
   R: @InterceptorBinding + @Interceptor + @AroundInvoke

   P: ¿Qué pasa sin context.proceed()?
   R: El método real no se ejecuta

PRÁCTICO (ESPERADO):

   "Crea un sistema de _____ que _____ con _____"
   
   Posibles espacios en blanco:
   - "sistema de compras que aplique descuentos con interceptores"
   - "sistema de notificaciones que envíe por múltiples canales"
   - "sistema de auditoría que registre todas las operaciones"
   - "sistema de inventario que mida tiempo de procesamiento"

================================================================================
                      18. FLUJO COMPLETO TÍPICO
================================================================================

FLUJO DE UNA APLICACIÓN TÍPICA EN PRUEBA:

1. MAIN INICIA
   └─ Quarkus.run(App.class)

2. CONTENEDOR CDI INICIA
   └─ Crea todas las instancias @ApplicationScoped

3. INYECTA DEPENDENCIAS
   └─ @Inject carga las instancias

4. LLAMA MÉTODO ANOTADO
   └─ @MiInterceptor en el método

5. INTERCEPTOR(es) SE EJECUTAN (en orden @Priority)
   └─ Priority(1) primero
   └─ Priority(2) segundo

6. MÉTODO REAL SE EJECUTA
   └─ context.proceed() en el interceptor

7. INTERCEPTOR TERMINA
   └─ Retorna resultado

8. RESULTADO SE RETORNA A QUIEN LLAMÓ
   └─ Puede haber más procesamiento

EJEMPLO PRÁCTICO:

   Método anotado: @log @MedirTiempo
   
   logInterceptor (@Priority(1))
   ├─ Registra entrada
   ├─ Llama context.proceed()
   │
   MedirTiempoInterceptor (@Priority(2))
   ├─ Registra tiempo inicio
   ├─ Llama context.proceed()
   │
   MÉTODO REAL
   ├─ Executa lógica de negocio
   │
   MedirTiempoInterceptor retorna
   ├─ Registra tiempo fin
   │
   logInterceptor retorna
   ├─ Registra salida

================================================================================
                    19. COMANDOS Y ESTRUCTURAS IMPORTANTES
================================================================================

ESTRUCTURAS CDI:

   Inyección simple:
   @Inject
   private MiServicio servicio;

   Inyección de múltiples implementaciones:
   @Inject
   private Instance<MiInterfaz> implementaciones;
   
   for(MiInterfaz impl : implementaciones) {
       impl.hacer();  // Se aplican en orden de @Priority
   }

ESTRUCTURAS DE MÉTODOS:

   Método con contexto de inyección:
   @Override
   public int run(String... args) throws Exception {
       // El contenedor ya inyectó las dependencias
       return 0;
   }

   Método interceptado:
   @MiInterceptor
   public void procesar(Parametro param) {
       // Se ejecutará rodeado de interceptores
   }

================================================================================
                  20. CONSEJOS PARA NO OLVIDAR NADA
================================================================================

DURANTE LA PRUEBA:

   1. LEE BIEN EL ENUNCIADO
      └─ Identifica qué Strategy necesitas
      └─ Identifica qué Interceptor necesitas
      └─ Identifica qué datos son compartidos

   2. EMPIEZA CON LA INTERFAZ
      └─ Define qué hace

   3. IMPLEMENTA LAS ESTRATEGIAS
      └─ Anotadas con @ApplicationScoped y @Priority si es necesario

   4. CREA SELECTOR SI LO NECESITA
      └─ Inyecta todas las implementaciones
      └─ Elige según criterios

   5. SI NECESITA INTERCEPTOR:
      └─ Crea anotación personalizada
      └─ Crea clase interceptor (checklist PIAC)
      └─ Anotota métodos que necesita interceptar

   6. SI NECESITA ESTADÍSTICAS
      └─ Usa @ApplicationScoped con variables acumulativas

   7. VERIFICA CADA PASO
      └─ No avances sin verificar lo anterior funciona

CHECKLIST FINAL ANTES DE ENTREGAR:

   ☐ ¿Toda clase de servicio tiene @ApplicationScoped?
   ☐ ¿Toda dependencia tiene @Inject?
   ☐ ¿Todos los interceptores tienen @Interceptor?
   ☐ ¿Todos los interceptores tienen @Priority(n)?
   ☐ ¿Todos los métodos interceptados tienen @AroundInvoke?
   ☐ ¿Todos los @AroundInvoke llaman context.proceed()?
   ☐ ¿Todos los métodos anotados usan la anotación?
   ☐ ¿Las anotaciones tienen @InterceptorBinding?
   ☐ ¿Las anotaciones tienen @Target y @Retention?
   ☐ ¿El código compila sin errores?
   ☐ ¿El programa se ejecuta correctamente?

================================================================================
                        21. MATERIALES DE REFERENCIA
================================================================================

ARCHIVOS IMPORTANTES EN TU PROYECTO:

   Ejemplos de Scopes:
   └─ AmbitoAplicacion.java
   └─ AmbitoSingleton.java
   └─ AmbitoInject.java
   └─ AmbitoRequest.java

   Ejemplos de Strategy:
   └─ PagoEfectivo.java
   └─ PagoTarjetaCredito.java
   └─ NotificadorMail.java
   └─ NotificadorSMS.java
   └─ ComprobantePDF.java
   └─ Factura.java

   Ejemplos de Selector:
   └─ NotificadorSelector.java

   Ejemplos de Interceptores:
   └─ MedirTiempoInterceptor.java
   └─ logInterceptor.java
   └─ MedirTiempo.java (anotación)
   └─ log.java (anotación)

   Ejemplos de Estadísticas:
   └─ EstadisticasVentasGobales.java

   Ejemplos de Extensiones:
   └─ DescuentoIVA.java
   └─ DescuentoBlackFriday.java
   └─ DescuentoISD.java
   └─ DescuentoSOLCA.java

   Servicios de Negocio:
   └─ PedidoService1.java
   └─ ProcesadorCompraService.java
   └─ ProcesadorVentaServiceTiempo.java
   └─ InventarioService.java

================================================================================
                      22. RESUMEN FINAL
================================================================================

LOS 10 CONCEPTOS CLAVE QUE DEBES DOMINAR:

   1. CDI = Contenedor que gestiona ciclo de vida
   2. @ApplicationScoped = Compartido entre todos
   3. @Dependent = Independiente cada uno
   4. @Inject = Inyecta dependencia automáticamente
   5. Strategy Pattern = Múltiples formas intercambiables
   6. Selector = Elige qué Strategy usar
   7. Interceptor = Código antes y después de métodos
   8. @Priority(n) = Define orden de ejecución
   9. context.proceed() = Ejecuta método real (CRÍTICO)
   10. @InterceptorBinding = Marca anotación como interceptor

LOS 5 ERRORES MÁS COMUNES:

   ❌ 1. Olvidar @Interceptor en la clase
   ❌ 2. Olvidar context.proceed()
   ❌ 3. Olvidar @AroundInvoke en el método
   ❌ 4. No anotar el método con la anotación personalizada
   ❌ 5. Olvidar @Priority(n)

LOS 5 PASOS CORRECTOS:

   ✅ 1. Crear anotación con @InterceptorBinding
   ✅ 2. Crear clase con @Interceptor @Priority(n)
   ✅ 3. Anotar método con @AroundInvoke
   ✅ 4. Llamar context.proceed() en el método
   ✅ 5. Anotar métodos de negocio con la anotación

================================================================================
                          BUENA SUERTE
================================================================================

RECUERDA:
   • La prueba es PRÁCTICA, no teórica
   • Necesitarás crear código que FUNCIONE
   • Los interceptores son lo MÁS IMPORTANTE
   • No olvides NUNCA el context.proceed()
   • @Priority ordena la ejecución
   • @ApplicationScoped es para compartir entre todos

CONSEJOS FINALES:
   ✓ Antes de empezar, lee TODO el enunciado
   ✓ Identifica qué patterns necesitas
   ✓ Empieza con interfaces y estrategias
   ✓ Luego crea los servicios
   ✓ Luego los interceptores
   ✓ Verifica cada paso funciona
   ✓ Si algo no funciona, revisa el checklist PIAC

¡ÉXITO EN TU PRUEBA! 🎓✨

================================================================================
Última actualización: 2026-05-08
Estudiante: ITSPAUL7-mod
Rama: taller12
================================================================================
