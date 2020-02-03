package com.kotlinnews.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

/**
 * Generic data object for Listing operations
 * Implemented in the Repository it will have it's mechanism for loading from Db and/or Network
 * */
data class GenericListResult<T>(
    /**
     * Paged List Live data that will interact with the viewmodel/ui
     * */
    val pagedList: LiveData<PagedList<T>>,

    /**
     * Live Data for the load operation state. It will indicate whenever it's loading, finished, or an error happened
     * */
    val loadState: LiveData<OperationState>,
    /**
     * Live Data for the refresh operation state. It will indicate whenever it's loading, finished, or an error happened
     * Refresh will mostly be called by swipe to refresh operations
     * */
    val refreshState: LiveData<OperationState>,

    /**
     * Live Data to indicate if there are updated news in the backend
     * */
    val loadAtFrontState: LiveData<OperationState>,

    /**
     * By calling this refresh, it will trigger the refresh mechanism in the repository
     * */
    val refresh: () -> Unit,
    /**
     * Called only when an error happened during the load. It will retry the load operation
     * */
    val retry: () -> Unit
)