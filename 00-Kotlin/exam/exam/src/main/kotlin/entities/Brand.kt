package entities

open class Brand (
    /* Constructor */
    /* ---------------------------------------------- */
    /* Attributes */
    /* ---------------------------------------------- */
    private val id: Int,
    private var name: String,
    private var price: Number,
    // A -> Active, N -> New, B -> Banned
    private var status: Char,
    private var hasWebpage: Boolean
) {
    /* Methods */
    /* ---------------------------------------------- */
    /* Get */
    fun getName(): String {
        return this.name
    }

    fun getHasWebpage(): Boolean {
        return this.hasWebpage
    }

    fun getPrice(): Number {
        return this.price
    }

    fun getId(): Int {
        return this.id
    }

    fun getStatus(): Char {
        return this.status
    }

    /* Set */
    fun setName(name: String) {
        this.name = name
    }

    fun getHasWebpage(hasWebpage: Boolean) {
        this.hasWebpage = hasWebpage
    }

    fun setPrice(price: Number) {
        this.price = price
    }

    fun getStatus(status: Char) {
        this.status = status
    }

    override fun toString(): String {
        return  "$id \t$name \t$${price}B \t$status \t$hasWebpage"
    }
}