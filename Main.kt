package minesweeper

const val BOARD_WIDTH = 9
const val BOARD_HEIGHT = 9

//val gameBoard = GameBoard(BOARD_WIDTH, BOARD_WIDTH)

fun main() {
    GameBoard(
        BOARD_WIDTH,
        BOARD_HEIGHT,
        mutableListOf(
            MutableList(BOARD_WIDTH) { '.' },
            MutableList(BOARD_WIDTH) { '.' },
            mutableListOf('.', '.', '.', '.', '.', '.', 'X', '.', '.'),
            mutableListOf('.', 'X', '.', '.', '.', '.', '.', '.', '.'),
            mutableListOf('.', '.', '.', '.', '.', '.', '.', '.', 'X'),
            mutableListOf('.', '.', '.', '.', '.', '.', 'X', '.', '.'),
            MutableList(BOARD_WIDTH) { '.' },
            MutableList(BOARD_WIDTH) { '.' },
            mutableListOf('.', 'X', '.', '.', '.', '.', '.', '.', '.'),
        )
    )

}
