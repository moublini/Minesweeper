package minesweeper

import java.lang.IndexOutOfBoundsException
import kotlin.random.Random
import kotlin.system.exitProcess

class GameBoard() {
    private var boardWidth: Int = 0
    private var boardHeight: Int = 0

    private var boardWithMines: MutableList<MutableList<Char>> = MutableList(boardWidth) {
        MutableList(boardHeight) { '.' }
    }
    private var boardWithMarks = boardWithMines.toMutableList()

    private var numOfMines = 0
    private var numOfMarks = 0

    //  Generates a new board based on an input describing the amount of mines
    constructor(width: Int, height: Int) : this() {
        this.boardWidth = width
        this.boardHeight = height

        boardWithMines = MutableList(boardWidth) {
            MutableList(boardHeight) { '.' }
        }

        boardWithMarks = MutableList(boardWidth) {
            MutableList(boardHeight) { '.' }
        }

        print("How many mines do you want on the field? ")
        val numMines = readln().toInt()
        var xAdded = 0
        mineLoop@ do {
            for (row in boardWithMines) {
                for (col in row.indices) {
                    when {
                        (xAdded == numMines) -> break@mineLoop
                        (Random.nextInt(0, 50) == 0 && row[col] != 'X') -> {
                            row[col] = 'X'
                            xAdded++
                        }
                    }
                }
            }
        } while (xAdded < numMines)
        init()
    }

    //  For testing with a predefined board
    constructor(width: Int, height: Int, board: MutableList<MutableList<Char>>) : this() {
        this.boardWidth = width
        this.boardHeight = height
        for (row in board) {
            for (col in row) {
                if (col == 'X') this.numOfMines++
            }
        }
        this.boardWithMines = board.toMutableList()
        this.boardWithMarks = MutableList(boardWidth) {
            MutableList(boardHeight) { '.' }
        }
        init()
    }

    //  Making it a function since it doesn't see boardWithMines declared when it's not a function
    fun init() {
        boardWithMines = getBoardWithNumbers()

        do {
            try {
                printBoard(boardWithMarks)
                print("Set/unset mines marks or claim cell as free: ")
                val input = readln().split(" ")

                val y = input[0].toInt() - 1
                val x = input[1].toInt() - 1

                when (input[2]) {
                    "mine" -> setMark(x, y)
                    "free" -> setFree(x, y,  boardWithMarks, boardWithMines)
                }
            } catch(_: IndexOutOfBoundsException) {}


        } while (!hasPlayerWon())
        println("Congratulations! You found all the mines!")
    }

    //  Recursive function to mark every cell as explored from a starting cell
    private fun setFree(x: Int, y: Int, oldBoard: MutableList<MutableList<Char>>, board: MutableList<MutableList<Char>>): MutableList<MutableList<Char>> {
        val boardCopy = board.toMutableList()
        var newBoard = oldBoard.toMutableList()

        try {
            when (board[x][y]) {
                in '1'..'8' -> {
                    newBoard[x][y] = board[x][y]
                }

                '.', '*' -> {
                    if (board[x][y] == '.') {
                        newBoard[x][y] = '/'
                        boardCopy[x][y] = '/'
                    }

                    val neighbourCellsPosition = mutableListOf(
                        //  Left / Right
                        mutableListOf(x - 1, y),
                        mutableListOf(x + 1, y),
                        // Up / Down
                        mutableListOf(x, y - 1),
                        mutableListOf(x, y + 1),
                        //  Left Up / Left Down
                        mutableListOf(x - 1, y - 1),
                        mutableListOf(x - 1, y + 1),
                        //  Right Up / Right Down
                        mutableListOf(x + 1, y - 1),
                        mutableListOf(x + 1, y + 1),
                    )

                    for (pos in neighbourCellsPosition) {
                        val (cellX, cellY) = pos
                        if (cellX !in 0..8) continue
                        if (cellY !in 0..8) continue
                        newBoard = setFree(cellX, cellY, newBoard, boardCopy)
                    }
                }

                'X' -> {
                    println("You stepped on a mine and failed!")
                    exitProcess(1)
                }
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            println("Yo stupid")
        }

        return newBoard
    }

    //  Sets a mark on the board
    private fun setMark(x: Int, y: Int) {
        try {
            when (boardWithMarks[x][y]) {
                in '1'..'8' -> println("There is a number here!")
                '.' -> {
                    boardWithMarks[x][y] = '*'
                    numOfMarks++
                }

                '*' -> {
                    boardWithMarks[x][y] = '.'
                    numOfMarks--
                }
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            println("Yo stupid")
        }
    }

    //  Check if player marked every mine and if they explored everything
    private fun hasPlayerWon(): Boolean {
        var numOfSpaces = 0
        for (row in boardWithMines.indices) {
            for (col in boardWithMines[row].indices) {
                val mineCell = boardWithMines[row][col]
                val markCell = boardWithMarks[row][col]

                if (markCell == '.') {
                    numOfSpaces++
                }

                if (mineCell != 'X')
                    continue
            }
        }
        if (numOfMines == numOfMarks + numOfSpaces)
            return true
        return false
    }

    //  Calculates the numbers of the tiles that are near mines
    private fun getBoardWithNumbers(): MutableList<MutableList<Char>> {
        val board = this.boardWithMines.toMutableList()
        numOfMines = 0
        for (x in board.indices) {
            for (y in board[x].indices) {
                if (board[x][y] != 'X') continue
                numOfMines++
                val neighbourCellsPosition = mutableListOf(
                    //  Left / Right
                    mutableListOf(x - 1, y),
                    mutableListOf(x + 1, y),
                    // Up / Down
                    mutableListOf(x, y - 1),
                    mutableListOf(x, y + 1),
                    //  Left Up / Left Down
                    mutableListOf(x - 1, y - 1),
                    mutableListOf(x - 1, y + 1),
                    //  Right Up / Right Down
                    mutableListOf(x + 1, y - 1),
                    mutableListOf(x + 1, y + 1),
                )

                for (pos in neighbourCellsPosition) {
                    val (cellX, cellY) = pos
                    if (cellX !in 0..8) continue
                    if (cellY !in 0..8) continue
                    when (board[cellX][cellY]) {
                        '.' -> board[cellX][cellY] = '1'
                        in '1'..'7' -> board[cellX][cellY]++
                    }
                }
            }
        }
        return board
    }

    //  Prints the board in a cool way
    private fun printBoard(board: MutableList<MutableList<Char>>) {
        println(" |123456789|")
        println("-|---------|")
        for (row in board.indices) {
            println("${row + 1}|${board[row].joinToString("")}|")
        }
        println("-|---------|")
    }
}