package com.github.raoe.codesnapidea.actions.pop

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import javax.swing.Icon

/**
 *  ClassName：PopupDialogAction
    Package:com.github.raoe.codesnapidea.actions
    @DATE:06/11/2024 10:34 pm
    @Author:XuYuanFeng
 */

class PopupDialogAction: AnAction {
    constructor() : super() {
        // This default constructor is used by the IntelliJ Platform framework to instantiate this class based on plugin.xml
        // declarations. Only needed in `PopupDialogAction` class because a second constructor is overridden.
    }

    constructor(text: String?, description: String?, icon: Icon?) : super(text, description, icon) {
        // This constructor is used to support dynamically added menu actions.
        // It sets the text, description to be displayed for the menu item.
        // Otherwise, the default AnAction constructor is used by the IntelliJ Platform.
    }

    override fun actionPerformed(event: AnActionEvent) {
        ShowSettingsUtil.getInstance().showSettingsDialog(event.getProject(), "OtherSettings");
    }

    override fun update(event: AnActionEvent) {
        // Set the availability based on whether a project is open
        val project: Project? = event.project
        event.presentation.isEnabledAndVisible = project != null
    }

}