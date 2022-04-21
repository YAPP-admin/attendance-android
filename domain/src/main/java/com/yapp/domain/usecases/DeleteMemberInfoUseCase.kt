package com.yapp.domain.usecases

import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.DispatcherProvider
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

class DeleteMemberInfoUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val memberRepository: MemberRepository,
    private val coroutineDispatcher: DispatcherProvider
) : BaseUseCase<Flow<Boolean>, Long?>(coroutineDispatcher) {
    override suspend fun invoke(params: Long?): Flow<Boolean> {
        return localRepository.deleteAllUserInfo().zip(
            memberRepository.deleteMember(params!!)
        ) { _, remoteSucceed -> remoteSucceed }
    }
}