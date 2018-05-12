import java.math.BigDecimal
import java.time.LocalDate

class StorageUnit(val width: Int, val length: Int, val height: Int, val type: Type, val standardPrice: BigDecimal) {
    init {
        require(width % 4 == 0) { "Width must be multiple of 4" }
        require(length % 4 == 0) { "Length must be multiple of 4" }
        require(height % 2 == 0) { "Height must be multiple of 2" }
        require(width > 0) { "Width must be positive" }
        require(length > 0) { "Length must be positive" }
        require(height > 0) { "Height must be positive" }
    }

    private var rental: Rental? = null

    val customer: Customer?
        get() = rental?.customer
    val rentedPrice: BigDecimal?
        get() = rental?.rentedPrice
    val rentalStartDate: LocalDate?
        get() = rental?.startDate

    val currentPrice
        get() = rentedPrice ?: standardPrice

    @JvmOverloads
    fun rentToCustomer(customer: Customer, date: LocalDate, price: BigDecimal = standardPrice) {
        rental = Rental(customer, price, date)
    }

    fun unrent() {
        rental = null
    }

    enum class Type {
        STANDARD, HUMIDITY_CONTROLLED, TEMPERATURE_CONTROLLED
    }
}

private data class Rental(val customer: Customer, val rentedPrice: BigDecimal, val startDate: LocalDate)
