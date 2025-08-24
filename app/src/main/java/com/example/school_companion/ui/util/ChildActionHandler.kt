package com.example.school_companion.ui.util

import androidx.navigation.NavController
import com.example.school_companion.data.model.Child
import com.example.school_companion.ui.card.child.ChildAction
import com.example.school_companion.ui.navigation.Screen
import com.example.school_companion.ui.viewmodel.ChildDetailViewModel

object ChildActionHandler {
    fun handle(
        token: String,
        child: Child,
        action: ChildAction,
        navController: NavController,
        childrenViewModel: ChildDetailViewModel
    ) {
        when (action) {
            is ChildAction.ViewDetails -> {
                navController.navigate("${Screen.ChildDetail.route}/${child.id}")
            }

            is ChildAction.Edit -> {
                childrenViewModel.updateChild(token, child.id, action.updatedChild)
            }

            is ChildAction.Delete -> {
                childrenViewModel.deleteChild(token, child.id)
            }
        }
    }
}
