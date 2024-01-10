<template>
  <a-grid :cols="24" :row-gap="16" class="panel">
    <a-grid-item
      class="panel-col"
      :span="{ xs: 12, sm: 12, md: 12, lg: 12, xl: 12, xxl: 6 }"
    >
      <a-space>
        <a-avatar :size="54" class="col-avatar">
          <img
            alt="avatar"
            src="/src/assets/images/all.png"
          />
        </a-avatar>
        <a-statistic
          :title="$t('workplace.onlineContent')"
          :value="
            countModel.todayCount > 100000
              ? countModel.todayCount / 10000
              : Math.round(countModel.todayCount)
          "
          :precision="countModel.todayCount > 100000 ? 1 : 0"
          :value-from="0"
          animation
          show-group-separator
        >
          <template #suffix>
            <span v-if="countModel.todayCount > 100000">W+</span>
            <span class="unit">{{ $t('workplace.pecs') }}</span>
          </template>
        </a-statistic>
      </a-space>
    </a-grid-item>
    <a-grid-item
      class="panel-col"
      :span="{ xs: 12, sm: 12, md: 12, lg: 12, xl: 12, xxl: 6 }"
    >
      <a-space>
        <a-avatar :size="54" class="col-avatar">
          <img
            alt="avatar"
            src="/src/assets/images/flying.png"
          />
        </a-avatar>
        <a-statistic
          :title="$t('workplace.putIn')"
          :value="countModel.flyingCount"
          :value-from="0"
          animation
          show-group-separator
        >
          <template #suffix>
            <span class="unit">{{ $t('workplace.pecs') }}</span>
          </template>
        </a-statistic>
      </a-space>
    </a-grid-item>
    <a-grid-item
      class="panel-col"
      :span="{ xs: 12, sm: 12, md: 12, lg: 12, xl: 12, xxl: 6 }"
    >
      <a-space>
        <a-avatar :size="54" class="col-avatar">
          <img
            alt="avatar"
            src="/src/assets/images/late.png"
          />
        </a-avatar>
        <a-statistic
          :title="$t('workplace.newDay')"
          :value="countModel.lateCount"
          :value-from="0"
          animation
          show-group-separator
        >
          <template #suffix>
            <span class="unit">{{ $t('workplace.pecs') }}</span>
          </template>
        </a-statistic>
      </a-space>
    </a-grid-item>
    <a-grid-item
      class="panel-col"
      :span="{ xs: 12, sm: 12, md: 12, lg: 12, xl: 12, xxl: 6 }"
      style="border-right: none"
    >
      <a-space>
        <a-avatar :size="54" class="col-avatar">
          <img
            alt="avatar"
            src="/src/assets/images/statistics.png"
          />
        </a-avatar>
        <a-statistic
          :title="$t('workplace.newFromYesterday')"
          :value="countModel.increase"
          :precision="1"
          :value-from="0"
          animation
        >
          <template #suffix> % <icon-caret-up v-if="countModel.increase > 0" class="up-icon" /> <icon-caret-down v-else class="down-icon"></icon-caret-down> </template>
        </a-statistic>
      </a-space>
    </a-grid-item>
    <a-grid-item :span="24">
      <a-divider class="panel-border" />
    </a-grid-item>
  </a-grid>
</template>

<script lang="ts" setup>
import { getHomeCount } from "@/api/apis";
import useLoading from "@/hooks/loading";
import { ref } from "vue";

  const { loading, setLoading } = useLoading();
  const genCountModel = () => {
    return {
      todayCount: 0,
      flyingCount: 0,
      lateCount: 0,
      increase: 0.0,
    }
  }
  const countModel = ref(genCountModel());
  const fetchData = async () => {
    try {
      setLoading(true);
      const { data } = await getHomeCount();
      countModel.value = data;
    } catch (err) {
      // you can report use errorHandler or other
    } finally {
      setLoading(false);
    }
  };
  fetchData();
</script>

<style lang="less" scoped>
  .arco-grid.panel {
    margin-bottom: 0;
    padding: 16px 20px 0 20px;
  }
  .panel-col {
    padding-left: 43px;
    border-right: 1px solid rgb(var(--gray-2));
  }
  .col-avatar {
    margin-right: 12px;
    background-color: var(--color-fill-2);
  }
  .up-icon {
    color: rgb(var(--red-6));
  }
  .down-icon {
    color: rgb(var(--green-6));
  }
  .unit {
    margin-left: 8px;
    color: rgb(var(--gray-8));
    font-size: 12px;
  }
  :deep(.panel-border) {
    margin: 4px 0 0 0;
  }
</style>
