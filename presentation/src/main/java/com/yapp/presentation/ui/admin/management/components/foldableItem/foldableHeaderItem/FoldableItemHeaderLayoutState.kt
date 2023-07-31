import androidx.compose.runtime.Stable
import com.yapp.presentation.ui.admin.management.components.foldableItem.FoldableItem
import com.yapp.presentation.ui.admin.management.components.foldableItem.foldableContentItem.FoldableContentItemState

/**
 * FoldableItem은 크게 2종류로 나위어 있는데, 그 중 Content를 나타내는 State
 * (**Header** : [FoldableHeaderItemState] - Content [FoldableContentItemState] )
 *
 * @property label Header의 Title
 * @property attendMemberCount 멤버의 출석
 * @property allTeamMemberCount 모든 멤버의 수
 * @property isExpanded 해당 Header의 Icon이 펼쳐진 상태인지를 타나낸다
 */
@Stable
interface FoldableHeaderItemState : FoldableItem {
    val label: String
    val attendMemberCount: Int?
    val allTeamMemberCount: Int
    val isExpanded: Boolean

    data class PositionType(
        override val attendMemberCount: Int?,
        override val allTeamMemberCount: Int,
        override val isExpanded: Boolean,
        val position: String,
    ) : FoldableHeaderItemState {

        override val label: String
            get() = position

    }

    data class TeamType(
        override val attendMemberCount: Int?,
        override val allTeamMemberCount: Int,
        override val isExpanded: Boolean,
        val teamName: String,
        val teamNumber: Int
    ) : FoldableHeaderItemState {

        override val label: String
            get() = "$teamName $teamNumber"

    }
}