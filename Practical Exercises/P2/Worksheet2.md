# Worksheet 1

## Question 1 - Thread Safety

### (a)

Java's built in ArrayList has no built-in thread safety, so `addBook` can race
condition, and `displayBooks` makes a immutable Iterator which could be broken
if another thread calls `addBook`.

### (b)

There are two mutexes for the same resource:

1. `lock`, used in `add`.
2. `this`, used in `equals`.

So a thread calling `add` could be halfway through writing to x when another
thread calls `equals`, messing up the result.

Probably switch to using `lock` for both, as using `this` is public so external
threads can use it.

### (c)

1. `data1.size()` is unsafe as you are adding to `data1` after the call.
2. `addData` and `dotProduct` can overlap.
3. Two threads calling `addData` can overlap (`data1` gets added to twice,
`data2` gets added to twice).

## Question 2 - Multithreaded Cron

See `cron/`.
