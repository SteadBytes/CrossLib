build: build-rust

build-rust: ## Build all Rust code
	cargo build --all

.PHONY: build build-rust

test: test-rust

test-rust: ## Run Rust tests for crosslib-core and crosslib-ffi
	cargo test --all

.PHONY: test test-rust

cbindgen: ## Regenerate the Rust FFI headers
	cbindgen \
		--config crosslib-core/ffi/cbindgen.toml \
		crosslib-core/ffi \
		--lockfile Cargo.lock \
		-o crosslib-core/ffi/crosslib.h

.PHONY: cbindgen
