import java.math.BigDecimal

class StorageLocation(val designation: String) {
    private val units = Array(12) { Array(20) { StorageUnit(4, 4, 4, StorageUnit.Type.STANDARD, BigDecimal("50.00")) } }
    private val customers = ArrayList<Customer>(100)

    private val pattern = "[A-Z]{2}[0-9]{2}.+".toRegex()
    init {
        require(designation.matches(pattern)) { "Designation `$designation` did not match required pattern" }
    }

    fun getStorageUnit(idx: Int): StorageUnit {
        val row = idx / units.size
        val offsetWithinRow = idx % units.size
        return units[row][offsetWithinRow]
    }

    fun addCustomer(customer: Customer) {
        customers.add(customer)
    }

    // Not actually asked for, but seems logical. Note: O(n) because ArrayList.
    fun removeCustomer(customer: Customer) {
        customers.remove(customer)
    }

    fun getCustomer(idx: Int): Customer = customers[idx]

    val customerCount: Int
        get() = customers.size

    fun getStorageUnitsRentedByCustomer(customer: Customer): Array<StorageUnit> {
        // The easy, sane way:
        // return units.flatMap { it.filter { it.customer == customer } }.toTypedArray()

        // The way that doesn't allocate any extra data structures, per the obnoxious footnote
        val count = units.sumBy { row -> row.count { storageUnit -> storageUnit.customer == customer } }
        val returnValue: Array<StorageUnit?> = arrayOfNulls(count)
        var idx = 0
        units.forEach { row ->
            row.forEach { storageUnit ->
                if (storageUnit.customer == customer) {
                    returnValue[idx++] = storageUnit
                }
            }
        }
        @Suppress("UNCHECKED_CAST")
        return returnValue as Array<StorageUnit>
    }

    /**
     * Get all unrented storage units, optionally filtering by type.
     * @param type to search for, or null if all types are permissible.
     * @return new array of [StorageUnit].
     */
    fun getUnrentedStorageUnits(typeFilter: StorageUnit.Type? = null): Array<StorageUnit> =
            units
                    .flatMap { row ->
                        row.filter { unit ->
                            unit.customer == null && (typeFilter == null || unit.type == typeFilter)
                        }
                    }
                    .toTypedArray()

    fun chargeCustomersMonthlyRent() {
        units.forEach { row ->
            row.forEach { unit ->
                unit.customer?.charge(unit.rentedPrice!!)
            }
        }
    }
}
