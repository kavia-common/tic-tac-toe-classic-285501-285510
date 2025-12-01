androidApplication {
    namespace = "org.example.app"

    // Enable Jetpack Compose using supported declarative key
    compose {
        enabled = true
        // The declarative DSL does not use 'composeCompilerExtension'; it derives the compiler from toolchain.
        // Keeping this block minimal avoids unresolved assignment target errors.
    }

    dependencies {
        // Core Compose
        implementation("androidx.activity:activity-compose:1.9.3")
        implementation("androidx.compose.ui:ui:1.7.5")
        implementation("androidx.compose.material3:material3:1.3.1")
        implementation("androidx.compose.ui:ui-tooling-preview:1.7.5")
        implementation("androidx.compose.material:material-icons-extended:1.7.5")
        implementation("androidx.compose.ui:ui-tooling:1.7.5")

        // Lifecycle + ViewModel
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    }
}
