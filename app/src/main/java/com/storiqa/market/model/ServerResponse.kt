package com.storiqa.market.model

import com.apollographql.apollo.api.Error
import com.apollographql.apollo.api.Operation

class ServerResponse <T : Operation.Data> (
        val successData: T?,
        errors: List<Error>?
) {

    var errorDetails: ErrorDetails = if (errors != null && errors.isNotEmpty()) {
        (errors[0].customAttributes()["data"] as List<ErrorDetails>)[0]
    } else {
        ErrorDetails()
    }

    var finalSuccess: Boolean = (errorDetails.errorCode == ErrorCode.NO_ERROR.code)

}