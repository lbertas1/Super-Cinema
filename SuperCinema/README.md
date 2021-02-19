Before you start using the application, for it to work properly you should put the file: "email.txt" in the project directory with your email address in the first line and your email password in the second. Mail should allow the use of less secure applications. This is needed to send an email with an attachment.

Of course, to run the application you need to set the system variable JAVA_HOME to java 15, and also the PATH variable has a bin folder.

To start the application, use the following command: sudo docker-compose up -d --build.

To view logs: sudo docker-compose logs -f

Cinema app, is a simple app designed to support cinema. It provides such functionalities as:
- ordering tickets
- registration and logging in, using the as above token.
- ticket selection
- choice of place
- buying more tickets
- receiving an e-mail confirming the order, as well as the generated QR code, in a pdf file.
- selection of a seance
- movie selection
- search for movies from a given city
- and many others.

Application characteristics:
- multi-module application, written in Java 15 with Spring and Spring Boot. 
- communication based on both the spark framework and a convenient menu that allows you to operate the application from the console.
- the application uses the jjwt library to generate and validate the user token.
- when you try to log in, the password is encrypted according to the implemented scheme and checked against the one saved in the database.
- the application is connected to the database, mysql.
- the jdbi tool is used to communicate with the database.
- based on itext, the application generates a pdf file with a ticket and QR code and sends it to the e-mail address provided.
- the application uses the j2html library to generate the message content.
- the application uses the gson library to generate data from gson files.
- the application includes a set of tests based on JUnit 5, Mockito and Hamcrest.
- application based on the bartos.lukasz.repository pattern.
- the application model is divided into two sections, the model reflecting the data in the database and 
data transfer objects used in the application output value.
- application available on both github and dockerhub:

The project contains a configuration in the docker-compose file that will set the mysql database instance to use the application.

At the start, the application always generates a new database schema with sample data. Among them is an exemplary user,
that allows to use the application without registering.


The application sends an email with the ticket in the attachment section. To receive an email, you should register your own account in the application.

The cinema app provides two different ways to control.
The first is by using console and the instructions displayed by the application.
The second way is to maintain the application by created endpoints to communicate with the application. You can choose a seance and purchase tickets in several ways, in the steps below:

Before you start using the application, for it to work properly you should put the file: "email.txt" in the project directory with your email address in the first line and 
your email password in the second. Mail should allow the use of less secure applications. This is needed to send an email with an attachment.

A) First way, selecting a movie by name:
1) if you have a user account, you must log in. If not, prior registration is required.
login, method POST:
http://localhost:8090/users/login
registration, method POST:
http://localhost:8090/users/register
2) Download the list of available movies below 
http://localhost:8090/movies/all, method GET
3) select a movie by entering name as the path parameter in the "movieName" place:
http://localhost:8090/movies/by/name/:movieName, method GET
4) download the list of screenings for the selected film:
http://localhost:8090/seances/by/movie, method GET 
5) choose your preferred show by entering the show id as a path variable under "seanceId":
http://localhost:8090/seances/:seanceId, method GET
6) Then you should download the list of seats for the selected screening:
http://localhost:8090/seats/empty, method GET
7) Select the seat by entering the seat number in the parameter location:
http://localhost:8090/seats/select/by-number/:number, method GET,
if you want to book more than one seat, select the next available seat in the same way
8) download the amount due for purchasing a ticket at the url:
http://localhost:8090/tickets/payment/get, method GET
9) make the payment by entering the amount received in the place of the "payment" parameter of the url address:
http://localhost:8090/tickets/payment/pay/:payment, method POST
If you enter the wrong amount, the ticket will be reserved but not purchased.
10) you will receive the purchased ticket in the form of a pdf document to the e-mail address provided by you during registration using the url address:
http://localhost:8090/email/send, method GET

B) The application also provides selection of the movie by genre.
1) First of all you should login under url:
http://localhost:8090/users/login, method POST
or create your account and next login
http://localhost:8090/users/register, method POST
2) the next step is to get the list of movies of the genre you are interested in, given as a parameter in the url in the place "type":
http://localhost:8090/movies/by-type/:type, method GET
And the remaining steps are exactly the same as in the previous method.

C) Third way is to select movie by date. 
1) start by logging in to application. 
http://localhost:8090/users/login, method POST
2) then you can search for available screenings for a given date. Enter the date you are interested in (in the format "yyyy-MM-dd" as the path parameter of url:
http://localhost:8090/seances/by/date/:date, method GET
3) choose your preferred show by entering the show id as a path variable under "seanceId":
http://localhost:8090/seances/:seanceId, method GET
4) Then you should download the list of seats for the selected screening:
http://localhost:8090/seats/empty, method GET
7) Select the seat by entering the seat number in the parameter location:
http://localhost:8090/seats/select/by-number/:number, method GET,
if you want to book more than one seat, select the next available seat in the same way
6) download the amount due for purchasing a ticket at the url:
http://localhost:8090/tickets/payment/get, method GET
7) make the payment by entering the amount received in the place of the "payment" parameter of the url address:
http://localhost:8090/tickets/payment/pay/:payment, method POST
If you enter the wrong amount, the ticket will be reserved but not purchased.
8) you will receive the purchased ticket in the form of a pdf document to the e-mail address provided by you during registration using the url address:
http://localhost:8090/email/send, method GET

D) Another way is to download the repertoire from the cinema you are interested in
1) Log in to the app first
http://localhost:8090/users/login, method POST
2) the next step is to download the list of cinemas supported by the app
http://localhost:8090/cinemas/all, method GET
3) select the cinema you are interested in by entering its name as the path parameter:
http://localhost:8090/cinemas/name/:cinemaName, method GET
4) another step: http://localhost:8090/cinemas/repertoire/:cinemaName to get repertoire from chosen cinema, "cinemaName"
is a parameter, where you should insert name of your cinema
5) select the video you are interested in from the received list, giving its name as the path parameter:
http://localhost:8090/movies/by-name/:movieName, method POST
6) download the list of screenings for the selected film:
http://localhost:8090/seances/by/movie, method GET 
7) choose your preferred show by entering the show id as a path variable under "seanceId":
http://localhost:8090/seances/:seanceId, method GET
8) Then you should download the list of seats for the selected screening:
http://localhost:8090/seats/empty, method GET
7) Select the seat by entering the seat number in the parameter location:
http://localhost:8090/seats/select/by-number/:number, method GET,
if you want to book more than one seat, select the next available seat in the same way
10) download the amount due for purchasing a ticket at the url:
http://localhost:8090/tickets/payment/get, method GET
11) make the payment by entering the amount received in the place of the "payment" parameter of the url address:
http://localhost:8090/tickets/payment/pay/:payment, method POST
If you enter the wrong amount, the ticket will be reserved but not purchased.
12) you will receive the purchased ticket in the form of a pdf document to the e-mail address provided by you during registration using the url address:
http://localhost:8090/email/send, method GET

E) You can also download a list of available screenings from selected city.
1) First of all, log in
http://localhost:8090/users/login, method POST
2) Download all cities supported by application
http://localhost:8090/cities/all, method GET
3) Another step is you should get repertoire from chosen city, add city name in path, method GET
http://localhost:8090/cities/repertoire/:cityName, method GET
Remaining steps are exactly the same like those from the previous method. 

If you decided to cancel your booking, you can download the list of your bookings under the url:
http://localhost:8090/reservations/:id, method GET
and delete the selected reservation by entering the reservation id as a parameter at the following address:
http://localhost:8090/reservations/remove/:id, method GET

To log out use:
http://localhost:8090/users/logout, method GET
this will clear the cached data.

Apart from the described methods of purchasing tickets, the application also provides other possibilities. 
The user can add movies to his "favorite movies" list, add to " to watch list", and rate movies and download information 
about movie ratings and rate them yourself.

1) http://localhost:8090/ratings, method POST to save your rating, you must provide rating object. 
2) http://localhost:8090/ratings/all, method GET to get all ratings
3) http://localhost:8090/ratings/user to get all user ratings, you must enter your username in path
4) http://localhost:8090/ratings/highest-rate, method GET, to download user highest rate, provide your username in path
5) http://localhost:8090/ratings/highest-average, method GET, to get movie with highest rate
6) http://localhost:8090/ratings/highest-average, method GET, to get movie with highest average
7) http://localhost:8090/ratings/highest-average/type/type, method GET, to get movie with highest average in given in url movie genre
8) http://localhost:8090/ratings/most-rating, method GET, to get most often rating movie

9) http://localhost:8090/to-watch, method POST to save movie on "toWatch" list, you must provide CreateToWatch object 
10) http://localhost:8090/to-watch/remove/movieName, method DELETE to remove movie from list, you must provide movie title in path  
11) http://localhost:8090/to-watch/movies, method GET to get all user movies on list 
12) http://localhost:8090/to-watch/by-type/type, method GET to get user movies on list by type, you must provide username and type in path

13) http://localhost:8090/favourite-movies, method POST to save movie on "favourite movies" list, you must provide CreateFavoriteMovies object 
14) http://localhost:8090/favourite-movies/remove/movieName, method DELETE to remove movie from list, you must provide movie title in path  
15) http://localhost:8090/favourite-movies/all, method GET to get all user movies on list 
16) http://localhost:8090/favourite-movies/by-type/type, method GET to get user movies on list by type, you must provide username and type in path
 
Of course you need to be logged in app to using above method.
 
The application also provides other methods, available only to users with the admin role. From the token transferred to the headers, 
obtained after logging in, the application checks the user's role and allows or rejects the request. 
These methods are working on a set of publicly available data in a database.
