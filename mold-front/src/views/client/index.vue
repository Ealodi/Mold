
<template>
  <!--  页头-->
  <el-row class="row">
    <el-col :span="24">
      <el-page-header :icon="ArrowLeft">
        <template #content>
          <span class="text-large font-600 mr-3"> 客户管理 </span>
        </template>
      </el-page-header>
    </el-col>
  </el-row>
  <!--  搜索栏-->
  <el-row class="row">
    <el-col :span="24">
      <el-form inline :model="searchObj">
<!--        <el-form-item label-->
        <el-form-item style="margin-top: 10px">
          <el-select v-model="searchObj.field" placeholder="Select" style="width: 100px">
            <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
          <el-input v-model="searchObj.keyword" style="width: 200px;left: 20px;"></el-input>
        </el-form-item>
        <el-form-item style="margin-top: 10px;margin-right: 10px">
          <el-button @click="doSearch" type="primary">搜索</el-button>
        </el-form-item>
        <el-form-item style="margin-top: 10px;">
          <el-button @click="clearSearch" type="primary">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
  <!--  工具-->
  <el-row class="row">
    <el-col :span="24">
      <el-button-group>
        <el-button @click="showAddWin" type="primary" >新增</el-button>
        <el-button type="danger">删除</el-button>
        <el-button type="primary">导出</el-button>
      </el-button-group>
    </el-col>
  </el-row>
  <!-- 列表 -->
  <el-row class="row">
    <el-col :span="24">
      <!-- 通过：data 绑定users数据，-->
      <el-table
          :data="clients"
          style="width: 100%"
          stripe
          border
      >
        <!-- 通过el-table-column 申明这个数据表中的每一列数据 -->
        <el-table-column type="selection" width="55" /><!-- 复选框 -->
        <!-- 角色编号  prop对应属性名，label设置列名-->
        <el-table-column type="index" label="#" width="50"/>
        <el-table-column prop="clientName" label="客户姓名" min-width="30%"/>
        <el-table-column prop="phoneNumber" label="手机号码"/>
        <el-table-column prop="email" label="邮箱"/>
        <el-table-column prop="address" label="地址" show-overflow-tooltip/>
        <el-table-column  label="操作">
          <template #default="scope">
            <el-button type="primary" size="small">ta的订单</el-button>
            <el-button @click="toEdit(scope.row.clientId)" type="primary" size="small">编辑</el-button>
            <el-popconfirm
              width="220"
              confirm-button-text="是"
              cancel-button-text="否"
              title="是否确定删除该数据？"
              @confirm="doDelete(scope.row.clientId)"
            >
              <template #reference>
                <el-button type="danger" size="small">删除</el-button>
              </template>
            </el-popconfirm>

          </template>
        </el-table-column>
      </el-table>
    </el-col>
  </el-row>
  <!-- 分页栏 -->
  <el-row class="row">
    <el-col :span="24">
      <el-pagination
          v-model:current-page="searchObj.pageNum"
          v-model:page-size="searchObj.pageSize"
          small
          background
          layout="prev, pager, next"
          :total="total"
          @current-change="handlePageChange"
          class="mt-4"
      />
    </el-col>
  </el-row>
  <!-- 添加客户窗口 -->
  <el-dialog v-model="addClientDialogVisible" title="新增客户" :width="'40%'">
      <el-form :model="newClient">
        <el-form-item label="客户名称" :label-width="formLabelWidth">
          <el-input v-model="newClient.clientName" style="width: 100px" autocomplete="off" />
        </el-form-item>
        <el-form-item label="电话号码" :label-width="formLabelWidth">
          <el-input v-model="newClient.phoneNumber" style="width: 300px" type="textarea" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="邮箱" :label-width="formLabelWidth">
          <el-input v-model="newClient.email" style="width: 300px" type="textarea" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="地址" :label-width="formLabelWidth">
          <el-input v-model="newClient.address" style="width: 300px" type="textarea" autocomplete="off"/>
        </el-form-item>
      </el-form>
      <template #footer>
  <span class="dialog-footer">
  <el-button @click="cancelAddClient">取消</el-button>
  <el-button type="primary" @click="doSubmit">提交</el-button>
  </span>
      </template>
    </el-dialog>
  <!-- 编辑客户的弹窗部分 -->
  <el-dialog v-model="updateClientDialogVisible" title="编辑客户" :width="'40%'">
    <el-form :model="updateClient">
        <el-form-item label="客户名称" :label-width="formLabelWidth">
          <el-input v-model="updateClient.clientName" style="width: 100px" autocomplete="off" />
        </el-form-item>
        <el-form-item label="电话号码" :label-width="formLabelWidth">
          <el-input v-model="updateClient.phoneNumber" style="width: 300px" type="textarea" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="邮箱" :label-width="formLabelWidth">
          <el-input v-model="updateClient.email" style="width: 300px" type="textarea" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="地址" :label-width="formLabelWidth">
          <el-input v-model="updateClient.address" style="width: 300px" type="textarea" autocomplete="off"/>
        </el-form-item>
    </el-form>
    <template #footer>
      <!-- 窗体的最下面的部分，一般都是一些按钮 -->
      <span class="dialog-footer">
      <el-button @click="cancelUpdateClient">取消</el-button>
      <el-button type="primary" @click="doUpdateSubmit">提交</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import {ref,onMounted} from "vue";
import axios from "axios";
import { ElMessage} from "element-plus";

let searchObj = ref({
  field: "",   // 对应下拉框选项，比如 "clientName" / "phoneNum"
  keyword: "",
  pageNum: 1,
  pageSize: 10
});
const total = ref(0);
let clients = ref([]);
const addClientDialogVisible = ref(false);
const updateClientDialogVisible = ref(false);
const newClient = ref({});
const updateClient = ref({});
const options = ref([
  { label: '客户名称', value: 'clientName' },
  { label: '电话号码', value: 'phoneNumber' }
]);

// 显示更新窗口
function showUpdateWin(){
  updateClientDialogVisible.value = true;
}
// 取消显示客户更新
function cancelUpdateClient(){
  updateClientDialogVisible.value = false;
  updateClient.value = {};
}
// 显示添加窗口
function showAddWin(){
  addClientDialogVisible.value = true;
}
// 取消显示客户添加
function cancelAddClient(){
  addClientDialogVisible.value = false;
  newClient.value = {};
}
// 重置搜索
function clearSearch(){
  loadClients();
  searchObj.value.keyword = "";
}
function doSearch() {
  let params = {};
  // 前端判断要传什么
  if (searchObj.value.field === "clientName") {
    params.clientName = searchObj.value.keyword;
  } else if (searchObj.value.field === "phoneNumber") {
    params.phoneNumber = searchObj.value.keyword;
  }
  params.pageSize = searchObj.value.pageSize;
  params.pageNum = searchObj.value.pageNum;
  axios.get("http://localhost:9090/client/list", { params }).then(res => {
    if (res.data.code === 200) {
      clients.value = res.data.data;
      total.value = res.data.total;
    }
  });
}
// 删除按钮确认的事件
function doDelete(clientId){
  // 根据ID删除
  axios.get("http://localhost:9090/client/remove?clientId=" + clientId).then(res=>{
    loadClients();
  });
  ElMessage({
    type:"success",
    message:"删除成功！"
  });
}
// 每一行编辑按钮的事件
function toEdit(clientId){
  axios.get("http://localhost:9090/client/get?clientId=" + clientId).then(res=>{
    // 这里的res.data就是我们接口响应的结果
    updateClient.value = res.data.data;
    // 弹出窗体
    updateClientDialogVisible.value = true;
  })
}
// 编辑角色的提交事件
function doUpdateSubmit(){
  // 提交修改请求
  axios.post("http://localhost:9090/client/update",updateClient.value).then(res=>{
    if(res.data.code == '200'){
      ElMessage({
        type:"success",
        message:"修改成功"
      });
      // 关闭窗体
      cancelUpdateClient();
      // 刷新列表数据
      loadClients();
    }else{
      ElMessage({
        type:"error",
        message:"修改失败"
      });
    }
  });
}
// 新增的提交按钮事件
function doSubmit(){
// 通过axios提交数据即可
  axios.post("http://localhost:9090/client/add",newClient.value).then(res=>{
    if(res.data.code == '200'){
      ElMessage({
        type:"success",
        message:"新增客户成功"
      });
      cancelAddClient();
      // 刷新数据列表
      loadClients();
    }else{
      ElMessage({
        type:"error",
        message:"新增客户失败"
      });
    }
  });
}
function loadClients(){
  axios.get("http://localhost:9090/client/list?pageNum="
        + searchObj.value.pageNum + "&pageSize=" + searchObj.value.pageSize).then(res=>{
    if(res.data.code == '200'){
      clients.value = res.data.data;
      total.value = res.data.total;
    }
  });
}
function handlePageChange(page){
  searchObj.pageNum = page;
  loadClients();
}
onMounted(() => {
  loadClients();
})
</script>

<style scoped>
.row{
  margin-left: 10px;
  margin-top: 10px;
  align-items: center;
}

</style>