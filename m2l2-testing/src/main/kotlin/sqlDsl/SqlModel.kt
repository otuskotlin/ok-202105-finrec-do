package sqlDsl

data class SqlModel(
    var table: Table = Table.NONE,
    var columns: Columns = Columns.EMPTY,
    var where: List<WhereModel> = emptyList()
)

@JvmInline
value class Table (val name: String) {
    companion object {
        val NONE = Table("")
    }
}

@JvmInline
value class Columns (private val columns: List<String>) {
    val string: String
        get() {
            if (columns.isEmpty()) return "*"
            return columns.joinToString()
        }

    companion object {
        val EMPTY = Columns(emptyList())
    }
}

data class WhereModel (
    val column: String = "",
    val isEquals: Boolean = false,
    val whereValue: WhereValue = WhereNull
) {

    val string: String
        get() {
            if (column.isEmpty()) return ""
            return "$column $equalsString ${
                when (whereValue) {
                    is WhereInt -> whereValue.value.toString()
                    WhereNull -> "null"
                    is WhereString -> "'${whereValue.value}'"
                }
            }"
        }

    private val equalsString: String
        get() {
            return when (whereValue) {
                is WhereInt, is WhereString -> if (isEquals) "=" else "!="
                WhereNull -> if (isEquals) "is" else "!is"
            }
        }
}

sealed class WhereValue

class WhereString(val value: String) : WhereValue()
class WhereInt(val value: Int) : WhereValue()
object WhereNull : WhereValue()
