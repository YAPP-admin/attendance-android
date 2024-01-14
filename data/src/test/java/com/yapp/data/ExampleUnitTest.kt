package com.yapp.data

import com.yapp.data.model.SessionEntity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    val json = """
        
        [{
            "sessionId": 0,
            "title": "YAPP 오리엔테이션",
            "type": "NEED_ATTENDACE"
            "startTime": "2023-10-28 14:00:00"
            "description": "오리엔테이션 입니다",
            "code": "1234"
          },
          {
            "sessionId": 1,
            "title": "팀 세션",
            "type": "DONT_NEED_ATTENDACE"
            "startTime": "2023-11-04 14:00:00"
            "description": "팀으로 모여 함께 의견을 공유하고 작업합니다.",
            "code": "1234"
          },
          {
            "sessionId": 2,
            "title": "전체 직군 세션",
            "type": "NEED_ATTENDACE"
            "startTime": "2023-11-11 14:00:00"
            "description": "같은 직군 멤버들과 소통하고 진행 상황을 공유하는 시간입니다.",
            "code": "1234"
          },
          {
            "sessionId": 3,
            "title": "팀 세션",
            "type": "DONT_NEED_ATTENDACE"
            "startTime": "2023-11-18 14:00:00"
            "description": "팀으로 모여 함께 의견을 공유하고 작업합니다.",
            "code": "1234"
          },
          {
            "sessionId": 4,
            "title": "YAPP 1차 Dev. Camp",
            "type": "NEED_ATTENDACE"
            "startTime": "2023-12-02 13:00:00"
            "description": "1차 Dev. Camp까지 1차 프로토타입을 완성해주세요.",
            "code": "1234"
          },
          {
            "sessionId": 5,
            "title": "휴얍",
            "type": "DAY_OFF"
            "startTime": "2023-12-09 14:00:00"
            "description": "팀으로 모여 함께 의견을 공유하고 작업합니다.",
            "code": "1234"
          },
          {
            "sessionId": 6,
            "title": "팀 세션",
            "type": "DONT_NEED_ATTENDACE"
            "startTime": "2023-12-16 14:00:00"
            "description": "팀으로 모여 함께 의견을 공유하고 작업합니다.",
            "code": "1234"
          },
          {
            "sessionId": 7,
            "title": "디자인 세션",
            "type": "DONT_NEED_ATTENDACE"
            "startTime": "2023-12-23 14:00:00"
            "description": "디자인 직군 멤버들이 소통하고 진행 상황을 공유하는 시간입니다.",
            "code": "1234"
          },
          {
            "sessionId": 9,
            "title": "팀 세션",
            "type": "DONT_NEED_ATTENDACE"
            "startTime": "2023-12-30 14:00:00"
            "description": "팀으로 모여 함께 의견을 공유하고 작업합니다.",
            "code": "1234"
          },
          {
            "sessionId": 10,
            "title": "개발 세션",
            "type": "DONT_NEED_ATTENDACE"
            "startTime": "2023-01-07 14:00:00"
            "description": "개발 직군 멤버들이 소통하고 진행 상황을 공유하는 시간입니다.",
            "code": "1234"
          },
          {
            "sessionId": 11,
            "title": "전체 직군 세션",
            "type": "NEED_ATTENDACE"
            "startTime": "2023-01-14 14:00:00"
            "description": "같은 직군 멤버들과 소통하고 진행 상황을 공유하는 시간입니다.",
            "code": "1234"
          },
          {
            "sessionId": 12,
            "title": "팀 세션",
            "type": "DONT_NEED_ATTENDACE"
            "startTime": "2023-01-21 14:00:00"
            "description": "팀으로 모여 함께 의견을 공유하고 작업합니다",
            "code": "1234"
          },
          {
            "sessionId": 13,
            "title": "YAPP 2차 Dev. Camp",
            "type": "NEED_ATTENDACE"
            "startTime": "2023-01-28 14:00:00"
            "description": "2차 Dev. Camp까지 2차 프로토타입을 완성해 주세요.",
            "code": "1234"
          },
          {
            "sessionId": 14,
            "title": "팀 세션",
            "type": "DONT_NEED_ATTENDACE"
            "startTime": "2023-02-03 14:00:00"
            "description": "팀으로 모여 함께 의견을 공유하고 작업합니다",
            "code": "1234"
          },
          {
            "sessionId": 15,
            "title": "팀 세션",
            "type": "DONT_NEED_ATTENDACE"
            "startTime": "2023-02-10 14:00:00"
            "description": "팀으로 모여 함께 의견을 공유하고 작업합니다",
            "code": "1234"
          },
          {
            "sessionId": 16,
            "title": "팀 세션",
            "type": "DONT_NEED_ATTENDACE"
            "startTime": "2023-02-17 14:00:00"
            "description": "팀으로 모여 함께 의견을 공유하고 작업합니다",
            "code": "1234"
          },
          {
            "sessionId": 17,
            "title": "팀 세션",
            "type": "DONT_NEED_ATTENDACE"
            "startTime": "2023-02-24 14:00:00"
            "description": "팀으로 모여 함께 의견을 공유하고 작업합니다",
            "code": "1234"
          },
          {
            "sessionId": 18,
            "title": "팀 세션",
            "type": "DONT_NEED_ATTENDACE"
            "startTime": "2023-03-02 14:00:00"
            "description": "팀으로 모여 함께 의견을 공유하고 작업합니다",
            "code": "1234"
          },
          {
            "sessionId": 19,
            "title": "성과공유회",
            "type": "NEED_ATTENDACE"
            "startTime": "2023-03-09 14:00:00"
            "description": "YAPP 활동을 마무리짓는 성과 공유회 시간입니다.\n지금까지 하나의 팀으로 작업한 결과를 YAPP 전원에게 보여주세요 🎉",
            "code": "1234"
          }]
       
    """.trimIndent()

    val singleJson = """
       
        {
            "sessionId": 0,
            "title": "YAPP 오리엔테이션",
            "type": "NEED_ATTENDACE"
            "startTime": "2023-04-23 14:00:00"
            "description": "오리엔테이션 입니다.",
            "code": "1234"
        }
          
    """.trimIndent()

    @Test
    fun addition_isCorrect() {
        val s = Json.decodeFromString<List<SessionEntity>>(json)
        println(s.toString())
    }
}
