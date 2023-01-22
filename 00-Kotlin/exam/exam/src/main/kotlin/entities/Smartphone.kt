package entities

open class Smartphone (
    /* Constructor */
    /* ---------------------------------------------- */
    /* Attributes */
    /* ---------------------------------------------- */
    private var id: Int,

    /*
    * M -> Brand New,
    * F -> Refurbished,
    * N -> Replacement device, and
    * P -> Personalized device
    * */
    private val serialType: Char,
    private val model: String,
    private val brandId: Int,
    private var price: Double,

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

    fun getBrandId(): Int {
        return this.brandId
    }

    fun getId(): Int {
        return this.id
    }

    fun getSerialType(): Char {
        return this.serialType
    }

    /* Set */
    fun setId(id: Int) {
        this.id = id
    }

    fun setPrice(price: Double) {
        this.price = price
    }

    override fun toString(): String {
        return  String.format(
            "| %-8s | %-15s | %-15s | %-8s | %-15s |",
            "$id","$serialType", model,"$brandId","$${price}"
        )
    }
}