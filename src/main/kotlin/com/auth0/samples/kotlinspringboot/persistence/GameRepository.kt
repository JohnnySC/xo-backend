package com.auth0.samples.kotlinspringboot.persistence

import com.auth0.samples.kotlinspringboot.model.Cell
import com.auth0.samples.kotlinspringboot.model.Game
import org.springframework.data.repository.CrudRepository

/**
 * @author Asatryan on {18.01.20}
 */
interface GameRepository : CrudRepository<Game, Long>

interface CellsRepository : CrudRepository<Cell, Int>
