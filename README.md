# gap-rule-test
### Gap rule implementation for a reservation system
An implementation of the "Gap Rule" for a reservation system where a new reservation may *not* be made leaving a gap of *some number* days.  In this implementation the gap is 1 day, but the gap value is implemented as a property in a text file, so this could be changed to *any* gap size with a restart of the system.

### Install/Build Instructions
#### Required software
   * Java 8 -- on my system I have Java 10 installed, but bytecode is compiled and tested to Java 8.
   * Apache Maven v.3.5.2
   * git -- on my system I have version 2.17.1, I would *guess* it works with others, but don't know that as a fact
   
### Instructions to install and run
1. In a terminal window navigate to the directory to which you would like to clone the git repository type `git clone https://github.com/orphord/gap-rule-test.git` (<--or copy-paste this text).
   * This will have created a directory called gap-rule-test/, navigate there (ie. cd gap-rule-test/)
2. Type `mvn clean install`, the build will start and the set of tests will run, I would expect no failures, **If there is a failure, please call or get in touch, that would be surprising and I may need to check out what's up**.
3. Type `java -jar target/gap-rule-test-0.0.1-SNAPSHOT.jar`.  This will start the server, when startup is complete requests can be sent to `http://localhost:8080/gaprule/search?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD`.
   * **Note:** In a production application I would have added some validation of these input values as well as validation for the data to be returned to the client.  Some example validation for the incoming strings would be that they are of the format above (ie. YYYY-MM-DD) and that the end date is later than or equal to the start date.
4. The essential output is a JSON string representing the sites available for the search criteria based on the business rules implemented.

---

### Some Assumptions I've made
* All sites are available after the latest unacceptable date of the existing reservations.  That is there's no forward looking time limit and a reservation 2 years (or more) in the future would be fine.  If a search is done with a start date *after* the latest unacceptable date for existing reservations, all sites would be available.
* I created a "start date", where time begins for the context of this application.  My first thought was to use today -- that is the startup date of the application -- but the test data provided had dates in the past, so in the `application.properties` file May 1, 2018 is the start date.  If a search begins before the start date, zero sites would match.
* I'm assuming a good/valid date inputs (as mentioned above), I definitely am serious about validating input, but felt that in the interest of time/clarity that would be overkill for this exercise.

### A few technical notes
1. In creating the data structures I decided to allow the Service layer to hold on to the Lists of sites and reservations instead of making a pseudo-DAO layer which I felt would have made the code more confusing to read without adding any value.
2. The *gap-rule* logic is in the `SitesByDateService.java` file.

### Approach
The basic approach to the solution is to map a set of sites to days which are acceptable for a potential reservation's start date, end date, or any day of the reservation (ie. is that site already reserved).  An example will help:

|Days->|Day 1|Day 2|Day 3|Day 4|Day 5|Day 6|Day 7|Day 8|
|---|---|---|---|---|---|---|---|---|
|Reserved?|||res1|res1,res2|res1,res2|res2|||

* Here "res1" and "res2" indicate site 1 and site 2 are reserved on the indicated day.
* For this example we're going to assume this is a three site facility.  Sites are 1, 2, and 3.
* The following table is a representation of the data structure for acceptable start dates for a potential reservation.

|Date|Sites for which Date is an acceptable start date|Note|
|---|---|---|
|Day 1|[1,2,3]||
|Day 2|[1,2,3]|*Site 1 would be available for an overnight stay*|
|Day 3|[2,3]|*Site 2 would be available for an overnight stay*|
|Day 4|[3]||
|Day 5|[3]||
|Day 6|[1,3]|*Site 1 is available the day after a reservation ends*|
|Day 7|[2,3]|*Site 1 is UNavailable due to gap rule*|
|Day 8|[1,3]|*Site 2 is unavailable due to gap rule*|
|Day 9+|[1,2,3]|*All sites available day 9 and later*|

* Above is a table representation of the key data structure I used to model availability.  The `SiteSearchByDateService` has three; one for acceptable start dates for potential reservations, one for acceptable end dates, and one for reserved dates only to check for the *span* case (see *Obervations* below).
* The technical details are that each of these is a Map<LocalDate,Set<Site>> (encapsulated in the `SitesByDate` class) where every day from some start date to the latest unacceptable start date among the existing reservations keys the set of sites for which that date is acceptable.
  * There are three `SitesByDate` objects which are owned by the `SitesSearchByDateService` class
    1. `sitesByUnreservedDate` -- the set of sites that do *not* have a reservation for the keyed date.
    2. `sitesByAcceptableStartDate` -- the set of sites where the keyed date is ok as a start date for a new reservation.
      * Note: reserved dates are accounted for as unacceptable start dates as it would be awkward for someone to start their stay in an already occupied site.
    3. `sitesByAcceptableEndDate` -- the set of sites where the keyed date is ok as an end date for a new reservation.
      * Note: similarly to start date, reserved dates are accounted for as unacceptable end dates.
  * The `mungeReservationData()` method creates these three objects getting the existing reservations from the ReservationService and the list of all site IDs from the SiteService.
    * The architectural decision to create a separate service layer component (`SitesSearchByDateService`) was because it really sits between reservations and sites and I felt it was a good logical component to separate from the data services.
    * Additionally, having a `SiteSearchService` that has the `SitesSearchByDateService` would make it fairly easy/obvious where to extend search capability to other dimensions.  For example if we needed to search for sites based on the max length of the site or tents only, additional services could be created and held by `SiteSearchService`.


#### Testing:
* I generally subscribe to the testing model of using two types of tests:
  1. Unit Tests -- Approximately all classes should have some level of test coverage.  Unit tests are to test the basic functionality of a class in isolation.
  2. Integration Tests -- This is a test (or set of tests) that test the system working as a whole.  In the case of a Spring Boot application, the whole application context needs to be created and wired together for a proper test.
* In this case, since the implementation is not massive, I've added the word "Integration" to the integration test class.  In a larger module, I might create a JUnit test suite for unit tests and integration tests to compartmentalize them.

#### Observations:
* A new reservation may start on the day following the existing reservation, but not the day after that day (hence the gap).
* Similarly for end dates except the question is about finishing before the existing reservation.
* A corner case is where a potential new reservation *spans* (ie. start date is before and end date is after) an existing reservation. In order to handle this case each day within the reservation must be checked.

