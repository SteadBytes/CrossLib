package com.steadbytes.crosslib

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@RunWith(JUnit4::class)
class IDGeneratorTest {
    @Test
    fun `Generates a sequence of IDs up to maxIterations`() {
        val seed = IntArray(32) { it }.map { it.toByte() }.toByteArray()
        val maxIterations = 10 // Arbitrary choice
        val gen = IDGenerator(seed, maxIterations)
        val ids = gen.asSequence().toList()
        assertEquals(maxIterations, ids.size)
        assertTrue { gen.exhausted }
    }

    @Test
    fun `Throws GeneratorExhausted when maxIterations == 0`() {
        // This is a side effect of pre-generating the next ID and should be re-designed.
        val seed = IntArray(32) { it }.map { it.toByte() }.toByteArray()
        val maxIterations = 0
        assertFailsWith<GeneratorExhausted> {
            IDGenerator(seed, maxIterations)
        }
    }
}
