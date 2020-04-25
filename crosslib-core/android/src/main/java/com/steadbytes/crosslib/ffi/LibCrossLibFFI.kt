package com.steadbytes.crosslib.ffi

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Pointer

/**
 * Read a String received from Rust and immediately free it **on the Rust side**.
 *
 * # Safety
 * **DO NOT use this pointer again after calling this**
 *
 * Reading the String from the pointer is assumed to be safe as the Rust library ensures
 * that it is **correctly null-terminated**.
 */
internal fun Pointer.getAndFreeStringFromRust(): String {
    try {
        // *Copies* native memory into Java String
        return this.getString(0, "utf8")
    } finally {
        LibCrossLibFFI.INSTANCE.crosslib_str_free(this)
    }
}

internal interface LibCrossLibFFI : Library {
    companion object {
        private val JNA_LIBRARY_NAME = "crosslib_ffi"

        internal var INSTANCE: LibCrossLibFFI =
            Native.load(JNA_LIBRARY_NAME, LibCrossLibFFI::class.java) as LibCrossLibFFI
    }

    // IDGenerator lifecycle functions
    fun id_generator_new(seed: ByteArray, max_iterations: Int): Pointer
    fun id_generator_free(ptr: Pointer)

    // IDGenerator usage functions
    fun id_generator_iterate(ptr: Pointer): Pointer?

    // Misc
    fun crosslib_str_free(ptr: Pointer)
}
