package com.yapp.data

import com.yapp.data.model.SessionModel
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
            "session_id": 0,
            "title": "YAPP 오리엔테이션",
            "description": "오리엔테이션 입니다",
            "date": "2022-04-23"
          },
          {
            "session_id": 1,
            "title": "전체 세션",
            "description": "모든 팀들이 같이 하는 시간입니다.",
            "date": "2022-04-09"
          },
          {
            "session_id": 2,
            "title": "팀 세션",
            "description": "팀 세션 입니다.",
            "date": "2022-04-16"
          },
          {
            "session_id": 3,
            "title": "중간고사 휴얍",
            "description": "시험 잘보세요",
            "date": "2022-04-23"
          },
          {
            "session_id": 4,
            "title": "기획 세션",
            "description": "기획 세션입니다.",
            "date": "2022-04-30"
          },
          {
            "session_id": 5,
            "title": "1차 Dev. Camp",
            "description": "1차 데브캠프입니다.",
            "date": "2022-05-07"
          },
          {
            "session_id": 6,
            "title": "팀 세션",
            "description": "팀세션 입니다.",
            "date": "2022-05-14"
          },
          {
            "session_id": 7,
            "title": "디자인 세션",
            "description": "디자인 세션 입니다.",
            "date": "2022-05-21"
          },
          {
            "session_id": 8,
            "title": "팀 세션",
            "description": "팀 세션입니다.",
            "date": "2022-05-28"
          },
          {
            "session_id": 9,
            "title": "2차 Dev. Camp",
            "description": "너무 신나는 2차 데브캠프",
            "date": "2022-06-04"
          },
          {
            "session_id": 10,
            "title": "기말고사 휴얍",
            "description": "시험 잘보세요",
            "date": "2022-06-11"
          },
          {
            "session_id": 11,
            "title": "팀 세션",
            "description": "팀 세션입니다.",
            "date": "2022-06-18"
          },
          {
            "session_id": 12,
            "title": "팀 세션",
            "description": "팀 세션입니다.",
            "date": "2022-06-25"
          },
          {
            "session_id": 13,
            "title": "개발 세션",
            "description": "개발 세션입니다.",
            "date": "2022-07-02"
          },
          {
            "session_id": 14,
            "title": "팀 세션",
            "description": "팀 세션입니다.",
            "date": "2022-07-09"
          },
          {
            "session_id": 15,
            "title": "3차 Dev. Camp",
            "description": "완성 못하면 지상렬",
            "date": "2022-07-16"
          },
          {
            "session_id": 16,
            "title": "팀 세션",
            "description": "팀 세션입니다.",
            "date": "2022-07-23"
          },
          {
            "session_id": 17,
            "title": "전체 세션",
            "description": "전체 세션입니다.",
            "date": "2022-07-30"
          },
          {
            "session_id": 18,
            "title": "성과공유회",
            "description": "성과공유회 입니다.",
            "date": "2022-08-06"
          }]
       
    """.trimIndent()

    val singleJson = """
        
        {
            "session_id": 0,
            "title": "YAPP 오리엔테이션",
            "description": "오리엔테이션 입니다",
            "date": "2022-04-23"
          }
          
    """.trimIndent()

    @Test
    fun addition_isCorrect() {
        val s = Json.decodeFromString<List<SessionModel>>(json)
        println(s.toString())
    }
}
