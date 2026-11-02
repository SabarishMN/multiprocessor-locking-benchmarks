# Multiprocessor Locking & Synchronization

## Downloading this template

If you know `git`, you can clone this repository and work on it.

Otherwise, click the `Clone or download` button on the repository page and then click `Download zip` to download a zip file.   

## Building and Running the template

The below instructions should be run from the top-level directory.

### Running Unit Test

Run the following command to run the unit test:

Linux: `./gradlew test`

Windows: `./gradlew.bat test`

### Building the project and producing binaries:

Linux: ` ./gradlew build -x test`

Windows: `./gradlew.bat build -x test`

This will produce the binaries in the build folder.

This project was completed as part of my multiprocessor programming course. It focuses on implementing and comparing the performance of different synchronization locks.

**Locks Implemented**

MCS Lock: A queue-based lock that is scalable and fair.
TTAS Lock: Reduces contention with a test-and-test-and-set approach.
TAS Lock: A simple spinlock using a test-and-set mechanism.
ALock: Uses an array to implement a queue-based lock.
CLH Lock: A scalable, queue-based lock with local spinning.
**Features**

Implementation of multiple locks in a modular way.
Performance testing under various contention levels.
Comparison of locks based on waiting time, throughput, and scalability
