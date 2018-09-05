package com.storiqa.market.domain

import com.storiqa.market.model.repository.ServerDataRepository

class ApiClientInteractor constructor(
        private val serverDataRepo: ServerDataRepository
) {

    fun getMeInfo() = serverDataRepo.getMeInfo()
    fun getCurrencies() = serverDataRepo.getCurrencies()
    fun getLanguages() = serverDataRepo.getLanguages()

}