package entities

open class Smartphone (
    /* Constructor */
    /* ---------------------------------------------- */
    /* Attributes */
    /* ---------------------------------------------- */private var model: String,
    private var price: Double,
    private val brandId: String,
    private val id: Int,

    /*
    * M -> entities.Brand New,
    * F -> Refurbished,
    * N -> Replacement device, and
    * P -> Personalized device
    * */
    private var firstSerialLetter: Char
) {
    /* Methods */
    /* ---------------------------------------------- */
    /* Get */
    fun getModel(): String {
        return this.model
    }

    fun getPrice(): Double {
        return this.price
    }

    fun getBrandId(): String {
        return this.brandId
    }

    fun getId(): Int {
        return this.id
    }

    fun getFirstSerialLetter(): Char {
        return this.firstSerialLetter
    }

    /* Set */
    fun setModel(model: String) {
        this.model = model
    }

    fun setPrice(price: Double) {
        this.price = price
    }

    fun setFirstSerialLetter(firstSerialLetter: Char) {
        this.firstSerialLetter = firstSerialLetter
    }
}