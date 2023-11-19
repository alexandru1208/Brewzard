package com.deskbird.datasource.remote.internal.errors

import retrofit2.Call
import retrofit2.CallAdapter

internal class ErrorsCallAdapter(
    private val delegateAdapter: CallAdapter<Any, Call<*>>,
    private val exceptionMapper: NetworkExceptionMapper,
) : CallAdapter<Any, Call<*>> by delegateAdapter {

    override fun adapt(call: Call<Any>): Call<*> {
        return delegateAdapter.adapt(CallWithErrorHandling(call, exceptionMapper))
    }
}
