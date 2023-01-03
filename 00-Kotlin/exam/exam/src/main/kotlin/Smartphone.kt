import java.time.LocalDate

class Smartphone {
    /* Attributes */
    /* ---------------------------------------------- */
    val model: String = ""
    val price: Double = 0.0
    val hasCardSlot: Boolean = true
    val storage: Int = 0

    /*
    * M -> Brand New,
    * F -> Refurbished,
    * N -> Replacement device, and
    * P -> Personalized device
    * */
    val deviceSource: Char = 'M'
}