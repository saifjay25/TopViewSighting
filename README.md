After you run the app it will show the 7 day forecast showing it in a gridlayout with the current day on top. 
Each part shows a day and when the app opens, it will display the date, temperature and an icon portraying the weather 
for that specific day. For today's date I retrieved data from the current weather API from darksky.com and displayed 
both of their data parsing the JSON objects. For the rest of the days I retrieved data from the daily weather API. 
I have also added a feature where if your device is offline it will display the most recent data that was cached to 
the SQLite room database. Another feature I added was if you click on a day in the layout it will display more data on 
the weather using a popup dialog and the dialog shows an animation sliding the dialog from left to right. I have 
also applied unit testing to this project testing the database, viewModel, API network, and UI using Mockito, Junit 4, 
Junit 5, and Espresso. I also used frameworks and libraries like Dagger 2, MVVM, Rxjava, LiveData, and Room.
