package com.github.raoe.codesnapidea.actions

import com.intellij.openapi.actionSystem.AnActionEvent

/**
 *  ClassName：SvgCodeSnapAction
    Package:com.github.raoe.codesnapidea.actions
    @DATE:10/11/2024 3:14 pm
    @Author:XuYuanFeng
 */
//继承Default
class SvgCodeSnapAction: DefaultCodeSnapAction() {
    //重写actionPerformed
    override fun actionPerformed(event: AnActionEvent) {
        DEFAULT_FORMATTER = ".svg"
        super.actionPerformed(event)
    }
}