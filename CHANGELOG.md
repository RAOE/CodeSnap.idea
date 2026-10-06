<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# CodeSnap.idea Changelog

## [Unreleased]

## [0.1.0] - 2026-10-06
### Added
- Copy code snapshot action: copy the selected code (or the whole file) as a syntax-highlighted image to the clipboard. Available from the Edit menu, the editor context menu and `Ctrl+Alt+Shift+P`.
- Settings page (`Settings | Tools | CodeSnap.idea`): snapshot scale 1x–3x, padding, optional line numbers and saving the snapshot as a PNG file.
- Chinese (Simplified) localization for all UI messages.
- Plugin icon.

### Changed
- Removed the template scaffolding (sample tool window, project service, activation listener, sample tests).

### Fixed
- Removed the 2024.2 upper compatibility bound so the plugin can be installed in all newer IDE versions.
