// A simple C program for traversal of a linked list
#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>
#include <stdbool.h>

int x = 55;

bool isPerfectSquare(int x);
bool isFibonacci(int x);

int main() {
    if (isFibonacci(x)) {
        printf("\non fibonaccin luku\n");
    }else {
        printf("\neipä ollu\n");
    }
    return 0;
}

bool isPerfectSquare(int x) {
    int s = sqrt(x);
    printf("x: %d\n", x);
    printf("s: %d\n\n", s);
    return (s*s == x);
}

// Returns true if n is a Fibonacci Number, else false
bool isFibonacci(int x) {
    return isPerfectSquare(5*x*x + 4) || isPerfectSquare(5*x*x - 4);
}
