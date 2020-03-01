package flashcards

import java.io.File
import java.io.FileNotFoundException

val cards = mutableMapOf<String, String>()

fun addCard() {
    println("The card:")
    val term = readLine()!!
    if (cards.containsKey(term)) {
        println("The card \"$term\" already exists.")
        return
    }
    println("The definition of the card:")
    val definition = readLine()!!
    if (cards.containsValue(definition)) {
        println("The definition \"$definition\" already exists.")
        return
    }
    cards[term] = definition
    println("The pair (\"$term\":\"$definition\") has been added.")
}

fun removeCard() {
    println("The card:")
    val term = readLine()!!
    if (!cards.containsKey(term)) {
        println("Can't remove \"$term\": there is no such card.")
        return
    }
    if (cards.remove(term) != null)
        println("The card has been removed.")
}

fun loadCards() {
    try {
        println("File name:")
        val filename = readLine()!!
        var term = ""
        var definition = ""
        var count = 0
        File(filename).forEachLine {
            if (term == "") {
                term = it
            } else {
                definition = it
                cards[term] = definition
                term = ""
                count++
            }
        }
        println("$count cards have been loaded.")
    } catch (fnfe: FileNotFoundException) {
        println("File not found.")
    }
}

fun saveCards() {
    try {
        println("File name:")
        val filename = readLine()!!
        var text = ""
        for ((k, v) in cards) {
            text += "$k\n$v\n"
        }
        File(filename).writeText(text)
        println("${cards.size} cards have been saved.")
    } catch (fnfe: FileNotFoundException) {
        println("File not found.")
    }
}

fun askCard() {
    val keys = cards.keys
    println("How many times to ask?")
    val times = readLine()!!.toInt()
    for (i in 1..times) {
        val k = keys.random()
        val v = cards[k]!!
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

fun main() {
    loop@ while (true) {
        println("Input the action (add, remove, import, export, ask, exit):")
        when (readLine()!!) {
            "add" -> addCard()
            "remove" -> removeCard()
            "import" -> loadCards()
            "export" -> saveCards()
            "ask" -> askCard()
            "exit" -> {
                println("Bye bye!")
                break@loop
            }
        }
        println()
    }
}
