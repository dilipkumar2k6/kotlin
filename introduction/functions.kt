fun printMessage(message: String) : Unit {
    println(message);
}

fun printMessageWithPrefix(message: String, prefix: String = "Info") : Unit {
    println("[$prefix] $message");
}

fun sum(x: Int, y: Int): Int {
    return x + y;
}

fun multiply(x: Int, y: Int) = x * y;

fun main() {
    printMessage("Run printMessage function");
    printMessageWithPrefix("Hello");                                   // 7
    printMessageWithPrefix(prefix = "Log", message = "Hello");           // 8
    println(sum(1, 2));                                                  // 9
    println(multiply(2, 4));  
}
