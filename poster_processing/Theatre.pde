class Theatre{
    String theatreName;
    String[] showtimes;
    float nameTextXPos = width/2;
    float nameTextYPos = 150;
    color textColor = color(255, 255, 255);
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

    void setTheatreName(String _theatreName){
        theatreName = _theatreName;
    }

    void setShowtimes(String[] _showtimes){
        showtimes = _showtimes;
        setOffset((showtimes.length-1) * showtimesXSpacing);  
    }

    void setOpacity(float _opacity){
        opacity = _opacity;
    }

    void setOffset(int _offset){
        offset = _offset;
    }

    void render(){ 
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