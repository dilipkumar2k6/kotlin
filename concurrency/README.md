# Summary
- Kotlin coroutines are extremely inexpensive in comparison to threads. Each time when we want to start a new computation asynchronously, we can create a new coroutine.
- To start a new coroutine we use one of the main "coroutine builders": `launch`, `async`, or `runBlocking`. Different libraries can define additional coroutine builders.
- `async` starts a new coroutine and returns a Deferred object. Deferred represents a concept known by other names such as Future or Promise: it stores a computation, but it defers the moment you get the final result; it promises the result sometime in the future.
- The main difference between `async` and `launch` is that `launch` is used for starting a computation that isn't expected to return a specific result. launch returns Job, which represents the coroutine. It is possible to wait until it completes by calling `Job.join()`.
- `Deferred` is a generic type which extends `Job`. An `async` call can return a `Deferred<Int>` or `Deferred<CustomType>` depending on what the lambda returns (the last expression inside the lambda is the result).
- To get the result of a coroutine, we call `await()` on the Deferred instance. While waiting for the result, the coroutine that this `await` is called from, `suspends`:
```
import kotlinx.coroutines.*

fun main() = runBlocking {
    val deferred: Deferred<Int> = async {
        loadData()
    }
    println("waiting...")
    println(deferred.await())
}

suspend fun loadData(): Int {
    println("loading...")
    delay(1000L)
    println("loaded!")
    return 42
}
```

- runBlocking is used as a bridge between regular and suspend functions, between blocking and non-blocking worlds. 
- 
# awaitAll
- If there is a list of deferred objects, it is possible to call awaitAll to await the results of all of them:
```
import kotlinx.coroutines.*

fun main() = runBlocking {
    val deferreds: List<Deferred<Int>> = (1..3).map {
        async {
            delay(1000L * it)
            println("Loading $it")
            it
        }
    }
    val sum = deferreds.awaitAll().sum()
    println("$sum")
}
```



