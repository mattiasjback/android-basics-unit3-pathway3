package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private var _currentWordCount = 0
    public val currentWordCount: Int
        get() = _currentWordCount

    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    private var _score = 0
    public val score: Int
        get() = _score

    private val _currentScrambledWord = MutableLiveData<String>()
    public val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord

    init {
        Log.d("GameFragment", "GameViewModel created")
        getNextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed")
    }

    /*
     * Get a new scrambled word and check if it has not been used before.
     */
    private fun getNextWord() {
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }

        if(wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord.value = String(tempWord)
            ++_currentWordCount
            wordsList.add(currentWord)
        }
    }

    /*
     * Update next word if not already exceeded max allowed words
     */
    fun nextWord(): Boolean {
        return if(_currentWordCount < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

    private fun increaseScore() {
        _score += SCORE_INCREASE
    }

    fun isPlayerWordCorrect(playerWord: String): Boolean {
        return if (playerWord.equals(currentWord, true)) {
            increaseScore()
            true
        } else {
            false
        }
    }

    fun reinitializeData() {
        _score = 0
        _currentWordCount = 0
        wordsList.clear()
        getNextWord()
    }
}