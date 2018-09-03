package com.storiqa.market.model

enum class ErrorCode (val code: Int) {
    NO_ERROR(0), // default value if no error
    READABLE_ERROR(100),
    SERVER_NETWORK_ERROR(200),
    SERVER_PARSE_ERROR(300),
    SERVER_UNKNOWN_ERROR(400)
}