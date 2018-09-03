package com.storiqa.market.domain

import com.storiqa.market.model.repository.ServerDataRepository

class ApiClientInteractor constructor(
        private val serverDataRepo: ServerDataRepository
) {
    fun getLanguages() = serverDataRepo.getLanguages()
    fun getMeInfo() = serverDataRepo.getMeInfo()
    fun getCurrencies() = serverDataRepo.getCurrencies()

    fun getLangsWithSideEffect() = serverDataRepo.getLanguagesAlt()

}