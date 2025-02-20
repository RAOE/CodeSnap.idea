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
import groovy.util.logging.Log4j2
import org.apache.log4j.Logger
import org.slf4j.LoggerFactory
/**
 *  ClassName：DefaultCodeSnapAction
    Package:com.github.raoe.codesnapidea.actions
    @DATE:10/11/2024 3:13 pm
    @Author:XuYuanFeng
 */
@Slf4j
open class DefaultCodeSnapAction: AnAction() {
    val DEFAULT_CODE_SNAP_PATH = "C:\\code_snap.idea\\CodeSnap.idea\\src\\main\\resources\\lib\\codesnap.exe";
    //默认png
    var formatter = ".png";
    //生成到桌面还是剪贴板 默认剪贴板
    var outputToClipboard = true;
    companion object {
        // 初始化 Slf4j 日志记录器
        private val logger = LoggerFactory.getLogger(DefaultCodeSnapAction::class.java)
    }
    /**
     * 默认数据
     * @param event
     */
    override fun actionPerformed(event: AnActionEvent) {
        val editor: Editor? = event.getData(CommonDataKeys.EDITOR)
        val project: Project? = event.getData(CommonDataKeys.PROJECT)
        var selectedText: String? = editor?.selectionModel?.selectedText
        val message = StringBuilder()
        var codesnapExePath = DEFAULT_CODE_SNAP_PATH;
        var codesnapExeExists = File(codesnapExePath).exists()
        if(!codesnapExeExists){
            codesnapExePath = getCodesnapExePath()
            codesnapExeExists = File(codesnapExePath).exists()
        }
        logger.info("codesnapExePath: $codesnapExePath");

        if (!selectedText.isNullOrEmpty()) {
            if (codesnapExeExists) {
                try {
                    executeCommand(codesnapExePath, selectedText, message,formatter)
                    message.append("\nSnapshot saved to ${System.getProperty("user.home")}\\desktop\\output"+formatter+" successful!")
                } catch (e: IOException) {
                    message.append("\n执行 codesnap.exe excuted failed! Error message：${e.message}")
                }
            } else {
                message.append("\ncan't find codesnap.exe file，execute failed！")
            }
        } else {
            message.append("No text selected!")
        }

        val title = "CodeSnap: Capture Code Snapshot"
        val icon: Icon = Messages.getInformationIcon()
        Messages.showMessageDialog(
            project,
            message.toString(),
            title,
            icon
        )
    }

    /**
     * 获取codesnap.exe路径
     */
    fun getCodesnapExePath(): String {
        val userHome = System.getProperty("user.home")
        val targetDir = File("$userHome/codeSnap")
        val targetFile = File(targetDir, "codesnap.exe")

        // 如果目标文件已经存在，直接返回路径
        if (targetFile.exists()) {
            logger.info("codesnap.exe already exists at ${targetFile.absolutePath}")
            return targetFile.absolutePath
        }

        // 确保目标文件夹存在
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }
        val resourcePath = "/lib/codesnap.exe"
        val inputStream: InputStream? = Class.forName("com.github.raoe.codesnapidea.actions.DefaultCodeSnapAction").getResourceAsStream(resourcePath)
        if (inputStream == null) {
            logger.info("Unable to find codesnap.exe in resources: $resourcePath")
            return ""
        }
        // 将资源文件写入目标路径
        try {
            Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
            logger.info("Successfully copied codesnap.exe to ${targetFile.absolutePath}")
            return targetFile.absolutePath
        } catch (e: Exception) {
            logger.error("Failed to copy codesnap.exe: ${e.message}", e)
            return ""
        }
    }
    /**
     * 执行命令
     */
    private fun executeCommand(codesnapExePath: String, selectedText: String, message: StringBuilder,format:String):String {
        try {
            val userHome = System.getProperty("user.home")
            val tempDir = File(userHome, "codeSnap")
            if (!tempDir.exists()) {
                tempDir.mkdirs()
            }
            val tempFile = File(tempDir, UUID.randomUUID().toString()+".txt")
            tempFile.writeText(selectedText)
            val tempFilePath = tempFile.absolutePath
            val selectedText = selectedText.trimIndent().replace("\"", "\\\"").replace("\n", "\\n")
            var command = """$codesnapExePath -c $selectedText --output clipboard"""
            if(outputToClipboard){
                command = """$codesnapExePath -c "$selectedText" --output clipboard"""
            }else{
                command = "$codesnapExePath -f $tempFilePath --output $userHome\\desktop\\output"+format
            }
            println("command: $command")
            val process = Runtime.getRuntime().exec(command)
            val stdInput = BufferedReader(InputStreamReader(process.getInputStream()))
            var s: String?
            logger.info("命令执行中...")
            val sb = StringBuilder();
            while ((stdInput.readLine().also { s = it }) != null) {
                sb.append(s);
                println(s)
            }
            // 捕获错误输出
            val stdError = BufferedReader(InputStreamReader(process.getErrorStream()))
            logger.info("错误输出:")
            while ((stdError.readLine().also { s = it }) != null) {
                println(s)
            }
            val exitCode = process.waitFor()
            logger.info("命令执行完毕，退出码: $exitCode")
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