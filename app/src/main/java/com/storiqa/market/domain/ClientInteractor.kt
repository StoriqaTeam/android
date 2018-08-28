package com.storiqa.market.domain

import com.storiqa.market.model.repository.ServerDataRepository
import javax.inject.Inject

class ClientInteractor @Inject constructor(
        private val serverDataRepo: ServerDataRepository
) {
    fun getLanguages() = serverDataRepo.getLanguages()
    fun getMeInfo() = serverDataRepo.getMeInfo()
}