import androidx.compose.runtime.Stable
import com.yapp.presentation.ui.admin.management.components.foldableItem.FoldableItem

@Stable
interface FoldableHeaderItemState : FoldableItem {
    val label: String
    val attendMemberCount: Int
    val allTeamMemberCount: Int
    val isExpanded: Boolean

    data class PositionType(
        override val attendMemberCount: Int,
        override val allTeamMemberCount: Int,
        override val isExpanded: Boolean,
        val position: String,
    ) : FoldableHeaderItemState {

        override val label: String
            get() = position

    }

    data class TeamType(
        override val attendMemberCount: Int,
        override val allTeamMemberCount: Int,
        override val isExpanded: Boolean,
        val teamName: String,
        val teamNumber: Int
    ) : FoldableHeaderItemState {

        override val label: String
            get() = "$teamName $teamNumber"

    }
}