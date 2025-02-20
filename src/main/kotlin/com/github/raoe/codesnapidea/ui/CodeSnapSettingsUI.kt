import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.components.ServiceManager
import java.awt.FlowLayout
import java.io.File
import javax.swing.*


class CodeSnapSettingsUI {
    private val properties: PropertiesComponent = ServiceManager.getService(PropertiesComponent::class.java)
    private var defaultSavePath: String
        get() = properties.getValue("codeSnap.defaultSavePath", "/default/path")
        set(value) = properties.setValue("codeSnap.defaultSavePath", value)
    private val defaultPanel: JPanel = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        border = BorderFactory.createEmptyBorder(0, 10, 0, 10)
    }
    private val defaultSavePathLabel: JLabel = JLabel("Default image save path:")
    private val mySavePathArea: JTextField = JTextField(20)  // 5行40列
    private val mySavePathButton: JButton = JButton("Choose")
    private val defaultJLabel: JLabel = JLabel("Default image format:")
    private val defaultJComboBox: JComboBox<String> = JComboBox(arrayOf("jpg", "png", "ascii", "jpeg", "svg"))
    private val defaultJsonConfiguration: JLabel = JLabel("Default JSON configuration:")
    private val optionConfig: JLabel = JLabel("Option configuration")
    //1、默认剪贴板快捷键：
    //              2、默认快照快捷键:
    //              3、默认快照格式设置
    //              4、config 自定义配置支持外部导入
    //              5、默认生成的背景颜色选择
    //              6、水印名称配置
    //              7、命令行能力？
    private val listData:List<String> = listOf("Default clipboard shortcuts","Default snapshot shortcut","Default snapshot format settings","Watermark name configuration","Command-line capabilities")
    init{
        val firstRowPanel = JPanel(FlowLayout(FlowLayout.LEFT,5,5)).apply {
            add(defaultSavePathLabel)
            add(mySavePathArea)
            add(mySavePathButton)
        }
        val secondRowPanel = JPanel(FlowLayout(FlowLayout.LEFT,5,5)).apply {
            add(defaultJLabel)
            add(defaultJComboBox)
        }
        val thirdRowPanel = JPanel(FlowLayout(FlowLayout.LEFT,5,5)).apply {
            add(defaultJsonConfiguration)
        }
        val fourthRowPanel = JPanel(FlowLayout(FlowLayout.LEFT,5,5)).apply {
            add(optionConfig)
            for (ele in listData) {
                 val box:JCheckBox = JCheckBox(ele);
                 add(box)
            }
        }
        defaultPanel.add(firstRowPanel)
        defaultPanel.add(secondRowPanel)
        defaultPanel.add(thirdRowPanel)
        defaultPanel.add(fourthRowPanel)
        mySavePathButton.addActionListener {
            val fileChooser = JFileChooser()
            fileChooser.selectedFile = File(mySavePathArea.text.trim())
            if (fileChooser.showOpenDialog(defaultPanel) == JFileChooser.APPROVE_OPTION) {
                mySavePathArea.text = fileChooser.selectedFile.absolutePath
                defaultSavePath = fileChooser.selectedFile.absolutePath
            }
        }
    }

    fun getPanel(): JPanel {
        return defaultPanel
    }

    fun isModified(): Boolean {
        return mySavePathArea.text.trim() != defaultSavePath
    }

    fun apply() {
        defaultSavePath = mySavePathArea.text.trim()
        // 日志记录
    }

    fun reset() {
        mySavePathArea.text = defaultSavePath
        // 日志记录
    }

    fun saveSettings() {
        properties.setValue("codeSnapSavePath", mySavePathArea.text.trim())
    }

    fun loadSettings() {
        mySavePathArea.text = properties.getValue("codeSnapSavePath", defaultSavePath)
    }
}