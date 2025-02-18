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
class DefaultCodeSnapAction: AnAction() {

    val DEFAULT_CODE_SNAP_PATH = "C:\\code_snap.idea\\CodeSnap.idea\\src\\main\\resources\\lib\\codesnap.exe";
    override fun actionPerformed(event: AnActionEvent) {
        val editor: Editor? = event.getData(CommonDataKeys.EDITOR)
        val project: Project? = event.getData(CommonDataKeys.PROJECT)
        val selectedText: String? = editor?.selectionModel?.selectedText
        val message = StringBuilder()
        var codesnapExePath = DEFAULT_CODE_SNAP_PATH;
        //获取lib下的\src\main\resources\lib\codesnap.exe
        var codesnapExeExists = File(codesnapExePath).exists()
        if(!codesnapExeExists){
            codesnapExePath = getCodesnapExePath()
            codesnapExeExists = File(codesnapExePath).exists()
        }
        thisLogger().info("codesnapExePath: $codesnapExePath");
        if (!selectedText.isNullOrEmpty()) {
            message.append(selectedText).append(" Selected!Default")
            if (codesnapExeExists) {
                try {
                    message.append(executeCommand(codesnapExePath, selectedText, message))
                } catch (e: IOException) {
                    message.append("\n执行 codesnap.exe excuted failed! Error message：${e.message}")
                }
            } else {
                message.append("\ncan't find codesnap.exe file，execute failed！")
            }
        } else {
            message.append("No text selected!")
        }

        val title = "codesnap"
        val icon: Icon = Messages.getInformationIcon()
        Messages.showMessageDialog(
            project,
            message.toString(),
            title,
            icon
        )
    }
    fun getCodesnapExePath(): String {
        val userHome = System.getProperty("user.home")
        val targetDir = File("$userHome/codeSnap")
        val targetFile = File(targetDir, "codesnap.exe")

        // 如果目标文件已经存在，直接返回路径
        if (targetFile.exists()) {
            thisLogger().info("codesnap.exe already exists at ${targetFile.absolutePath}")
            return targetFile.absolutePath
        }

        // 确保目标文件夹存在
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }
        val resourcePath = "/lib/codesnap.exe"
        val inputStream: InputStream? = Class.forName("com.github.raoe.codesnapidea.actions.DefaultCodeSnapAction").getResourceAsStream(resourcePath)
        if (inputStream == null) {
            thisLogger().info("Unable to find codesnap.exe in resources: $resourcePath")
            return ""
        }
        // 将资源文件写入目标路径
        try {
            Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
            thisLogger().info("Successfully copied codesnap.exe to ${targetFile.absolutePath}")
            return targetFile.absolutePath
        } catch (e: Exception) {
            thisLogger().error("Failed to copy codesnap.exe: ${e.message}", e)
            return ""
        }
    }
    /**
     * 执行命令
     */
    private fun executeCommand(codesnapExePath: String, selectedText: String, message: StringBuilder):String {
        try {
            val userHome = System.getProperty("user.home")
            val tempDir = File(userHome, "codeSnap")
            if (!tempDir.exists()) {
                tempDir.mkdirs()
            }
            val tempFile = File(tempDir, UUID.randomUUID().toString()+".txt")
            tempFile.writeText(selectedText)
            val tempFilePath = tempFile.absolutePath
            val command = "$codesnapExePath -f $tempFilePath --output $userHome\\desktop\\output.png"
            println("执行命令：$command")
            val process = Runtime.getRuntime().exec(command)
            val stdInput = BufferedReader(InputStreamReader(process.getInputStream()))
            var s: String?
            println("标准输出:")
            val sb = StringBuilder();
            while ((stdInput.readLine().also { s = it }) != null) {
                sb.append(s);
                println(s)
            }
            // 捕获错误输出
            val stdError = BufferedReader(InputStreamReader(process.getErrorStream()))
            println("错误输出:")
            while ((stdError.readLine().also { s = it }) != null) {
                println(s)
            }
            val exitCode = process.waitFor()
            println("命令执行完毕，退出码: " + exitCode)
            //清空临时文件
            if (tempFile.exists()){
                tempFile.delete()
            }
            return sb.toString();
        } catch (e: Exception) {
            e.printStackTrace()
            return "执行命令失败！错误信息：${e.message}"
        }
        return ""
    }
}