# gap-rule-test
### Gap rule implementation for campspot hiring process
An implementation of the "Gap Rule" for Campspot where a new reservation may *not* be made leaving a gap of <gap> days.  In this implementation <gap> is 1 day, but it is implemented as a property in a text file, so this could be changed to *any* gap size with a restart of the system.

### Install/Build Instructions
#### Required software
   * Java 8 -- on my system I have Java 10 installed, but bytecode is compiled and tested with Java 8.
   * Apache Maven v.3.5.2
   * git -- on my system I have version 2.17.1, I would *guess* it works with others, but don't know that as a fact
   
### Instructions
   1. In a terminal window navigate to the directory to which you would like to clone the git repository type `git clone https://github.com/orphord/gap-rule-test.git` (<--or copy-paste this text).
    * This will have created a directory called `gap-rule-test/`, navigate there (ie. `cd gap-rule-test/`) (please forgive that I added "test" at the end of the name, I'm thinking of this as a test, so I named the repo that.
   2. Type `mvn clean install`, the build will start and the set of tests will run, I would expect no failures, **If there is a failure, please call or get in touch, that would be surprising and I may need to check out what's up**.
   3. Type `java -jar target/gap-rule-test-0.0.1-SNAPSHOT.jar`.  This will run this system with the `test-case.json` provided with the instructions.
     * Note: I did add the feature to allow a user to specify a json file with the command line parameter --file.loc, so on my system for example I would enter `java -jar target/gap-rule-test-0.0.1-SNAPSHOT.jar --file.loc=/home/orphord/dir/to/another-file.json` and that file would get picked up and processed.
   4. The essential output is to the console as log4j messages.  The important information is between "======================..." and "=================..."

---

### A few technical notes
1. In creating the data structures I decided to allow the Service layer to hold on to the Lists of campsites and reservations instead of making a pseudo-DAO layer which I felt would have made the code more confusing to read without adding any value.
2. The *gap-rule* logic is in the `CampsitesByDateService.java` file.
3. I'm assuming a good/valid JSON file, I definitely am serious about testing input, but felt that in the interest of time/clarity that would be overkill for this exercise.

### Approach
In solving the gap issue, I started with the idea that there are *good* days and *bad* days, as I thought through it more I found the question of good or bad were not deep enough.  There is a difference between a good start day and a good end day, and just because a search's start and end days happen to be *good*, does **not** mean the reservation is acceptable.

Here's an example:

|Days->|Day 1|Day 2|Day 3|Day 4|Day 5|Day 6|Day 7|
|---|---|---|---|---|---|---|---|
|Reserved?|||res1|res1|res1|||
|Can start New reservation?|Y|Y|N|N|N|Y|N|
|Can end New reservation?|N|Y|N|N|N|Y|Y|

Observations I made through this process are the following:
* A new reservation may start on the day following the existing reservation, but not the day after that day (hence the gap).
* Similarly for end dates except the question is about finishing before the existing reservation.
* A corner case is where a potential new reservation spans (ie. start date is before and end date is after) an existing reservation. In order to handle this case each day within the reservation must be checked.

