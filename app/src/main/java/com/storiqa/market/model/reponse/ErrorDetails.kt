package com.storiqa.market.model.reponse

class ErrorDetails  (
        val errorCode: Int? = ErrorCode.DEFAULT_ERROR.code, //default value
        val payload: String? = ErrorMessage.DEFAULT_SERVER_ERROR.name
)
