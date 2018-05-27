<template>
  <div id="dt-cake" class="root">
    <div class="container">
      <div id="details">
        <div class="dt-logo"></div>
        <div class="dt-cake-details">
          <div>
            <p><span class="tit">龙币当前分红</span><span class="sign">￥</span><span class="money">{{nowCake}}</span></p>
            <p><span class="tit">龙币昨日分红</span><span class="sign">￥</span><span class="money">{{yerstodayCake}}</span></p>
          </div>
          <div>
            <p><span class="tit">单人刷蛋成本</span><span class="sign">￥</span><span class="money">{{onePeopleCost}}</span></p>
            <p><span class="tit">双人刷蛋成本</span><span class="sign">￥</span><span class="money">{{doublePeopleCost}}</span></p>
          </div>
          <div>
            <p><span class="tit">昨日单刷成本</span><span class="sign">￥</span><span class="money">{{yerstodayOnePeopleCost}}</span></p>
            <p><span class="tit">昨日双刷成本</span><span class="sign">￥</span><span class="money">{{yerstodayDoublePeopleCost}}</span></p>
          </div>
        </div>
      </div>
      <div id="hourCharts"></div>
      <div id="dayCharts"></div>
    </div>
  </div>
</template>

<script>
  import echarts from 'echarts/lib/echarts';
  // 引入柱状图
  require('echarts/lib/chart/line');
  // 引入提示框和标题组件
  require('echarts/lib/component/tooltip');
  require('echarts/lib/component/title');
  export default {
    name: 'DTCake',
    data() {
      return {
        month_DT_date: [],
        month_DT_dividend: [],
        DT_onePeopleCost: [],
        DT_doublePeopleCost: [],
        today: [],
        yesterday: [],
        yesterdayBefore: [],
        nowCake: '',
        onePeopleCost: '',
        doublePeopleCost: '',
        yerstodayCake: '',
        yerstodayDoublePeopleCost: '',
        yerstodayOnePeopleCost: ''
      }
    },
    methods: {
      getDtDayLine() {
        let This = this;
        this.$http({
          url: `/api/niuba/dt/getDtDayLine`,
          method: 'post',
        }).then((res) => {
          let data = res.data;
          This.month_DT_date = data.month_DT_date;
          This.month_DT_dividend = data.month_DT_dividend;
          This.DT_onePeopleCost = data.DT_onePeopleCost;
          This.DT_doublePeopleCost = data.DT_doublePeopleCost;
          let index = This.month_DT_date.indexOf(null);
          This.month_DT_date = This.month_DT_date.slice(0, index);
          This.month_DT_dividend = This.month_DT_dividend.slice(0, index);
          This.DT_onePeopleCost = This.DT_onePeopleCost.slice(0, index);
          This.DT_doublePeopleCost = This.DT_doublePeopleCost.slice(0, index);

          This.renderChartDtDayLine();
        }).catch((error) => {
          console.log('error', error);
          alert('请求失败，请刷新页面重新尝试');
        });
      },
      renderChartDtDayLine() {

        let This = this;
        var colors = ['#F24A4A', '#67DECE', '#C3C3C3'];
        
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('dayCharts'));
        var option = {
          title : {
            text : '龙蛋每日分红'
          },
          color: colors,
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['当日分红', '当日单刷成本', '当日双刷成本']
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
              data: This.month_DT_date
            }
          ],
          yAxis: [
            {
              type: 'value',
              axisLabel : {
                formatter: '{value} CNY'
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
              name:'当日分红',
              type:'line',
              smooth: true,
              color: colors[0],
              data: This.month_DT_dividend
            },
            {
              name:'当日单刷成本',
              type:'line',
              smooth: true,
              color: colors[1],
              data: This.DT_onePeopleCost
            },
            {
              name:'当日双刷成本',
              type:'line',
              smooth: true,
              color: colors[2],
              data: This.DT_doublePeopleCost
            }
          ]
        };
        // 绘制图表
        myChart.setOption(option);
      },

      getDtHourLine() {
        let This = this;
        this.$http({
          url: `/api/niuba/dt/getDtInfo`,
          method: 'post',
        }).then((res) => {
          let data = res.data;
          if (data.msg == 'success') {
            let result = data.result;
            This.today = result.today;
            This.yesterday = result.yesterday;
            This.yesterdayBefore = result.yesterdayBefore;
            This.nowCake = result.nowCake;
            This.onePeopleCost = result.onePeopleCost;
            This.doublePeopleCost = result.doublePeopleCost;
            This.yerstodayCake = result.yerstodayCake;
            This.yerstodayDoublePeopleCost = result.yerstodayDoublePeopleCost;
            This.yerstodayOnePeopleCost = result.yerstodayOnePeopleCost;
            This.renderChartDtHourLine();
          }
        }).catch((error) => {
          console.log('error', error);
          alert('请求失败，请刷新页面重新尝试');
        });
      },
      renderChartDtHourLine() {
        let This = this;

        var colors = ['#F24A4A', '#67DECE', '#C3C3C3'];
        var strHours = [];

        for (let i = 0; i < 24; i++) {
          strHours.push(i + ':00');
        }
        
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('hourCharts'));
        var option = {
          title : {
            text : '龙蛋分红'
          },
          color: colors,
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['当日分红', '昨日分红', '前日分红']
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
              data: strHours
            }
          ],
          yAxis: [
            {
              type: 'value',
              axisLabel : {
                formatter: '{value} CNY',
              },
              axisLine: {
                lineStyle: {
                  color: '#555'
                }
              },
            }
          ],
          series: [
            {
              name:'当日分红',
              type:'line',
              smooth: true,
              color: colors[0],
              data: This.today
            },
            {
              name:'昨日分红',
              type:'line',
              smooth: true,
              color: colors[1],
              data: This.yesterday
            },
            {
              name:'前日分红',
              type:'line',
              smooth: true,
              color: colors[2],
              data: This.yesterdayBefore
            }
          ]
        };
        // 绘制图表
        myChart.setOption(option);
      }
    },
    mounted() {
      this.getDtDayLine();
      this.getDtHourLine();
    }
  }
</script>

<style lang="scss" scoped>
  #dayCharts, #hourCharts {
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
    background: url(../assets/DT.png) no-repeat;
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
