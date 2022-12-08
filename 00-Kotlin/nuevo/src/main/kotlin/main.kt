import java.util.*

// Main.kt
fun main() {
    println("Hello");

    // Tipos de variables:

    // Inmutables (no se puede reasignar):
    val inmutable: String = "Jonathan";

    // Mutables (se puede reasignar):
    var mutable: String = "Puglla"
    mutable = "Lalvay";

    // Sintaxis Duck Typing
    val ejemploVariable = "Ejemplo";
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
