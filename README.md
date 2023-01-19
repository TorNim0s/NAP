<p align="center">
<img src="https://user-images.githubusercontent.com/93203695/213384703-be24ee6d-e4f8-4ca1-a7a7-94b01dd5dfcd.jpg" width="400" height="400">
</p>

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Table Of Content</summary>
  <ol>
    <li><a href="#introduction">Introduction</a></li>
    <li><a href="#project-description">Project Description</a></li>
    <li><a href="#app-appearence">App Appearence</a></li>
    <li><a href="#setup">Setup</a></li>
    <li><a href="#running-the-project">Running The Project</a></li>
    <li><a href="#design-and-architecture">Design And Architecture</a></li>
    
  </ol>
</details>

---

# Introduction
![image](https://user-images.githubusercontent.com/93203695/213488312-75381c9b-daec-4a09-971c-cb1e14b5b9ec.png)

Welcome to NAP (Need A Parking), an Android application that allows users to rent private parking spaces from other users. With NAP, users can search for available parking spaces in their desired location and rent them on an hourly basis. The app also allows users to list their own parking spaces for rent. The main features of the app include searching for available parking spaces, booking parking spaces, and managing your own listed parking spaces. In this README, you will find information on how to use the app, the technologies used in the development of the app, and how to contribute to the project.

# Project Description
The project is a parking rental app that allows users to rent private parking spaces from other users. The app includes features such as searching for available parking spaces, renting a parking space, and managing rental agreements. The app also includes a user profile section where users can view and edit their personal information, such as their name, phone number, and email address. The app utilizes Firebase for user authentication and database storage. The MVP pattern used in the project, where Model, View, and Presenter are separated and interact with each other.

# App Appearence
<a href="https://gifyu.com/image/SvAPw"><img src="https://s3.gifyu.com/images/ezgif-2-6dde363093.gif" alt="ezgif-2-6dde363093.gif" border="0" /></a>

#####     &emsp;&emsp;&emsp;&emsp; Main	&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; Information &emsp;&emsp;&emsp;&emsp;&emsp;	Signin	&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; Signup		&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;	Logged-Main  
<img src="https://user-images.githubusercontent.com/93203695/213489974-369e53fd-9a1a-460d-b09e-3728770f01c8.png" alt="main" width=145 border="0"/><img src="https://user-images.githubusercontent.com/93203695/213490422-57467868-f8ac-4bbc-a58c-93dffb69b4ff.png" alt="info" width=145 border="0"/><img src="https://user-images.githubusercontent.com/93203695/213490766-f12a5559-812d-4357-a638-1e7a5385e242.png" alt="signin" width=145 border="0"/><img src="https://user-images.githubusercontent.com/93203695/213512511-798176f0-0063-43d6-ad19-2fc455d06899.png" alt="signup" width=145 border="0"/><img src="https://user-images.githubusercontent.com/93203695/213513063-6dd49f12-8f20-4297-b591-b39084c1fa8f.png" alt="logged" width=145  border="0"/>

#####    &emsp;&emsp;&emsp;&emsp; Post &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; Profile		&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;  Add 	&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; Edit
<img src="https://user-images.githubusercontent.com/93203695/213513254-bbfa2b0e-bfa8-4316-8855-f094d7905930.png" alt="post" width=145  border="0"/><img src="https://user-images.githubusercontent.com/93203695/213513599-93b68dca-107c-4669-9490-84de294b1260.png" alt="profile" width=145  border="0"/><img src="https://user-images.githubusercontent.com/93203695/213514107-aac2f049-ca45-4ea8-96d8-8b860f24e2d7.png" alt="add" width=145  border="0"/><img src="https://user-images.githubusercontent.com/93203695/213514269-675de3d3-737c-4904-a402-a23999514e14.png" alt="edit" width=145  border="0"/>

# Setup
* Clone the repository to your local machine using git clone https://github.com/TorNim0s/NAP.git
* Open the project in Android Studio
* In the top menu, go to File > Open > build.gradle to open the project's build file
* In the dependencies block, make sure that the following dependencies are included:</br>
dependencies {</br>
    // Firebase dependencies</br>
    implementation 'com.google.firebase:firebase-auth:19.3.1'</br>
    implementation 'com.google.firebase:firebase-firestore:21.4.3'</br>

    // Other dependencies</br>
    implementation 'com.android.support:appcompat-v7:29.0.0'</br>
    implementation 'com.android.support:design:29.0.0'</br>
    implementation 'com.android.support:recyclerview-v7:29.0.0'</br>
}</br>
* Click the Run button in the top right corner to build and run the project on an emulator or connected device.</br>
  * Note: make sure that you have the latest version of the Android SDK and build tools installed, and that your device or emulator is running on the same version or higher of Android.

# Running The Project
1. Make sure you have Android Studio installed on your computer.

2. Clone or download the project repository to your computer.

3. Open Android Studio and select "Open an existing Android Studio project" from the welcome screen.

4. Navigate to the location where you cloned or downloaded the project and select the root folder of the project.

5. In the project window, navigate to the app folder and open the build.gradle file.

6. Connect your Android device to your computer using a USB cable.

7. In the top right corner of Android Studio, select the device you want to run the app on and press the "Play" button to build and run the project.
</br>

- The app should now be installed and running on your Android device.

- Please note that you may need to configure some settings on your device and/or computer to allow for debugging and installing apps from external sources. The specific steps for this may vary depending on your device and operating system.

# Design And Architecture
The NAP project uses the MVP (Model-View-Presenter) design pattern. The model contains the data and business logic for the app, the view is responsible for displaying the data to the user and handling user input, and the presenter acts as a mediator between the model and the view.</br>
#####    &emsp;&emsp;&emsp;&emsp; Model &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; View		&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;  Presenter
<img src="https://user-images.githubusercontent.com/93203695/213521189-246426df-b070-42d0-96d1-e2d59e8a0329.png" alt="model" width=145  border="0"/><img src="https://user-images.githubusercontent.com/93203695/213521323-b9f72941-cd9e-4d5c-bbd1-f12d9db26693.png" alt="view" width=145  border="0"/><img src="https://user-images.githubusercontent.com/93203695/213521446-7c476a45-4072-4ff4-bcc5-7ed3f1bdc482.png" alt="presenter" width=145  border="0"/>
</br></br>
The project also includes UML and diagrams to provide a visual representation of the project's design and architecture.</br>
</br>
### &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; Activity Diagram
<img src="https://user-images.githubusercontent.com/93203695/213521879-2411aca4-a5cf-4718-9b5b-5ffe300edb11.png" alt="activity" border="0"/>
</br></br>

### &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; Use-Case Diagram
<img src="https://user-images.githubusercontent.com/93203695/213522486-2e70649f-0038-467f-a6ec-6e441a4a8185.png" alt="usecase" border="0"/>
</br></br>

### &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; State Machine Diagram
<img src="https://user-images.githubusercontent.com/93203695/213522880-91fac04b-4851-495f-8a9e-eb5667554f60.png" alt="statemachine" border="0"/>
</br></br>

### &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; Sequence Diagram
<img src="https://user-images.githubusercontent.com/93203695/213523043-092d84e1-85d6-413b-a2cb-b75d1e18c533.png" alt="sequence" border="0"/>
</br></br>

### &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; ERD
<img src="https://user-images.githubusercontent.com/93203695/213523205-8e2eb69c-b3a4-4c60-b45a-f4459644cf47.png" alt="erd" border="0"/>
</br></br>

### &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; Class UML
<img src="https://user-images.githubusercontent.com/93203695/213523851-be8d6ec3-6a0a-48e4-a0ce-cdbc9863b912.png" alt="class" border="0"/>
</br></br>

### &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; Object UML
<p align="center">
<img src="https://user-images.githubusercontent.com/93203695/213523958-2081b338-b86f-4dfd-999e-b9aa1232c0e5.png" alt="object" border="0"/>
</a>
</p>
</br></br>


