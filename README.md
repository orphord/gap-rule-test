# gap-rule-test
Gap rule implementation for campspot hiring process

A few things of note to start
1. The data acquisition process is a bit non-standard due to the fact that all data is being read from a single JSON file.  I've given the user the ability to pass in a path to a JSON file of their choice on the local file system, if none is passed then the test-case.json -- which is packaged in the jar file created -- is used.
2. In creating the data structures I decided to allow the Service layer to hold on to the Lists of campsites and reservations instead of making a pseudo-DAO layer which I felt would have made the code more confusing to read without adding any value.
3. 
