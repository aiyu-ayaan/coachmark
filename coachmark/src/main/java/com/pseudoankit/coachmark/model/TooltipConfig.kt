package com.pseudoankit.coachmark.model

import com.pseudoankit.coachmark.util.CoachMarkKey

public data class TooltipConfig(
    val layout: Layout,
    val toolTipPlacement: ToolTipPlacement,
    val key: CoachMarkKey,
    val highlightedViewShape: HighlightedViewConfig.Shape,
    val animationState: AnimationState = AnimationState()
) {

    public data class Layout(
        val width: Int,
        val height: Int,
        val startX: Float,
        val startY: Float,
    ) {
        val endX: Float = startX + width
        val endY: Float = startY + height
    }

    public data class AnimationState(
        val from: State = State.Gone,
        val to: State = State.Gone
    )

    public enum class State {
        Visible, Gone
    }
}
