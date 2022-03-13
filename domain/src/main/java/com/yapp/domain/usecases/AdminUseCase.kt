package com.yapp.domain.usecases

import com.yapp.domain.repository.AdminRepository
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class JoinAdminUseCase @Inject constructor(
    private val adminRepository: AdminRepository
) : BaseUseCase<Flow<TaskResult<Unit>>, JoinAdminUseCase.Params>() {

    override suspend fun invoke(params: Params): Flow<TaskResult<Unit>> {
        return adminRepository.setAdmin(params.memberId)
            .toResult()
    }

    class Params(
        val memberId: Long
    )

}