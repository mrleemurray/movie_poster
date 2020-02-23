class Title{
    String title;
    float textXPos = width/2;
    float textYPos = height/2;
    float textContainerWidth = width/2-100;
    float textContainerHeight = height*0.75;
    color textColor = color(255, 255, 255);
    PFont font;
    int fontSize;
    boolean show;

    Title(PFont _font, int _fontSize){
        font = _font;
        fontSize = _fontSize;
    }

    void setTitle(String _title){
        title = _title;
    }

    void setShow(boolean _show){
        show = _show;
    }

    void render(){ 
        if(show){ 
            fill(255, 255);
            textSize(fontSize);
            textAlign(CENTER, CENTER);
            textFont(font, fontSize);
            text(title, textXPos, textYPos, textContainerWidth, textContainerHeight);
        }
    }

}