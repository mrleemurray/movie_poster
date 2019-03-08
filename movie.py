import urllib
import json
import unidecode
import config as keys

class Movie:
    def __init__(self, title, runtime, metascore, theatres, showtimes):
        self.title = title
        self.posterUrl = self.findPoster(self.title)
        self.runtime = runtime
        self.metascore = metascore
        self.theatres = [theatres]
        self.showtimes = [showtimes]

    def addTheatre(self, theatre):
        self.theatres.append(theatre)
    
    def addShowtimes(self, showtimes):
        self.showtimes.append(showtimes)

    def setPosterURL(self, url):
        self.posterUrl = url

    def setMetaScore(self, score):
        self.metascore = score
    
    def setRuntime(self, runtime):
        self.runtime = runtime

    def makeApiCall(self, url, input, apiKey):
        assembledUrl = url % {"apiKey" : apiKey, "input" : input}
        response = urllib.urlopen(assembledUrl)
        return json.loads(response.read())
    
    def findPoster(self, name):
        try:
            name = unidecode.unidecode(name.split("(")[0].strip().replace(" ","%20"))
            movieData = self.makeApiCall("https://api.themoviedb.org/3/search/movie?api_key=%(apiKey)s&language=en-US&query=%(input)s&page=1&include_adult=false", name, keys.config['movieDBApiKey'])
        except:
            return None    
        try:
            return "http://image.tmdb.org/t/p/original" + movieData["results"][0]["poster_path"]
        except:
            return None
        
