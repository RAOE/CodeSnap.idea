package com.github.raoe.codesnapidea.actions

import com.github.raoe.codesnapidea.MyBundle
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.colors.EditorFontType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.awt.image.BufferedImage

/**
 * Copies the selected code, or the whole file when nothing is selected,
 * to the clipboard as a syntax-highlighted image rendered with the
 * editor's current color scheme.
 */
class CopyCodeSnapshotAction : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.EDT

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.getData(CommonDataKeys.EDITOR) != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val project = e.project

        val selectionModel = editor.selectionModel
        val start = if (selectionModel.hasSelection()) selectionModel.selectionStart else 0
        val end = if (selectionModel.hasSelection()) selectionModel.selectionEnd else editor.document.textLength
        if (start >= end) {
            notify(project, MyBundle.message("notification.nothingToCopy"), NotificationType.WARNING)
            return
        }

        val image = try {
            renderSnapshot(editor, start, end)
        } catch (t: Throwable) {
            notify(project, MyBundle.message("notification.renderFailed", t.message ?: t.javaClass.simpleName), NotificationType.ERROR)
            return
        }
        if (image == null) {
            notify(project, MyBundle.message("notification.tooLarge"), NotificationType.WARNING)
            return
        }

        Toolkit.getDefaultToolkit().systemClipboard.setContents(ImageTransferable(image), null)
        notify(project, MyBundle.message("notification.copied", image.width, image.height), NotificationType.INFORMATION)
    }

    private fun notify(project: Project?, message: String, type: NotificationType) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("CodeSnap.idea")
            .createNotification(message, type)
            .notify(project)
    }

    /** Returns null when the text is too large to render as a single image. */
    private fun renderSnapshot(editor: Editor, start: Int, end: Int): BufferedImage? {
        val scheme = editor.colorsScheme
        val tabSize = try {
            editor.settings.getTabSize(editor.project)
        } catch (_: Throwable) {
            4
        }.coerceAtLeast(1)

        val collected = collectLines(editor, scheme, start, end)
        val lines = if (collected.size > 1 && collected.last().isEmpty()) collected.dropLast(1) else collected

        val scale = 2.0
        val schemeFont = scheme.getFont(EditorFontType.PLAIN)
        val baseFont = schemeFont.deriveFont((schemeFont.size * scale).toFloat()).deriveFont(Font.PLAIN)

        val scratch = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)
        val scratchGraphics = scratch.createGraphics()
        val baseMetrics = scratchGraphics.getFontMetrics(baseFont)
        val lineHeight = baseMetrics.height
        val ascent = baseMetrics.ascent
        val padding = (20 * scale).toInt()

        val rows = ArrayList<List<Piece>>(lines.size)
        var maxWidth = 0
        for (line in lines) {
            var column = 0
            var x = 0
            val pieces = ArrayList<Piece>(line.size)
            for (run in line) {
                val segment = expandTabs(run.text, column, tabSize)
                if (segment.isEmpty()) continue
                column += segment.length
                val font = baseFont.deriveFont(run.fontType)
                val width = scratchGraphics.getFontMetrics(font).stringWidth(segment)
                pieces.add(Piece(x, segment, font, run.fg))
                x += width
            }
            if (x > maxWidth) maxWidth = x
            rows.add(pieces)
        }
        scratchGraphics.dispose()

        val imageWidth = maxWidth + padding * 2
        val imageHeight = rows.size * lineHeight + padding * 2
        if (imageWidth > 20_000 || imageHeight > 20_000) return null

        val image = BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB)
        val g = image.createGraphics()
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
            g.color = scheme.defaultBackground ?: Color(0x2B2B2B)
            val corner = (12 * scale).toInt()
            g.fillRoundRect(0, 0, imageWidth, imageHeight, corner, corner)
            rows.forEachIndexed { index, pieces ->
                val baseline = padding + index * lineHeight + ascent
                for (piece in pieces) {
                    g.font = piece.font
                    g.color = piece.color
                    g.drawString(piece.text, padding + piece.x, baseline)
                }
            }
        } finally {
            g.dispose()
        }
        return image
    }

    private fun collectLines(editor: Editor, scheme: EditorColorsScheme, start: Int, end: Int): List<List<Run>> {
        val document = editor.document
        val defaultFg = scheme.getAttributes(HighlighterColors.TEXT)?.foregroundColor ?: Color.BLACK
        val lines = ArrayList<MutableList<Run>>()
        lines.add(ArrayList())

        fun append(text: String, fg: Color, fontType: Int) {
            var rest = text
            while (true) {
                val nl = rest.indexOf('\n')
                if (nl < 0) {
                    if (rest.isNotEmpty()) lines.last().add(Run(rest, fg, fontType))
                    return
                }
                if (nl > 0) lines.last().add(Run(rest.substring(0, nl), fg, fontType))
                lines.add(ArrayList())
                rest = rest.substring(nl + 1)
            }
        }

        val highlighter = editor.highlighter
        if (highlighter == null) {
            append(document.getText(TextRange(start, end)), defaultFg, Font.PLAIN)
        } else {
            var lastCovered = start
            val iterator = highlighter.createIterator(start)
            while (!iterator.atEnd() && iterator.start < end) {
                val from = maxOf(iterator.start, start)
                val to = minOf(iterator.end, end)
                if (to > from) {
                    val attributes = iterator.textAttributes
                    append(
                        document.getText(TextRange(from, to)),
                        attributes?.foregroundColor ?: defaultFg,
                        attributes?.fontType ?: Font.PLAIN
                    )
                }
                lastCovered = maxOf(lastCovered, to)
                iterator.advance()
            }
            if (lastCovered < end) {
                append(document.getText(TextRange(lastCovered, end)), defaultFg, Font.PLAIN)
            }
        }
        return lines
    }

    private fun expandTabs(text: String, startColumn: Int, tabSize: Int): String {
        if (!text.contains('\t')) return text
        val sb = StringBuilder(text.length)
        var column = startColumn
        for (ch in text) {
            if (ch == '\t') {
                val spaces = tabSize - (column % tabSize)
                repeat(spaces) { sb.append(' ') }
                column += spaces
            } else {
                sb.append(ch)
                column++
            }
        }
        return sb.toString()
    }

    private class ImageTransferable(private val image: BufferedImage) : Transferable {

        override fun getTransferDataFlavors(): Array<DataFlavor> = arrayOf(DataFlavor.imageFlavor)

        override fun isDataFlavorSupported(flavor: DataFlavor): Boolean = flavor == DataFlavor.imageFlavor

        override fun getTransferData(flavor: DataFlavor): Any {
            if (flavor != DataFlavor.imageFlavor) throw UnsupportedFlavorException(flavor)
            return image
        }
    }

    private class Run(val text: String, val fg: Color, val fontType: Int)

    private class Piece(val x: Int, val text: String, val font: Font, val color: Color)
}
