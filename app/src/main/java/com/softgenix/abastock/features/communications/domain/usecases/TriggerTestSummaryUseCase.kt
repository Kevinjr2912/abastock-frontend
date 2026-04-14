package com.softgenix.abastock.features.communications.domain.usecases

import com.softgenix.abastock.features.communications.domain.repositories.CommunicationsRepository
import javax.inject.Inject

class TriggerTestSummaryUseCase @Inject constructor(
    private val repository: CommunicationsRepository
) {
    suspend operator fun invoke(): Result<Unit> = repository.triggerTestSummary()
}