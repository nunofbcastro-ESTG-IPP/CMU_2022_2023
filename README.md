<h1 align="center">Practical Work on Computação Móvel e Ubíqua (Mobile and Ubiquitous Computing)</h1>

<p>
  <img src="http://img.shields.io/static/v1?style=for-the-badge&label=School%20year&message=2022/2023&color=sucess"/>
  <img src="http://img.shields.io/static/v1?style=for-the-badge&label=Discipline&message=CMU&color=sucess"/>
  <img src="http://img.shields.io/static/v1?style=for-the-badge&label=Grade&message=18&color=sucess"/>
  <a href="https://github.com/nunofbcastro-ESTG-IPP/CMU_2022_2023/blob/main/Trabalho_Pratico.pdf" target="_blank">
    <img src="https://img.shields.io/badge/-Utterance-grey?style=for-the-badge"/>
  </a>
  <a href="https://github.com/nunofbcastro-ESTG-IPP/CMU_2022_2023/blob/main/Relat%C3%B3rioGrupo7.pdf" target="_blank">
    <img src="https://img.shields.io/badge/-Report-grey?style=for-the-badge"/>
  </a>
</p>

---

<h2>Screens</h2>

---

<h3>Authentication</h3>

<img src=".github/assets/login_screen.png" width="auto" height="300"> <img src=".github/assets/regist_screen.png" width="auto" height="300">

---

<h3>Sensors</h3>

<img src=".github/assets/house_screen_light.png" width="auto" height="300"> <img src=".github/assets/divisions_screen.png" width="auto" height="300">

---

<h3>Listings</h3>

<img src=".github/assets/lights_screen.png" width="auto" height="300"> <img src=".github/assets/sockets_screen.png" width="auto" height="300"> <img src=".github/assets/blinds_screen.png" width="auto" height="300">

---

<h3>Color mode</h3>

<img src=".github/assets/house_screen_light.png" width="auto" height="300"> <img src=".github/assets/house_screen_dark.png" width="auto" height="300">

---

<h2>Languages</h2>
<p align="left"> 
  <img src="https://img.shields.io/badge/Android%20Studio-3DDC84.svg?style=for-the-badge&amp;logo=android-studio&amp;logoColor=white" alt="Android Studio">
  <img src="https://img.shields.io/static/v1?style=for-the-badge&amp;message=Jetpack+Compose&amp;color=4285F4&amp;logo=Jetpack+Compose&amp;logoColor=FFFFFF&amp;label=" alt="Jetpack Compose">
  <img src="https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&amp;logo=kotlin&amp;logoColor=white" alt="Kotlin">
  <img src="https://img.shields.io/badge/Firebase-039BE5?style=for-the-badge&amp;logo=Firebase&amp;logoColor=white" alt="Firebase">
  <img src="https://img.shields.io/badge/node.js-6DA55F?style=for-the-badge&amp;logo=node.js&amp;logoColor=white" alt="NodeJS">
  <img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&amp;logo=docker&amp;logoColor=white" alt="Docker">
</p>

---

<h2>How to run</h2>

<h3>API</h3>

To simulate the sensors, we will use a Node.js project.

```
cd shelly-mock-api-master
npm install
npm start plug 20 light 20 blind 20
```

or

```
cd shelly-mock-api-master
docker compose up
```

After running this command, the API is available at the IP address 10.0.2.2 if you run it on the emulator or the device where you ran the API, otherwise it will be the IP of the device where the API was executed. The ports used for the shutter devices range from 3000 to 3019, while the ports used for the socket devices range from 3020 to 3039, and the ports used for the lighting devices range from 3040 to 3059.

<h3>Android project</h3>

1. Importing Database Folder Files to Firebase using Firestore:
   In order to import the files from the Database folder to Firebase, the Firestore service must be used. This can be done by logging into the Firebase console, creating a new database, and then using the import option to select the files from the Database folder.

1. Exporting the google-services.json File from Firebase:
   The google-services.json file is crucial for connecting the project to Firebase. To export this file, log into the Firebase console, go to the project settings, and select the "Download google-services.json" option. Then, add the file to the CMU_07_8200398_8200591_8200592\app folder.

1. Modifying the AndroidManifest.xml File:
   Finally, the AndroidManifest.xml file needs to be modified in order to change the value of the android:value attribute to the appropriate key for the Google Maps API. This can be done by opening the file in a text editor, locating the relevant line of code, and changing the value to the key that was obtained from the Google Maps API Console.

```
<meta-data
  android:name="com.google.android.geo.API_KEY"
  android:value="key"
/>
```

4. To run the project in Android Studio, open the project, click on the "Run" button, or select "Run" from the "Run" menu, and verify the output to ensure the changes made to the project have taken effect.

5. The project also contains UI tests and unit tests to verify the visual appearance and behavior of the app and the individual units of code, respectively, ensuring the quality and reliability of the application.

---

<h2>Authors</h2>

<h3>
  Nuno Castro
  <a href="https://github.com/nunofbcastro?tab=followers">
    <img src="https://img.shields.io/github/followers/nunofbcastro.svg?style=for-the-badge&label=Follow" height="20"/>
  </a>
</h3>

<h3>
  Jorge Correia
  <a href="https://github.com/JorgeMFC?tab=followers">
    <img src="https://img.shields.io/github/followers/JorgeMFC.svg?style=for-the-badge&label=Follow" height="20"/>
  </a>
</h3>

<h3>
  Luís Sousa
  <a href="https://github.com/luisousa14?tab=followers">
    <img src="https://img.shields.io/github/followers/luisousa14.svg?style=for-the-badge&label=Follow" height="20"/>
  </a>
</h3>
