<script setup lang="ts">
  import * as echarts from "echarts";
  import china from "@/assets/china.json";
  import { onMounted } from 'vue';

  const init = () => {
    const myChart = echarts.init(document.getElementById("body"));
    echarts.registerMap('china', china);
    const option = {
      series: [
        {
          type: "map",
          map: 'china', // 引入地图数据
        },
      ],
    };
    myChart.setOption(option);
  };

  onMounted(() => {
    init();
  });
</script>

<script lang="ts">
export default {
  name: 'BigScreen',
};
</script>

<template>
  <div class="container">
    <Breadcrumb
      :items="['menu.visualization', 'menu.visualization.BigScreen']"
    />
    <a-space direction="vertical" :size="16" fill>
      <a-grid :cols="24" :col-gap="16" :row-gap="16">
        <a-grid-item
          :span="{ xs: 24, sm: 24, md: 24, lg: 18, xl: 18, xxl: 18 }"
        >
          <div id="body" class="body">
          </div>
        </a-grid-item>
      </a-grid>
    </a-space>
  </div>
</template>

<style scoped lang="less">
.body {
  margin-left: 100px;
  height: 780px;
  width: 1000px;
}
</style>