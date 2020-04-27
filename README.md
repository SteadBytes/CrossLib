# CrossLib

> Work in progress.

An example of implementing a shared core library to be wrapped in multiple
higher level libraries across multiple platforms e.g. Android, iOS, Python etc.

CrossLib provides facilities for randomly generating IDs. This was chosen to be
indicative of a self contained yet potentially complex set of functionality. In
this case the ID generation is little more than a wrapper around [UUID 4](https://en.wikipedia.org/wiki/Universally_unique_identifier)
however one can imagine more complex requirements e.g. application specific ID
schemes, security, performance etc.

## Build Environment Quickstart

### Android

Note: This is not required if you are not intending to use the `Makefile` tasks and instead use the
Gradle features of Android Studio.

1. Open project in Android Studio
2. Install Android build dependencies via `Tools -> SDK Manager -> SDK Tools`:
    * Android SDK Tools
    * NDK r21
    * CMAke
    * LLDB
3. Set `ANDROID_HOME` environment variable to the path Android SDK Location from `Tools -> SDK Manager`.
4. Set `ANDROID_NDK_ROOT` environment variable to `$ANDROID_HOME/ndk-bundle`.
5. Set `JAVA_HOME` to the location of the Android Studio JDK from "Project Structure" menu.

### Rust

1. Install Rust https://www.rust-lang.org/tools/install
2. Add platform specific toolchains for Rust cross-compilation:

```
$ rustup target add aarch64-linux-android
$ rustup target add armv7-linux-androideabi
$ rustup target add i686-linux-android
$ rustup target add x86_64-linux-android
```

### Python

1. Install Python >= 3.5
2. Run `make python-setup` to automatically create a virtual environment with development
   dependencies.

### Tasks

After the language pre-requisites are complete, see `Makefile` for pre-defined build/test tasks.

## TODO

* iOS library
* Documentation
  * Project wide
    * Overall structure & explanation thereof
    * Build processes
  * Per library
* Artifact publishing
  * Push Rust crates to crates.io
  * Upload `.aar`, `.jar` to Maven repo
  * Publish `pip` package for Python library
* Investigate [PyO3](https://github.com/PyO3/pyo3) to generate Python library from Rust directly
* CI
  * Run tests across each platform
  * Build/upload docs
  * Automatically publish on new releases
