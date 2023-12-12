fun main(args: Array<String>) {
    val str = "Hello World!123"
    val count = StringUtil.lettersCount(str)
    println("string is: $str, letterCount is : $count")
    println("string is: $str, letterCount is : ${str.lettersCount()}")

    if ("hello".contains("he")){
        println(true)
    }
    if ("he" in "hello"){
        println(true)
    }

    val  rmb = Money(7.0,"¥",1.0)
    val  dollar = Money(6.0,"$",7.1264772)
    Money.printMoneyOperation(dollar,dollar,"+")
    Money.printMoneyOperation(dollar,dollar,"-")
    Money.printMoneyOperation(dollar,rmb,"+")
    Money.printMoneyOperation(dollar,rmb,"-")
    Money.printMoneyOperation(rmb,dollar,"+")
    Money.printMoneyOperation(rmb,dollar,"-")
    Money.printMoneyOperation(rmb,rmb,"+")
    Money.printMoneyOperation(rmb,rmb,"-")
    println("Dollar(6.0) + RMB(7.0) is ${(Dollar(6.0) + RMB(7.0)).value}${(Dollar(6.0) + RMB(7.0)).getCurrency()}.\n" +
            "RMB(7.0) + Dollar(6.0) is ${(RMB(7.0) + Dollar(6.0)).value}${(RMB(7.0) + Dollar(6.0)).getCurrency()}.")
}

fun String.lettersCount(): Int{
    var count = 0
    for (char in this){
        if (char.isLetter()){
            count++
        }
    }
    return count
}

class Money(val value: Double, val currency: String, val exchangeRate: Double = 1.0){

    operator fun plus(money: Money): Money {
        val convertedValue = money.value * (money.exchangeRate / this.exchangeRate)
        return Money(this.value + convertedValue, this.currency, this.exchangeRate)
    }
    operator fun minus(money: Money): Money {
        val convertedValue = money.value * (money.exchangeRate / this.exchangeRate)
        return Money(this.value - convertedValue, this.currency, this.exchangeRate)
    }
    companion object {
        fun printMoneyOperation(money1: Money, money2: Money, operation: String): Money {
            val result = when (operation) {
                "+" -> money1 + money2
                "-" -> money1 - money2
                else -> throw IllegalArgumentException("Unsupported operation: $operation")
            }
            println("${money1.value}${money1.currency} $operation ${money2.value}${money2.currency} is ${result.value}${result.currency}.")
            return result
        }
    }

}
class RMB(val value: Double){

    operator fun plus(dollar: Dollar): RMB {
        val sum = value + (dollar.value * 7.1264772)
        return RMB(sum)
    }
    operator fun plus(rmb: RMB): RMB {
        val sum = value + rmb.value
        return RMB(sum)
    }

    fun getCurrency(): String{
        return "¥"
    }

}
class Dollar(val value: Double){

    operator fun plus(rmb: RMB): Dollar {
        val sum = value + (rmb.value * 0.140322)
        return Dollar(sum)
    }
    operator fun plus(dollar: Dollar): Dollar {
        val sum = value + dollar.value
        return Dollar(sum)
    }
    fun getCurrency(): String{
        return "$"
    }

}

object StringUtil {
    fun lettersCount(str: String): Int {
        var count = 0
        for (char in str) {
            if (char.isLetter()) {
                count++
            }
        }
        return count
    }
}