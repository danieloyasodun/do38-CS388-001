package com.example.wordle

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wordle.FourLetterWordList


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val simpleEditText = findViewById<EditText>(R.id.et_simple)
        val button = findViewById<Button>(R.id.button5)
        val wordList = FourLetterWordList()
        var wordToGuess = wordList.getRandomFourLetterWord()
        val targetWord = findViewById<TextView>(R.id.targetWord)
        targetWord.text = wordToGuess
        var guess_count = 0
        val linearLayout = findViewById<LinearLayout>(R.id.linearLayout)

        fun checkGuess(guess: String) : String {
            var result = ""
            for (i in 0..3) {
                if (guess[i] == wordToGuess[i]) {
                    result += "O"
                }
                else if (guess[i] in wordToGuess) {
                    result += "+"
                }
                else {
                    result += "X"
                }
            }
            return result
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fun resetGame() {
            wordToGuess = wordList.getRandomFourLetterWord()
            targetWord.text = wordToGuess
            targetWord.visibility = View.INVISIBLE
            guess_count = 0
            button.text = "SUBMIT"
            simpleEditText.text.clear()
            linearLayout.removeAllViews()
        }

        fun hideKeyBoard() {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            val view = this.currentFocus
            if (view != null) {
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        fun isValidGuess(guess: String): Boolean {
            return guess.length == 4 && guess.matches(Regex("^[A-Z]+$"))
        }

        button.setOnClickListener {
            if (button.text == "RESET"){
                resetGame()
            }
            else {
                val guess = simpleEditText.text.toString().uppercase()
                if (!isValidGuess(guess)) {
                    Toast.makeText(this, "Please enter a valid 4-letter word using only A-Z.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                hideKeyBoard()
                guess_count++
                val checked_guess = checkGuess(guess)
                val newTextView = TextView(this)
                val guess_check = TextView(this)

                if (guess_count >= 3) {
                    targetWord.visibility = View.VISIBLE
                    button.text = "RESET"
                }

                val text_guess = "Guess #$guess_count                               $guess"
                val text_check = "Guess#$guess_count CHECK                  $checked_guess"

                newTextView.text = text_guess
                newTextView.textSize = 25f
                guess_check.text = text_check
                guess_check.textSize = 25f
                newTextView.setPadding(16, 16, 16, 16)
                guess_check.setPadding(16, 16, 16, 16)


                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                newTextView.layoutParams = layoutParams
                guess_check.layoutParams = layoutParams


                linearLayout.addView(newTextView)
                linearLayout.addView(guess_check)

            }
        }
    }
}