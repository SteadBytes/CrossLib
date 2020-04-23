use crosslib_core::IDGenerator;
use ffi_support::define_string_destructor;
use libc::c_uchar;
use std::convert::TryInto;
use std::ffi::CString;

/// Callers are responsible for freeing the returned pointer using
/// [`id_generator_free`]
#[no_mangle]
pub extern "C" fn id_generator_new(seed: *const c_uchar, max_iterations: u32) -> *mut IDGenerator {
    // FIXME: Panic
    assert!(!seed.is_null(), "null pointer to seed received");
    let seed_slice: &[u8] = unsafe { std::slice::from_raw_parts(seed, 32) };
    // FIXME: Panic
    let seed_arr: [u8; 32] = seed_slice
        .try_into()
        .expect("cannot create array from seed");
    Box::into_raw(Box::new(IDGenerator::new(seed_arr, max_iterations)))
}

/// Callers are responsible for freeing the returned pointer using
/// [`crosslib_str_free`]
#[no_mangle]
pub extern "C" fn id_generator_iterate(ptr: *mut IDGenerator) -> *const c_uchar {
    // FIXME: Panic
    assert!(!ptr.is_null());
    let gen = unsafe { &mut *ptr };
    if let Some(id) = gen.next() {
        // FIXME: Panic
        CString::new(id.to_string())
            .expect("Could not convert UUID into CString")
            .into_raw() as *const c_uchar
    } else {
        // Generator exhausted
        // TODO: How to best signal this case to FFI caller?
        std::ptr::null()
    }
}

#[no_mangle]
pub extern "C" fn id_generator_free(ptr: *mut IDGenerator) {
    if ptr.is_null() {
        return;
    }
    unsafe {
        Box::from_raw(ptr);
    }
}

define_string_destructor!(crosslib_str_free);
