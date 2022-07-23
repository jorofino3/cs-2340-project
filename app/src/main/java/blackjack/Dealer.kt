package blackjack

import java.util.*

class Dealer(private var deck: Deck = Deck(), var hand: Hand = Hand()) {

    fun deal(hand: Hand, faceUp: Boolean = true) {
        if (deck.isEmpty()) {
            deck = Deck()
        }

        val card: Card = deck.getTopCard()

        hand.cards.add(Card(card.suite, card.rank, faceUp))
    }

    fun payout() {
        TODO("Not yet implemented")
    }

    fun play(game: Game, transitionQueue: Queue<GameTransition>) {
        //Dealer turns card that is face down face up
        for (i in hand.cards.indices) {
            if (!hand.cards[i].faceUp) {
                hand.cards[i] = Card(hand.cards[i].suite, hand.cards[i].rank)
                transitionQueue.add(FlipTransition(game.copy()))
            }
        }

        //Check total of current hand
        //If hand is below 16, keep drawing
        //If hand is at least 17, stand
        while (hand.getHardValue() < 16) {
            deal(hand)
            transitionQueue.add(DealTransition(game.copy()))
        }
    }

    fun settle(player: Player, betAmount: Double, hand: Hand) {
        player.bank += betAmount
        player.hands.remove(hand)
    }

    fun copy(): Dealer {
        return Dealer(deck.copy(), hand.copy())
    }

}