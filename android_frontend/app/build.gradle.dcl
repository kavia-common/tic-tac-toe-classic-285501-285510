androidApplication {
    // This must match the package in Kotlin sources (.MainActivity) and Manifest.
    namespace = "org.example.app"

    // Enable Jetpack Compose via the supported Declarative DSL.
    // NOTE: org.gradle.experimental.android-ecosystem uses `compose { enabled = true }`.
    // The key 'composeCompilerExtension' is NOT recognized; compiler extension is derived from toolchain.
    compose {
        enabled = true
    }

    dependencies {
        // Core Compose UI
        implementation("androidx.activity:activity-compose:1.9.3")
        implementation("androidx.compose.ui:ui:1.7.5")
        implementation("androidx.compose.material3:material3:1.3.1")
        implementation("androidx.compose.ui:ui-tooling-preview:1.7.5")
        implementation("androidx.compose.material:material-icons-extended:1.7.5")

        // Tooling - keep in main config due to declarative constraints
        implementation("androidx.compose.ui:ui-tooling:1.7.5")

        // Lifecycle + ViewModel
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

        // Material Components (XML-backed themes like Theme.Material3.* for AAPT)
        implementation("com.google.android.material:material:1.12.0")
    }
}
