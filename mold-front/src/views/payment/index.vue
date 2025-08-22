<template>
  <!--  页头-->
  <el-row class="row">
    <el-col :span="24">
      <el-page-header :icon="ArrowLeft">
        <template #content>
          <span class="text-large font-600 mr-3"> 流水管理 </span>
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
          <el-select v-model="searchObj.field" placeholder="全部" style="width: 100px">
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
        <el-form-item style="margin-top: 10px;">
          <el-button type="primary">导出</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
  <!-- 列表 -->
  <el-row class="row">
    <el-col :span="20">
      <!-- 通过：data 绑定users数据，-->
      <el-table
          :data="payments"
          style="width: 100%"
          stripe
          border
      >
        <!-- 通过el-table-column 申明这个数据表中的每一列数据 -->
        <el-table-column type="selection" width="55" /><!-- 复选框 -->
<!--        <el-table-column type="index" label="#"/>-->
        <el-table-column prop="orderId" label="订单编号" min-width="100"/>
        <el-table-column prop="clientName" label="客户名称"/>
        <el-table-column prop="amount" label="支付金额"/>
        <el-table-column prop="payWay" label="付款方式"/>
        <el-table-column prop="payTime" label="付款时间" min-width="110"/>
        <el-table-column  label="操作" fixed="right">
          <template #default="scope">
            <el-button @click="toEdit(scope.row.id)" type="primary" size="small">编辑</el-button>
            <el-popconfirm
                width="220"
                confirm-button-text="是"
                cancel-button-text="否"
                title="是否确定删除该数据？"
                @confirm="doDelete(scope.row.id)"
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
  <!-- 编辑付款记录的弹窗部分 -->
  <el-dialog v-model="updatePaymentDialogVisible" title="编辑付款记录" :width="'40%'">
    <el-form :model="updatePayment">
      <el-form-item label="支付金额" :label-width="formLabelWidth">
        <el-input-number v-model="updatePayment.amount" style="width: 200px" type="textarea" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="付款方式" :label-width="formLabelWidth">
        <el-radio-group v-model="updatePayment.payWay">
          <el-radio value="现金">现金</el-radio>
          <el-radio value="承兑汇票">承兑汇票</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <!-- 窗体的最下面的部分，一般都是一些按钮 -->
      <span class="dialog-footer">
      <el-button @click="cancelUpdatePayment">取消</el-button>
      <el-button type="primary" @click="doUpdateSubmit">提交</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import {ref, onMounted} from "vue";
import axios from "axios";
import {ElMessage} from "element-plus";

const payments = ref([]);
const total = ref(0);
const options = ref([
  { label: '订单编号', value: 'orderId' },
  { label: '客户名称', value: 'clientName' }
]);
let searchObj = ref({
  field: "",
  keyword: "",
  pageNum: 1,
  pageSize: 10
});
const updatePaymentDialogVisible = ref(false);
const updatePayment = ref({});
const clients = ref([]);

// 显示更新窗口
function showUpdateWin(){
  updatePaymentDialogVisible.value = true;
}
// 取消显示流水更新窗口
function cancelUpdatePayment(){
  updatePaymentDialogVisible.value = false;
  updatePayment.value = {};
}
// 重置搜索
function clearSearch(){
  loadPayments();
  searchObj.value.keyword = "";
}
function doSearch() {
  let params = {};
  // 前端判断要传什么
  if (searchObj.value.field === "clientName") {
    params.clientName = searchObj.value.keyword;
  } else if (searchObj.value.field === "orderId") {
    params.orderId = searchObj.value.keyword;
  }
  params.pageSize = searchObj.value.pageSize;
  params.pageNum = searchObj.value.pageNum;
  axios.get("http://localhost:9090/payment/list", { params }).then(res => {
    if (res.data.code === 200) {
      payments.value = res.data.data;
      total.value = res.data.total;
    }
  });
}
// 删除按钮确认的事件
function doDelete(id){
  // 根据ID删除
  axios.get("http://localhost:9090/payment/remove?id=" + id).then(res=>{
    loadPayments();
  });
  ElMessage({
    type:"success",
    message:"删除成功！"
  });
}
// 每一行编辑按钮的事件
function toEdit(id){
  axios.get("http://localhost:9090/payment/get?id=" + id).then(res=>{
    // 这里的res.data就是我们接口响应的结果
    updatePayment.value = res.data.data;
    // 弹出窗体
    updatePaymentDialogVisible.value = true;
  })
}
// 编辑角色的提交事件
function doUpdateSubmit(){
  // 提交修改请求
  axios.post("http://localhost:9090/payment/update",updatePayment.value).then(res=>{
    if(res.data.code == '200'){
      ElMessage({
        type:"success",
        message:"修改成功"
      });
      // 关闭窗体
      cancelUpdatePayment();
      // 刷新列表数据
      loadPayments();
    }else{
      ElMessage({
        type:"error",
        message:"修改失败"
      });
    }
  });
}
function loadPayments(){
  axios.get("http://localhost:9090/payment/list?pageNum="
      + searchObj.value.pageNum + "&pageSize=" + searchObj.value.pageSize).then(res=>{
    if(res.data.code == '200'){
      payments.value = res.data.data;
      total.value = res.data.total;
    }
  });
}
function handlePageChange(page){
  searchObj.pageNum = page;
  loadPayments();
}
function loadClients(){
  axios.get("http://localhost:9090/client/list").then(res =>{
    if(res.data.code == '200'){
      clients.value = res.data.data;
    }
  })
}


onMounted(() => {
  loadPayments();
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