package com.storiqa.market.model.reponse

import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Response
import org.json.JSONException

class MarketServerResponse <T : Operation.Data> (
        response: Response<T>
) {

    val successData: T? = response.data()

    var errorDetails: ErrorDetails = if (response.errors().isNotEmpty()) {
        try {
            val errorTop = response.errors()[0]
            val erMap = errorTop.customAttributes() as MutableMap<String, Any?>
            val erData = erMap[ErrorKeys.DATA] as MutableMap<*, *>
            val erCode = (erData[ErrorKeys.CODE] as Number).toInt()
            if (erCode == ErrorCode.READABLE_ERROR.code) {
                val internalMap = erData[ErrorKeys.DETAILS] as MutableMap<*, *>
                val internalMsg = internalMap[ErrorKeys.PAYLOAD] as String
                    //internalCode parsed as string because of declared type
                val internalCode = (internalMap[ErrorKeys.CODE] as String?)?.toInt()
                ErrorDetails(
                        internalCode ?: ErrorCode.DEFAULT_ERROR.code,
                        internalMsg
                )
            } else {
                ErrorDetails(erCode, ErrorMessage.DEFAULT_SERVER_ERROR.name)
            }
        } catch (ex:Exception) {
            when(ex) {
                is JSONException,
                is ClassCastException,
                is NullPointerException -> {
                    ErrorDetails()
                }
                else -> throw ex
            }
        }
    } else {
        ErrorDetails(
                ErrorCode.NO_ERROR.code,
                ErrorMessage.NO_SERVER_ERROR.name
        )
    }

    var finalSuccess: Boolean =
            (successData != null && errorDetails.errorCode == ErrorCode.NO_ERROR.code)

}