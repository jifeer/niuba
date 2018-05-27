<template>
  <div id="home" class="root">
    <div class="container">
      <div id="banner">
        <img src="@/assets/banner.jpeg" alt="">
      </div>

      <!-- 龙币分红  开始 DT-->
      <div class="DT-cake clearfix">
        <div class="cake-title fl">
          <h3>龙币分红</h3>
        </div>
        <div class="today-cake fl">
          <p>当前分红 <span class="sign">￥</span><span class="money">0.2071</span></p>
          <p>当前成本 <span class="sign">￥</span><span class="money">0.2071</span></p>
        </div>
        <div class="yesterday-cake fl">
          <p>昨日分红 <span class="sign">￥</span><span class="money">0.2071</span></p>
          <p>昨日成本 <span class="sign">￥</span><span class="money">0.2071</span></p>
        </div>
      </div>
      <!-- 龙币分红  结束 -->

      <!-- 其他币分红  结束 -->
      
      <div class="clearfix other-cake">
        <div class="CHT-cake fl">
          <h3>CHT分红</h3>
          <p>昨日分红 <span class="sign">￥</span><span class="money">0.2071</span></p>
        </div>
        <div class="HXC-cake fr">
          <h3>HXC分红</h3>
          <p>昨日分红 <span class="sign">￥</span><span class="money">0.2071</span></p>
        </div>
      </div>

      <!-- 其他币分红  结束 -->

      <div class="inflow">
        <h4>资金流入排行（前12）</h4>
        <h5>1小时数据统计</h5>
      </div>

    </div>
  </div>
</template>

<script>
  import { DatabaseURL } from '@/config';
  export default {
    name: 'Home',
    data () {
      return {
        DT_nowCake: '',
        DT_onePeopleCost: '',
        DT_doublePeopleCost: '',
        DT_yerstodayCake: '',
        DT_yerstodayOnePeopleCost: '',
        DT_yerstodayDoublePeopleCost: ''
      }
    },
    methods: {
      getDTInfo() {
        this.$http({
          url: `/api/niuba/dt/getDtInfo`,
          method: 'post',
        }).then((res) => {
          let data = res.data;
          if (data.msg == 'success') {
            console.log(data.result);
          }
        }).catch((error) => {
          alert('请求失败，请刷新页面重新尝试');
        });
      },
      getCHTInfo() {
        this.$http({
          url: `/api/niuba/cht/getChtDayLine`,
          method: 'post',
        }).then((res) => {
          let data = res.data;
          if (data.msg == 'success') {
            console.log(data.result);
          }
        }).catch((error) => {
          alert('请求失败，请刷新页面重新尝试');
        });
      },
      getHXCInfo() {
        this.$http({
          url: `/api/niuba/hxc/getHxcDayLine`,
          method: 'post',
        }).then((res) => {
          let data = res.data;
          if (data.msg == 'success') {
            console.log(data.result);
          }
        }).catch((error) => {
          alert('请求失败，请刷新页面重新尝试');
        });
      }
    },
    beforeCreate() {
      
    },
    mounted() {
      this.getDTInfo();
      this.getCHTInfo();
      this.getHXCInfo();
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>

  .sign {
    color: #DF5858;
    font-size: 16px;
    margin: 0 8px;
  }

  .money {
    color: #DF5757;
    font-size: 25px;
  }

  @mixin common-h3 {
    height: 40px;
    margin: 10px;
    line-height: 40px;
    text-indent: 60px;
    box-sizing: border-box;
  }

  .DT-cake {
    background: #fff;
    margin-top: 50px;
    padding: 10px 0 10px 10px;
    border-radius: 5px;
    > .cake-title {
      width: 33.3333%;
      > h3 {
        @include common-h3;
        background: url(../assets/DT.png) no-repeat;
      }
    }
    > .today-cake {
      width: 33.3333%;
      > p {
        font-size: 16px;
      }
    }
    > .yesterday-cake {
      width: 33.3333%;
      > p {
        font-size: 16px;
      }
    }
  }

  @mixin cake {
    width: 495px;
    background: #fff;
    padding: 10px 0 10px 10px;
    box-sizing: border-box;
    border-radius: 5px;
    > p {
      text-align: right;
      padding-right: 40px;
    }
  }

  .other-cake {
    margin-top: 10px;
    > .CHT-cake {
      @include cake;
      > h3 {
        @include common-h3;
        background: url(../assets/CHT.png) no-repeat;
      }
    }

    > .HXC-cake {
      @include cake;
      > h3 {
        @include common-h3;
        background: url(../assets/HXC.png) no-repeat;
      }
    }
  }

  .inflow {
    margin-top: 80px;
    > h4 {
      font-size: 20px;
      padding: 0;
      margin: 0;
      text-align: center;
    }
    > h5 {
      font-size: 14px;
      color: #888;
      text-align: center;
    }
  }

  
</style>
