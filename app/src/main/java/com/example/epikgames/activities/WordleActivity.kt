package com.example.epikgames.activities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.*

import androidx.appcompat.app.AlertDialog

import androidx.core.graphics.drawable.DrawableCompat

import com.example.epikgames.R
import wordle.Board
import wordle.BoardColor
import wordle.BoardController
import wordle.WIDTH
const val keyWidth = 95
const val keyHeight = 130

// For logging values in the Logcat, use Log.d(TAG, message)
const val TAG = "Wordle Activity: "

class WordleActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        val boardC: BoardController = BoardController()
        var board = Board(solution = boardC.getRandWord())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wordle)


        val rulesButton = findViewById<ImageButton>(R.id.wordleRulesButton)
        rulesButton.setOnClickListener {
            val intent = Intent(this, WordleRulesActivity::class.java)
            startActivity(intent)
        }

        val exitButton = findViewById<ImageButton>(R.id.wordleExitButton)
        exitButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        
        val wordleGrid: androidx.gridlayout.widget.GridLayout = this.findViewById(R.id.wordle_grid)

        // Step 1: Add tiles to empty grid layout
        for (tile in board.tileArray) {
            if (tile.id < 5 * board.getRow()) {
                val tileView: View = layoutInflater.inflate(R.layout.wordle_tile, null)
                tileView.id = tile.id
                val tileBox: View = tileView.findViewById(R.id.chess_tile)
                val tileChar: TextView = tileView.findViewById(R.id.tile_char)
                tileChar.text = tile.char.toString()
                tileChar.setTextColor(Color.WHITE)
                var roundedBorder = tileBox.background
                roundedBorder = DrawableCompat.wrap(roundedBorder)
                DrawableCompat.setTint(roundedBorder, Color.parseColor(tile.color.rgb))
                wordleGrid.addView(tileView)
                continue
            }

            println(tile)
            val tileView: View = layoutInflater.inflate(R.layout.wordle_tile, null)
            tileView.id = tile.id
            val tileBox: View = tileView.findViewById(R.id.chess_tile)
            val tileChar: TextView = tileView.findViewById(R.id.tile_char)
            var roundedBorder = tileBox.background
            roundedBorder.setTintList(null)
            tileChar.text = tile.char.toString()
            wordleGrid.addView(tileView)
        }


        // Step 2: Add buttons to rows in keyboard
        val keyboardRowOne: LinearLayout = this.findViewById(R.id.keyboard_row_1)

        for (ch in "QWERTYUIOP") {
            addKeyboardButton(keyboardRowOne, ch.toString(), ch.code)
        }

        val keyboardRowTwo: LinearLayout = this.findViewById(R.id.keyboard_row_2)

        for (ch in "ASDFGHJKL") {
            addKeyboardButton(keyboardRowTwo, ch.toString(), ch.code)
        }

        // Note: For the enter and back keys, the id is the hashcode of the Enter and Back string

        val keyboardRowThree: LinearLayout = this.findViewById(R.id.keyboard_row_3)

        addKeyboardButton(keyboardRowThree, "Enter", "Enter".hashCode(), 2 * keyWidth)


        for (ch in "ZXCVBNM") {
            addKeyboardButton(keyboardRowThree, ch.toString(), ch.code)
        }

        addKeyboardButton(keyboardRowThree, "Back", "Back".hashCode(), 2 * keyWidth)


    }
    

    private fun addKeyboardButton(keyboardRow: LinearLayout, text: String, id: Int,
                                  width: Int = keyWidth) {
        val button = Button(this)
        button.layoutParams = LinearLayout.LayoutParams(width, keyHeight)
        button.text = text
        button.id = id
        if (text.length == 1) {
            setKeyColor(button, board.letterStatus[button.id - 65])
        }
        button.setOnClickListener(this)
        keyboardRow.addView(button)
    }

    override fun onClick(v: View?) {
        if (v == null) {
            return
        }

//        if (v.id == "Enter".hashCode() && board.getCurTile() % WIDTH != (WIDTH - 1)) {
//            showToast()
//            return
//        }

        if (v.id == "Enter".hashCode()) {
            //if there is less than 5 letters typed show error message
            if (!board.canGuess()) {
                showToast()
                return
            }

            board.guess()
            updateBoardGUI()
            updateTileColor()
            if (board.guessCorrect()) {
                val alert: AlertDialog.Builder = AlertDialog.Builder(this)
                val dialogView: View = layoutInflater.inflate(R.layout.wordle_success_pop_up, null)
                alert.setView(dialogView)
                val playAgain: Button = dialogView.findViewById(R.id.play_again)

                playAgain.setOnClickListener {
                    val intent = Intent(this,
                        WordleActivity::class.java)
                    startActivity(intent)
                }

                val quitGame: Button = dialogView.findViewById(R.id.quit_game)
                quitGame.setOnClickListener {
                    val intent = Intent(this,
                        MainActivity::class.java)
                    startActivity(intent)
                }
                board = Board(solution = boardC.getRandWord())
                alert.create()
                alert.show()
            } else if (board.loseGame()) {
                val alert: AlertDialog.Builder = AlertDialog.Builder(this)
                val dialogView: View = layoutInflater.inflate(R.layout.wordle_failure_pop_up, null)
                val failureTextView = dialogView.findViewById<TextView>(R.id.failureTextView)
                failureTextView.text = "Sorry! You ran out of guesses. The correct word was ${board.solution}"
                alert.setView(dialogView)
                val playAgain: Button = dialogView.findViewById(R.id.play_again)

                playAgain.setOnClickListener {
                    val intent = Intent(this,
                        WordleActivity::class.java)
                    startActivity(intent)
                }
                val quitGame: Button = dialogView.findViewById(R.id.quit_game)
                quitGame.setOnClickListener {
                    val intent = Intent(this,
                        MainActivity::class.java)
                    startActivity(intent)
                }
                board = Board(solution = boardC.getRandWord())
                alert.create()
                alert.show()
            }
            return
        }

        if (v.id == "Back".hashCode()) {
            board.delete()
            updateBoardGUI()
            return
        }

        board.type((v.id).toChar())
        updateBoardGUI()
    }

    private fun updateBoardGUI() {
        // Gets the current row and updates the tiles as needed

        var row = board.getRow()
        println(row)

        if (row == 6) {
            row--
        }

        for (i in row * WIDTH until row * WIDTH + WIDTH) {
            val tileView: View = this.findViewById(i)
            val tileChar: TextView = tileView.findViewById(R.id.tile_char)
            tileChar.text = board.tileArray[i].char.toString()
            tileChar.setTextColor(Color.BLACK)
        }
    }

    // Updates the color of tiles in a row following a guess
    private fun updateTileColor() {
        val row = board.getRow() - 1
        for (i in row * WIDTH until row * WIDTH + WIDTH) {
            val tileView: View = this.findViewById(i)
            val tile: View = tileView.findViewById(R.id.chess_tile)
            val tileChar: TextView = tileView.findViewById(R.id.tile_char)
            tileChar.setTextColor(Color.WHITE)
            var roundedBorder = tile.background
            roundedBorder = DrawableCompat.wrap(roundedBorder)
            DrawableCompat.setTint(roundedBorder, Color.parseColor(board.tileArray[i].color.rgb))
        }

        for (i in board.letterStatus.indices) {
            val key: View = this.findViewById(i + 65)
            setKeyColor(key, board.letterStatus[i])
        }
    }

    private fun showToast() {
        Toast.makeText(baseContext, "Not enough letters", Toast.LENGTH_SHORT).show()
    }

    private fun setKeyColor(key: View, state: Int) {
        when (state) {
            2 -> {
                key.setBackgroundColor(Color.parseColor(BoardColor.GREEN.rgb))
            }
            1 -> {
                key.setBackgroundColor(Color.parseColor(BoardColor.YELLOW.rgb))
            }
            0 -> {
                key.setBackgroundColor(Color.parseColor(BoardColor.DARK_GRAY.rgb))
            }
        }
    }
}


