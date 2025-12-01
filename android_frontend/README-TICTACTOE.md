# Tic Tac Toe - Ocean Professional (Jetpack Compose)

This module implements a complete two-player Tic Tac Toe game using Kotlin and Jetpack Compose with a modern Ocean Professional theme.

Features:
- 3x3 centered grid with rounded cards and subtle elevation
- Player indicator (X/O) and status bar (playing, win, draw)
- Win and draw detection; winning cells subtly animated
- Reset button
- State survives configuration changes (ViewModel + SavedStateHandle)
- Material 3 color scheme using Ocean Professional palette

Build & Run:
1) From the android_frontend directory:
   ./gradlew :app:installDebug
2) Launch the app "Tic Tac Toe Classic" on the connected device/emulator.

Project Notes:
- Compose enabled in app/build.gradle.dcl
- Single-activity architecture (MainActivity) with GameViewModel.
- No external services required.
