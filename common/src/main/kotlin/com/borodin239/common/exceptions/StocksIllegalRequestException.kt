package com.borodin239.common.exceptions

class StocksIllegalRequestException : RuntimeException {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable) : super(message, cause)
}