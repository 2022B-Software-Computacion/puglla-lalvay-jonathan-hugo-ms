package entities

open class Smartphone (
    /* Constructor */
    /* ---------------------------------------------- */
    /* Attributes */
    /* ---------------------------------------------- */
    private val id: Int,

    /*
    * M -> entities.Brand New,
    * F -> Refurbished,
    * N -> Replacement device, and
    * P -> Personalized device
    * */
    private var serialType: Char,
    private var model: String,
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
    fun setModel(model: String) {
        this.model = model
    }

    fun setPrice(price: Double) {
        this.price = price
    }

    fun setSerialType(serialType: Char) {
        this.serialType = serialType
    }

    override fun toString(): String {
        return  "$id \t$serialType \t$model \t$brandId \t$${price}"
    }
}