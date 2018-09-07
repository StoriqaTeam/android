package com.storiqa.market.model.reponse

open class MarketServException(
        val errorDetails: ErrorDetails
) : Exception(errorDetails.payload)

class AuthException(
        val authError: AuthErrorPayload
): MarketServException(ErrorDetails())