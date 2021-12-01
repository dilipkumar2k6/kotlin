# Summary
- A coroutine is an instance of suspendable computation. 
- It is conceptually similar to a thread, in the sense that it takes a block of code to run that works concurrently with the rest of the code. 
- However, a coroutine is not bound to any particular thread. It may suspend its execution in one thread and resume in another one.
- Coroutines can be thought of as light-weight threads, but there is a number of important differences that make their real-life usage very different from threads.
Sample non blocking code
```
fun main() = runBlocking { // this: CoroutineScope
    launch {
        delay(100L)
        println("World")
    }
    println("Hello")
}
```
- `launch` is a coroutine builder. It launches new coroutine concurrently with rest of the code, which continous to work independetly. 
- `delay` is a special suspending function. It suspends the coroutine for a specific time. Suspending a coroutine does not block the underlying thread, but allows other coroutines to run and use the underlying thread for their code.
- `runBlocking` is also a coroutine builder that bridges the non-coroutine world of a regular fun main() and the code with coroutines inside of runBlocking { ... } curly braces. This is highlighted in an IDE by this: CoroutineScope hint right after the runBlocking opening curly brace.
    - The name of runBlocking means that the thread that runs it (in this case — the main thread) gets blocked for the duration of the call, until all the coroutines inside runBlocking { ... } complete their execution.
- 
# Extract function refactoring
```
fun main() = runBlocking { // this: CoroutineScope
    launch { doWorld() }
    println("Hello")
}

// this is your first suspending function
suspend fun doWorld() {
    delay(1000L)
    println("World!")
}
```
# coroutineScope
- In addition to the coroutine scope provided by different builders, it is possible to declare your own scope using the coroutineScope builder. 
- It creates a coroutine scope and does not complete until all launched children complete.
- runBlocking and coroutineScope builders may look similar because they both wait for their body and all its children to complete. 
- The main difference is that the runBlocking method blocks the current thread for waiting, while coroutineScope just suspends, releasing the underlying thread for other usages. 
- Because of that difference, runBlocking is a regular function and coroutineScope is a suspending function.
```
fun main() = runBlocking {
    doWorld()
}

suspend fun doWorld() = coroutineScope {  // this: CoroutineScope
    launch {
        delay(1000L)
        println("World!")
    }
    println("Hello")
}
```
# Scope builder and concurrency
- A coroutineScope builder can be used inside any suspending function to perform multiple concurrent operations. 
- Let's launch two concurrent coroutines inside a doWorld suspending function:
```
// Sequentially executes doWorld followed by "Done"
fun main() = runBlocking {
    doWorld()
    println("Done")
}

// Concurrently executes both sections
suspend fun doWorld() = coroutineScope { // this: CoroutineScope
    launch {
        delay(2000L)
        println("World 2")
    }
    launch {
        delay(1000L)
        println("World 1")
    }
    println("Hello")
}
```
# An explicit job﻿
- A launch coroutine builder returns a Job object that is a handle to the launched coroutine and can be used to explicitly wait for its completion. 
- For example, you can wait for completion of the child coroutine and then print "Done" string:
```
val job = launch { // launch a new coroutine and keep a reference to its Job
    delay(1000L)
    println("World!")
}
println("Hello")
job.join() // wait until child coroutine completes
println("Done") 
```