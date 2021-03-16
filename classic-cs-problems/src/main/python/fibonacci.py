def calculate_fib(n: int) -> int:
    if n < 2:
        return n
    return calculate_fib(n - 2) + calculate_fib(n - 1)


if __name__ == '__main__':
    print(calculate_fib(5))
