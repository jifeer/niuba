<template>
  <div id="cht-cake" class="root">
    <div class="container">
      <div id="details">
        <div class="dt-logo"></div>
        <div class="dt-cake-details">
          <div>
            <p><span class="tit">昨日分红(BTC)</span> <span class="money">{{yesterday_info.HT_BTC_dividend}}</span></p>
            <p><span class="tit">昨日成交量(BTC)</span> <span class="money">{{yesterday_info.HT_BTC}}</span></p>
          </div>
          <div>
            <p><span class="tit">昨日分红(ETH)</span> <span class="money">{{yesterday_info.HT_ETH_dividend}}</span></p>
            <p><span class="tit">昨日成交量(ETH)</span> <span class="money">{{yesterday_info.HT_ETH}}</span></p>
          </div>
          <div>
            <p><span class="tit">可分红数量</span> <span class="money">{{yesterday_info.HT_can}}</span></p>
            <p><span class="tit">总流通量</span> <span class="money">{{yesterday_info.HT_count}}</span></p>
          </div>
        </div>
      </div>
      <div id="dayCharts"></div>
    </div>
  </div>
</template>

<script>
  // 引入 ECharts 主模块
  import echarts from 'echarts/lib/echarts';
  // 引入柱状图
  require('echarts/lib/chart/line');
  // 引入提示框和标题组件
  require('echarts/lib/component/tooltip');
  require('echarts/lib/component/title');
  export default {
    name: "CHTCake",
    data() {
      return {
        dividend_histroy_BTC: [],
        dividend_histroy_ETH: [],
        dividend_histroy_count: [],
        dividend_histroy_date: [],
        yesterday_info: {},
      }
    },
    methods: {
      getChtDayLine() {
        let This = this;
        this.$http({
          url: `/api/niuba/cht/getChtDayLine`,
          method: 'post',
        }).then((res) => {
          let data = res.data;
          if (data.msg == 'success') {
            let result = data.result;
            This.dividend_histroy_BTC = result.dividend_histroy_BTC;
            This.dividend_histroy_ETH = result.dividend_histroy_ETH;
            // This.dividend_histroy_count = result.dividend_histroy_count;
            This.dividend_histroy_date = result.dividend_histroy_date;

            This.HT_BTC = result.HT_BTC;
            This.HT_ETH = result.HT_ETH;
            This.HT_BTC_dividend = result.HT_BTC_dividend;
            This.HT_ETH_dividend = result.HT_ETH_dividend;
            This.yesterday_info = result.yesterday_info;
          
            for (let i = 0; i < This.dividend_histroy_BTC.length; i++) {
              This.dividend_histroy_BTC[i] = This.dividend_histroy_BTC[i].replace(/ BTC/, '');
              This.dividend_histroy_ETH[i] = result.dividend_histroy_ETH[i].replace(/ ETH/, '');
            }

            This.renderChartChtDayLine();
          }
        }).catch((error) => {
          console.log('error', error);
          alert('请求失败，请刷新页面重新尝试');
        });
      },
      renderChartChtDayLine() {
        let This = this;
        var colors = ['#F24A4A', '#67DECE', '#C3C3C3'];
        
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('dayCharts'));
        var option = {
          title : {
            text : '热币'
          },
          color: colors,
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['当日BTC分红', '当日ETH分红']
          },
          xAxis: [
            {
              type: 'category',
              axisTick: {
                alignWithLabel: true
              },
              axisLine: {
                lineStyle: {
                  color: '#555'
                }
              },
              data: This.dividend_histroy_date
            }
          ],
          yAxis: [
            {
              type: 'value',
              axisLabel : {
                formatter: '{value}'
              },
              axisLine: {
                lineStyle: {
                  color: '#555'
                }
              }
            }
          ],
          series: [
            {
              name:'当日分红BTC',
              type:'line',
              smooth: true,
              color: colors[0],
              data: This.dividend_histroy_BTC
            },
            {
              name:'当日分红ETH',
              type:'line',
              smooth: true,
              color: colors[1],
              data: This.dividend_histroy_ETH
            }
          ]
        };
        // 绘制图表
        myChart.setOption(option);
      }
    },
    mounted() {
      this.getChtDayLine();
    }
  }
</script>

<style lang="scss" scoped>
  #dayCharts {
    width: 1000px;
    height: 700px;
  }
  #details {
    width: 1000px;
    margin: 50px auto;
    background: #fff;
  }
  .dt-logo {
    width: 40px;
    height: 40px;
    background: url(../assets/CHT.png) no-repeat;
    position: relative;
    margin: 0 auto;
    margin-top: 50px;
    top: -20px;
  }
  .dt-cake-details {
    display: flex;
    justify-content: space-around;
  }
  .tit {
    color: #555;
    font-size: 16px;
  }

  .sign {
    color:#DF5858;
    font-size: 16px;
    padding: 0 5px;
  }

  .money {
    color: #DF5757;
    font-size: 25px;
  }
</style>
