package com.github.raoe.codesnapidea.settings

import com.github.raoe.codesnapidea.MyBundle
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.util.ui.FormBuilder
import javax.swing.JCheckBox
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel

class CodeSnapConfigurable : Configurable {

    private val scaleCombo = JComboBox(arrayOf("1x", "2x", "3x"))
    private val paddingSpinner = JSpinner(SpinnerNumberModel(20, 0, 100, 2))
    private val lineNumbersCheckBox = JCheckBox(MyBundle.message("settings.showLineNumbers"))
    private val saveToFileCheckBox = JCheckBox(MyBundle.message("settings.saveToFile"))
    private val directoryField = TextFieldWithBrowseButton()

    private var panel: JPanel? = null

    override fun getDisplayName(): String = "CodeSnap.idea"

    override fun createComponent(): JComponent {
        directoryField.addActionListener {
            val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
            val chosen = FileChooser.chooseFile(descriptor, null, null)
            if (chosen != null) directoryField.text = chosen.path
        }
        saveToFileCheckBox.addActionListener {
            directoryField.isEnabled = saveToFileCheckBox.isSelected
        }
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(MyBundle.message("settings.scale"), scaleCombo)
            .addLabeledComponent(MyBundle.message("settings.padding"), paddingSpinner)
            .addComponent(lineNumbersCheckBox)
            .addComponent(saveToFileCheckBox)
            .addLabeledComponent(MyBundle.message("settings.saveDirectory"), directoryField)
            .addComponentFillVertically(JPanel(), 0)
            .panel
        return panel!!
    }

    override fun isModified(): Boolean {
        val settings = CodeSnapSettings.getInstance()
        return scaleCombo.selectedIndex + 1 != settings.scale ||
            (paddingSpinner.value as Int) != settings.padding ||
            lineNumbersCheckBox.isSelected != settings.showLineNumbers ||
            saveToFileCheckBox.isSelected != settings.saveToFile ||
            directoryField.text.trim() != settings.saveDirectory
    }

    override fun apply() {
        val settings = CodeSnapSettings.getInstance()
        settings.scale = (scaleCombo.selectedIndex + 1).coerceIn(1, 3)
        settings.padding = (paddingSpinner.value as Int).coerceIn(0, 100)
        settings.showLineNumbers = lineNumbersCheckBox.isSelected
        settings.saveToFile = saveToFileCheckBox.isSelected
        settings.saveDirectory = directoryField.text.trim()
    }

    override fun reset() {
        val settings = CodeSnapSettings.getInstance()
        scaleCombo.selectedIndex = (settings.scale - 1).coerceIn(0, 2)
        paddingSpinner.value = settings.padding.coerceIn(0, 100)
        lineNumbersCheckBox.isSelected = settings.showLineNumbers
        saveToFileCheckBox.isSelected = settings.saveToFile
        directoryField.text = settings.saveDirectory
        directoryField.isEnabled = settings.saveToFile
    }

    override fun disposeUIResources() {
        panel = null
    }
}
