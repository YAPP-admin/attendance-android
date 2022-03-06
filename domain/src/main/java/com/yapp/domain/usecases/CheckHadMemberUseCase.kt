package com.yapp.domain.usecases

import com.yapp.domain.model.AttendanceEntity
import com.yapp.domain.model.AttendanceEntity.Companion.MAX_SESSION
import com.yapp.domain.model.AttendanceTypeEntity
import com.yapp.domain.model.MemberEntity
import com.yapp.domain.repository.AdminRepository
import com.yapp.domain.repository.LocalRepository
import com.yapp.domain.repository.MemberRepository
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class CheckLocalMemberUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    private val localRepository: LocalRepository
) : BaseUseCase<Flow<TaskResult<Boolean>>, CheckLocalMemberUseCase.Params>() {

    override suspend fun invoke(params: Params): Flow<TaskResult<Boolean>> {
        return localRepository.getMemberId()
            .map { id ->
                id != null
            }.toResult()
    }

    class Params()

}

class SignUpUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    private val adminRepository: AdminRepository
) : BaseUseCase<Flow<TaskResult<Boolean>>, SignUpUseCase.Params>() {

    override suspend fun invoke(params: Params): Flow<TaskResult<Boolean>> {
        return adminRepository.checkAdmin(params.id)
            .flatMapLatest { isAdmin ->
                val member = MemberEntity(
                    id = params.id,
                    name = params.name,
                    position = params.position,
                    isAdmin = isAdmin,
                    team = params.team,
                    attendances = List(MAX_SESSION) { index -> AttendanceEntity(index, AttendanceTypeEntity.Empty) }
                )

                memberRepository.setMember(member)
                    .toResult()
            }
    }

    class Params(
        val id: Long,
        val name: String,
        val position: String,
        val team: String
    )

}