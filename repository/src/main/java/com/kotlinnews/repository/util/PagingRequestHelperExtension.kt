package com.kotlinnews.repository.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingRequestHelper
import com.kotlinnews.repository.OperationState

private fun getError(report: PagingRequestHelper.StatusReport): Throwable {
    return PagingRequestHelper.RequestType.values().mapNotNull {
        report.getErrorFor(it)
    }.first()
}

fun PagingRequestHelper.createStatusLiveData(): LiveData<OperationState> {
    val liveData = MutableLiveData<OperationState>()
    addListener { report ->
        when {
            report.hasRunning() -> liveData.postValue(OperationState.loading())
            report.hasError() -> liveData.postValue(
                OperationState.error(getError(report)))
            else -> liveData.postValue(OperationState.success())
        }
    }
    return liveData
}