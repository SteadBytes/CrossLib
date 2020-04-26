from crosslib import IdGenerator


def test_generator():
    max_iterations = 10
    gen = IdGenerator("Welcome to crosslib!", max_iterations)

    ids = list(gen)

    assert len(ids) == max_iterations