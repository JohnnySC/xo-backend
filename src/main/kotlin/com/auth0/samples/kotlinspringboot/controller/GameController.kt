package com.auth0.samples.kotlinspringboot.controller

import com.auth0.samples.kotlinspringboot.model.*
import com.auth0.samples.kotlinspringboot.persistence.CellsRepository
import com.auth0.samples.kotlinspringboot.persistence.GameRepository
import org.springframework.web.bind.annotation.*

/**
 * @author Asatryan on {18.01.20}
 */
@RestController
@RequestMapping("/game")
class GameController(val repository: GameRepository,
					 val cells: CellsRepository) {

	@GetMapping("/start/{id}")
	fun startNewGame(@PathVariable id: Long): GameOutput {
		if (repository.exists(GameId)) {
			if (getGame().state != INITIAL) {
				repository.delete(GameId)
				cells.deleteAll()
				repository.save(Game().apply {
					firstPlayerId = id
				})
			} else
				getGame().apply {
					secondPlayerId = id
					state = STARTED
					nowPlayingPlayerId = firstPlayerId
					repository.save(this)
				}
		} else
			repository.save(Game().apply {
				firstPlayerId = id
			})
		val game = getGame()
		return GameOutput(game.firstPlayerId, game.secondPlayerId, game.state, game.nowPlayingPlayerId, game.winnerId, ArrayList(cells.findAll().toList()))
	}

	@GetMapping
	fun getCurrentGame(): GameOutput {
		val game = getGame()
		game.checkWinState(cells)
		repository.save(game)
		return GameOutput(game.firstPlayerId, game.secondPlayerId, game.state, game.nowPlayingPlayerId, game.winnerId, ArrayList(cells.findAll().toList()))
	}

	@PostMapping("/addCell")
	fun udpateGame(@RequestBody cell: Cell): GameOutput {
		cells.save(cell)
		val game = getGame()
		if (game.nowPlayingPlayerId == game.firstPlayerId)
			game.nowPlayingPlayerId = game.secondPlayerId
		else
			game.nowPlayingPlayerId = game.firstPlayerId
		game.checkWinState(cells)
		repository.save(game)
		return GameOutput(game.firstPlayerId, game.secondPlayerId, game.state, game.nowPlayingPlayerId, game.winnerId, ArrayList(cells.findAll().toList()))
	}

	private fun getGame() = repository.findOne(GameId)
}
