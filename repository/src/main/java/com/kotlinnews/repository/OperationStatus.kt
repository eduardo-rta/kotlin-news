package com.kotlinnews.repository

enum class OperationStatus {
    LOADING,
    SUCCESS,
    ERROR
}

@Suppress("DataClassPrivateConstructor")
class OperationState private constructor(val status: OperationStatus, val message: String? = null, val throwable: Throwable? = null) {
    companion object {
        fun loading() = OperationState(OperationStatus.LOADING)
        fun success() = OperationState(OperationStatus.SUCCESS)
        fun error(throwable: Throwable?, message: String? = null) = OperationState(OperationStatus.ERROR, message, throwable)
    }
}