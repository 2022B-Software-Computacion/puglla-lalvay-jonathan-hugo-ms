package entities

open class Brand() {
    /* Attributes */
    /* ---------------------------------------------- */
    private var name: String? = null
    private var hasWebpage: Boolean? = null
    private var price: Double? = null
    private var smartphones: ArrayList<Smartphone>? = null

    // A -> Active, N -> New, B -> Banned
    private var status: Char? = null

    /* Constructor */
    /* ---------------------------------------------- */
    constructor(name: String, hasWebPage: Boolean, price: Double, smartphones: ArrayList<Smartphone>, status: Char) : this() {
        this.name = name
        this.hasWebpage = hasWebPage
        this.price = price
        this.smartphones = smartphones
        this.status = status
    }
}