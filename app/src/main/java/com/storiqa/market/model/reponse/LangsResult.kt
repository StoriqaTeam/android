package com.storiqa.market.model.reponse

class LangsResult(
        val data: MarketServerResponse<Languages_Query.Data>?,
        val errorCode: Int?,
        val errorMessage: String?
)
