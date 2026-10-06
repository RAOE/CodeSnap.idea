<img width="390" src="/doc/logo.png" />

> [!WARNING]  
> Notice: This project is still in WIP, and hasn't published to plugin market yet.

# CodeSnap.idea

**Capture beautiful code snapshots right inside your IDE.**

Select some code, copy it as a syntax-highlighted image, and paste it anywhere — docs, chats, slides, issue trackers.

![Build](https://github.com/RAOE/CodeSnap.idea/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/MARKETPLACE_ID.svg)](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/MARKETPLACE_ID.svg)](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID)

<!-- Plugin description -->
**CodeSnap.idea — capture code snapshots inside IntelliJ IDEA.**

Copy the selected code as a syntax-highlighted image to the clipboard in one action. The snapshot is rendered with your current editor color scheme and font, so it looks exactly like your IDE — dark or light themes included. Rendered at 2x resolution for crisp pasting into documents, chats and slides.

- Copy selection, or the whole file when nothing is selected
- Respects your color scheme, editor font and tab size
- Trigger from <kbd>Edit</kbd> menu, the editor context menu, or press <kbd>Ctrl</kbd>+<kbd>Alt</kbd>+<kbd>Shift</kbd>+<kbd>C</kbd>
- Balloon notification confirms the snapshot is on your clipboard
<!-- Plugin description end -->

## ✨ Features

- 📋 **Copy code as image** — the selected code (or the entire file, if nothing is selected) is rendered and copied to the clipboard as a PNG.
- 🎨 **Native look** — uses your editor's color scheme, font and tab size, so dark and light themes both look exactly like your IDE.
- 🔍 **Sharp output** — rendered at 2x scale with antialiasing and rounded corners; paste it anywhere without it looking blurry.
- ⌨️ **Easy to trigger** — <kbd>Edit</kbd> menu, right-click context menu, or the <kbd>Ctrl</kbd>+<kbd>Alt</kbd>+<kbd>Shift</kbd>+<kbd>C</kbd> shortcut.
- 🔔 **Feedback** — a balloon notification shows the size of the copied snapshot.

## 🚀 Development

```bash
./gradlew runIde        # run a sandbox IDE with the plugin installed
./gradlew buildPlugin   # build the installable plugin zip (build/distributions)
./gradlew test          # run tests
```

## Installation

Once published to the JetBrains Marketplace, the plugin can be installed via:

- Using the IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "CodeSnap.idea"</kbd> >
  <kbd>Install</kbd>

- Using JetBrains Marketplace:

  Go to [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID) and install it by clicking the <kbd>Install to ...</kbd> button in case your IDE is running.

  You can also download the [latest release](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID/versions) from JetBrains Marketplace and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

- Manually (from a local build):

  Build with `./gradlew buildPlugin`, then install the zip from `build/distributions` using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation
