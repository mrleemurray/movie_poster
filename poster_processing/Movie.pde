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

    String getTitle(){
        return title;
    }

    String getRuntime(){
        return runtime;
    }

    String getMetascore(){
        return metascore;
    }

    String getPosterUrl(){
        return posterUrl;
    }

    PImage getPosterArt(){
        return posterArt;
    }

    ArrayList<String> getTheatres(){
        return theatres;
    }

    ArrayList<String[]> getShowtimes(){
        return showtimes;
    }


}