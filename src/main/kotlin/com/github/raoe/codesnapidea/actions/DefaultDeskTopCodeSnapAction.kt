package com.github.raoe.codesnapidea.actions
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import groovy.util.logging.Slf4j
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.UUID
import javax.swing.Icon
import kotlin.jvm.java
import com.intellij.openapi.diagnostic.thisLogger

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