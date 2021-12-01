fun main() {

    infix fun Int.times(str: String) = str.repeat(this);
    println(2 times "Bye")

    val pair = "Ferrari" to "Katrina";
    println(pair);

    infix fun String.onto(other: String) = Pair(this, other);
    val myPair = "McLaren" onto "Lucas";
    println(myPair);
  


    val sophie =  Person("Sophie");
    val claudia =  Person("Claudia");
    sophie likes claudia;
}

class Person (val name: String) {
    val likedPeople = mutableListOf<Person>();
    infix fun likes(other: Person) {
        likedPeople.add(other);
    }
}