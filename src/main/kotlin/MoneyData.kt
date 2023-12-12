data class MoneyData(val value: Double, val currency: String, val exchangeRate: Double = 1.0)

operator fun MoneyData.plus(other: MoneyData): MoneyData {
//    require(this.currency == other.currency) { "Currencies don't match: ${this.currency} and ${other.currency}" }
    val convertedValue = other.value * (other.exchangeRate / this.exchangeRate)
    return MoneyData(this.value + convertedValue, this.currency, this.exchangeRate)
}

fun printMoneyOperation(money1: MoneyData, money2: MoneyData, operation: String) {
    val result = when (operation) {
        "+" -> money1 + money2
        else -> throw IllegalArgumentException("Unsupported operation: $operation")
    }

    println("${money1.value}${money1.currency} $operation ${money2.value}${money2.currency} is ${result.value}${result.currency}.")
}

fun main() {
    val rmb1 = MoneyData(1.0, "¥", 1.0)
    val dollar1 = MoneyData(1.0, "$", 7.1264772)// Assuming the exchange rate from Dollar to RMB is 7.1264772

    printMoneyOperation(dollar1, rmb1, "+")
    printMoneyOperation(rmb1, dollar1, "+")
}
