package com.github.raoe.codesnapidea.actions
import com.intellij.openapi.actionSystem.AnActionEvent
/**
 *  ClassName：AsciiCodeSnapAction
    Package:com.github.raoe.codesnapidea.actions
    @DATE:10/11/2024 4:02 pm
    @Author:XuYuanFeng
 */
class AsciiCodeSnapAction: DefaultCodeSnapAction() {
    override fun actionPerformed(event: AnActionEvent) {
        outputToClipboard = true;
        outputToImg = false;
        super.actionPerformed(event)
    }
}