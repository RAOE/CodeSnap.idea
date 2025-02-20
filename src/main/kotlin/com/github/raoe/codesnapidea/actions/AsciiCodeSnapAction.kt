package com.github.raoe.codesnapidea.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import javax.swing.Icon

/**
 *  ClassName：AsciiCodeSnapAction
    Package:com.github.raoe.codesnapidea.actions
    @DATE:10/11/2024 4:02 pm
    @Author:XuYuanFeng
 */
class AsciiCodeSnapAction: AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val editor: Editor? = event.getData(CommonDataKeys.EDITOR)
        val project: Project? = event.getData(CommonDataKeys.PROJECT)
        val selectedText: String? = editor?.selectionModel?.selectedText
        val message = StringBuilder()
        if (!selectedText.isNullOrEmpty()) {
            message.append(selectedText).append(" Selected!JPG")
        } else {
            message.append("No text selected!")
        }
        val title = "Selection Info"
        val icon: Icon = Messages.getInformationIcon()
        Messages.showMessageDialog(
            project,
            message.toString(),
            title,
            icon
        )
    }
}