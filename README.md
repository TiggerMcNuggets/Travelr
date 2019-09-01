# Readme

### NOTE: please refer to the wiki for the user guide

### Project Structure
-   app/  					Application source code    
-   conf/  					Configuration files required to build the project
-   test/ Unit and Integration tests for backend

### Live Urls
To access the latest production (tagged) version of the application:
[https://csse-s302g3.canterbury.ac.nz/](https://csse-s302g3.canterbury.ac.nz/)

To access the latest development (master commit) version of the application:
[https://csse-s302g3.canterbury.ac.nz:8443/](https://csse-s302g3.canterbury.ac.nz:8443/)

### Email Notifications
Before using email notifications you will need email Flynn at frd15@uclive.ac.nz. Then you must accept an invitation email from 'Mail Gun' to be authorized.
    
## Running the Project (Lab Machine)
To run the project for **production**:
1. Navigate to the eng-git repository located [here](https://eng-git.canterbury.ac.nz/seng302-2019/team-300) 
2. Ensure that the Master branch is selected and download the latest artifact
3. Navigate to: `target/universal`
4. Unzip: `team300-travelea-1.0.0-SNAPSHOT.zip`
5. Navigate to: `team300-travelea-1.0.0-SNAPSHOT/bin`
6. From here, you can run the command: `bash team300-travelea`.
7. Navigate to: `localhost:9000`
8. Login with email: *admin@admin.com*, password: *adminadmin123*

To run the project for **development**:
1. Clone the version you'd like to run from the eng-git repository
2. Navigate to the project's root directory
3. To run the **backend**, run the command: `sbt run`
4. Once the backend is built and running, navigate to: `localhost:9000`
5. To run the **frontend**, navigate to `/frontend` and run the commands: `npm install` and `npm run serve`
6. Navigate to: `localhost:8080` OR whatever the console states when running the previous command


## Admin Login:

Username: admin@admin.com  
Password: sprint5

## Running the Project Locally with MySQL Database

1. Follow steps 1-5 above from "To run the project for production"
2. From here, you can run the command: `bash team300-travelea -Dconfig.resource=production.conf`.

This will run the application and connect to our production database.   
**NOTE**: If trying this after the next sprint 
has ended may fail due to different database schemas.
