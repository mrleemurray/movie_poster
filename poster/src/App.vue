<template>
  <div id="app">
    <slider ref="slider" :options="options">
          <!-- slideritem wrapped package with the components you need -->
          <slideritem v-for="(movie,index) in movies" :key="index">
            <Poster
              :movie = movie
            />
          </slideritem>
          <!-- Customizable loading -->
          <div slot="loading">loading...</div>
      </slider>
  </div>
</template>

<script>
import Movies from '../public/static/movieData.json';
import Poster from './components/Poster.vue';
import {slider, slideritem} from 'vue-concise-slider';

export default {
  name: 'App',
  components: {
    Poster,
    slider,
    slideritem,
  },
  created() {
    this.movies = Movies.movies;
  },
  data() {
    return {
      movies: [],
      options: {
        effect: 'fade',
        loop: true,
        autoplay: 20000,
        speed: 1000,
        pagination: false
      }
    }
  }
}
</script>

<style>
@font-face {
  font-family: cambo;
  src:  url('./assets/fonts/Cambo-Regular.otf');
}

#app {
  text-align: center;
  color: #2c3e50;
  margin: 0px;
  user-select: none;
  height: 100vh;
}
body {
  font-family: cambo, serif;
  margin: 0;
  height: 100%;
  overflow: hidden;
}
</style>
