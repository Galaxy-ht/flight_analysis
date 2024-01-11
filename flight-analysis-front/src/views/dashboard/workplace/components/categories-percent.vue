<template>
  <a-spin :loading="loading" style="width: 100%">
    <a-card
      class="general-card"
      :header-style="{ paddingBottom: '0' }"
      :body-style="{
        padding: '20px',
      }"
    >
      <template #title>
        {{ $t('workplace.categoriesPercent') }}
      </template>
      <Chart height="310px" :option="chartOption" />
    </a-card>
  </a-spin>
</template>

<script lang="ts" setup>
  import useLoading from '@/hooks/loading';
  import useChartOption from '@/hooks/chart-option';
  import { ref } from "vue";
  import { getPieChart, pieChart } from "@/api/apis";

  const { loading, setLoading } = useLoading();
  const genPieModel = () => {
    return {
      total: '0',
      late: '0',
      onTime: '0',
      before: '0',
    }
  }
  const pie = ref(genPieModel());
  const fetchData = async () => {
    try {
      setLoading(true);
      const { data } = await getPieChart();
      pie.value = data;
    } catch (err) {
      // you can report use errorHandler or other
    } finally {
      setLoading(false);
    }
  };
  fetchData();
  const { chartOption } = useChartOption((isDark) => {
    // echarts support https://echarts.apache.org/zh/theme-builder.html
    // It's not used here
    return {
      legend: {
        left: 'center',
        data: ['晚点', '准点', '提前'],
        bottom: 0,
        icon: 'circle',
        itemWidth: 8,
        textStyle: {
          color: isDark ? 'rgba(255, 255, 255, 0.7)' : '#4E5969',
        },
        itemStyle: {
          borderWidth: 0,
        },
      },
      tooltip: {
        show: true,
        trigger: 'item',
      },
      graphic: {
        elements: [
          {
            type: 'text',
            left: 'center',
            top: '40%',
            style: {
              text: '到达量',
              textAlign: 'center',
              fill: isDark ? '#ffffffb3' : '#4E5969',
              fontSize: 14,
            },
          },
          {
            type: 'text',
            left: 'center',
            top: '50%',
            style: {
              text: pie.value.total,
              textAlign: 'center',
              fill: isDark ? '#ffffffb3' : '#1D2129',
              fontSize: 16,
              fontWeight: 500,
            },
          },
        ],
      },
      series: [
        {
          type: 'pie',
          radius: ['50%', '70%'],
          center: ['50%', '50%'],
          label: {
            formatter: '{d}%',
            fontSize: 14,
            color: isDark ? 'rgba(255, 255, 255, 0.7)' : '#4E5969',
          },
          itemStyle: {
            borderColor: isDark ? '#232324' : '#fff',
            borderWidth: 1,
          },
          data: [
            {
              value: [pie.value.late],
              name: '晚点',
              itemStyle: {
                color: isDark ? '#3D72F6' : '#249EFF',
              },
            },
            {
              value: [pie.value.onTime],
              name: '准点',
              itemStyle: {
                color: isDark ? '#A079DC' : '#313CA9',
              },
            },
            {
              value: [pie.value.before],
              name: '提前',
              itemStyle: {
                color: isDark ? '#6CAAF5' : '#21CCFF',
              },
            },
          ],
        },
      ],
    };
  });
</script>

<style scoped lang="less"></style>
