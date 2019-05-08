# Readme

### NOTE: please refer to the wiki for the user guide

### Project Structure
-   app/  					Application source code    
-   doc/  					User and design documentation    
-   doc/examples/  	        Demo example files for use with the application    
-   conf/  					Configuration files required to build the project

### SonarQube Analytics
To view project analytics, simply navigate to the url: `http://csse-s302g3.canterbury.ac.nz:8080`

To login as the PO, you may login with the credentials: Login: *Moffat*, Password: *Moffat*.

### Live Urls
To access the latest production version of the application:
http://csse-s302g3.canterbury.ac.nz:443/ 

To access the latest development version of the application:
http://csse-s302g3.canterbury.ac.nz:8443/ 
    
## Running the Project (Lab Machine)
To run the project for **production**:
1. Navigate to the eng-git repository located [here](https://eng-git.canterbury.ac.nz/seng302-2019/team-300) 
2. Ensure that the Master branch is selected and download the latest artifact
3. Navigate to: `backend/target/universal`
4. Unzip: `team300-travelea-1.0.0-SNAPSHOT.zip`
5. Navigate to: `team300-travelea-1.0.0-SNAPSHOT/bin`
6. From here, you can run the command: `bash team300-travelea`.
7. Navigate to: `localhost:9000`
8. Login with email: *admin@admin.com*, password: *admin*

To run the project for **development**:
1. Clone the version you'd like to run from the eng-git repository
2. Navigate to the project's root directory
3. To run the **backend**, navigate to `/backend` and run the command: `sbt dist`
4. Once the backend is built and running, navigate to: `localhost:9000`
5. To run the **frontend**, navigate to `/frontend/travelea` and run the commands: `npm install` and `npm run serve`
6. Navigate to: `localhost:8080` OR whatever the console states when running the previous command
