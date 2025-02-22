package com.github.raoe.codesnapidea.actions
import com.intellij.openapi.actionSystem.AnActionEvent
import groovy.util.logging.Slf4j
/**
 *  ClassName：DefaultCodeSnapAction
    Package:com.github.raoe.codesnapidea.actions
    @DATE:10/11/2024 3:13 pm
    @Author:XuYuanFeng
 */
@Slf4j
open class DefaultDeskTopCodeSnapAction: DefaultCodeSnapAction() {

    override fun actionPerformed(event: AnActionEvent) {
        outputToClipboard = false;
        super.actionPerformed(event)
    }
}