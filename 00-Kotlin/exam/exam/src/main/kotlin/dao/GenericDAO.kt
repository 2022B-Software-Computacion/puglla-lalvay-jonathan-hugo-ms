package dao

interface GenericDAO<T, ID> {
    /* Methods */
    /* ---------------------------------------------- */

    /* CRUD */
    fun create(entity: T)

    fun read(id: ID)

    fun update(entity: T)

    fun delete(entity: T)
}