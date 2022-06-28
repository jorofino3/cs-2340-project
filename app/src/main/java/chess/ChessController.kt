package chess

import android.widget.GridLayout
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move
import java.lang.Exception
import java.lang.IllegalArgumentException

class ChessController(tileIdArr: Array<Int>) {
    private val tileIdSquareMap = mutableMapOf<Int, Square>()

    init {
        if (tileIdArr.size < 64) {
            throw Exception()
        }

        val squareValues = Square.values()

        for (i in 0..63) {
            tileIdSquareMap[tileIdArr[i]] = squareValues[i]
        }
    }

    fun movePiece(board: Board, tileIdOne: Int, tileIdTwo: Int) {
        val squareOne = getSquare(tileIdOne)
        val squareTwo = getSquare(tileIdTwo)

        board.doMove(Move(squareOne, squareTwo))
    }

    fun undo() {
        TODO()
    }

    fun resign() {
        TODO()
    }

    private fun getSquare(tileId: Int): Square {
        if (tileIdSquareMap[tileId] == null) {
            throw IllegalArgumentException("Not a valid tileId")
        }

        return tileIdSquareMap[tileId]!!
    }

}