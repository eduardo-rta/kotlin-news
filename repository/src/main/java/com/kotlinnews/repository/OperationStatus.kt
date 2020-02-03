package com.kotlinnews.repository

enum class OperationStatus {
    LOADING,
    SUCCESS,
    ERROR
}

@Suppress("DataClassPrivateConstructor")
data class OperationState private constructor(val status: OperationStatus, val message: String? = null, val throwable: Throwable? = null, val affectedItems: Int? = null) {
    companion object {
        fun loading() = OperationState(OperationStatus.LOADING)
        fun success(affectedItems: Int? = null) = OperationState(OperationStatus.SUCCESS, null, null, affectedItems)
        fun error(throwable: Throwable?, message: String? = null) = OperationState(OperationStatus.ERROR, message, throwable)
    }
}