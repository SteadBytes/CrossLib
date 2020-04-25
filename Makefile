
build: build-rust

build-rust: ## Build all Rust code
	cargo build --all

build-kotlin: ## Build all Kotlin code
	./gradlew build -x test

.PHONY: build build-rust build-kotlin

test: test-rust

test-rust: ## Run Rust tests for crosslib-core and crosslib-ffi
	cargo test --all

test-kotlin: ## Test all Kotlin code
	./gradlew testDebugUnitTest

.PHONY: test test-rust test-kotlin

cbindgen: ## Regenerate the Rust FFI headers
	cbindgen \
		--config crosslib-core/ffi/cbindgen.toml \
		crosslib-core/ffi \
		--lockfile Cargo.lock \
		-o crosslib-core/ffi/crosslib.h

.PHONY: cbindgen
