package com.example.school_companion.ui.util

import androidx.navigation.NavController
import com.example.school_companion.data.model.Child
import com.example.school_companion.feature.children.child.ChildAction
import com.example.school_companion.navigation.Screen
import com.example.school_companion.feature.children.ChildrenViewModel

object ChildActionHandler {
    fun handle(
        child: Child,
        action: ChildAction,
        navController: NavController,
        childrenViewModel: ChildrenViewModel
    ) {
        when (action) {
            is ChildAction.ViewDetails -> {
                navController.navigate("${Screen.ChildDetail.route}/${child.id}")
            }

            is ChildAction.Edit -> {
                childrenViewModel.updateChild(child.id, action.updatedChild)
            }

            is ChildAction.Delete -> {
                childrenViewModel.deleteChild(child.id)
            }
        }
    }
}
