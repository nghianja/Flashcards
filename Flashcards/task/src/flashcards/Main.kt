package flashcards

import java.io.File
import java.io.FileNotFoundException

val cards = mutableMapOf<String, String>()
val stats = mutableMapOf<String, Int>()
val logs = mutableListOf<String>()

val logLine: (String) -> String = {
    logs.add(it)
    it
}

fun addCard() {
    println(logLine("The card:"))
    val term = logLine(readLine()!!)
    if (cards.containsKey(term)) {
        println(logLine("The card \"$term\" already exists."))
        return
    }
    println(logLine("The definition of the card:"))
    val definition = logLine(readLine()!!)
    if (cards.containsValue(definition)) {
        println(logLine("The definition \"$definition\" already exists."))
        return
    }
    cards[term] = definition
    println(logLine("The pair (\"$term\":\"$definition\") has been added."))
}

fun removeCard() {
    println(logLine("The card:"))
    val term = logLine(readLine()!!)
    if (!cards.containsKey(term)) {
        println(logLine("Can't remove \"$term\": there is no such card."))
        return
    }
    if (cards.remove(term) != null) {
        stats.remove(term)
        println(logLine("The card has been removed."))
    }
}

fun loadCards() {
    try {
        println(logLine("File name:"))
        val filename = logLine(readLine()!!)
        var count = 0
        File(filename).forEachLine {
            if (it.isNotBlank()) {
                val tokens = it.split(":")
                cards[tokens[0]] = tokens[1]
                stats[tokens[0]] = tokens[2].toInt()
                count++
            }
        }
        println(logLine("$count cards have been loaded."))
    } catch (fnfe: FileNotFoundException) {
        println(logLine("File not found."))
    }
}

fun saveCards() {
    try {
        println(logLine("File name:"))
        val filename = logLine(readLine()!!)
        var text = ""
        for ((k, v) in cards) {
            val m = stats.getOrDefault(k, 0)
            text += "$k:$v:$m\n"
        }
        File(filename).writeText(text)
        println(logLine("${cards.size} cards have been saved."))
    } catch (fnfe: FileNotFoundException) {
        println(logLine("File not found."))
    }
}

fun askCard() {
    val keys = cards.keys
    println(logLine("How many times to ask?"))
    val times = logLine(readLine()!!).toInt()
    for (i in 1..times) {
        val k = keys.random()
        val v = cards[k]!!
        println(logLine("Print the definition of \"$k\":"))
        val answer = logLine(readLine()!!)
        if (answer == v) {
            println(logLine("Correct answer."))
        } else {
            val filtered = cards.filterValues { it == answer }
            val iterator = filtered.iterator()
            if (iterator.hasNext()) {
                println(logLine("Wrong answer. The correct one is \"$v\", you've" +
                    " just written the definition of \"${iterator.next().key}\"."))
            } else {
                println(logLine("Wrong answer. The correct one is \"$v\"."))
            }
            stats[k] = stats.getOrDefault(k, 0) + 1
        }
    }
}

fun saveLog() {
    try {
        println(logLine("File name:"))
        val filename = logLine(readLine()!!)
        File(filename).writeText(logs.joinToString(postfix = "\n"))
        println(logLine("The log has been saved."))
    } catch (fnfe: FileNotFoundException) {
        println(logLine("File not found."))
    }
}

fun getHardest() {
    val hardest = stats.maxBy { it.value }
    if (hardest == null) {
        println(logLine("There are no cards with errors."))
        return
    }
    val hardestCards = stats.filterValues { it > 0 && it == hardest.value }
    var output: String
    if (hardestCards.size > 1) {
        output = "The hardest cards are "
        val terms = mutableListOf<String>()
        hardestCards.forEach {
            terms.add("\"${it.key}\"")
        }
        output += terms.joinToString(", ") + ". You have ${hardest.value} errors answering them."
    } else {
        output = "The hardest card is \"${hardest.key}\". You have ${hardest.value} errors answering them."
    }
    println(logLine(output))
}

fun resetStats() {
    stats.clear()
    println(logLine("Card statistics has been reset."))
}

fun main() {
    loop@ while (true) {
        println(logLine("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):"))
        when (logLine(readLine()!!)) {
            "add" -> addCard()
            "remove" -> removeCard()
            "import" -> loadCards()
            "export" -> saveCards()
            "ask" -> askCard()
            "exit" -> {
                println(logLine("Bye bye!"))
                break@loop
            }
            "log" -> saveLog()
            "hardest card" -> getHardest()
            "reset stats" -> resetStats()
        }
        println(logLine(""))
    }
}
