import com.yapp.domain.util.DateParser
import com.yapp.domain.util.DateUtil
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class DateUtilTest : BehaviorSpec({
    val dateParser = DateParser()
    val dateUtil = DateUtil()

    given("[세션시간]과 그로 부터 [30분 전 시간]이 주어진 경우") {
        val currentTime = dateParser.parse(rawDate = "2024-01-04 14:00:00")
        val sessionTime = dateParser.parse(rawDate = "2024-01-04 14:30:00")

        `when`("두 시간의 차를 분 단위로 구하면") {
            val result = with(dateUtil) { currentTime elapsedFrom sessionTime }

            then("차이는 -30분 이다") { result shouldBe -30 }
        }

        `when`("곧 앞으로 다가올 Date인지 확인 했을때") {
            val result = with(dateUtil) { currentTime isBeforeFrom sessionTime }

            then("앞으로 다가올 Date 이다") { result shouldBe true }
        }

        `when`("이미 지난 Date인지 확인 했을때") {
            val result = with(dateUtil) { currentTime isAfterFrom sessionTime }

            then("이미 지난 Date가 아니다") { result shouldBe false }
        }
    }

    given("[세션시간]과 그로 부터 [20분이 지난 시간]이 주어진 경우") {
        val currentTime = dateParser.parse(rawDate = "2024-01-04 14:20:00")
        val sessionTime = dateParser.parse(rawDate = "2024-01-04 14:00:00")

        `when`("두 시간의 차를 분 단위로 구했을 때") {
            val result = with(dateUtil) { currentTime elapsedFrom sessionTime }

            then("차이는 20분 이다") { result shouldBe 20 }
        }

        `when`("앞으로 다가올 Date인지 확인 했을때") {
            val result = with(dateUtil) { currentTime isBeforeFrom sessionTime }

            then("앞으로 다가올 세션이 아니다") { result shouldBe false }
        }

        `when`("이미 지난 Date인지 확인 했을때") {
            val result = with(dateUtil) { currentTime isAfterFrom sessionTime }

            then("이미 지난 Date 이다") { result shouldBe true }
        }
    }

    given("날짜가 다른, [현재시간]과 [세션시간]이 주어진 경우") {
        val currentTime = dateParser.parse(rawDate = "2024-01-04 14:20:00")
        val sessionTime = dateParser.parse(rawDate = "2024-01-05 14:00:00")

        `when`("앞으로 다가올 Date인지 확인 했을때") {
            val result = with(dateUtil) { currentTime isBeforeFrom sessionTime }

            then("앞으로 다가올 Date이다") { result shouldBe true }
        }

        `when`("이미 지난 Date인지 확인 했을때") {
            val result = with(dateUtil) { currentTime isAfterFrom sessionTime }

            then("이미 지난 Date가 아니다") { result shouldBe false }
        }
    }

    given("30분 50초 차의 두 시간이 주어진 경우") {
        val currentTime = dateParser.parse(rawDate = "2024-01-04 14:00:00")
        val sessionTime = dateParser.parse(rawDate = "2024-01-04 14:30:50")

        `when`("두 시간의 차를 분 단위로 구했을 때") {
            val result = with(dateUtil) { currentTime elapsedFrom sessionTime }

            then("초 단위는 올림 or 반올림 되지 않고 버려진다") { result shouldBe -30 }
        }
    }
})