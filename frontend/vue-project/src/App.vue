<script setup>
import { ref, reactive, onMounted } from 'vue'

const msg = ref('Hello Vue 3!')

onMounted(() => {
  console.log('组件已挂载')
})
</script>

<template>
  <div class="container">
    <h1>{{ msg }}</h1>
  </div>
</template>

<style scoped lang="scss">
.container {
  padding: 20px;
}
</style>