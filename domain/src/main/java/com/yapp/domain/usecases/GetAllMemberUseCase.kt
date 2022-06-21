package com.yapp.domain.usecases

import com.yapp.domain.model.MemberEntity
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.DispatcherProvider
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetAllMemberUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    private val dispatcherProvider: DispatcherProvider
) : BaseUseCase<Flow<TaskResult<List<MemberEntity>>>, Unit>(dispatcherProvider) {

    override suspend fun invoke(params: Unit?): Flow<TaskResult<List<MemberEntity>>> {
        return memberRepository.getAllMember()
            .toResult()
    }

}