###cinema data scraper###
from requests import get
from bs4 import BeautifulSoup
import json
import re
import datetime
import os
from movie import Movie
from theatre import Theatre

def findCinemaIndex(name, threshold):
        for x in range(0,len(theatre_containers)):
            cinema_name = theatre_containers[x].div.h3.a.text
            if name in cinema_name:
                return x
            
            probability = nameProbabilityMatch(name, cinema_name)
            if (probability > threshold):
                return x
        return None

### TODO: Make this much better!
def nameProbabilityMatch(google_name, imdb_name):
    checkLength = min(len(google_name), len(imdb_name))
    probabiltyCounter = 0
    for x in range(0, checkLength):
        if(google_name[x] == imdb_name[x]):
            probabiltyCounter += 1
    return (float(probabiltyCounter) / float(checkLength))

def checkForExistingMovie(title, movies):
    parsed_title = title.split('(')[0].strip()
    for x in range(0, len(movies)):
        if(parsed_title == movies[x].title):
            return x
    return None

def findMovieTitle(listing):
    return listing.find('div', class_ = 'info').h4.span.a.text
    

def findMovieRuntime(listing):
    try:
        times = listing.find('div', class_ = 'info').findAll("time")
        return times[0].text
    except:
        return None

def findMovieScore(listing):
    try:
        score = listing.find('div', class_ = 'info').find('span', class_ = 'metascore').text.strip()
        return(score)
    except:
        return None

def findMovieShowtimes(listing):
    try:
        times = []
        showtimes = listing.find('div', class_ = 'info').find('div', class_ = 'showtimes').findAll('a')
        if len(showtimes) <= 0:
            times = re.sub(r'\s+', '', listing.find('div', class_ = 'info').find('div', class_ = 'showtimes').text.replace("m","").replace("a","").replace("p","")).split("|")
        else:
            for i in range(1,len(showtimes)):
                times.append(showtimes[i].text.strip().split(' ')[0])
        return times
    except:
        return None

theatres = []

absolute_path = os.path.dirname(__file__)
with open(absolute_path + '/cinemas.json') as cinemas_file:
    cinemas = json.load(cinemas_file)
    for x in range(0, len(cinemas['cinemas']['names'])):
        theatres.append(Theatre(cinemas['cinemas']['names'][x]))
    cinemas_file.close()

movies = []
for location in range(0, len(theatres)):
    name = theatres[location].displayName
    region = theatres[location].region
    zipcode = theatres[location].zipcode.replace(' ', '%20')

    for day in range(1,2):
        today = datetime.datetime.today() 
        next_day = today + datetime.timedelta(day)
        next_date = datetime.datetime.strftime(next_day,'%Y-%m-%d')

        url = 'https://www.imdb.com/showtimes/' + region + '/' + zipcode + '/' + next_date

        response = get(url)

        html_soup = BeautifulSoup(response.text, 'html.parser')
        type(html_soup)

        theatre_containers = html_soup.find_all('div', class_ = 'list_item odd') + html_soup.find_all('div', class_ = 'list_item even')

        cinemaIndex = (findCinemaIndex(name, 70))

        try:
            if cinemaIndex is not None:
                listings = theatre_containers[cinemaIndex].find_all('div', class_ = 'list_item')

                for x in range(0, len(listings)):
                    title = findMovieTitle(listings[x])
                    runtime = findMovieRuntime(listings[x])
                    metascore = findMovieScore(listings[x])
                    showtimes = findMovieShowtimes(listings[x])
                    cinemaIndex = checkForExistingMovie(title, movies)
                    if cinemaIndex is None:
                        movies.append(Movie(title, runtime, metascore, name, showtimes, next_date))
                    else:
                        movies[cinemaIndex].addTheatre(name)
                        movies[cinemaIndex].addShowtimes(showtimes)
        except:
            print('Could not find data for: ' + name)


def printOutput():
    for x in range(0, len(movies)):
        print("########################")
        print ("Movie title: " + movies[x].title)
        try:
            print ("Runtime: " + movies[x].runtime)
        except:
            print ("No runtime info")
        try:
            print ("Metascore: " + movies[x].metascore)
        except:
            print ("No metascore")
        try:
            print ("Poster: " + movies[x].posterUrl)
        except:
            print ("No poster available")
        for i in range(0, len(movies[x].theatres)):
            print ("Playing at: " + movies[x].theatres[i])
            for j in range(0, len(movies[x].showtimes[i])):
                print ("Time: " + movies[x].showtimes[i][j])

def generateJSONFile(movieList, relative_path):
    JSONData = {}
    JSONData['movies'] = []

    for x in range(0,len(movieList)):
        playingAt = []
        if movieList[x].posterUrl is not None:
            for y in range(0, len(movieList[x].theatres)):

                playingAt.append({
                    'theatre': movieList[x].theatres[y],
                    'showtimes': movieList[x].showtimes[y]
                })

            JSONData['movies'].append({
                'title': movieList[x].title,
                'runningTime': movieList[x].runtime,
                'metascore': movieList[x].metascore,
                'poster': movieList[x].posterUrl,
                'date': movieList[x].date,
                'playingAt': playingAt
            })

    absolute_path = os.path.dirname(__file__)
    full_path = os.path.join(absolute_path, relative_path)
    with open(full_path + 'movieData.json', 'w') as outfile:  
        json.dump(JSONData, outfile, sort_keys=True, indent=4)

generateJSONFile(movies, './poster/public/static/')