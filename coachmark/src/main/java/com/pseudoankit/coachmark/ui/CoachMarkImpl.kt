package com.pseudoankit.coachmark.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.pseudoankit.coachmark.model.UnifyCoachMarkGlobalConfig
import com.pseudoankit.coachmark.scope.CoachMarkScope
import com.pseudoankit.coachmark.scope.CoachMarkScopeImpl
import com.pseudoankit.coachmark.util.clickable
import com.pseudoankit.coachmark.util.toDp

@Composable
internal fun <KEY> CoachMarkImpl(
    globalCoachMarkConfig: UnifyCoachMarkGlobalConfig<KEY> = UnifyCoachMarkGlobalConfig(),
    content: @Composable CoachMarkScope<KEY>.() -> Unit
) {
    val coachMark =
        remember(globalCoachMarkConfig) { CoachMarkScopeImpl<KEY>(globalCoachMarkConfig) }
    val item = coachMark.activeItem

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        content(coachMark)

        if (item != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(item.overlayConfig.overlayColor)
                    .clickable(
                        showRipple = false,
                        onClick = coachMark::onOverlayClicked
                    )
            ) {
                Box(
                    modifier = Modifier
                        .offset(
                            x = item.coordinate.x.toDp(LocalDensity.current),
                            y = item.coordinate.y.toDp(LocalDensity.current),
                        )
                        .then(item.itemConfig.modifier),
                    contentAlignment = Alignment.Center
                ) {
                    Text(color = item.itemConfig.textColor, text = item.itemConfig.text)
                }
            }
        }
    }
}