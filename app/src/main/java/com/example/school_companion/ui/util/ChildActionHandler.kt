package com.example.school_companion.ui.util

import com.example.school_companion.data.api.ChildDto
import com.example.school_companion.data.model.Child
import com.example.school_companion.feature.children.child.ChildAction
import com.example.school_companion.navigation.NavigateToWithArgs
import com.example.school_companion.navigation.Screen

object ChildActionHandler {
    fun handle(
        child: Child,
        action: ChildAction,
        onNavigate: NavigateToWithArgs,
        onDeleteChild: (childId: Long) -> Unit,
        onEditChild: (childId: Long, updatedChild: ChildDto) -> Unit,
        onSetSelectedChild: ((Child) -> Unit)? = null
    ) {
        when (action) {
            is ChildAction.ViewDetails -> {
                onSetSelectedChild?.invoke(child)
                onNavigate(Screen.ChildDetail, mapOf("childId" to child.id))
            }

            is ChildAction.Edit -> {
                onEditChild(child.id, action.updatedChild)
            }

            is ChildAction.Delete -> {
                onDeleteChild(child.id)
            }
        }
    }
}
