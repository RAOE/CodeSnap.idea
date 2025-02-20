package com.github.raoe.codesnapidea.actions
import com.intellij.openapi.actionSystem.AnActionEvent
/**
 *  ClassName：JpgCodeSnapAction
    Package:com.github.raoe.codesnapidea.actions
    @DATE:10/11/2024 3:15 pm
    @Author:XuYuanFeng
 */

class JpgCodeSnapAction: DefaultCodeSnapAction() {
    /**
     * 执行命令
     * @param event
     */
    override fun actionPerformed(event: AnActionEvent) {
        formatter = ".jpg";
        super.actionPerformed(event)
    }
}