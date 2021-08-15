import org.junit.Test
import sqlDsl.SqlSelectBuilder
import sqlDsl.query
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SqlDslUnitTest {


    @Test
    fun `simple select all from table`() {
        val expected = "select * from table"
        val real = query {
            from("table")
        }
        checkSQL(expected, real)
    }

    @Test
    fun `check that select can't be used without table`() {
        assertFailsWith<Exception> {
            query {
                select("col_a")
            }.build()
        }
    }

    @Test
    fun `select certain columns from table`() {
        val expected = "select col_a, col_b from table"
        val real = query {
            select("col_a", "col_b")
            from("table")
        }
        checkSQL(expected, real)
    }

    @Test
    fun `select certain columns from table 1`() {
        val expected = "select col_a, col_b from table 1"
        val real = query {
            select("col_a", "col_b")
            from("table 1")
        }
        checkSQL(expected, real)
    }

    /**
     * __eq__ is "equals" function. Must be one of char:
     *  - for strings - "="
     *  - for numbers - "="
     *  - for null - "is"
     */
    @Test
    fun `select with complex where condition with one condition`() {
        val expected = "select * from table where col_a = 'id'"
        val real = query {
            from("table")
            where { "col_a" eq "id" }
        }
        checkSQL(expected, real)
    }

    /**
     * __nonEq__ is "non equals" function. Must be one of chars:
     *  - for strings - "!="
     *  - for numbers - "!="
     *  - for null - "!is"
     */
    @Test
    fun `select with complex where condition with two conditions`() {
        val expected = "select * from table where col_a != 0"
        val real = query {
            from("table")
            where { "col_a" nonEq 0 }
        }
        checkSQL(expected, real)
    }

    @Test
    fun `select with complex where condition with two conditions2`() {
        val expected = "select * from table where (col_a !is null and col_a != 0)"
        val real = query {
            from("table")
            where {
                "col_a" nonEq null
                "col_a" nonEq 0
            }
        }
        checkSQL(expected, real)
    }

    private fun checkSQL(expected: String, sql: SqlSelectBuilder) {
        assertEquals(expected, sql.build())
    }
}