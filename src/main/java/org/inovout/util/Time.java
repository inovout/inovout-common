package org.inovout.util;

/**
 * Utility methods for getting the time and computing intervals.
 */
public final class Time {

  /**
   * Current system time.  Do not use this to calculate a duration or interval
   * to sleep, because it will be broken by settimeofday.  Instead, use
   * monotonicNow.
   * @return current time in msec.
   */
  public static Long now() {
    return System.currentTimeMillis();
  }
  
  /**
   * Current time from some arbitrary time base in the past, counting in
   * milliseconds, and not affected by settimeofday or similar system clock
   * changes.  This is appropriate to use when computing how much longer to
   * wait for an interval to expire.
   * @return a monotonic clock that counts in milliseconds.
   */
  public static long monotonicNow() {
    final long NANOSECONDS_PER_MILLISECOND = 1000000;

    return System.nanoTime() / NANOSECONDS_PER_MILLISECOND;
  }
}

