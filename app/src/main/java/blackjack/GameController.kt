package blackjack

import com.example.epikgames.activities.BlackJackActivity
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList

class GameController {

    fun initGame(vararg playerName: String): Game {
        val game = Game()

        for (name in playerName) {
            game.addPlayer(name)
        }

        return game
    }

    fun placeBet(player: Player, amount: Int) {
        player.roundOver = false
        player.placeInitialBet(amount)
    }

    fun dealFirstRound(game: Game, transitionQueue: Queue<GameTransition>) {

        val players = game.players
        val dealer = game.dealer
        val dealerHand = game.dealer.hand

        for (player in players) {
            if (player.hands.size == 0) {
                throw NullPointerException("Hand does not exist")
            }

            if (player.hands[0].cards.size > 0) {
                throw IllegalArgumentException("First round has already been dealt")
            }
        }

        for (i in 1..2) {
            for (player in players) {
                val hand: Hand = player.hands[0]
                dealer.deal(hand)
                transitionQueue.add(DealTransition(game.copy()))
            }
            dealer.deal(dealerHand, i % 2 == 1)
            transitionQueue.add(DealTransition(game.copy()))
        }

        if ((dealerHand.cards[0].value == 10 || dealerHand.cards[0].value == 11)
            && dealerHand.getValue() == 21) {
            dealer.flipCard()
            transitionQueue.add(FlipTransition(game.copy()))
            for (player in players) {
                if (player.hands[0].getValue() == 21) {
                    game.dealer.settle(player, player.hands[0].betAmount.toDouble(), player.hands[0])
                }
                game.dealer.settle(player, 0.0, player.hands[0])
                transitionQueue.add(SettlementTransition(game.copy()))
            }
            dealer.resetCards()
            transitionQueue.add(ResetTransition(game.copy()))
            return
        }

        for (player in game.players) {
            if (player.hands[0].getValue() == 21) {
                game.dealer.settle(player, 2.5 * player.hands[0].betAmount, player.hands[0])
                transitionQueue.add(SettlementTransition(game.copy()))
            }
        }

        if (roundOverForPlayers(game)) {
            dealer.resetCards()
            transitionQueue.add(ResetTransition(game.copy()))
        }
    }

    fun roundOverForPlayers(game: Game): Boolean {
        for (player in game.players) {
            if (!player.roundOver) {
                return false
            }
        }
        return true
    }

    fun stand(game: Game): Boolean {
        if (game.getCurrentPlayer() == null) {
            throw IllegalArgumentException("Game has no players")
        }

        game.getCurrentPlayer()!!.stand()

        return game.moveToNextPlayer()
    }

    fun hit (game: Game, transitionQueue: Queue<GameTransition>) {
        val player = game.getCurrentPlayer()
        if (player != null && player.hands.size > 0) {
            player.hit(game)
            transitionQueue.add(DealTransition(game.copy()))

            if (player.roundOver) {
                game.dealer.settle(player, 0.0, player.hands[0])
                transitionQueue.add(SettlementTransition(game.copy()))
                game.dealer.resetCards()
                transitionQueue.add(ResetTransition(game.copy()))
            }
        }

    }

    fun endRound(game: Game, transitionQueue: Queue<GameTransition>) {
        val dealer = game.dealer

        dealer.play(game, transitionQueue)

        for (player in game.players) {
            if (player.hands.size > 0) {
                if (dealer.hand.getValue() > player.hands[0].getValue()) {
                    game.dealer.settle(player, 0.0, player.hands[0])
                } else if (dealer.hand.getValue() == player.hands[0].getValue()) {
                    game.dealer.settle(player, player.hands[0].betAmount.toDouble(), player.hands[0])
                } else {
                    game.dealer.settle(player, 2 * player.hands[0].betAmount.toDouble(), player.hands[0])
                }

                transitionQueue.add(SettlementTransition(game.copy()))
            }
        }
        game.dealer.resetCards()
        transitionQueue.add(ResetTransition(game.copy()))
    }
}