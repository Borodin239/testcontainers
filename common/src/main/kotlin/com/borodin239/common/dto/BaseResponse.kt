package com.borodin239.common.dto

data class BaseResponse(val success: Boolean, val errorDto: ErrorDto? = null)