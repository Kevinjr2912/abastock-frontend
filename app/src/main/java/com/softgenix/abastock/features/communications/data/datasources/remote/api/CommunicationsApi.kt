package com.softgenix.abastock.features.communications.data.datasources.remote.api

import com.softgenix.abastock.features.communications.data.datasources.remote.models.RegisterTokenRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CommunicationsApi {
    @POST("auth/device-token")
    suspend fun registerDeviceToken(
        @Body request: RegisterTokenRequestDto
    ): Response<Unit>

    @GET("auth/device-token/test-summary")
    suspend fun triggerTestSummary(): Response<Unit>
}