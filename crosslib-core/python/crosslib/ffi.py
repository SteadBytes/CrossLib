import ctypes
import sys
from ctypes import POINTER, Structure, c_char_p, c_uint32
from pathlib import Path
from uuid import UUID

PKG_DIR = Path(__file__).absolute().parent


def shared_object_for_platform() -> Path:
    """
    Return the expected filename of the crosslib_ffi core library for the
    current platform.

    >>> import sys
    >>> sys.platform
    'linux'
    >>> shared_object_for_platform()
    libcrosslib_ffi.so
    """
    if sys.platform == "linux":
        return Path("libcrosslib_ffi.so")
    elif sys.platform == "darwin":
        return Path("libcrosslib_ffi.dylib")
    elif sys.platform.startswith("win"):
        return Path("crosslib_ffi.ddl")
    raise ValueError(f"Unsupported platform: {sys.platform}")


class IDGeneratorS(Structure):
    pass


class c_char_p_rust(c_char_p):
    def __del__(self):
        lib.crosslib_str_free(self)


lib = ctypes.cdll.LoadLibrary((PKG_DIR / shared_object_for_platform()).absolute())

lib.id_generator_new.argtypes = (c_char_p, c_uint32)
lib.id_generator_new.restype = POINTER(IDGeneratorS)
lib.id_generator_free.argtypes = (POINTER(IDGeneratorS),)
lib.id_generator_iterate.argtypes = (POINTER(IDGeneratorS),)
lib.id_generator_iterate.restype = c_char_p_rust
lib.crosslib_str_free.argtypes = (c_char_p_rust,)


class IdGenerator:
    def __init__(self, seed, max_iterations):
        self.obj = lib.id_generator_new(seed.encode("utf-8"), max_iterations)

    def __del__(self):
        lib.id_generator_free(self.obj)

    def __iter__(self):
        return self

    def __next__(self) -> UUID:
        _id = lib.id_generator_iterate(self.obj)
        if not _id:
            raise StopIteration()
        else:
            return UUID(hex=_id.value.decode("utf-8"))
