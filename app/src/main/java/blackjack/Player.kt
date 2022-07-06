package blackjack

class Player(val name: String, var bank: Double = 1000.0) {

    val hands = arrayOfNulls<Hand>(4)

    fun placeInitialBet(amount: Int) {
        if (amount < 2 || amount > 500) {
            throw IllegalArgumentException("Not a valid amount")
        }
        hands[0] = Hand(betAmount = amount)
        bank -= amount
    }

    fun stand() {
        TODO("Not yet implemented")
    }

    fun hit() {
        TODO("Not yet implemented")
    }

    fun split() {
        TODO("Not yet implemented")
    }

    fun doubleDown() {
        TODO("Not yet implemented")
    }
}