package com.auth0.samples.kotlinspringboot.model

import com.auth0.samples.kotlinspringboot.persistence.CellsRepository
import javax.persistence.Entity
import javax.persistence.Id

/**
 * @author Asatryan on {18.01.20}
 */
@Entity
class Game(
		@Id
		var id: Long = GameId,
		var firstPlayerId: Long = 0,
		var secondPlayerId: Long = 0,
		var state: String = INITIAL,
		var nowPlayingPlayerId: Long = 0,
		var winnerId: Long = 0
)

const val INITIAL = "INITIAL"
const val STARTED = "STARTED"
const val DRAW = "DRAW"
const val WIN = "WIN"

@Entity
class Cell(
		@Id
		var id: Int = 0,
		var playerId: Long = 0
)

class GameOutput(
		var firstPlayerId: Long = 0,
		var secondPlayerId: Long = 0,
		var state: String = INITIAL,
		var nowPlayingPlayerId: Long = 0,
		var winnerId: Long = 0,
		var cells: ArrayList<Cell> = ArrayList()
)

const val GameId = 1234567890L

fun Game.checkWinState(repository: CellsRepository) {

	val idsArray = arrayOf(
			arrayOf(0, 1, 2),
			arrayOf(3, 4, 5),
			arrayOf(6, 7, 8),

			arrayOf(0, 3, 6),
			arrayOf(1, 4, 7),
			arrayOf(2, 5, 8),

			arrayOf(0, 4, 8),
			arrayOf(2, 4, 6)
	)

	var foundWinner = false
	for (ids in idsArray) {
		foundWinner = checkCells(ids[0], ids[1], ids[2], repository)
		if (foundWinner)
			break
	}

	if (!foundWinner) {
		val cells = repository.findAll()
		if (cells.count() == 9) {
			nowPlayingPlayerId = 0
			state = DRAW
		}
	}

}

fun Game.checkCells(id0: Int, id1: Int, id2: Int, cellsRepository: CellsRepository): Boolean {
	val cells = cellsRepository.findAll()
	val cell0 = cells.find { it.id == id0 }
	val cell1 = cells.find { it.id == id1 }
	val cell2 = cells.find { it.id == id2 }

	if (
			cell0 != null &&
			cell1 != null &&
			cell2 != null
	) {
		if (
				cell0.playerId == cell1.playerId &&
				cell0.playerId == cell2.playerId
		) {
			winnerId = if (cell0.playerId == firstPlayerId)
				firstPlayerId
			else
				secondPlayerId

			nowPlayingPlayerId = 0
			state = WIN
			return true
		}
	}
	return false
}
