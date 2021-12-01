# Summary
- Writing code with a shared mutable state is known to be quite difficult and error-prone (even in this tutorial we had a chance encounter while implementing the solution using callbacks). 
- Sharing information by communication instead of sharing information using the common mutable state tries to simplify this. 
- Coroutines can communicate with each other via channels.
- Channels are communication primitives that allow us to pass data between different coroutines. 
- One coroutine can send some information to a channel, while the other one can receive this information from it:
![](https://play.kotlinlang.org/resources/hands-on/Introduction%20to%20Coroutines%20and%20Channels/assets/8-channels/UsingChannel.png)
- A coroutine that sends (produces) information is often called a producer, and a coroutine that receives (consumes) information is called a consumer. 
- When needed, many coroutines can send information to the same channel, and many coroutines can receive information from it:
![](https://play.kotlinlang.org/resources/hands-on/Introduction%20to%20Coroutines%20and%20Channels/assets/8-channels/UsingChannelManyCoroutines.png)
- Note that when many coroutines receive information from the same channel, each element is handled only once by one of the consumers; handling it automatically means removing this element from the channel.
- We can think of a channel as similar to a collection of elements (the direct analog would be a queue: elements are added to the one end and received from the another). 
- However, there's an important difference: unlike collections, even in their synchronized versions a channel can suspend send and receive operations. This happens when the channel is empty or full (the channel's size might be constrained, and then it can be full).
- Channel is represented via three different interfaces: `SendChannel`, `ReceiveChannel`, and `Channel` which extends the first two. 
- You usually create a channel and give it to producers as a `SendChannel` instance, so that only they can send to it, and to consumers as `ReceiveChannel` instance, so that only they can receive from it. Note that both send and receive methods are declared as `suspend`:
```
interface SendChannel<in E> {
    suspend fun send(element: E)
    fun close(): Boolean
}

interface ReceiveChannel<out E> {
    suspend fun receive(): E
}    

interface Channel<E> : SendChannel<E>, ReceiveChannel<E>
```
- The producer can close a channel to indicate that no more elements are coming.
- Several types of channels are defined in the library. 
    - They differ in how many elements they can internally store, and whether the send call can suspend or not. 
    - For all channel types, the receive call behaves in the same manner: it receives an element if the channel is not empty, and otherwise suspends.
- Unlimited channels
![](https://play.kotlinlang.org/resources/hands-on/Introduction%20to%20Coroutines%20and%20Channels/assets/8-channels/UnlimitedChannel.png)
    - An unlimited channel is the closest analog to queue: 
    - producers can send elements to this channel, and it will grow infinitely. 
    - The send call will never be suspended. 
    - If there's no more memory, you'll get an OutOfMemoryException. 
    - The difference with a queue appears when a consumer tries to receive from an empty channel and gets suspended until some new elements are sent to this channel.
 - Bufferred Channel
    ![](https://play.kotlinlang.org/resources/hands-on/Introduction%20to%20Coroutines%20and%20Channels/assets/8-channels/BufferedChannel.png)
    - A buffered channel's size is constrained by the specified number. 
    - Producers can send elements to this channel until the size limit is reached. 
    - All the elements are internally stored. When the channel is full, the next send call on it suspends until more free space appears.
    - "Rendezvous” channel
        ![](https://play.kotlinlang.org/resources/hands-on/Introduction%20to%20Coroutines%20and%20Channels/assets/8-channels/RendezvousChannel.png)
        - The “Rendezvous” channel is a channel without a buffer; 
        - it's the same as creating a buffered channel with zero size. 
        - One of the functions (send or receive) always gets suspended until the other is called. 
        - If the send function is called and there's no suspended receive call ready to process the element, then send suspends. 
        - Similarly, if the receive function is called and the channel is empty - or in other words, there's no suspended send call ready to send the element - the receive call suspends. 
        - The "rendezvous" name ("a meeting at an agreed time and place") refers to the fact that send and receive should "meet on time".
    - Conflated channel
        - A new element sent to the conflated channel will overwrite the previously sent element, so the receiver will always get only the latest element. The send call will never suspend.
        - When you create a channel, you specify its type or the buffer size (if you need a buffered one):



