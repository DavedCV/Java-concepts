# Exceptions, Assertions and Logging

In a perfect world, users would never enter data in the wrong form, files they
choose to open would always exist, and code would never have bugs.

Encountering errors is unpleasant. If a user loses all the work he or she did
during a program session because of a programming mistake or some external
circumstance, that user may forever turn away from your program. At the
very least, you must:

- Notify the user of an error
- Save all work
- Allow users to gracefully exit the program.

During testing, you need to run lots of checks to make sure your program
does the right thing. But those checks can be time-consuming and unnecessary
after testing has completed. You could just remove the checks and stick them
back in when additional testing is required—but that is tedious. Use the assertion
facility for selectively activating checks.

When your program does the wrong thing, you can’t always communicate
with the user or terminate. Instead, you may want to record the problem for
later analysis. For this use a logging framework.
