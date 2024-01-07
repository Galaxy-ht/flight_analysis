<template>
  <a-carousel
    indicator-type="slider"
    show-arrow="hover"
    auto-play
    style="width: 100%; height: 170px; border-radius: 4px; overflow: hidden"
  >
    <a-carousel-item v-for="(src, idx) in imageSrc" :key="idx">
      <div>
        <img :src="src" style="width: 100%" />
      </div>
    </a-carousel-item>
  </a-carousel>
</template>

<script lang="ts" setup>
  const imageSrc = [
    '/src/assets/images/flight5.jpg',
    '/src/assets/images/flight1.jpg',
    '/src/assets/images/flight2.jpg',
    '/src/assets/images/flight3.jpg',
    '/src/assets/images/flight4.jpg',
  ];
</script>
