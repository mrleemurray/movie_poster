import de.looksgood.ani.*;

String filePath = "../movieData.txt";
ArrayList<Movie> movies = new ArrayList<Movie>();
PImage poster;
PFont titleFont;
PFont infoFont;
PFont posterFont;
Title title;
Metascore score;
Runtime time;
Theatre theatre;
boolean showDetails = false;
int index = 0;
int theatreIndex = 0;
int savedTime;
int savedTheatreTime;

float maskOpacity = 0;
float informationOpacity = 0;
float containerOpacity = 0;
float transitionOpacity = 0;
int interval = 8000;

int[] updateTime = {02, 00}; //hour, minute (24 hour format)
boolean updateRequired = true;
boolean posterUpdated = false;
boolean ready = false;

void setup(){
//   size(500, 500);
  fullScreen();
  smooth();
  Ani.init(this);
  movies = new ArrayList<Movie>();
  titleFont = loadFont("BariolSerif-Italic-30.vlw");
  infoFont = loadFont("Gotham-Light-24.vlw");
  posterFont = loadFont("BariolSerif-Italic-96.vlw");
  generateMovieObjects(filePath);
  title = new Title(titleFont, 30);
  time = new Runtime(infoFont, 20, loadImage("clock.png"));
  score = new Metascore(infoFont, 20);
  theatre = new Theatre(titleFont, 30, infoFont, 24);
  savedTime = millis();
  updateInformation();
  ready = true;
}

void draw() {
    checkForUpdates(updateTime);
    if (readyForNext(interval)){
            if(!showDetails){
                resetTheatreIndex();
                // updateInformation();
                posterUpdated = true;
            }
            else{
                if(readyForNextTheatre(interval)){
                    theatreIndex = incrementCounter(theatreIndex, movies.get(index).getTheatres().size());
                    updateTheatreInformation(index, theatreIndex);
                }
            }
    }
    updateAnimations();
    drawContent(index);
}

void drawContent(int _index){
    background(0);

    drawPoster(poster);
    title.render();

    drawMask(maskOpacity);
    drawInfoContainer(containerOpacity);

    time.setOpacity(informationOpacity);
    time.render();
    score.setOpacity(informationOpacity);
    score.render();

    drawDivider();

    theatre.setOpacity(informationOpacity);
    theatre.render();

    drawMask(transitionOpacity);
}

void drawPoster(PImage _poster){
    imageMode(CORNER);
    noStroke();
    _poster.resize(0, height);
    tint(255, 255);
    image(_poster, (width/2)-(_poster.width/2), 0);
}

void drawMask(float _opacity){
    noStroke();
    fill(0, _opacity);
    rectMode(CORNER);
    rect(0, 0, width, height);   
}

void drawInfoContainer(float _opacity){
    fill(0, _opacity);
    rectMode(CORNER);
    rect(0,0, width, 235, 10);
}

void drawDivider(){
    stroke(100, informationOpacity);
    line(width/2 - 250, 90, width/2 + 250, 90);
}

void resetTheatreIndex(){
    theatreIndex = 0;
}

boolean readyForNextTheatre(int _interval){
  int passedTime = millis() - savedTheatreTime;
  if (passedTime > _interval) {
    savedTheatreTime = millis();
    return true;
  }
  return false;
}

boolean readyForNext(int _interval) {
  // Calculate how much time has passed
  int passedTime = millis() - savedTime;
  if (passedTime > _interval) {
    savedTime = millis();
    return true;
  }
  return false;
}

void restartTimers(){
    savedTheatreTime = millis();
    savedTime = millis();
}

void updateAnimations(){
  if(showDetails){
    Ani.to(this, 0.5, "informationOpacity", 255);
    Ani.to(this, 0.5, "maskOpacity", 100);
    Ani.to(this, 0.5, "containerOpacity", 180);
  }
  else{
      Ani.to(this, 0.1, "informationOpacity", 0, Ani.LINEAR, "onEnd:resetTheatreIndex");
      Ani.to(this, 0.5, "maskOpacity", 0);
      Ani.to(this, 0.5, "containerOpacity", 0);
  }
  managePoster(posterUpdated);
}

void managePoster(boolean _change){
    if(_change){
        Ani.to(this, 0.5, "transitionOpacity", 255);
        if(transitionOpacity >= 254){
            updateInformation();
        }
    }
    else{
        Ani.to(this, 1.0, "transitionOpacity", 0);
    }
}

void updateInformation(){
    index = incrementCounter(index, movies.size());
    String url = movies.get(index).getPosterUrl();
    poster = movies.get(index).getPosterArt();
    title.setTitle(movies.get(index).getTitle());
    title.setShow(checkForOfficialPoster(movies.get(index).getPosterUrl(), "picsum.photos"));
    time.setRuntime(movies.get(index).getRuntime());
    score.setScore(movies.get(index).getMetascore());
    updateTheatreInformation(index, 0);
    posterUpdated = false;
}

void updateTheatreInformation(int _index, int _subIndex){
    theatre.setTheatreName(movies.get(_index).getTheatres().get(_subIndex));
    theatre.setShowtimes(movies.get(_index).getShowtimes().get(_subIndex));
}

boolean checkForOfficialPoster(String _url, String _keyword){
    if(_url.contains(_keyword)){
        return true;
    }
    return false;
}

int incrementCounter(int _current, int _limit){
    int result = _current+1;
    if (result >= _limit){
        return 0;
    }
    return result;
}

void checkForUpdates(int[] _time){
    if(_time[0] == hour() && _time[1] == minute()){
        if(updateRequired){
            generateMovieObjects(filePath);
            updateRequired = false;
        }
    }
    else{
        if(!updateRequired){
            updateRequired = true;
        }
    }
}

void generateMovieObjects(String _file_path) {
    JSONArray movieData;
    movies.clear();
    movieData = loadJSONObject(_file_path).getJSONArray("movies");
    for (int i = 0; i < movieData.size(); i++){
        String title = split(movieData.getJSONObject(i).getString("title"),'(')[0];
        String runtime = "?? min";
        String metascore = "??";
        String poster = "placeholder.png";
        try{
            runtime = movieData.getJSONObject(i).getString("runningTime");
        }
        catch (Exception e){

        }
        try{
            metascore = movieData.getJSONObject(i).getString("metascore");
        }
        catch (Exception e) {
            // metascore = "N/A";
        }
        try{
            poster = movieData.getJSONObject(i).getString("poster");
        }
        catch (Exception e) {   
            poster = "https://picsum.photos/700/1050/?random";
        }
        JSONArray playingAt = movieData.getJSONObject(i).getJSONArray("playingAt");
        ArrayList<String> theatres = new ArrayList<String>();
        ArrayList<String[]> showtimes = new ArrayList<String[]>();
        for(int j = 0; j < playingAt.size(); j++){
            theatres.add(playingAt.getJSONObject(j).getString("theatre"));
            showtimes.add(playingAt.getJSONObject(j).getJSONArray("showtimes").
            getStringArray());
        }
        movies.add(new Movie(title, runtime, metascore, poster, theatres, showtimes));
    }
    println("Number of movies: " + movies.size());
}

void keyPressed() {
    if(ready){
        if(key == ' '){
            showDetails = !showDetails;
            restartTimers();
        }
    }
}