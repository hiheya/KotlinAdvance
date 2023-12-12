inline fun num1AndNum2(num1: Int, num2: Int, operation: (Int, Int) -> Int): Int {
    return operation(num1, num2)
}
fun plus(num1: Int, num2: Int): Int {
    return num1 + num2
}
fun minus(num1: Int, num2: Int): Int {
    return num1 - num2
}
fun StringBuilder.build(block: StringBuilder.() -> Unit): StringBuilder{
    block()
    return this
}
inline fun printString(str: String, block: (String) -> Unit) {
    println("printString begin")
    block(str)
    println("printString end")
}
inline fun runRunnable(crossinline block: () -> Unit) {
    val runnable = Runnable {
        block()
    }
    runnable.run()
}
fun main() {
    println("main start")
    val str = ""
    printString(str) {s ->
        println("lambda start")
        if (s.isEmpty()) return
        println(s)
        println("lambda end")
    }
    println("main end")
    val num1 = 100
    val num2 = 80
    val result1 = num1AndNum2(num1, num2, ::plus)
    val result2 = num1AndNum2(num1, num2, ::minus)
    val result3 = num1AndNum2(num1, num2) { n1, n2 ->
        n1 % n2
    }
    val result4 = num1AndNum2(num1, num2) { n1, n2 ->
        n1 * n2
    }
    println("result is $result1")
    println("result is $result2")
    println("result is $result3")
    println("result is $result4")
    val word = listOf("Apple", "Banana", "Orange", "Pear", "Grape")
    val result = StringBuilder().build{
        append("start eating fruits.\n")
        for (fruit in word){
            append(fruit).append("\n")
        }
        append("ate all fruits.")
    }
    println(result)
}