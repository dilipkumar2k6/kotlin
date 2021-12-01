# Summary
- Kotlin functions are first class 
    - Can be stored as variable and data structure
    - Can be passed as argument
    - Can be returned from other function
# Higher Order function
- A Higher order function is a function that takes functions a parameters or return a function.
# Lambda
```
val items = listOf(1, 2, 3, 4, 5)

// Lambdas are code blocks enclosed in curly braces.
items.fold(0, { 
    // When a lambda has parameters, they go first, followed by '->'
    acc: Int, i: Int -> 
    print("acc = $acc, i = $i, ") 
    val result = acc + i
    println("result = $result")
    // The last expression in a lambda is considered the return value:
    result
})

// Parameter types in a lambda are optional if they can be inferred:
val joinedToString = items.fold("Elements:", { acc, i -> acc + " " + i })

// Function references can also be used for higher-order function calls:
val product = items.fold(1, Int::times)
```
# Instantiating Function Type
- Use a block within function literal
    - lambda expression
    ```
    { a, b -> a + b }
    ```
    - an anonymus function
    ```
    fun(s: String): Int { return s.toIntOrNull() ?: 0 }
    ```
    - Use a callable reference to existing declaration
        - `::isOdd`
        - `String::toInt`
        - `List<Int>::size`
        - `::Regex`
    - Use instance of custom class
    ```
    class IntTransformer: (Int) -> Int {
        override operator fun invoke(x: Int): Int = TODO()
    }

    val intFunction: (Int) -> Int = IntTransformer()
    ```
    - Complier can infer variable types if enough information is provided
    ```
    val a = { i: Int -> i + 1 } // The inferred type is (Int) -> Int
    ```

    # Invoking a function type instance
    - A value of a function type can be invoked by using its 
        - invoke(...) 
        - f.invoke(x) 
        - f(x)
    ```
    val stringPlus: (String, String) -> String = String::plus
    val intPlus: Int.(Int) -> Int = Int::plus

    println(stringPlus.invoke("<-", "->"))
    println(stringPlus("Hello, ", "world!"))

    println(intPlus.invoke(1, 1))
    println(intPlus(1, 2))
    println(2.intPlus(3)) // extension-like call
    ```
# lambda expression
- Always surrounded by curley braces
- Parameter declarations in the full syntactic form go inside curly braces and have optional type annotations.
- The body goes after the ->.
- If the inferred return type of the lambda is not Unit, the last (or possibly single) expression inside the lambda body is treated as the return value.
- If you leave all the optional annotations out, what's left looks like this:
```
val sum = { x: Int, y: Int -> x + y }
```
# Passing trailing lambdas
- if the last parameter of a function is a function, then a lambda expression passed as the corresponding argument can be placed outside the parentheses:
```
val product = items.fold(1) { acc, e -> acc * e }
```
- It's very common for a lambda expression to have only one parameter.
- If the compiler can parse the signature without any parameters, the parameter does not need to be declared and -> can be omitted. 
- The parameter will be implicitly declared under the name it:
```
ints.filter { it > 0 } // this literal is of type '(it: Int) -> Boolean'
```
# Returning a value from a lambda expression﻿
- You can explicitly return a value from the lambda using the qualified return syntax. 
- Otherwise, the value of the last expression is implicitly returned.
```
ints.filter {
    val shouldFilter = it > 0
    shouldFilter
}

ints.filter {
    val shouldFilter = it > 0
    return@filter shouldFilter
}
```
# Underscore for unused variables﻿
If the lambda parameter is unused, you can place an underscore instead of its name:
```
map.forEach { _, value -> println("$value!") }
```
# Anonymous function
- Its body can be either an expression:

```
fun(x: Int, y: Int): Int = x + y
```
- or a block:
```
fun(x: Int, y: Int): Int {
    return x + y
}
```



