import java.math.BigDecimal

data class Customer(val name: String, val phoneNumber: String, var accountBalance: BigDecimal) {
    fun charge(amount: BigDecimal) {
        require(amount >= BigDecimal.ZERO) { "Amount must be non-negative" }
        accountBalance += amount
    }

    fun credit(amount: BigDecimal) {
        require(amount >= BigDecimal.ZERO) { "Amount must be non-negative" }
        accountBalance -= amount
    }
}
