import ctypes
import sys
from ctypes import POINTER, Structure, c_char_p, c_uint32
from pathlib import Path

from uuid import UUID


def shared_object_path() -> Path:
    lib_dir = Path(__file__).parent
    if sys.platform == "linux":
        return lib_dir / Path("libcrosslib_ffi.so")
    elif sys.platform == "darwin":
        return lib_dir / Path("libcrosslib_ffi.dylib")
    elif sys.platform.startswith("win"):
        return lib_dir / Path("crosslib_ffi.ddl")
    raise ValueError(f"Unsupported platform: {sys.platform}")


class IDGeneratorS(Structure):
    pass


class c_char_p_rust(c_char_p):
    def __del__(self):
        lib.crosslib_str_free(self)


so = shared_object_path()
lib = ctypes.cdll.LoadLibrary(so.absolute())

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
