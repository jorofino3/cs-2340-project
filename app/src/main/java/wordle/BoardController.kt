package wordle

import WordsList
import kotlin.random.Random


class BoardController() {
    private val words = WordsList.values()

    fun getRandWord(): String {
        val index: Int = Random.nextInt(words.size)
        return words[index].toString().uppercase()
    }

    fun getWords(): Array<WordsList> {
        return words
    }
}