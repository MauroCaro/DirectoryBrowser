package com.base.directorybrowser.util

data class Data<RequestData>(
    var responseType: Status,
    var data: RequestData? = null,
    var error: String? = null
)

enum class Status {
    SUCCESSFUL,
    LOADING,
    EMPTY,
    ERROR
}
