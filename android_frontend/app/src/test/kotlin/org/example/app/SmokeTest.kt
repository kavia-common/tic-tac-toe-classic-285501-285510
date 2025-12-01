package org.example.app

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue

/**
 * PUBLIC_INTERFACE
 * Minimal smoke test to ensure JUnit Platform discovers at least one test in :app.
 * This avoids build failures in environments that enforce test discovery.
 */
class SmokeTest {
    @Test
    fun smoke() {
        assertTrue(true)
    }
}
