<h1 align="center">SQL Delight caching mechanism </h1>

<details open ="open">
  <summary>Contains</summary>
  <ol>
    <li>
      <a href='#about-the-project'>About Project</a>
        <ul>
          <li><a href="#built-with">Built With</a></li>
        </ul>
    </li>
 <li>
      <a href='#usage'>Usage</a>
        <ul>
          <li><a href="#how-caching-works">How caching works</a></li>
        </ul>
    </li> 
    <li>
      <a href='#contacts'>Contacts</a> 
    </li>
  </ol>
</details>



## About Project
This SQLDelight caching mechanism project is simple application which interacts with simple database.
Application displays list of persons fetched from remote and local database. Application allows you to 
add/delete Persons in online & offline mode. When added/deleted while offline values gets cached into local
database and after application restart if device is online values are added/ deleted in server. 

### Built With 

This application is built in  [Android Studio version 2020.3.1 (Artic Fox)](https://developer.android.com/studio?gclid=CjwKCAjwgb6IBhAREiwAgMYKRlU8WsxaTu6kg3JANeH6rEr8MrWyit5JaDfcTy0v1tTP0-DOmL1QnRoCxrcQAvD_BwE&gclsrc=aw.ds) 
using :

* [SQLDelight](https://cashapp.github.io/sqldelight/)
* [Kotlin](https://developer.android.com/kotlin)
* [Jetpack Compose version 1.0.5](https://developer.android.com/jetpack/compose?gclid=EAIaIQobChMImIyxhI-i8gIVlgCiAx3kZgYlEAAYASAAEgL1J_D_BwE&gclsrc=aw.ds)
* [Courotines](https://developer.android.com/kotlin/coroutines?gclid=EAIaIQobChMIqZC4jo-i8gIVsAZ7Ch1rOASzEAAYASAAEgKAwvD_BwE&gclsrc=aw.ds)
* [DaggerHilt](https://developer.android.com/training/dependency-injection/hilt-android)
* [Retrofit & OkHttp](https://square.github.io/retrofit/)


## Usage

### How caching mechanism works

Here you can see empty PersonsDatabase in App Inspection provided by Android Studio and empty PersonsDatabase server in MongoDB Compass

![product-screenshot](https://live.staticflickr.com/65535/51756665397_c28d61b30d.jpg)
![product-screenshot](https://live.staticflickr.com/65535/51756665362_8640b9eb41.jpg)

Application where 2 persons are added in online mode where "done" icon is in green and one person is added in offline mode where icon is red.
In App Inspection you can see database with 3 Persons and for the last person Synced atrribute is false ("0"), that means that current person is not synced to remote database

![product-screenshot](https://live.staticflickr.com/65535/51758367405_0cfd4c480e.jpg)
![product-screenshot](https://live.staticflickr.com/65535/51758367325_7b7e1c4362.jpg)

For this case MongoDB Compass shows that there are only 2 persons, that is because person is stored in local database and waits until it gets synced

![product-screenshot](https://live.staticflickr.com/65535/51758149189_c3399be35d.jpg)

After internet is connected and application restarted then person gets Synced to server and isSynced atribute is true ("1").

![product-screenshot](https://live.staticflickr.com/65535/51758149214_d97b47273d.jpg)
![product-screenshot](https://live.staticflickr.com/65535/51758367375_13c034de95.jpg)
![product-screenshot](https://live.staticflickr.com/65535/51756665332_691e8fb620.jpg)

For delete functionality there is seperate table, when person is deleted offline persons id gets stored in this table, and after internet connection is restored 
this Id gets deleted from remote database. 

![product-screenshot](https://live.staticflickr.com/65535/51758149324_dc1e9d833f.jpg)



   ## Contacts

     Ugis Ozols - ugisozols.97@gmail.com , LinkedIn - www.linkedin.com/in/ozols-ugis


     Project Link - https://github.com/OzolsUgis/SQLDelightCachingMech

      
















