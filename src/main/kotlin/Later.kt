import kotlin.reflect.KProperty
fun <T> later(block: () -> T) = Later(block)
class Later<T>(val block: () -> T) {
    private var value: Any? = null
    operator fun getValue(any: Any?, prop: KProperty<*>) : T {
        if (value == null){
            value = block()
        }
        println("value: $value")
        return value as T
    }
}

val heavyObject by later { HeavyObject() }

class HeavyObject {
    init {
        println("HeavyObject is being created.")
    }
    fun use(){
        println("HeavyObject is being used.")
    }
}

fun main() {
    println("main step in")
    // 在这里，heavyObject 还没有被创建
    if (needHeavyObject()) {
        println("if step in")
        // 在这里，heavyObject 被创建
        heavyObject.use()
    }
}

fun needHeavyObject(): Boolean {
    return true
}