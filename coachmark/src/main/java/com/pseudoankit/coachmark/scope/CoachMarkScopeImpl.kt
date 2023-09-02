package com.pseudoankit.coachmark.scope

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import com.pseudoankit.coachmark.model.HighlightedViewConfig
import com.pseudoankit.coachmark.model.OverlayClickEvent
import com.pseudoankit.coachmark.model.ToolTipPlacement
import com.pseudoankit.coachmark.model.TooltipConfig
import com.pseudoankit.coachmark.util.CoachMarkKey

internal class CoachMarkScopeImpl(
    internal val onOverlayClicked: (CoachMarkKey) -> OverlayClickEvent
) : CoachMarkScope {

    private var _currentVisibleTooltip: TooltipConfig? by mutableStateOf(null)
    private var _lastVisibleTooltip: TooltipConfig? by mutableStateOf(null)
    private val coachMarkItems = mutableMapOf<CoachMarkKey, TooltipConfig>()

    private var visibleTooltips = listOf<TooltipConfig>()
        set(value) {
            visibleTooltipIndex = 0
            updateVisibleItem(value, visibleTooltipIndex)
            field = value
        }

    private var visibleTooltipIndex = 0
        set(value) {
            updateVisibleItem(visibleTooltips, value)
            field = value
        }

    override val lastVisibleTooltip: TooltipConfig?
        get() = _lastVisibleTooltip

    override val currentVisibleTooltip: TooltipConfig?
        get() = _currentVisibleTooltip

    override fun Modifier.enableCoachMark(
        key: CoachMarkKey,
        toolTipPlacement: ToolTipPlacement,
        highlightedViewConfig: HighlightedViewConfig,
    ): Modifier = onGloballyPositioned { layoutCoordinates ->
        coachMarkItems[key] = TooltipConfig(
            toolTipPlacement = toolTipPlacement,
            key = key,
            layout = TooltipConfig.Layout(
                width = layoutCoordinates.size.width,
                height = layoutCoordinates.size.height,
                startX = layoutCoordinates.positionInRoot().x,
                startY = layoutCoordinates.positionInRoot().y,
            ),
            highlightedViewConfig = highlightedViewConfig
        )
    }

    override fun show(vararg keys: CoachMarkKey) {
        visibleTooltips = keys.map { key ->
            coachMarkItems[key]
                ?: throw NotImplementedError("definition for key $key not found, please provide key by using [Modifier.enableCoachMark]")
        }
    }

    override fun hide() {
        visibleTooltips = listOf()
    }

    fun onOverlayClicked() {
        val item = currentVisibleTooltip ?: return

        when (onOverlayClicked(item.key)) {
            OverlayClickEvent.GoNext -> visibleTooltipIndex++
            OverlayClickEvent.DismissAll -> hide()
            OverlayClickEvent.None -> {}
            OverlayClickEvent.GoPrevious -> visibleTooltipIndex--
        }
    }

    private fun updateVisibleItem(items: List<TooltipConfig>, visibleTooltipIndex: Int) {
        _lastVisibleTooltip = _currentVisibleTooltip
        _currentVisibleTooltip = items.getOrNull(visibleTooltipIndex)
    }
}