"""
This file exists to enable pytest to 'find' the crosslib module when invoking
via py.test *without* manually modifying PYTHONPATH directly.
https://docs.pytest.org/en/latest/pythonpath.html#standalone-test-modules-conftest-py-files

This could, of course, also be used for normal conftest.py purposes e.g.
fixtures, commands, hooks etc.
"""
