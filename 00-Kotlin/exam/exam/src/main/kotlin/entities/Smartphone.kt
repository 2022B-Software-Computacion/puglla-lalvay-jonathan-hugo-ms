package entities

open class Smartphone {
    /* Attributes */
    /* ---------------------------------------------- */
    private var model: String
    private var price: Double
    private var brandId: String
    private var id: Int

    /*
    * M -> entities.Brand New,
    * F -> Refurbished,
    * N -> Replacement device, and
    * P -> Personalized device
    * */
    private var firstSerialLetter: Char

    /* Constructor */
    /* ---------------------------------------------- */
    constructor(model: String, price: Double, brandId: String, id: Int, firstSerialLetter: Char) {
        this.model = model
        this.price = price
        this.brandId = brandId
        this.id = id
        this.firstSerialLetter = firstSerialLetter
    }

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

    fun setBrandId(brandId: String) {
        this.brandId = brandId
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun setFirstSerialLetter(firstSerialLetter: Char) {
        this.firstSerialLetter = firstSerialLetter
    }

}