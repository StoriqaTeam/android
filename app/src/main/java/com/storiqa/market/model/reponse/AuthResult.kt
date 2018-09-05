package com.storiqa.market.model.reponse


/**
 * AuthResult is handled separately due to more detailed error parsing.
 * @resVariant has no default value, should be set in constructor
 * */
class AuthResult (
        val resVariant: ResultVariants,
        val marketResp: Login_Mutation.Data? = null,
        val emailError: String? = null,
        val passError: String? = null,
        val commonError: String? = null
)

enum class ResultVariants {
    SUCCESS, // resp data checked manually
    COMMON_ERROR,
    DETAILED_ERRORS
}