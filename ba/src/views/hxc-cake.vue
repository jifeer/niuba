<template>
  <div id="hxc-cake" class="root">
    <div class="container">
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
    name: "HXCCake",
    data() {
      return {
        hxc_date: [],
        hxc_income: []
      }
    },
    methods: {
      getHxcDayLine() {
        let This = this;
        this.$http({
          url: `/api/niuba/hxc/getHxcDayLine`,
          method: 'post',
        }).then((res) => {
          let data = res.data;
          if (data.msg == 'success') {
            let result = data.result;
            
            console.log('result: ', result);

            This.hxc_date = result.hxc_date;
            This.hxc_income = result.hxc_income;

            This.renderChartHxcDayLine();
          }
        }).catch((error) => {
          console.log('error', error);
          alert('请求失败，请刷新页面重新尝试');
        });
      },
      renderChartHxcDayLine() {
        let This = this;
        var colors = ['#F24A4A', '#67DECE', '#C3C3C3'];
        
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('dayCharts'));
        var option = {
          title : {
            text : '火星币分红'
          },
          color: colors,
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['单币分红']
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
              data: This.hxc_date
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
              name:'单币分红',
              type:'line',
              smooth: true,
              color: colors[0],
              data: This.hxc_income
            }
          ]
        };
        // 绘制图表
        myChart.setOption(option);
      }
    },
    mounted() {
      this.getHxcDayLine();
    }
  }
</script>

<style lang="scss" scoped>
  #dayCharts {
    width: 1000px;
    height: 700px;
  }
</style>
