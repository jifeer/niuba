<template>
  <div id="nav">
    <div id="logo">
      <img src="@/assets/logo.png" alt="">
    </div>
    <div v-for="(item, index) in navData" :key="index">
      <div class="nav-title" v-if="item.title">{{item.title}}</div>
      <router-link :to="nav.url" tag="div" class="nav-el" v-for="(nav, idx) in item.els" :key="idx">{{nav.name}}</router-link>
    </div>
  </div>
</template>

<script>
  export default {
    name: "NavBar",
    data() {
      return {
        navData: [
          {
            title: '',
            els: [
              {
                name: '首页',
                url: '/'
              }
            ]
          },
          {
            title: '分红币详情',
            els: [
              {
                name: 'DT分红',
                url: '/dt-cake'
              },
              {
                name: 'CHT分红',
                url: '/cht-cake'
              },
              {
                name: 'HXC分红',
                url: '/hxc-cake'
              }
            ]
          },
          {
            title: '数据分析',
            els: [
              {
                name: '成交量排行',
                url: '/d'
              },
              {
                name: '资金流向排行',
                url: '/e'
              },
              {
                name: '交易所排行',
                url: '/f'
              }
            ]
          },
          {
            title: '分红平台',
            els: [
              {
                name: 'DragonEx（龙网）',
                url: '/g'
              },
              {
                name: 'CoinHot',
                url: '/h'
              },
              {
                name: 'HuoXing',
                url: '/i'
              }
            ]
          },
          {
            title: '',
            els: [{
              name: '交易网站汇集',
              url: '/j'
            }]
          }
        ]
      }
    }
  }
</script>

<style lang="scss" scoped>
  dl {
    padding: 0;
    margin: 0;
  }
  #nav {
    position: fixed;
    top: 0;
    left: 0;
    width: $navBar_width;
    height: 100%;
    overflow-y: auto;
    background: #1D2027;
    color: #fff;
  }

  #logo {
    width: 100%;
    height: 150px;
    text-align: center;
    > img {
      margin-top: 50px;
    }
  }
  .nav-title {
    font-size: 14px;
    color: #364D7E;
    padding-left: 30px;
    position: relative;
    margin: 20px 0;
  }
  .nav-title:before {
    content: '';
    display: block;
    width: 6px;
    height: 6px;
    background: #364D7E;
    border-radius: 50%;
    position: absolute;
    top: 5px;
    left: 15px;
  }
  .nav-el {
    padding-left: 30px;
    height: 40px;
    line-height: 40px;
    position: relative;
    cursor: pointer;
  }

  .router-link-exact-active {
    color: #4588FF;
    &:after {
      content: '';
      display: block;
      width: 4px;
      height: 20px;
      background: #4588FF;
      position: absolute;
      top: 10px;
      right: 10px;
    }
  }
</style>
