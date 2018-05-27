<template>
  <div id="app">
    <nav-bar></nav-bar>
    <router-view class="router-view"/>
  </div>
</template>

<script>
  import NavBar from '@/components/NavBar';
  export default {
    name: 'App',
    components: {
      NavBar
    }
  }
</script>

<style lang="scss">
  html, body {
    width: 100%;
    height: 100%;
  }
  body {
    background: #f1f2f9;
  }
  #app {
    width: 100%;
    height: 100%;
    overflow-x: hidden;
  }
  
</style>
