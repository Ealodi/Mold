<template>
<!--  标题-->
  <el-row class="header">
    <el-col class="title" :span = "24">
      模具信息管理系统
    </el-col>
  </el-row>
<!--  菜单-->
  <el-row>
    <el-col :span="4">
      <h3 class="mb-2">系统菜单</h3>
      <el-menu
          default-active="1"
          class="el-menu-vertical-demo"
          @open="handleOpen"
          @close="handleClose"
          router
      >
        <el-menu-item index="/">
          <el-icon><icon-menu /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="orders">
          <el-icon><icon-menu /></el-icon>
          <span>订单管理</span>
        </el-menu-item>
        <el-menu-item index="client">
          <el-icon><setting /></el-icon>
          <span>客户管理</span>
        </el-menu-item>
        <el-menu-item index="payment">
          <el-icon><setting /></el-icon>
          <span>流水管理</span>
        </el-menu-item>
      </el-menu>
    </el-col>
    <el-col :span="20">
      <router-view/>
    </el-col>
  </el-row>

</template>

<script setup>

</script>

<style scoped>
.title{
  text-align: center;
  font-size: 25px;
  font-family: 黑体;
  font-weight: 700;
  color: aliceblue;
  letter-spacing: 3px;
}
.header {
  background-color: #409eff;
  margin: 0;
  height: 60px;
  align-items: center;
}
.mb-2 {
  border-bottom: 2px solid black;
  align-items: center;
}
</style>
