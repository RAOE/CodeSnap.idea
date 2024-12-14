package com.github.raoe.codesnapidea.configuration;

import CodeSnapSettingsUI

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.Nullable
import javax.swing.JComponent


/**
 * ClassName：CodeSnapConfiguration
 * Package:com.github.raoe.codesnapidea.configuration
 *
 * @DATE:13/11/2024 9:34 pm
 * @Author:XuYuanFeng TODO:
 */
class CodeSnap: Configurable {
    private var mySettingsUI: CodeSnapSettingsUI? = null
    @Nls
    override fun getDisplayName(): String {
        return "CodeSnap Configuration" // 配置页面的显示名称
    }

    @Nullable
    override fun getHelpTopic(): String? {
        return "help-topic-id" // 配置页面的帮助主题ID
    }

    @Nullable
    override fun createComponent(): JComponent? {
        mySettingsUI = CodeSnapSettingsUI()
        return mySettingsUI?.getPanel() // 返回配置界面的组件
    }

    override fun isModified(): Boolean {
        return mySettingsUI?.isModified() ?: false // 检查设置是否被修改
    }

    @Throws(ConfigurationException::class)
    override fun apply() {
        mySettingsUI?.apply() // 应用设置
    }

    override fun reset() {
        mySettingsUI?.reset() // 重置设置
    }

    override fun disposeUIResources() {
        mySettingsUI = null // 释放UI资源
    }

}
