package com.github.raoe.codesnapidea.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.xmlb.XmlSerializerUtil

@Service
@State(name = "CodeSnapSettings", storages = [Storage("codesnap.xml")])
class CodeSnapSettings : PersistentStateComponent<CodeSnapSettings> {

    /** Snapshot scale multiplier: 1, 2 (default) or 3. */
    var scale: Int = 2

    /** Base padding around the code, in 1x pixels. */
    var padding: Int = 20

    var showLineNumbers: Boolean = false

    var saveToFile: Boolean = false

    /** Blank means the user's home directory. */
    var saveDirectory: String = ""

    override fun getState(): CodeSnapSettings = this

    override fun loadState(state: CodeSnapSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): CodeSnapSettings = service()
    }
}
