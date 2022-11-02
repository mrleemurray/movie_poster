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
      fixed-height="180px"
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
  mounted() {
    setTimeout(function() {
      window.location.reload();
    }, 3 * 60 * 60 * 1000);
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
      output += `<div class="divider"><div class="line"></div></div>`
      output += `<div class="date">${Moment(date).format('Do MMMM')}</div>`
      // output += `<div class="times">`
      // for (let i=0; i < input.showtimes.length; i++) {
      //   output += `<div class="time">${input.showtimes[i]}</div>`
      // }
      // output += `</div>`
      output += `</div>`
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
    /* background-image: linear-gradient(rgba(0, 0, 0, 0.8), rgba(0, 0, 0, 0)); */
    background-image: linear-gradient(to bottom, black 0%, rgba(0, 0, 0, 0.738) 19%, rgba(0, 0, 0, 0.541) 34%, rgba(0, 0, 0, 0.382) 47%, rgba(0, 0, 0, 0.278) 56.5%, rgba(0, 0, 0, 0.194) 65%, rgba(0, 0, 0, 0.126) 73%, rgba(0, 0, 0, 0.075) 80.2%, rgba(0, 0, 0, 0.042) 86.1%, rgba(0, 0, 0, 0.021) 91%, rgba(0, 0, 0, 0.008) 95.2%, rgba(0, 0, 0, 0.002) 98.2%, transparent 100%);
    font-size: 2rem;
  }
  .info {
    position: fixed;
    display: flex;
    justify-content: center;
    align-items: end;
    width: 100%;
    height: 10%;
    padding-bottom: 16px;
    bottom: 0;
    font-size: 1.2rem;
    background-image: linear-gradient(to top, black 0%, rgba(0, 0, 0, 0.738) 19%, rgba(0, 0, 0, 0.541) 34%, rgba(0, 0, 0, 0.382) 47%, rgba(0, 0, 0, 0.278) 56.5%, rgba(0, 0, 0, 0.194) 65%, rgba(0, 0, 0, 0.126) 73%, rgba(0, 0, 0, 0.075) 80.2%, rgba(0, 0, 0, 0.042) 86.1%, rgba(0, 0, 0, 0.021) 91%, rgba(0, 0, 0, 0.008) 95.2%, rgba(0, 0, 0, 0.002) 98.2%, transparent 100%);

    /* background-image: linear-gradient(rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.8)); */
  }
  .runTime {
    opacity: 0.8;
  }

  .metascore {
    padding: 2px 4px;
    border-radius: 2px;
    margin-right: 350px;
    opacity: 0.8;
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
    background-image: linear-gradient(rgba(0, 0, 0, 0.8), rgba(0, 0, 0, 0));
  }

  .vueperslide__content-wrapper:not(.vueperslide__content-wrapper--outside-top):not(.vueperslide__content-wrapper--outside-bottom) {
    justify-content: start !important;
    padding-top: 12px;
  }
  .theatre > h3 {
    position: relative;
    font-size: 2rem;
    margin: 0px 0px 4px 0px;
    opacity: 0.8;
  }

  .divider {
    box-sizing: border-box;
    position: relative;
    display: flex;
    align-content: center;
    width: 100%;
    height: 1px;
  }

  .line {
    margin: 0 auto; 
    width: 70%;
    height: 100%;
    background: #ffffff;
    opacity: 0.4;
    
  }
  .date {
    font-size: 1.2rem;
    margin-bottom: 8px;
    margin-top: 4px;
    opacity: 0.6;
  }
  .times {
    display: flex;
    justify-content: center;
    font-family: monospace;
    z-index: 999;
  }
  .time {
    font-size: 1rem;
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
