package flashcards

fun main() {
    println("Input the number of cards:")
    val numCards = readLine()!!.toInt()
    val cards = mutableMapOf<String, String>()
    for (i in 1..numCards) {
        println("The card #$i:")
        var term = readLine()!!
        while (cards.containsKey(term)) {
            println("The card \"$term\" already exists. Try again:")
            term = readLine()!!
        }
        println("The definition of the card #$i:")
        var definition = readLine()!!
        while (cards.containsValue(definition)) {
            println("The definition \"$definition\" already exists. Try again:")
            definition = readLine()!!
        }
        cards[term] = definition
    }
    for ((k, v) in cards) {
        println("Print the definition of \"$k\":")
        val answer = readLine()
        if (answer == v) {
            println("Correct answer.")
        } else {
            print("Wrong answer. The correct one is \"$v\"")
            val filtered = cards.filterValues { it == answer }
            val iterator = filtered.iterator()
            if (iterator.hasNext()) {
                println(", you've just written the definition of \"${iterator.next().key}\".")
            } else {
                println(".")
            }
        }
    }
}
