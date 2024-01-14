import com.yapp.domain.util.DateParser
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class DateParserTest : BehaviorSpec({
    val dateParser = DateParser()

    given("2024-01-04 14:00:00 String 날짜를") {
        val rawDate = "2024-01-04 14:12:34"

        `when`("parsing 했을 때") {
            val result = dateParser.parse(rawDate = rawDate)

            then("Year 은 2024년 이다") { result.year shouldBe 2024 }
            then("Month 는 1월 이다") { result.monthValue shouldBe 1 }
            then("Day 는 4일 이다") { result.dayOfMonth shouldBe 4 }

            then("Hour 는 4일 이다") { result.hour shouldBe 14 }
            then("Minute 는 4일 이다") { result.minute shouldBe 12 }
            then("Second 는 4일 이다") { result.second shouldBe 34 }
        }
    }

    given("2024-01-04 14:00:00 LocalDateTime을") {
        val date = dateParser.parse("2024-01-04 14:00:00")

        `when`("Formatting 했을 때") {
            val rawString = dateParser.format(date = date)

            then("'2024-01-04 14:00:00' 의 문자열을 반환한다") { rawString shouldBe "2024-01-04 14:00:00" }
        }
    }
})