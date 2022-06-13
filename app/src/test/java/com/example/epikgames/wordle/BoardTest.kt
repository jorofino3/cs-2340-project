package com.example.epikgames.wordle

import android.graphics.Color
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertArrayEquals
import org.junit.Test
import wordle.Board
import wordle.HEIGHT
import wordle.Tile
import wordle.WIDTH

class BoardTest {

    private val board: Board = Board()

    @Test
    fun typeLetter() {
        board.type('A')
        assertEquals('A', board.tileArray[0].char)
    }

    @Test
    fun typeLetterAtRowEdge() {
        // Initialization
        board.type('C')
        board.type('R')
        board.type('A')
        board.type('N')
        board.type('E')

        board.type('B')

        assertEquals('E', board.tileArray[WIDTH - 1].char)
        assertEquals(' ', board.tileArray[WIDTH].char)

    }

    @Test
    fun typeLetterWithFullBoard() {
        // Initialization
        for (i in 1..(WIDTH * HEIGHT)) {
            board.type('A')
        }

        board.type('A')
    }

    //Colors for letters are based off of https://mywordle.strivemath.com/?word=yoigd
    @Test
    fun guessWordFuncIrons01() {
        //Set wordList to index 2 for word: irons

        val actualColors: Array<Tile> = Array(30) { i -> Tile(i) }

        actualColors[0] = Tile(0, 'D', Color.GRAY)
        actualColors[1] = Tile(1, 'I', Color.YELLOW)
        actualColors[2] = Tile(2, 'A', Color.GRAY)
        actualColors[3] = Tile(3, 'R', Color.YELLOW)
        actualColors[5] = Tile(4, 'Y', Color.GRAY)

        board.type('D')
        board.type('I')
        board.type('A')
        board.type('R')
        board.type('Y')
        board.guess()

        assertArrayEquals(actualColors, board.tileArray)

    }

    //Test case fails. My code has difficult differentiating between marking duplicate letters as yellow or gray
    @Test
    fun guessWordFuncHello01() {
        //Set wordList to index 0 for word: hello
        val actualColors: Array<Tile> = Array(30) { i -> Tile(i) }

        actualColors[0] = Tile(0, 'L', Color.YELLOW)
        actualColors[1] = Tile(1, 'E', Color.GREEN)
        actualColors[2] = Tile(2, 'V', Color.GRAY)
        actualColors[3] = Tile(3, 'E', Color.GRAY)
        actualColors[4] = Tile(4, 'L', Color.YELLOW)

        board.type('L')
        board.type('E')
        board.type('V')
        board.type('E')
        board.type('L')
        board.guess()

        assertArrayEquals(actualColors, board.tileArray)
    }

    @Test
    fun guessWordFuncCards01() {
        //Set wordList to index 1 for word: cards
        val actualColors: Array<Tile> = Array(30) { i -> Tile(i) }

        actualColors[0] = Tile(0, 'D', Color.YELLOW)
        actualColors[1] = Tile(1, 'E', Color.GRAY)
        actualColors[2] = Tile(2, 'C', Color.YELLOW)
        actualColors[3] = Tile(3, 'K', Color.GRAY)
        actualColors[4] = Tile(4, 'S', Color.GREEN)

        board.type('D')
        board.type('E')
        board.type('C')
        board.type('K')
        board.type('S')
        board.guess()

        assertArrayEquals(actualColors, board.tileArray)

    }
}