## 六、泛型和委托

### 6.1 泛型的基本用法

准确来讲，泛型并不是什么新鲜的事物。Java早在1.5版本中就引入了泛型的机制，Kotlin自然也就支持了泛型功能。但是Kotlin中的泛型和Java中的泛型有同有异。

首先解释一下什么是泛型。在一般的编程模式下，我们需要给任何一个变量指定一个具体的类型，而**泛型允许我们在不指定具体类型的情况下进行编程，这样编写出来的代码将会拥有更好的扩展性。**

举个栗子🌰：List是一个可以存放数据的列表，但是List并没有限制我们只能存放整型数据或字符串数据，**因为它没有指定一个具体的类型，而是使用泛型来实现的**。也正是如此，我们才可以使用`List<Int>`、`List<String>`之类的语法来构建具体类型的列表。

那么要怎样才能定义自己的泛型实现呢？这里先来学习一下基本的语法。

泛型主要有两种定义方式：**一种是定义泛型类**，**另一种是定义泛型方法**，使用的语法结构都是`<T>`。**当然括号内的T并不是固定要求的，事实上你使用任何英文字母或单词都可以，但是通常情况下，T是一种约定俗成的泛型写法。**

如果我们要定义一个泛型类，就可以这么写：

```kotlin
class MyClass<T> {
    fun method(param: T): T{
        return param
    }
}
```

此时的MyClass就是一个泛型类，MyClass中的方法允许使用T类型的参数和返回值。

我们在调用MyClass类和method()方法的时候，就可以将泛型指定成具体的类型，如下所示：

```kotlin
val myClass = MyClass<Int>()
val result = myClass.method(123)
```

这里我们将MyClass类的泛型指定成Int类型，于是`method()`方法就可以接收一个Int类型的参数，并且它的返回值也变成了Int类型。

而如果我们不想定义一个泛型类，只是想定义一个泛型方法，应该要怎么写呢？也很简单，只需要将定义泛型的语法结构写在方法上面就可以了，如下所示：

```kotlin
class MyClass {
    fun <T> method(param: T): T {
        return param
    }
}
```

此时的调用方式也需要进行相应的调整：

```kotlin
val myClass = MyClass()
val result = myClass.method<Int>(123)
```

可以看到，现在是在调用method()方法的时候指定泛型类型了。另外，Kotlin还拥有非常出色的**类型推导机制**，例如**我们传入了一个Int类型的参数，它能够自动推导出泛型的类型就是Int型**，因此这里也可以直接省略泛型的指定：

```kotlin
val myClass = MyClass()
val result = myClass.method(123)
```

Kotlin**还允许我们对泛型的类型进行限制**。目前你可以将`method()`方法的泛型指定成任意类型，但是如果这并不是你想要的话，还**可以通过指定上界的方式来对泛型的类型进行约束**，**比如这里将`method()`方法的泛型上界设置为Number类型**，如下所示：

```kotlin
class MyClass {
    fun <T: Number> method(param: T): T{
        return param
    }
}
```

这种写法就表明，**我们只能将method()方法的泛型指定成数字类型**，比如`Int`、`Float`、`Double`等。但是**如果你指定成字符串类型，就肯定会报错，因为它不是一个数字。**

另外，**在默认情况下，所有的泛型都是可以指定成可空类型的**，这是因为在**不手动指定上界的时候**，**泛型的上界默认是`Any?`**。而如果**想要让泛型的类型不可为空**，只需要将泛型的**上界**手动**指定成**`Any`就可以了。

之前高阶函数那节有编写一个 build函数代码如下：

```kotlin
fun StringBuilder.build(block: StringBuilder.() -> Unit): StringBuilder {
    block()
    return this
}
```

这个函数的作用和`apply`函数基本是一样的，只是`build`函数只能作用在`StringBuilder`类上面，而`apply`函数是可以作用在**所有类**上面的。现在我们就通过本小节所学的泛型知识对build函数进行扩展，让它实现和apply函数完全一样的功能。

思考一下，其实并不复杂，只需要使用<T>将build函数定义成泛型函数，再将原来所有强制指定StringBuilder的地方都替换成T就可以了。新建一个build.kt文件，并编写如下代码：

```kotlin
fun <T> T.build(block: T.() -> Unit): T{
    block()
    return this
}
```

### 6.2 类委托和委托属性

**委托是一种设计模式，它的基本理念是：操作对象自己不会去处理某段逻辑，而是会把工作委托给另外一个辅助对象去处理。**这个概念对于Java程序员来讲可能相对比较陌生，因为Java对于委托并没有语言层级的实现，而像C#等语言就对委托进行了原生的支持。Kotlin中也是支持委托功能的，并且将**委托功能分为了两种：类委托和委托属性。**下面我们逐个进行学习。

#### 6.2.1 类委托

首先来看**类委托**，**它的核心思想在于将一个类的具体实现委托给另一个类去完成**。我们曾经使用过Set这种数据结构，它和**List**有点类似，只是它所存储的数据是无序的，并且不能存储重复的数据。**Set**是一个接口，如果要使用它的话，需要使用它具体的实现类，比如**HashSet**。而借助于委托模式，我们可以轻松实现一个自己的实现类。比如这里定义一个**MySet**，并让它实现**Set**接口，代码如下所示：

```kotlin
class MySet<T>(val helperSet: HashSet<T>) : Set<T>{
    override val size: Int
        get() = helperSet.size

    override fun isEmpty() = helperSet.isEmpty()

    override fun iterator(): Iterator<T> {
        return helperSet.iterator()
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return helperSet.containsAll(elements)
    }

    override fun contains(element: T): Boolean {
        return helperSet.contains(element)
    }
}
```

可以看到，`MySet`的构造函数中接收了一个`HashSet`参数，这就相当于一个辅助对象。然后在`Set`接口所有的方法实现中，我们都没有进行自己的实现，而是调用了辅助对象中相应的方法实现，这其实就是一种委托模式。

那么，这种写法的**好处**是什么呢？既然都是调用辅助对象的方法实现，那还不如直接使用辅助对象得了。这么说确实没错，但如果我们只是让大部分的方法实现调用辅助对象中的方法，少部分的方法实现由自己来重写，甚至加入一些自己独有的方法，那么`MySet`就会成为一个全新的数据结构类，这就是委托模式的意义所在。

但是这种写法也有一定的弊端，如果接口中的待实现方法比较少还好，要是有几十甚至上百个方法的话，每个都去这样调用辅助对象中的相应方法实现，那可真是要写哭了。那么这个问题有没有什么解决方案呢？在`Java`中确实没有，但是在`Kotlin`中可以通过类委托的功能来解决。

**Kotlin中委托使用的关键字是by**，我们只需要在接口声明的后面使用by关键字，再接上受委托的辅助对象，就可以免去之前所写的一大堆模板式的代码了，如下所示：使用类委派机制：

```kotlin
class MySet<T>(val helperSet: HashSet<T>) : Set<T> by helperSet{
    
}
```

这两段代码在功能上是一样的。它们都定义了一个名为 `MySet` 的类，这个类实现了 `Set` 接口，并且使用 `HashSet` 作为辅助工具来提供 `Set` 接口的实现。

然而，它们在实现方式上有所不同：

- 第一段代码中，`MySet` 类显式地实现了 `Set` 接口的每一个方法。每一个方法的实现都是通过调用 `helperSet` 的对应方法来完成的。
- 第二段代码中，`MySet` 类使用了 Kotlin 的类委托特性，将 `Set` 接口的所有方法的实现委托给了 `helperSet` 对象。这意味着 `MySet` 类会自动拥有 `Set` 的所有方法，并且这些方法的实现会直接使用 `helperSet` 的对应方法。

所以，虽然这两段代码在功能上是一样的，但是第二段代码更简洁，因为它利用了 Kotlin 的类委托特性，避免了手动实现每一个方法的需要。

在第二段代码中，如果我们要对某个方法进行重新实现，只需要单独重写那一个方法就可以了，其他的方法仍然可以享受类委托所带来的便利。

#### 6.2.2 委托属性

掌握了类委托之后，接下来我们开始学习委托属性。它的基本理念也非常容易理解，真正的难点在于如何灵活地进行应用。

类委托的核心思想是将一个类的具体实现委托给另一个类去完成，而委托属性的核心思想是将一个属性（字段）的具体实现委托给另一个类去完成。

我们看一下委托属性的语法结构，如下所示：

```kotlin
class MyClass {
    var p by Delegate()
}
```

可以看到，这里使用by关键字连接了左边的p属性和右边的`Delegate`实例，这是什么意思呢？这种写法就代表着将p属性的具体实现委托给了`Delegate`类去完成。当调用p属性的时候会自动调用`Delegate`类的`getValue()`方法，当给p属性赋值的时候会自动调用`Delegate`类的`setValue()`方法。

因此，我们还得对Delegate类进行具体的实现才行，代码如下所示：

```kotlin
class Delegate {
    var propValue : Any? = null
    operator fun getValue(myClass: MyClass, prop: KProperty<*>): Any? {
        return propValue
    }
    
    operator fun setValue(myClass: MyClass, prop: KProperty<*>, value: Any?) {
        propValue = value
    }
}
```

这是一种标准的代码实现模板，在Delegate类中我们必须实现`getValue()`和`setValue()`这两个方法，并且都要使用operator关键字进行声明。

`getValue()`方法要接收两个参数：第一个参数用于声明该Delegate类的委托功能可以在什么类中使用，这里写成MyClass表示仅可在MyClass类中使用；第二个参数`KProperty<*>`是Kotlin中的一个属性操作类，可用于获取各种属性相关的值，在当前场景下用不着，但是必须在方法参数上进行声明。另外，`<*>`这种泛型的写法表示你不知道或者不关心泛型的具体类型，只是为了通过语法编译而已，有点类似于Java中`<?>`的写法。至于返回值可以声明成任何类型，根据具体的实现逻辑去写就行了，上述代码只是一种示例写法。

`setValue()`方法也是相似的，只不过它要接收3个参数。前两个参数和`getValue()`方法是相同的，最后一个参数表示具体要赋值给委托属性的值，这个参数的类型必须和`getValue()`方法返回值的类型保持一致。

整个委托属性的工作流程就是这样实现的，现在当我们给`MyClass`的`p`属性赋值时，就会调用`Delegate`类的`setValue()`方法，当获取`MyClass`中`p`属性的值时，就会调用`Delegate`类的`getValue()`方法。

不过，其实还存在一种情况可以不用在`Delegate`类中实现`setValue()`方法，那就是`MyClass`中的p属性是使用`val`关键字声明的。这一点也很好理解，如果`p`属性是使用`val`关键字声明的，那么就意味着`p`属性是无法在初始化之后被重新赋值的，因此也就没有必要实现`setValue()`方法，只需要实现`getValue()`方法就可以了。

#### 6.2.3 实现一个自己的lazy函数

我们**初始化变量**时可以把想要延迟执行的代码放到by lazy代码块中，这样代码块中的代码在一开始的时候就不会执行，只有当**变量**首次被调用的时候，代码块中的代码才会执行。

学习了`Kotlin`的委托功能之后，我们就可以对`by lazy`的工作原理进行解密了，它的基本语法结构如下：

```kotlin
val p by lazy { ... }
```

实际上，`by lazy`并不是连在一起的关键字，只有`by`才是Kotlin中的关键字，`lazy`在这里只是一个高阶函数而已。在`lazy`函数中会创建并返回一个`Delegate`对象，当我们调用p属性的时候，其实调用的是`Delegate`对象的`getValue()`方法，然后`getValue()`方法中又会调用`lazy`函数传入的Lambda表达式，这样表达式中的代码就可以得到执行了，并且调用p属性后得到的值就是Lambda表达式中最后一行代码的返回值。

这样看来，Kotlin的懒加载技术也并没有那么神秘，掌握了它的实现原理之后，我们也可以实现一个自己的`lazy`函数。

```kotlin
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
```

这段代码演示了如何使用延迟初始化来创建 `heavyObject`。只有在 `heavyObject` 第一次被使用时，它才会被创建。这是一种很好的做法，特别是在对象创建代价高昂，且不总是需要该对象的情况下，可以帮助节省资源，提高应用程序的性能。

