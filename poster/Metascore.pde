class Metascore{
    String score;
    float xPos = width/2 + 155;
    float yPos = 47;
    float containerWidth = 46;
    float containerHeight = 46;
    float thickness = 1;
    color containerColor;
    color textColor = color(255, 255, 255);
    float opacity;
    PFont font;
    int fontSize;

    Metascore(PFont _font, int _fontSize){
        font = _font;
        fontSize = _fontSize;
    }

    void setScore(String _score){
        score = _score;
        try{
            containerColor = calculateScoreColor(Integer.parseInt(_score));
        }
        catch(Exception e){
            containerColor = color(150, 150, 150);
        }
    }

    void setOpacity(float _opacity){
        opacity = _opacity;
    }

    color calculateScoreColor(int _score){
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

    void render(){  
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