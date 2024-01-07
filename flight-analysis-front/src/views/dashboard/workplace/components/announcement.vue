<template>
  <a-card
    class="general-card"
    :title="$t('workplace.announcement')"
    :header-style="{ paddingBottom: '0' }"
    :body-style="{ padding: '15px 20px 13px 20px' }"
  >
    <template #extra>
      <a-link>{{ $t('workplace.viewMore') }}</a-link>
    </template>
    <div>
      <div v-for="(item, idx) in list" :key="idx" class="item">
        <a-tag :color="item.type" size="small">{{ item.label }}</a-tag>
        <span class="item-content">
          {{ item.content }}
        </span>
      </div>
    </div>
  </a-card>
</template>

<script lang="ts" setup>
  const list = [
    {
      type: 'orangered',
      label: '特情',
      content: '沈阳大雾天气，可见度不足10m',
    },
    {
      type: 'cyan',
      label: '特情',
      content: '常州爆炸了',
    },
    {
      type: 'blue',
      label: '特情',
      content: '常大学子十分想家',
    },
    {
      type: 'blue',
      label: '特情',
      content: '江苏安排免费飞机接送学生寒假归家',
    },
    {
      type: 'cyan',
      label: '特情',
      content: '兰州下雪了',
    },
  ];
</script>

<style scoped lang="less">
  .item {
    display: flex;
    align-items: center;
    width: 100%;
    height: 24px;
    margin-bottom: 4px;
    .item-content {
      flex: 1;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      margin-left: 4px;
      color: var(--color-text-2);
      text-decoration: none;
      font-size: 13px;
      cursor: pointer;
    }
  }
</style>
