package sqlDsl

class SqlSelectBuilder {
    private val query = SqlModel()

    fun from(from: String): SqlSelectBuilder {
        query.table = Table(from)
        return this
    }

    fun select(vararg columns: String): SqlSelectBuilder {
        query.columns = Columns(columns.toList())
        return this
    }

    fun where(block: SqlSelectWhereBuilder.() -> SqlSelectWhereBuilder): SqlSelectBuilder {
        val sqlWhereBuilder = SqlSelectWhereBuilder()
        query.where = block(sqlWhereBuilder).build()
        return this
    }

    fun build(): String {
        if (query.table == Table.NONE) throw Exception("Need specify table")
        return "select ${query.columns.string} from ${query.table.name}${
            when {
                query.where.isEmpty() -> ""
                query.where.size == 1 -> " where ${query.where.first().string}"
                query.where.size > 1 -> " where (${query.where.joinToString(" and ") { it.string }})"

                else -> ""
            }
        }"
    }
}

class SqlSelectWhereBuilder {
    private var where = mutableListOf<WhereModel>()

    infix fun String.eq(value: Any?): SqlSelectWhereBuilder {
        setupWhere(this, true, value)
        return this@SqlSelectWhereBuilder
    }

    infix fun String.nonEq(value: Any?): SqlSelectWhereBuilder {
        setupWhere(this, false, value)
        return this@SqlSelectWhereBuilder
    }

    private fun setupWhere(columnName: String, isEqual: Boolean, value: Any?) {
        where.add(
            WhereModel(
                columnName, isEqual,
                whereValue = when (value) {
                    is String -> WhereString(value)
                    is Int -> WhereInt(value)
                    null -> WhereNull
                    else -> throw Exception("Value can be String, Int or null")
                }
            )
        )
    }

    fun build(): List<WhereModel> {
        return where
    }
}

fun query(block: SqlSelectBuilder.() -> SqlSelectBuilder): SqlSelectBuilder {
    val builder = SqlSelectBuilder()
    return block(builder)
}