package entities

open class Brand (
    /* Constructor */
    /* ---------------------------------------------- */
    /* Attributes */
    /* ---------------------------------------------- */
    private var id: Int,
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

    fun setId(id: Int) {
        this.id = id
    }

    fun setHasWebpage(hasWebpage: Boolean) {
        this.hasWebpage = hasWebpage
    }

    fun setPrice(price: Number) {
        this.price = price
    }

    fun setStatus(status: Char) {
        this.status = status
    }

    override fun toString(): String {
        return  String.format(
            "| %-8s | %-15s | %-15s | %-8s | %-15s |",
            "$id", name,"$${price}B","$status","$hasWebpage"
        )
    }
}