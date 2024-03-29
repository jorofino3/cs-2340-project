package blackjack

class Hand(val cards: ArrayList<Card> = arrayListOf(), val betAmount: Int = 0) {
    fun getValue(): Int {
        var total = 0
        var numAces = 0

        for (card in cards) {
            total += card.value
            if (card.rank == Rank.ACE) {
                numAces++
            }
        }

        while (total > 21 && numAces > 0) {
            total -= 10
            numAces -= 1
        }

        return total
    }

    fun copy(): Hand {
        val cards: ArrayList<Card> = arrayListOf()

        for (card in this.cards) {
            cards.add(card.copy())
        }

        return Hand(cards, this.betAmount)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Hand) {
            return false
        }

        for (i in cards.indices) {
            if (cards[i] != other.cards[i]) {
                return false
            }
        }

        return other.betAmount == betAmount
    }

    fun getHardValue(): Int {
        var total = 0

        for (card in cards) {
            total += card.value
        }

        return total
    }

    override fun toString(): String {
        var s = ""

        for (card in cards) {
            s += card
            s += ", "
        }
        return s
    }
}