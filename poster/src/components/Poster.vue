<template>
  <div class="poster">
    <img 
      class="movie-poster"
      :src=posterSrc
    />
    <div class="showtimes">
    <vueper-slides 
      fade 
      :touchable="false"
      :arrows="false"
      :autoplay="true"
      :bullets="false"
      :duration="5000"
      :transitionSpeed="1000"
      fixed-height="140px"
    >
      <vueper-slide 
        v-for="(showtime, index) in movie.playingAt"
        :key="index"
        :content="showtimeInfo(showtime, movie.date)"
        :style="'background-color: transparent'"
        @clicked="talk(index)"
      />
    </vueper-slides>
    </div>
    <div class="info">
      <div
        class="metascore"
        v-if="movie.metascore != null"
        :class="metaScore"
      >
        {{ movie.metascore }}
      </div>
      <div class="runTime">
        {{ movie.runningTime }}
      </div>
    </div>
  </div>
</template>

<script>
import { VueperSlides, VueperSlide } from 'vueperslides';
import 'vueperslides/dist/vueperslides.css';
import Moment from 'moment';

export default {
  name: 'Poster',
  components: {
    VueperSlides,
    VueperSlide,
  },
  props: {
    movie: null,
  },
  data() {
    return {
    }
  },
  computed: {
    posterSrc() {
      try { 
        return this.movie.poster;
      } catch {
        return '';
      }
    },
    metaScore() {
      try {
        if (this.movie.metascore === null) {
          return '';
        }
        if (this.movie.metascore >= 0 && this.movie.metascore < 33) {
          return 'bad';
        }
        if (this.movie.metascore >= 33 && this.movie.metascore < 66) {
          return 'okay';
        }
        if (this.movie.metascore >= 66 && this.movie.metascore < 100) {
          return 'good';
        }
      } catch {
        return '';
      }
      return '';
    }
  },
  methods: {
    showtimeInfo(input, date) {
      let output = `<div class="theatre"><h3>${input.theatre}</h3>`;
      output += `<div class="date">${Moment(date).format('Do MMMM')}</div>`
      output += `<div class="times">`
      for (let i=0; i < input.showtimes.length; i++) {
        output += `<div class="time">${input.showtimes[i]}</div>`
      }
      output += `</div></div>`
      return output;
    },
    talk(input) {
      console.log(input);
    }
  },
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .poster {
    height: 100%;
    width: 100vw;
    background-color: #000000;
  }
  .movie-poster {
    position: relative;
    height: 100vh;
    width: auto;
  }
  .showtimes {
    position: fixed;
    top: 0%;
    width: 100%;
    background-image: linear-gradient(rgba(0, 0, 0, 0.8), rgba(0, 0, 0, 0));
    height: 50vh;
    font-size: 2rem;
  }
  .info {
    position: fixed;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 8%;
    padding-top: 4px;
    top: 92%;
    font-size: 1.3rem;
    background-image: linear-gradient(rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.8));
  }
  .metascore {
    padding: 2px 4px;
    border-radius: 2px;
    margin-right: 300px;
  }
  .metascore.bad {
    background-color: rgba(255, 0, 0, 0.5);
  }
  .metascore.okay {
    background-color: rgba(255, 165, 0, 0.5);
  }
  .metascore.good {
    background-color: rgba(0, 128, 0, 0.5);
  }
</style>
<style>
  .vueperslides__parallax-wrapper:after, .vueperslides:not(.no-shadow):not(.vueperslides--3d) {
    box-shadow:  none !important;
  }
  .theatre > h3 {
    margin: 0px 0px 18px 0px;
  }
  .date {
    font-size: 1.4rem;
    margin-bottom: 10px;
    margin-top: 10px;
  }
  .times {
    display: flex;
    justify-content: center;
    font-family: monospace;
    z-index: 999;
  }
  .time {
    font-size: 1.2rem;
    margin: 0px 10px;
    padding: 2px 8px;
    border: 1px solid white;
    font-family: monospace;
    color: white;
    border-radius: 4px;
    background-color: rgba(0, 0, 0, 0.2);
    
  }
  .time:hover {
    background-color: rgba(255, 255, 255, 0.8);
    color: black;
  }
</style>
