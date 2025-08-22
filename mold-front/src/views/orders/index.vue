<template>
  <!--  页头-->
  <el-row class="row">
    <el-col :span="24">
      <el-page-header :icon="ArrowLeft">
        <template #content>
          <span class="text-large font-600 mr-3"> 订单管理 </span>
        </template>
      </el-page-header>
    </el-col>
  </el-row>
  <!--  搜索栏-->
  <el-row class="row">
    <el-form :inline="true" :model="searchObj" class="search-form">
      <!-- 客户名 -->
      <el-form-item label="客户">
        <el-input v-model="searchObj.clientName" placeholder="客户名" clearable/>
      </el-form-item>
      <!-- 下单日期范围 -->
      <el-form-item label="日期">
        <el-date-picker
            v-model="searchObj.dateRange"
            type="daterange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            clearable
        />
      </el-form-item>
      <!-- 单价 -->
      <el-form-item label="单价范围">
        <el-input v-model="searchObj.unitPrice.st" :min="0" style="width:50px"/>
        <span style="margin: 0 3px;">-</span>
        <el-input v-model="searchObj.unitPrice.ed" :min="0" style="width:50px"/>
      </el-form-item>
      <!-- 数量 -->
      <el-form-item label="数量">
        <el-input v-model="searchObj.workPieceNum.st" :min="0" style="width:50px"/>
        <span style="margin: 0 3px;">-</span>
        <el-input v-model="searchObj.workPieceNum.ed" :min="0" style="width:50px"/>
      </el-form-item>
      <!-- 付款状态 -->
      <el-form-item label="付款状态">
        <el-select v-model="searchObj.paymentStatus" placeholder="全部" clearable style="width: 120px">
          <el-option label="已付款" value="已付款"/>
          <el-option label="未付款" value="未付款"/>
        </el-select>
      </el-form-item>
      <!-- 订单状态 -->
      <el-form-item label="订单状态">
        <el-select v-model="searchObj.status" placeholder="全部" clearable style="width: 120px">
          <el-option label="进行中" value="进行中"/>
          <el-option label="完成" value="完成"/>
          <el-option label="未开始" value="未开始"/>
        </el-select>
      </el-form-item>
      <!-- 付款方式 -->
      <el-form-item label="付款方式">
        <el-select v-model="searchObj.payWay" placeholder="全部" clearable style="width: 120px">
          <el-option label="现金" value="现金"/>
          <el-option label="承兑汇票" value="承兑汇票"/>
        </el-select>
      </el-form-item>
      <!-- 按钮 -->
      <el-form-item>
        <el-button type="primary" @click="doSearch">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>
  </el-row>
  <!--  工具-->
  <el-row class="row">
    <el-col :span="24">
      <el-button-group>
        <el-button @click="showAddWin" type="primary">新增</el-button>
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
          :data="orders"
          style="width: 100%"
          stripe
          border
      >
        <!-- 通过el-table-column 申明这个数据表中的每一列数据 -->
        <el-table-column type="selection" width="55" /><!-- 复选框 -->
         角色编号  prop对应属性名，label设置列名
        <el-table-column type="index" label="#"/>
        <el-table-column prop="orderId" label="订单编号" min-width="165"/>
        <el-table-column prop="clientName" label="客户"/>
        <el-table-column prop="partNum" label="零件号"/>
        <el-table-column prop="nameArticle" label="品名/规格" min-width="100"/>
        <el-table-column prop="process" label="工序"/>
        <el-table-column prop="moldType" label="类型"/>
        <el-table-column prop="unitPrice" label="单价"/>
        <el-table-column prop="workPieceNum" label="数量"/>
        <el-table-column prop="totalAmount" label="总金额"/>
        <el-table-column prop="paymentStatus" label="付款状态" min-width="100">
          <template #default="scope">
            <el-tag :type="scope.row.paymentStatus === '已付款' ? 'success' : 'danger'">
              {{ scope.row.paymentStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="payWay" label="付款方式" min-width="100"/>
        <el-table-column prop="status" label="订单状态" min-width="100">
          <template #default="scope">
            <el-tag>{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="200"/>
        <el-table-column prop="completeTime" label="付款时间" min-width="200"/>
        <el-table-column  label="操作" fixed="right" min-width="250">
          <template #default="scope">
            <el-button @click="showAddPaymentWin([scope.row.orderId,scope.row.clientName])" type="primary" size="small">添加流水</el-button>
            <el-button @click="toEdit(scope.row.orderId)" type="primary" size="small">编辑</el-button>
            <el-popconfirm
                width="220"
                confirm-button-text="是"
                cancel-button-text="否"
                title="是否确定删除该数据？"
                @confirm="doDelete(scope.row.orderId)"
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
  <!--  添加订单窗口-->
  <el-dialog v-model="addOrderDialogVisible"
             title="新增订单"
             :width="'40%'">
    <el-form :model="newOrder" label-width="auto" style="max-width: 600px">
      <el-form-item label="零件号" style="width: 300px">
        <el-input v-model="newOrder.partNum" />
      </el-form-item>
      <el-form-item label="客户选择" style="width: 200px">
        <el-select v-model="newOrder.clientName">
          <el-option
              v-for="option in clients"
              :key="option.clientId"
              :label="option.clientName"
              :value="option.clientName"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="品名/规格" style="width: 200px">
        <el-select v-model="newOrder.nameArticle">
          <el-option label="模仁" value="模仁" />
          <el-option label="电极" value="电极" />
          <el-option label="滑块" value="滑块" />
          <el-option label="镶件" value="镶件" />
          <el-option label="配件" value="配件" />
        </el-select>
      </el-form-item>
      <el-form-item label="加工工序" style="width: 300px">
        <el-select v-model="newOrder.process">
          <el-option label="CNC" value="CNC" />
          <el-option label="放电" value="放电" />
          <el-option label="线切割" value="线切割" />
          <el-option label="全加工" value="全加工" />
        </el-select>
      </el-form-item>
      <el-form-item label="工件单价" style="width: 300px">
        <el-input v-model="newOrder.unitPrice" />
      </el-form-item>
      <el-form-item label="工件数量" style="width: 200px">
        <el-input v-model="newOrder.workPieceNum" />
      </el-form-item>
      <el-form-item label="类型">
        <el-radio-group v-model="newOrder.moldType">
          <el-radio value="修模">修模</el-radio>
          <el-radio value="新模">新模</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="付款状态">
        <el-radio-group v-model="newOrder.paymentStatus">
          <el-radio value="已付款">已付款</el-radio>
          <el-radio value="未付款">未付款</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="付款方式">
        <el-radio-group v-model="newOrder.payWay">
          <el-radio value="现金">现金</el-radio>
          <el-radio value="承兑汇票">承兑汇票</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="订单状态">
        <el-radio-group v-model="newOrder.status">
          <el-radio value="进行中">进行中</el-radio>
          <el-radio value="未开始">未开始</el-radio>
          <el-radio value="完成">完成</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="doSubmit">确定</el-button>
        <el-button @click="hideAddWin">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
  <!--  编辑窗口-->
  <el-dialog v-model="updateOrderDialogVisible"
             title="编辑订单"
             :width="'40%'">
    <el-form :model="updateOrder" label-width="auto" style="max-width: 600px">
      <el-form-item label="零件号" style="width: 300px">
        <el-input v-model="updateOrder.partNum" />
      </el-form-item>
      <el-form-item label="客户选择" style="width: 200px">
        <el-select v-model="updateOrder.clientName">
          <el-option
              v-for="option in clients"
              :key="option.clientId"
              :label="option.clientName"
              :value="option.clientId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="品名/规格" style="width: 200px">
        <el-select v-model="updateOrder.nameArticle">
          <el-option label="模仁" value="模仁" />
          <el-option label="电极" value="电极" />
          <el-option label="滑块" value="滑块" />
          <el-option label="镶件" value="镶件" />
          <el-option label="配件" value="配件" />
        </el-select>
      </el-form-item>
      <el-form-item label="加工工序" style="width: 300px">
        <el-select v-model="updateOrder.process">
          <el-option label="CNC" value="CNC" />
          <el-option label="放电" value="放电" />
          <el-option label="线切割" value="线切割" />
          <el-option label="全加工" value="全加工" />
        </el-select>
      </el-form-item>
      <el-form-item label="工件单价" style="width: 300px">
        <el-input v-model="updateOrder.unitPrice" />
      </el-form-item>
      <el-form-item label="工件数量" style="width: 200px">
        <el-input v-model="updateOrder.workPieceNum" />
      </el-form-item>
      <el-form-item label="类型">
        <el-radio-group v-model="updateOrder.moldType">
          <el-radio value="修模">修模</el-radio>
          <el-radio value="新模">新模</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="付款状态">
        <el-radio-group v-model="updateOrder.paymentStatus">
          <el-radio value="已付款">已付款</el-radio>
          <el-radio value="未付款">未付款</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="付款方式">
        <el-radio-group v-model="updateOrder.payWay">
          <el-radio value="现金">现金</el-radio>
          <el-radio value="承兑汇票">承兑汇票</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="订单状态">
        <el-radio-group v-model="updateOrder.status">
          <el-radio value="进行中">进行中</el-radio>
          <el-radio value="未开始">未开始</el-radio>
          <el-radio value="完成">完成</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="doUpdateSubmit">确定</el-button>
        <el-button @click="hideUpdateWin">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
  <!-- 添加流水窗口 -->
  <el-dialog v-model="addPaymentDialogVisible" title="新增付款记录" :width="'40%'">
    <el-form :model="newPayment">
      <el-form-item label="订单号" :label-width="formLabelWidth">
        <span>{{ newPayment.orderId }}</span>
      </el-form-item>
      <el-form-item label="客户" :label-width="formLabelWidth">
        <span>{{ newPayment.clientName }}</span>
      </el-form-item>
      <el-form-item label="支付金额" :label-width="formLabelWidth">
        <el-input-number v-model="newPayment.amount" style="width: 200px" type="textarea" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="付款方式" :label-width="formLabelWidth">
        <el-radio-group v-model="newPayment.payWay">
          <el-radio value="现金">现金</el-radio>
          <el-radio value="承兑汇票">承兑汇票</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
  <span class="dialog-footer">
  <el-button @click="cancelAddPayment">取消</el-button>
  <el-button type="primary" @click="paymentAddSubmit">提交</el-button>
  </span>
    </template>
  </el-dialog>
</template>

<script setup>
import {ref,onMounted} from "vue";
import axios from "axios";
import {ElMessage} from "element-plus";

const addOrderDialogVisible = ref(false);
const updateOrderDialogVisible = ref(false);
const defaultOrder = ref({
  partNum: "",
  moldType: "新模",
  nameArticle: "模仁",
  process: "CNC",
  paymentStatus: "未付款",
  payWay: "现金",
  status: "未开始"
});
const newOrder = ref({...defaultOrder.value})
const updateOrder =  ref({});
const searchObj = ref({
  clientName: '',
  dateRange: [],
  unitPrice: {st:null,ed:null},
  workPieceNum: {st:null,ed:null},
  paymentStatus: '',
  status: '',
  payWay: '',
  pageNum: 1,
  pageSize: 10
})
const orders = ref([]);
let clients = ref([]);
const total = ref(0);
const addPaymentDialogVisible = ref(false);
const newPayment = ref({
  orderId: "",
  clientName: "",
  payWay: "现金"
});

function showAddWin(){
  addOrderDialogVisible.value = true;
}
function hideAddWin(){
  addOrderDialogVisible.value = false;
  newOrder.value = { ...defaultOrder.value };
}
function hideUpdateWin(){
  updateOrderDialogVisible.value = false;
  updateOrder.value = {};
}
function loadClients(){
  axios.get("http://localhost:9090/client/list").then(res =>{
    if(res.data.code == '200'){
      clients.value = res.data.data;
    }
  })
}
function loadOrders(){
  doSearch();
}
// 删除按钮确认的事件
function doDelete(orderId){
  // 根据ID删除
  axios.get("http://localhost:9090/orders/remove?orderId=" + orderId).then(res=>{
    // 直接刷新数据
    axios.get("http://localhost:9090/orders/list").then(res=>{
      orders.value = res.data.data;
    })
  });
  ElMessage({
    type:"success",
    message:"删除成功！"
  });
}
function doSubmit(){
  newOrder.value.orderId = generateId('ORD');
  // 通过axios提交数据即可
  axios.post("http://localhost:9090/orders/add",newOrder.value).then(res=>{
    if(res.data.code == '200'){
      ElMessage({
        type:"success",
        message:"新增订单成功"
      });
      hideAddWin();
      // 刷新数据列表
      loadOrders();
    }else{
      ElMessage({
        type:"error",
        message:"新增订单失败"
      });
    }
  });
}
// 根据时间戳生成订单号
function generateId(front) {
  const timestamp = new Date().getTime();
  return front + `${timestamp}`;
}
// 编辑角色的提交事件
function doUpdateSubmit(){
  //console.log(updateOrder.value);
  // 提交修改请求
  axios.post("http://localhost:9090/orders/update",updateOrder.value).then(res=>{
    if(res.data.code == '200'){
      ElMessage({
        type:"success",
        message:"修改成功"
      });
      // 关闭窗体
      hideUpdateWin();
      // 刷新列表数据
      loadOrders();
    }else{
      ElMessage({
        type:"error",
        message:"修改失败"
      });
    }
  });
}
// 每一行编辑按钮的事件
function toEdit(orderId){
  axios.get("http://localhost:9090/orders/get?orderId=" + orderId).then(res=>{
    // 这里的res.data就是我们接口响应的结果
    updateOrder.value = res.data.data;
    // 弹出窗体
    updateOrderDialogVisible.value = true;
  })
}
function doSearch(){
  axios.post("http://localhost:9090/orders/search",searchObj.value).then(res=>{
    if(res.data.code == '200'){
      orders.value = res.data.data;
      total.value = res.data.total;
    }
  })
}
function resetSearch(){
  searchObj.value ={
    clientName: '',
    dateRange: [],
    unitPrice: {st:null,ed:null},
    workPieceNum: {st:null,ed:null},
    paymentStatus: '',
    status: '',
    payWay: '',
    pageNum: 1,
    pageSize: 10
  }
  doSearch();
}
function handlePageChange(page){
  searchObj.pageNum = page;
  doSearch();
}
// 取消显示流水添加
function cancelAddPayment(){
  addPaymentDialogVisible.value = false;
  newPayment.value = {
    orderId: "",
    clientName: "",
    payWay: "现金"
  };
}
// 显示添加流水窗口
function showAddPaymentWin(v){
  newPayment.value.orderId = v[0];
  newPayment.value.clientName = v[1];
  addPaymentDialogVisible.value = true;
}
// 提交流水添加
function paymentAddSubmit(){
  newPayment.value.id = generateId('PM');
  axios.post("http://localhost:9090/payment/add",newPayment.value).then(res=>{
    if(res.data.code == '200'){
      ElMessage({
        type:"success",
        message:"添加记录成功"
      });
      cancelAddPayment();
    }else{
      ElMessage({
        type:"error",
        message:"添加记录失败"
      });
    }
  });
}

onMounted(() => {
  loadOrders();
  loadClients();
})
</script>

<style scoped>
.row{
  margin-left: 10px;
  margin-top: 10px;
  align-items: center;
}
:deep(.el-table .cell) {
  white-space: nowrap;
  width: auto;
}

</style>