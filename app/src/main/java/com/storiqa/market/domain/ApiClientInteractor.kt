package com.storiqa.market.domain

import com.storiqa.market.model.reponse.MarketServException
import com.storiqa.market.model.repository.ServerDataRepository

class ApiClientInteractor constructor(
        private val serverDataRepo: ServerDataRepository
) {

    fun getMeInfo() = serverDataRepo.getMeInfo()
    @Throws(MarketServException::class)
    fun getLanguages() = serverDataRepo.getLanguages()

}