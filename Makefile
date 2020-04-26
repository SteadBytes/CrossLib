PYVENV := $(shell python3 -c "import sys; print('crosslib-core/python/.venv' + '.'.join(str(x) for x in sys.version_info[:2]))")

python-setup: $(PYVENV)/bin/python3 ## Create a Python virtualenv for developing the Python library
	@:

$(PYVENV)/bin/python3:
	python3 -m venv $(PYVENV)
	$(PYVENV)/bin/pip install --upgrade pip
	$(PYVENV)/bin/pip install -r crosslib-core/python/requirements_dev.txt

build: build-rust

build-rust: ## Build all Rust code
	cargo build --all

build-kotlin: ## Build all Kotlin code
	./gradlew build -x test

build-apk: ## Build CrossLib example application APK
	./gradlew crosslib:build
	./gradlew crosslib-example-app:build

build-python: python-setup build-rust ## Build the Python library
	$(PYVENV)/bin/python3 crosslib-core/python/setup.py install

.PHONY: build build-rust build-kotlin build-apk

test: test-rust

test-rust: ## Run Rust tests for crosslib-core and crosslib-ffi
	cargo test --all

test-kotlin: ## Run Kotlin tests
	./gradlew testDebugUnitTest

test-python: ## Run Python tests
	$(PYVENV)/bin/py.test crosslib-core/python/tests

.PHONY: test test-rust test-kotlin

cbindgen: ## Regenerate the Rust FFI headers
	cbindgen \
		--config crosslib-core/ffi/cbindgen.toml \
		crosslib-core/ffi \
		--lockfile Cargo.lock \
		-o crosslib-core/ffi/crosslib.h

.PHONY: cbindgen
