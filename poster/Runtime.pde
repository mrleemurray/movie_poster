class Runtime{
    String runtime;
    float textXPos = width/2 - 150;
    float textYPos = 47;
    float iconXPos = width/2 - 200;
    float iconYPos = 47;
    float iconWidth = 30;
    float iconHeight = 30;
    color textColor = color(255, 255, 255);
    float opacity;
    PFont font;
    int fontSize;
    PImage icon;

    Runtime(PFont _font, int _fontSize, PImage _icon){
        font = _font;
        fontSize = _fontSize;
        icon = _icon;
    }

    void setRuntime(String _runtime){
        runtime = _runtime;
    }

    void setOpacity(float _opacity){
        opacity = _opacity;
    }

    void render(){  
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