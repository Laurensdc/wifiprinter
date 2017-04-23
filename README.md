# ![Logo of the project](/app/src/main/res/mipmap-mdpi/ic_launcher.png) Order & Print

> Printing bills like it's 2017

Android app made for restaurants.
Restaurant menu is read from a Prestashop database, and waiters can add items to a bill.
Bill is sent to a WiFi printer.


## Installing / Running the app

* Install [ESC/POS printer driver](https://play.google.com/store/apps/details?id=com.fidelier.posprinterdriver&hl=en)
* Install Order & Print app
* Configure WiFi printer with fixed IP address
* Enter the IP address in Order & Print settings menu
* When printing for the first time, select ESC/POS printer app and select 'always'


## Developing

The app communicates with a REST API as a link with the database.
The code for this is in `_devdata/api`.  
Push this code to a server and update the link in the Android code.


## Features

* REST API for security that reads information from Prestashop database
* Add/remove items to and from a bill
* Settings menu with bill headings and printer IP address
* Print bills over WiFi to ESC/POST printer


## Configuration


## Licensing

The code in this project is licensed under MIT license.
