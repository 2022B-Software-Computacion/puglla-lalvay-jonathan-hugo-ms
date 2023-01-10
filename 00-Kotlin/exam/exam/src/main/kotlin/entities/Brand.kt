package entities

open class Brand (
    /* Constructor */
    /* ---------------------------------------------- */
    /* Attributes */
    /* ---------------------------------------------- */
    private var name: String,
    hasWebPage: Boolean,
    private var price: Double,
    private val foundationYear: Int,// A -> Active, N -> New, B -> Banned
    private var status: Char,
    private var hasWebpage: Boolean = hasWebPage
) {
    /* Methods */
    /* ---------------------------------------------- */
    /* Get */
    fun getName(): String {
        return this.name
    }

    fun getHasWebPage(): Boolean {
        return this.hasWebpage
    }

    fun getPrice(): Double {
        return this.price
    }

    fun getFoundationYear(): Int {
        return this.foundationYear
    }

    fun getStatus(): Char {
        return this.status
    }

    /* Set */
    fun setName(name: String) {
        this.name = name
    }

    fun getHasWebPage(hasWebPage: Boolean) {
        this.hasWebpage = hasWebpage
    }

    fun setPrice(price: Double) {
        this.price = price
    }

    fun getStatus(status: Char) {
        this.status = status
    }
}