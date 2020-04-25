package com.steadbytes.crosslib

import com.steadbytes.crosslib.ffi.LibCrossLibFFI
import com.steadbytes.crosslib.ffi.getAndFreeStringFromRust
import java.util.*


class GeneratorExhausted : IllegalStateException()


/**
 * Generates up to [maxIterations] IDs from a given [seed] value.
 *
 * @throws GeneratorExhausted when [maxIterations] = 0
 *
 * This class encapsulates FFI calls to the core Rust library; "shielding" consumers of the Android
 * library from the lower FFI level - especially handling nulls! One could imagine that this might
 * utilise the Android Keystore to generate a seed from a secure key, for example.
 */
class IDGenerator(seed: ByteArray, maxIterations: Int) : Iterator<UUID> {
    private val ptr = LibCrossLibFFI.INSTANCE.id_generator_new(seed, maxIterations)

    /**
     * Publicly visible for introspection purposes, privately modifiable to ensure correctness;
     * code outside this class does (and should not) know what causes a generator to be exhausted
     * and thus should not be able to modify this value.
     */
    var exhausted = false
        private set

    /**
     * The next ID is generated ahead of time to ensure Iterator hasNext() method is correct since
     * id_generator_iterate returns null to indicate exhaustion. This is not ideal and indicates
     * that the core library FFI interface needs some work!
     *
     * As a side note, this is not a problem in Python due to EAFP and simply raising StopIteration
     * when the null is returned.
     */
    private var nextId = iterate()

    private fun iterate(): UUID {
        // Returns null to when the Rust IDGenerator is exhausted
        val idPtr = LibCrossLibFFI.INSTANCE.id_generator_iterate(ptr)
        if (idPtr != null) {
            return UUID.fromString(idPtr.getAndFreeStringFromRust())
        } else {
            throw GeneratorExhausted()
        }
    }

    override fun hasNext() = !exhausted

    override fun next(): UUID {
        val currentId = nextId
        try {
            nextId = iterate()
        } catch (e: GeneratorExhausted) {
            // Prevents further iteration via Iterator hasNext()
            exhausted = true
        } finally {
            return currentId
        }
    }

    protected fun finalize() {
        // Free Rust memory when this object is GC'd
        LibCrossLibFFI.INSTANCE.id_generator_free(ptr)
    }
}
