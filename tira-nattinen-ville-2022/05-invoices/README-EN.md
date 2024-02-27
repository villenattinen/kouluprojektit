# 05-invoices Large arrays sorting and data matching exercise

Data structures and algorithms 2022.

> Note: in this exercise, the code, when executed, generates small and large output text files, e.g. `l-to-collect.txt`. **Do not add these to git** since there is no need to version these and especially no need to upload them to the remote repository.

## The goal

The goal of this exercise is to learn how to design fast algorithms for handling of large arrays.

The exercise has three phases:

1. Study an existing algorithm to create new invoices from unpaid ones and test both the functional correctness and the time complexity of it.
2. Implement a functionally correct and a better time complexity version of the algorithm of phase 1.
3. Test the time complexity of your implementation and fix it if necessary.

**Note** that in this task (as in all of them) you **may not** use the Java container classes (anything that directly or indirectly implements the `Collection` or `Map` interface, for example `ArrayList`, `Vector`, etc.), nor algorithms from the classes `Arrays` nor `Collections` -- you will implement the algorithms yourself. In this exercise there is no need (nor you should implement) for any new data structures, but the existing ones in the code are enough.

## Prerequisites

As usual, the tools need to work, you need to know how to execute unit tests and deliver your finshed exercise as instructed in the course.

You have already implemented generic algorithms in your `Algorithms` class: a slow sorting algorithm and and a binary search algorithm.

**Before you continue ahead**, copy the `Algorithms.java` file from the `05-binsearch` exercise where you implemented binary search to it. **Put** the copied file in the **sources** directory of this exercise project.

## Phase 1 - Analysing the slow implementation

The structure of the exercise is depicted in the UML class diagram below.

![UML class diagram](classes.png)

The project contains following Java source files:

* `Invoice` having an invoice number, sum to invoice and the due date, invoiced from our clients.
* `Payment`, having an invoice number and sum paid for that invoice by our client.
* `InvoiceInspector`, a class which goes through the invoices and paid sums and generates new invoices to a new file, for those invoices not fully paid yet, with the same invoice number.

Note that the Invoice and Payment classes implement the Java `Comparable` interface. Read the documentation of the [Comparable interface](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Comparable.html). As all Java classes, Invoice and Payment are also subclasses of [Object](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html).

Rules of the game are:

* the payments file contains *only zero or one payment* per invoice, *not* many payments.
* the payments file may have a payment for an invoice *not* in the invoices file.
* the new invoices generated *must* be in *ascending* order by the invoice number and saved as such (not your job) in the new invoices file.

Reading the invoices and payments from input files and writing new invoices of unpaid invoices to output files are already implemented for you in the `InvoiceInspector.readInvoicesAndPayments()` and `InvoiceInspector.saveNewInvoices()` methods. So you do not have to and you should not implement reading or saving files.

**Study** the slow code to see how it:

1. goes through all the invoices, and
1. for each invoice, goes through all the payments and
1. for the payment found for an invoice, calculates the remaining sum to pay, and
1. if the payment didn't fully cover the invoice, a new invoice is created,
1. and if no payment at all was found for the invoice, the invoice is included in the output,
1. then the invoices are sorted to ascending order by the invoice number, as required, **using your slow `Algorithms.sort` method**!, and
1. finally, the new invoices to send are being saved to a file "to-collect....txt".

There are two test classes for testing the `InvoiceInspector` implementation:

* `InvoicesPaymentsSmallTests` -- it is used to test the `InvoiceInspector` with small amout of data. Using it we can make sure our functionality works before using the class for large amounts of invoices and payments. The small test data files are `invoices.txt` and `payments.txt` -- you *must not change* these files in any way during the exercise!
* `InvoicesPaymentsLargeTests` -- it is used to test the `InvoiceInspector` with large amouts of data. This is to make sure functionality is not only OK but also performant with large number of invoices and payments.

To make sure our algorithm for resending invoices works, *a checksum* has been calculated for the *expected* test data output file. The tests calculate a checksum for the generated file and compare it to the expected checksum. It the checksums differ, the generated invoice file is not valid.

**Run** the `InvoicesPaymentsSmallTests.newInvoicesFromPaymentsSlowTest` **test** to make sure the functionality works. Either run the test method test directly from VS Code by clicking "Run Test" above the test method name, or from the terminal:

```console
 mvn test -Dtest=InvoicesPaymentsSmallTests#newInvoicesFromPaymentsSlowTest test
 ```

The test should pass without issues. If you **take a look** at the output file `to-collect-slow.txt` that is generated by the test run, and compare it to `invoices.txt` and `payments.txt` you should see that it really is correct. 

For example, the small `invoices.txt` file contains the following invoice number 1111111 for 100 euros. Last long number is having the due date of the invoice in number of milliseconds since the standard base time known as "the epoch", namely January 1, 1970, 00:00:00 GMT.

```
1111111,100,1641031773865
```

So invoice number 1111111 has been sent (earlier), with 100€ bill to the customer. Our company got the payments customers have paid from the bank, contain the following payment in the `payments.txt` file:

```
1111111,10
```

From here you can see that for the bill 1111111 customer has paid 10€, so the third file generated by the algorithm should include a new bill for that customer with 90€ remaining to pay, in the `to-collect-slow.txt` file:

```
1111111,90,1642762545478
```
with a new invoice date in the future.

So the code works, everything is OK and *we can transfer our implementation into production use*? 

**Not necessarily.**

Code that is functionally OK, is not necessarily OK for production, since it has not been tested with *real data* from the production environment. With real *amounts of data*. So, now **execute** the same tests using **large** test files:

```console
 mvn test -Dtest=InvoicesPaymentsLargeTests#newInvoicesFromPaymentsSlowTest test
 ```

Or again from the VS Code by clicking the "Run Test" text right above the `InvoicesPaymentsLargeTests.newInvoicesFromPaymentsSlowTest` method name.

You may wait for a while for the test to finish. But do not wait too long unless you have something more interesting to do while waiting:

```console
Starting to handle the invoices the slow way...
Handling the large invoices & payments files took 116583 ms
```

The slow algorithm took around 116583 milliseconds, that is about 116 seconds which is almost two minutes. The test computer executing that sample is far from a slow computer so cannot blame that one. If you have a slow machine, you can **abort the tests** with `ctrl-c` in terminal or using the stop button in VS Code after a few minutes.

The reason for the slow performance is that the algorithm in `InvoiceInspector.handleInvoicesAndPaymentsSlow` is processing the **89999** invoices and **72000** payments in the large test data files, in random order in the arrays. It is using two loops within loops to do this. We already saw in the course lectures that this is not a good thing with large data sets.

The *time complexity* of the above algorithm is N x M, that is 89999 x 72000, meaning **6 479 928 000 comparisons to handle** in the worst case. That is **not acceptable**.

> Note that the time above does not include reading the data files into memory nor writing the new invoices to a new file. It only measures the time taken to handle the invoices and payments already in the memory.

Surely we must do better!? That is the topic for the next step below.

## Design and implement a faster algorithm

Your goal is to **design** an algorithm and then implement it in the method: 

```Java
InvoiceInspector.handleInvoicesAndPaymentsFast()
```

So that it is **significantly faster** than the current algorithm. One teacher implementation with the same test PC as above slow test took:

```console
Starting to handle the invoices the faster way...
Handling the large invoices & payments files took 133 ms
```

That is about **876 times faster** than the slow algorithm with two nested loops.

Then execute the test `InvoicesPaymentsSmallTests.newInvoicesFromPaymentsFastTest` to make sure your algorithm produces the correct result, having the correct checksum in the results file.

Note that you **do not need to use*** any new **data structures** than those already used in the code. You just need to find out how you can speed up the processing of the invoices and payments with the current data structures. So design how to use **algorithms** introduced in the course that make this faster using the existing arrays as the data structure.

Note also that the insertion sort you implemented earlier (`01-arrays`) is too slow with large data sets. The course lectures introduce several fast sorting algorithms. **Implement a faster sorting algorithm** as a generic method in your `Algorithms.java` source file:

```Java
public static <E extends Comparable<E>> void fastSort(Comparable<E>[] array)
```

If you are unsure about what algorithms to use, **ask the teachers** in the course exercises or discussion forums! Most of the problems students bang their heads against the wall in this course is because you never ask -- so please, do **ask**.

When designing the algorithms here, think of

1. The slow algorithm presented here and,
2. the earlier `05-binsearch` exercise -- what is necessary for this task (hint hint) to be *fast*.

---

After design and implementation, execute the tests with *large test files* to make sure your algorithm is *considerably* faster than the one tested above. If the slow algorithm several minutes to produce the correct output file, your new implementation should be significantly faster.

Reminding of the rules of handling the invoices and payments:

1. Ignore payments that have no corresponding invoice.
1. If a payment could *not* be found for the invoice, create a new identical invoice to be resent to the client.
1. If a payment could be found for an invoice, and invoiced - payed is > 0, create a new invoice for the remaining sum.
1. If a payment is equal or larger to the sum in invoice, it is considered paid an no new invoice is created
1. The output must have the invoices sorted in ascending order by the invoice number

**Implement** the method so that the resulting file contains the new invoices to be sent to the customers so that you: 

1. follow the instructions above,
1. without changing the input data files in *any way*,
1. using the existing arrays as data structures,
1. so that the tests pass.

**First** run only the `InvoicesPaymentsSmallTests` tests, since it uses the small files. Using it you can verify that your algorithm works, quickly:

```console
mvn -Dtest=InvoicesPaymentsSmallTests#newInvoicesFromPaymentsFastTest test
```

As mentioned, the tests know the right answer -- the contents of the output file. A *SHA checksum* has been calculated beforehand from the correct output file (ignoring the changing date value, considering only invoice numbers and sums). The test calculates the hash from *your* output file. If the checksums differ, the files are different. Meaning your implementation does not follow the instructions correctly and/or does not have the correct output. You may perhaps either:

* miss invoices from the output file, 
* invoice may have a wrong sum in the invoice,
* have invoices that do not belong in the output file, or 
* the invoices are not in ascending order by the invoice number.

If the test fails, find out why and fix the issues.

## Test the faster algorithm

When your algorithm works, test the **time complexity** of the implementation with the large files by running the `InvoicesPaymentsLargeTests` tests:

```console
mvn -Dtest=InvoicesPaymentsLargeTests#newInvoicesFromPaymentsFastTest test
```

**Make sure** your implementation is fast enough. Instead of N x M mentioned above (almost N^2), it should be around N log2 M.

You should see **significant** improvements.

## Delivery

When your test pass *and* the time complexity of your implementation is much faster than the slow one, you are ready for delivery.

> Note: in this exercise, the code, when executed, generates small and large output text files, e.g. `l-to-collect.txt`. **Do not add these to git** since there is no need to version these and especially no need to upload them to the remote repository.

Remember to commit added and changed .java files to git.

Deliver the exercise as instructed in the course, after your tests pass and the implementation is fast enough.

## Questions or problems?

Participate in the course lectures, exercises and online support group.

## About

* Course material for Tietorakenteet ja algoritmit | Data structures and algorithms 2021.
* Study Program for Information Processing Science, University of Oulu.
* (c) Antti Juustila, INTERACT Research Group.
