package com.github.raoe.codesnapidea.ui

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

/**
 *  ClassName：CodeSnapSettings
    Package:com.github.raoe.codesnapidea.ui
    @DATE:13/11/2024 10:11 pm
    @Author:XuYuanFeng
 */
@State(name = "CodeSnapSettings", storages = [Storage("codeSnapSettings.xml")])
class CodeSnapSettings : PersistentStateComponent<CodeSnapSettings.State> {
    class State(var shortcutEnabled: Boolean = false, var shortcut: String = "defaultShortcut", var defaultSavePath: String = "/default/path")

    private var state = State()

    override fun getState(): State {
        return state
    }

    override fun loadState(state: State) {
        this.state = state
    }

    var shortcutEnabled: Boolean
        get() = state.shortcutEnabled
        set(value) { state.shortcutEnabled = value }

    var shortcut: String
        get() = state.shortcut
        set(value) { state.shortcut = value }

    var defaultSavePath: String
        get() = state.defaultSavePath
        set(value) { state.defaultSavePath = value }
}