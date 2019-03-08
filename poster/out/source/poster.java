import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.looksgood.ani.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class poster extends PApplet {



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

public void setup(){
//   size(500, 500);
  
  
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

public void draw() {
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

public void drawContent(int _index){
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

public void drawPoster(PImage _poster){
    imageMode(CORNER);
    noStroke();
    _poster.resize(0, height);
    tint(255, 255);
    image(_poster, (width/2)-(_poster.width/2), 0);
}

public void drawMask(float _opacity){
    noStroke();
    fill(0, _opacity);
    rectMode(CORNER);
    rect(0, 0, width, height);   
}

public void drawInfoContainer(float _opacity){
    fill(0, _opacity);
    rectMode(CORNER);
    rect(0,0, width, 235, 10);
}

public void drawDivider(){
    stroke(100, informationOpacity);
    line(width/2 - 250, 90, width/2 + 250, 90);
}

public void resetTheatreIndex(){
    theatreIndex = 0;
}

public boolean readyForNextTheatre(int _interval){
  int passedTime = millis() - savedTheatreTime;
  if (passedTime > _interval) {
    savedTheatreTime = millis();
    return true;
  }
  return false;
}

public boolean readyForNext(int _interval) {
  // Calculate how much time has passed
  int passedTime = millis() - savedTime;
  if (passedTime > _interval) {
    savedTime = millis();
    return true;
  }
  return false;
}

public void restartTimers(){
    savedTheatreTime = millis();
    savedTime = millis();
}

public void updateAnimations(){
  if(showDetails){
    Ani.to(this, 0.5f, "informationOpacity", 255);
    Ani.to(this, 0.5f, "maskOpacity", 100);
    Ani.to(this, 0.5f, "containerOpacity", 180);
  }
  else{
      Ani.to(this, 0.1f, "informationOpacity", 0, Ani.LINEAR, "onEnd:resetTheatreIndex");
      Ani.to(this, 0.5f, "maskOpacity", 0);
      Ani.to(this, 0.5f, "containerOpacity", 0);
  }
  managePoster(posterUpdated);
}

public void managePoster(boolean _change){
    if(_change){
        println("fade in");
        Ani.to(this, 0.5f, "transitionOpacity", 255);
        if(transitionOpacity >= 254){
            updateInformation();
        }
    }
    else{
        println("fade out");
        Ani.to(this, 1.0f, "transitionOpacity", 0);
    }
}

public void updateInformation(){
    index = incrementCounter(index, movies.size());
    String url = movies.get(index).getPosterUrl();
    poster = movies.get(index).getPosterArt();
    title.setTitle(movies.get(index).getTitle());
    title.setShow(checkForOfficialPoster(movies.get(index).getPosterUrl(), "picsum.photos"));
    time.setRuntime(movies.get(index).getRuntime());
    score.setScore(movies.get(index).getMetascore());
    updateTheatreInformation(index, 0);
    posterUpdated = false;
    println("Information updated.");
}

public void updateTheatreInformation(int _index, int _subIndex){
    theatre.setTheatreName(movies.get(_index).getTheatres().get(_subIndex));
    theatre.setShowtimes(movies.get(_index).getShowtimes().get(_subIndex));
}

public boolean checkForOfficialPoster(String _url, String _keyword){
    if(_url.contains(_keyword)){
        return true;
    }
    return false;
}

public int incrementCounter(int _current, int _limit){
    int result = _current+1;
    if (result >= _limit){
        return 0;
    }
    return result;
}

public void checkForUpdates(int[] _time){
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

public void generateMovieObjects(String _file_path) {
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

public void keyPressed() {
    if(ready){
        if(key == ' '){
            showDetails = !showDetails;
            restartTimers();
        }
    }
}
class Metascore{
    String score;
    float xPos = width/2 + 155;
    float yPos = 47;
    float containerWidth = 46;
    float containerHeight = 46;
    float thickness = 1;
    int containerColor;
    int textColor = color(255, 255, 255);
    float opacity;
    PFont font;
    int fontSize;

    Metascore(PFont _font, int _fontSize){
        font = _font;
        fontSize = _fontSize;
    }

    public void setScore(String _score){
        score = _score;
        try{
            containerColor = calculateScoreColor(Integer.parseInt(_score));
        }
        catch(Exception e){
            containerColor = color(150, 150, 150);
        }
    }

    public void setOpacity(float _opacity){
        opacity = _opacity;
    }

    public int calculateScoreColor(int _score){
        if(_score <= 33){
            return color(220, 20, 60);
        }
        if(_score >33 && _score <= 66){
            return color(255, 204, 0);
        }
        if(_score > 66){
            return color(50, 205, 50);
        }
        return color(150, 150, 150);
    }

    public void render(){  
        noFill();
        strokeWeight(thickness);
        stroke(containerColor, opacity);
        rectMode(CENTER);
        rect(xPos, yPos, containerWidth, containerHeight);
        textAlign(CENTER, CENTER);
        textFont(font, fontSize);
        fill(textColor, opacity);
        text(score, xPos, yPos); 
    }

}
class Movie{
    String title;
    String runtime;
    String metascore;
    String posterUrl;
    ArrayList<String> theatres;
    ArrayList<String[]> showtimes; 
    PImage posterArt;

    Movie(String _title, String _runtime, String _metascore, String _posterUrl, ArrayList<String> _theatres, ArrayList<String[]> _showtimes){
        title = _title;
        runtime = _runtime;
        metascore = _metascore;
        posterUrl = _posterUrl;
        theatres = _theatres;
        showtimes = _showtimes;
        posterArt = loadImage(_posterUrl, "jpg");
    }

    public String getTitle(){
        return title;
    }

    public String getRuntime(){
        return runtime;
    }

    public String getMetascore(){
        return metascore;
    }

    public String getPosterUrl(){
        return posterUrl;
    }

    public PImage getPosterArt(){
        return posterArt;
    }

    public ArrayList<String> getTheatres(){
        return theatres;
    }

    public ArrayList<String[]> getShowtimes(){
        return showtimes;
    }


}
class Runtime{
    String runtime;
    float textXPos = width/2 - 150;
    float textYPos = 47;
    float iconXPos = width/2 - 200;
    float iconYPos = 47;
    float iconWidth = 30;
    float iconHeight = 30;
    int textColor = color(255, 255, 255);
    float opacity;
    PFont font;
    int fontSize;
    PImage icon;

    Runtime(PFont _font, int _fontSize, PImage _icon){
        font = _font;
        fontSize = _fontSize;
        icon = _icon;
    }

    public void setRuntime(String _runtime){
        runtime = _runtime;
    }

    public void setOpacity(float _opacity){
        opacity = _opacity;
    }

    public void render(){  
        fill(255, opacity);
        tint(255, opacity);
        imageMode(CENTER);
        image(icon, iconXPos, iconYPos, iconWidth, iconHeight);
        textSize(fontSize);
        textAlign(LEFT, CENTER);
        textFont(font, fontSize);
        text(runtime, textXPos, textYPos);
    }

}
class Theatre{
    String theatreName;
    String[] showtimes;
    float nameTextXPos = width/2;
    float nameTextYPos = 150;
    int textColor = color(255, 255, 255);
    float opacity;
    PFont nameFont;
    int nameFontSize;
    PFont showtimeFont;
    int showtimeFontSize;

    int showtimesXSpacing = 150;
    int showtimesYSpacing = 100;
    int startX = width/2;
    int startY = 100;
    int offset;  

    Theatre(PFont _nameFont, int _nameFontSize, PFont _showtimeFont, int _showtimeFontSize){
        nameFont = _nameFont;
        nameFontSize = _nameFontSize;
        showtimeFont = _showtimeFont;
        showtimeFontSize = _showtimeFontSize;
    }

    public void setTheatreName(String _theatreName){
        theatreName = _theatreName;
    }

    public void setShowtimes(String[] _showtimes){
        showtimes = _showtimes;
        setOffset((showtimes.length-1) * showtimesXSpacing);  
    }

    public void setOpacity(float _opacity){
        opacity = _opacity;
    }

    public void setOffset(int _offset){
        offset = _offset;
    }

    public void render(){ 
        fill(255, opacity);
        textSize(nameFontSize);
        textAlign(CENTER);
        textFont(nameFont, nameFontSize);
        text(theatreName, nameTextXPos, nameTextYPos);

        textFont(showtimeFont, showtimeFontSize); 
        for(int i = 0; i < showtimes.length; i++){
            fill(255, opacity);
            text(showtimes[i], startX - offset/2 + (i*showtimesXSpacing), startY + showtimesYSpacing);
            
            strokeWeight(2);
            stroke(100, informationOpacity);

            if(i != showtimes.length-1){
                line(startX - offset/2 + (i*showtimesXSpacing) + (showtimesXSpacing/2), startY + showtimesYSpacing - ((showtimeFontSize/5)*4), startX - offset/2 + (i*showtimesXSpacing) + (showtimesXSpacing/2), startY + showtimesYSpacing + (showtimeFontSize/5));
            }
        }
    }

}
class Title{
    String title;
    float textXPos = width/2;
    float textYPos = height/2;
    float textContainerWidth = width/2-100;
    float textContainerHeight = height*0.75f;
    int textColor = color(255, 255, 255);
    PFont font;
    int fontSize;
    boolean show;

    Title(PFont _font, int _fontSize){
        font = _font;
        fontSize = _fontSize;
    }

    public void setTitle(String _title){
        title = _title;
    }

    public void setShow(boolean _show){
        show = _show;
    }

    public void render(){ 
        if(show){ 
            fill(255, 255);
            textSize(fontSize);
            textAlign(CENTER, CENTER);
            textFont(font, fontSize);
            text(title, textXPos, textYPos, textContainerWidth, textContainerHeight);
        }
    }

}
  public void settings() {  fullScreen();  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--hide-stop", "poster" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
