# movie_poster

Quick outline of project:

I wanted a display in my home to show poster artwork of movies playing in cinemas of my choosing; they would continously scroll through until I approach it & a proximity sensor (TBC) detects me, pauses the scroll & displays relevant information of the movie that it's paused on. When I walk away, the information dissapears & begins scrolling through the artwork again.
The final solution will run on a Raspberry Pi, connected to a HDMI display (housed in a frame) & a proximity sensor.

The system is broken down into 2 parts:

1. The backend - a python script that:
  - takes user inputted cinema names (e.g. "Alamo Drafthouse Brooklyn")
  - locates the official establishment names & corresponding ZIP codes using Google places API
  - uses the ZIP codes & official names to find movie showtimes via webscraping IMDB (I know it's not great, but I am looking for suitable alternatives)
  - Groups movie data by cinemas playing, rather than movies playing at each cinema (removes duplicates) 
  - Locates corresponding poster artwork urls for each movie using MovieDB API
  - Outputs a json file for further processing by front end SW called "movieData.txt"
  
2. The frontend - (currently) a Processing sketch that:
  - Reads the generated json file
  - Downloads artwork using the URLs
  - Renders artwork & information (when activated) on screen
  - Detects user input to activate information (currently pressing the space bar, but eventually will be a proximity sensor)
  - Animates transisitons between posters & active/inactive information states
  - Checks for updates periodically
  
  
  TO DO:
  
  - Incorporate proximity sensor into processing sketch
  - Find more efficient alternative to Processing (Pygame, etc. doesn't play well with Mac OS Mojave at the moment...)
