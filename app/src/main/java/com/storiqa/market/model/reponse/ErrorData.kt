package com.storiqa.market.model.reponse

object ErrorKeys {
    const val DATA = "data"
    const val CODE ="code"
    const val DETAILS ="details"
    const val PAYLOAD = "payload"
}

enum class ErrorCode (val code: Int) {
    NO_ERROR(-100),
    DEFAULT_ERROR(0), // default value if no error
    READABLE_ERROR(100),
    SERVER_NETWORK_ERROR(200),
    SERVER_PARSE_ERROR(300),
    SERVER_UNKNOWN_ERROR(400)
}

enum class ErrorMessage {
    NO_SERVER_ERROR,
    DEFAULT_SERVER_ERROR,
    UNAUTHORIZED_ERROR
}