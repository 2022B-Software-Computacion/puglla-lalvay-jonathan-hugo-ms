import java.util.*
import kotlin.collections.ArrayList

// Main.kt
fun main() {
    println("Hello")

    // Tipos de variables:

    // Inmutables (no se puede reasignar):
    val inmutable: String = "Jonathan"

    // Mutables (se puede reasignar):
    var mutable = "Puglla"
    mutable = "Lalvay";

    // Sintaxis Duck Typing
    val ejemploVariable = "Ejemplo"
    val edadEjemplo: Int = 12;
    ejemploVariable.trim()

    // Variables primitivas
    val nombreProfesor: String = "Adrian Eguez"
    val sueldo: Double = 1.2
    val estadoCivil: Char = 'S'
    val mayorEdad: Boolean = true

    // Clases JAVA
    val fechaNacimiento:Date = Date();

    // If Else
    if (true) {

    } else {

    }

    // Switch no existe
    val estadoCivilWhen = "S"
    when (estadoCivilWhen) {
        ("S") -> {
            println("acercarse")
        }

        "C" -> {
            println("alejarse")
        }

        "UN" -> println("hablar")

        else -> println("No reconocido")
    }

    val coqueteo = if (estadoCivilWhen == "S") "SI" else "NO"

    val sumaUno = Suma(1, 1)
    val sumaDos = Suma(null, 1)
    val sumaTres = Suma(1, null)
    val sumaCuatro = Suma(null, null)

    sumaUno.sumar()
    sumaDos.sumar()
    sumaTres.sumar()
    sumaCuatro.sumar()

    Suma.pi
    Suma.elevarAlCuadrado(2)
    Suma.historialSumas

    // Static Array
    val arregloEstatico: Array<Int> = arrayOf<Int>(1, 2, 3)
    println(arregloEstatico)

    // Dynamic Array
    val arregloDinamico: ArrayList<Int> = arrayListOf<Int>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    println(arregloDinamico)
    arregloDinamico.add(11)
    arregloDinamico.add(12)
    println(arregloDinamico)

    // OPERATORS -> Works with both array
    // FOR EACH -> Unit
    val respuestaForEach: Unit = arregloDinamico
        .forEach{
            valorActual: Int ->
            println("Valor actual: ${valorActual}")
        }
    arregloEstatico
        .forEachIndexed { indice: Int, valorActual: Int ->
            println("Valor ${valorActual} Indice: ${indice}")
        }
    // Iterate in an array
    println(respuestaForEach)

    // MAP -> Muta el arreglo (Cambia el arreglo)
    // 1) Se envía el nuevo valor de la iteración
    // 2) Nos devuelve un NUEVO ARREGLO con los valores modificados

    val respuestaMap: List<Double> = arregloDinamico
        .map {valorActual: Int ->
            return@map valorActual.toDouble() + 100.00
        }

    println(respuestaMap)

    val respuestaMapDos = arregloDinamico.map { it + 15 }
    // .map { valorActual: Int ->
    // return@map valorActual + 15
    // }
    println(respuestaMapDos)

    // Filter -> FILTRAR EL ARREGLO
    // 1) Devolver una expresión (TRUE o FALSE)
    // 2) Nuevo arreglo filtrado

    val respuestaFilter: List<Int> = arregloDinamico
        .filter {valorActual: Int ->
            val mayoresACinco: Boolean = valorActual > 5 // Expresión Condición
            return@filter mayoresACinco
        }

    val respuestaFilterDos = arregloDinamico.filter { it <= 5 }
    println(respuestaFilter)
    println(respuestaFilterDos)

    // OR AND
    // OR -> ANY (Alguno cumple?)
    // AND -> ALL (Todos cumplen?)

    val respuestaAny: Boolean = arregloDinamico
        .any{valorActual: Int ->
            return@any (valorActual > 5)
        }
    println("Respuesta Any: " + respuestaAny)

    val respuestaAll: Boolean = arregloDinamico
        .all{valorActual: Int ->
            return@all (valorActual > 5)
        }
    println("Respuesta All: " + respuestaAll) // false

    // REDUCE -> Valor acumulado
    // Valor acumulado = 0 (Siempre 0 en lenguaje Kotlin)
    // [1, 2, 3, 4, 5] -> Sumeme todos los valores del arreglo
    // valorIteracion1 = valorEmpieza + 1 = 0 + 1 = 1 -> Iteracion 1
    // valorIteracion2 = valorIteracion1 + 2 = 1 + 2 = 3 -> Iteracion 2
    // valorIteracion3 = valorIteracion2 + 3 = 3 + 3 = 6 -> Iteracion 3
    // valorIteracion4 = valorIteracion3 + 4 = 6 + 4 = 10 -> Iteracion 4
    // valorIteracion5 = valorIteracion4 + 5 = 10 + 5 = 15 -> Iteracion 5

    println("Operador REDUCE: ")
    val respuestaReduce: Int = arregloDinamico
        .reduce{ // acumulado = 0 -> SIEMPRE EMPIEZA EN 0
            acumulado: Int, valorActual: Int ->
            return@reduce (acumulado + valorActual) // Lógica negocio
        }
    println("Respuesta reduce: " + respuestaReduce) //78
}



// Unit == void
fun imprimirNombre(nombre: String): Unit {
    println("Nombre: ${nombre}")
}

fun calcularSueldo(
    sueldo: Double, // Requerido
    tasa: Double = 12.00, // Opcional (Defecto)
    bonoEspecial: Double? = null, // Opcional (Null) -> nullable
): Double {
    // String -> String?
    // Int -> Int?
    // Date -> Data?
    if (bonoEspecial == null) {
        return sueldo * (100 / tasa)
    } else {
        return sueldo * (100 / tasa) + bonoEspecial
    }
}

abstract class NumerosJava {
    protected val numeroUno: Int
    private val numeroDos: Int

    constructor(
        uno: Int,
        dos: Int
    ) { // Code block
        this.numeroUno = uno
        this.numeroDos = dos
        println("Inicializado")
    }
}

abstract class Numeros( // Constructor Primario
//    uno: Int, // Parameters
//    public var uno: Int, // Public class property
//    var uno: Int,
    protected val numeroUno: Int,
    protected val numeroDos: Int
) {
    // protected val numeroUno: Int
    // var cedula: String = ""
    // public var cedula: String = ""

    init { // Code block of the PRIMARY constructor
        // this.numeroUno = uno
        this.numeroUno
        numeroUno
        this.numeroDos
        numeroDos
        println("Inicializado")
    }
}

class Suma( // Primary Constructor Suma
    uno: Int, // Parameter
    dos: Int  // Parameter
): Numeros(uno, dos) {
    init { // Bloque constructor primario
        this.numeroUno
        this.numeroDos
    }

    constructor( // Second Constructor
        uno: Int?, // Parameter
        dos: Int? // Parameter
    ) : this( // Calling the main constructor
        if(uno == null) 0 else uno,
        if(dos == null) 0 else dos
    ) {

    }

    public fun sumar(): Int {
        return numeroUno + numeroDos
    }

    companion object { // Instances share attributes and methods
        val pi = 3.14
        fun elevarAlCuadrado(num: Int): Int {
            return num * num
        }

        val historialSumas = arrayListOf<Int>()

        fun agregarHistorial (valorNuevaSuma: Int) {
            historialSumas.add(valorNuevaSuma)
        }
    }
}
