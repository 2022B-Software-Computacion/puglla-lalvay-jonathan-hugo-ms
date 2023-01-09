package entities

open class Smartphone() {
    /* Attributes */
    /* ---------------------------------------------- */
    private var model: String? = null
    private var price: Double? = null
    private var brand: Brand? = null
    private var id: Int? = null

    /*
    * M -> entities.Brand New,
    * F -> Refurbished,
    * N -> Replacement device, and
    * P -> Personalized device
    * */
    private var firstSerialLetter: Char? = null

    /* Constructor */
    /* ---------------------------------------------- */
    constructor(model: String, price: Double, brand: Brand, id: Int, firstSerialLetter: Char) : this() {
        this.model = model
        this.price = price
        this.brand = brand
        this.id = id
        this.firstSerialLetter = firstSerialLetter
    }
}