import shutil
import sys
from pathlib import Path

import toml
from setuptools import find_packages, setup

LIBNAME = "crosslib"
FFI_LIBNAME = LIBNAME + "_ffi"
HEADER_FILENAME = LIBNAME + ".h"

# Unfortunately, this exact logic is duplicated within crosslib/ffi.py when
# loading the shared object. This shouldn't change often but will need changing
# in crosslib/ffi.py as well as here if it does.
if sys.platform == "linux":
    shared_object = Path(f"lib{FFI_LIBNAME}.so")
elif sys.platform == "darwin":
    shared_object = Path(f"lib{FFI_LIBNAME}.dylib")
elif sys.platform.startswith("win"):
    shared_object = Path(f"{FFI_LIBNAME}.ddl")
else:
    raise ValueError(f"Unsupported platform: {sys.platform}")

PY_DIR = Path(__file__).parent.absolute()
PKG_DIR = PY_DIR / "crosslib"
ROOT = PY_DIR.parent.parent
CORE_DIR = ROOT / "crosslib-core"
FFI_DIR = CORE_DIR / "ffi"
SO_BUILD_DIR = ROOT / "target/debug"

with (ROOT / "README.md").open() as f:
    readme = f.read()

# Note: Using the core library Cargo version may not be appropriate depending on
# the release/versioning strategy used by a project. This is assuming that new
# versions of *all* the platform libraries are released simultaneously, however
# one may with to update them separately in which extra care will need to be
# taken to manage a "compatibility matrix" with the core library.
with (CORE_DIR / "Cargo.toml").open() as f:
    cargo_toml = toml.load(f)
    version = cargo_toml["package"]["version"]


# Copy core library headers and shared object into package
shutil.copyfile(FFI_DIR / HEADER_FILENAME, PKG_DIR / HEADER_FILENAME)
shutil.copyfile(SO_BUILD_DIR / shared_object, PKG_DIR / shared_object)


setup(
    author="Ben Steadman",
    author_email="steadmanben1@gmail.com",
    classifiers=[
        "Intended Audience :: Developers",
        "Programming Language :: Python :: 3",
        "Topic :: Software Development :: Libraries",
    ],
    long_description=readme,
    long_description_content_type="text/markdown",
    include_package_data=True,
    name="crosslib",
    packages=find_packages(include=["crosslib", "crosslib.*"]),
    zip_safe=False,
    package_data={LIBNAME: [HEADER_FILENAME, str(shared_object)]},
)
