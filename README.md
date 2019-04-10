# Readme
    
### Project Structure
-   app/  						Application source code    
-   doc/  						User and design documentation    
-   doc/examples/  	Demo example files for use with the application    
-   conf/  					Configuration files required to build the project

### SonarQube Analytics
To view project analytics, simply log into a UC lab computer and access the url: `http://csse-s302g3.canterbury.ac.nz:9090`

For remote access to project analytics, open a terminal and run: `ssh -L 50000:localhost:9090 sengstudent@csse-s302g3.canterbury.ac.nz`. This will open a tunnel into UC's network, and will enable you to access SonarQube locally (`localhost:9090`).

To login as the PO, you may login with the credentials: Login: *Moffat*, Password: *Moffat*.
    
## Running the Project (Lab Machine)
To run the project for **production**:
1. Navigate to the eng-git repository located [here](https://eng-git.canterbury.ac.nz/seng302-2019/team-300) 
2. Ensure that the Master branch is selected and download the latest artifact
3. Navigate to: `backend/target/universal`
4. Unzip: `team300-travelea-1.0.0-SNAPSHOT.zip`
5. Navigate to: `team300-travelea-1.0.0-SNAPSHOT/bin`
6. From here, you can run the command: `bash team300-travelea -Dplay.http.secret.key=QCYtAnfkaZiwrNwnxIlR6CTfG3gf90Latabg5241ABR5W1uDFNIk1 -Dplay.evolutions.db.default.autoApply=true`.
6. If you do not wish to run that command, you can create a file called `run` with the contents below:
	```
	#!/bin/bash
	chmod +x team300-travelea
	./team300-travelea -Dplay.http.secret.key=QCYtAnfkaZiwrNwnxIlR6CTfG3gf90Latabg5241ABR5W1uDFNIk1 -Dplay.evolutions.db.default.autoApply=true
	```
    (Run the file by double clicking it and choosing "run in terminal" OR more simply run the command: `./run`)
8. Regardless of which method you choose, to populate the database, run the following command: `curl -X POST localhost:9000/api/populate`
9. Navigate to: `localhost:9000`
10. Login with email: *adam@test.com*, password: *123*

**Note: If there are permission issues executing the `run` file, simply give the `run` file execution permissions by running the  command:** `chmod -R 777 ./run`
 
To run the project for **development**:
1. Clone the version you'd like to run from the eng-git repository
2. Navigate to the project's root directory
3. To run the **backend**, navigate to `/backend` and run the command: `sbt dist`
4. Once the backend is built and running, navigate to: `localhost:9000` and click "Apply the script now!", you should see an Action Not Found screen.
5. To populate the database, run the following command: `curl -X POST localhost:9000/api/populate`
6. To run the **frontend**, navigate to `/frontend/travelea` and run the commands: `npm install` and `npm run serve`
7. Navigate to: `localhost:8080` OR whatever the console states when running the previous command
8. To populate the database, run the following command: `curl -X POST localhost:9000/api/populate`
