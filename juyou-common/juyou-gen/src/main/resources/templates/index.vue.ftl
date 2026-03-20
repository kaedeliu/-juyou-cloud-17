<template>
  <div class="v-list">
    <el-form ref="form" class="form" :model="queryData" label-width="80px">
      <sysFormItem
        v-for="(obj, i) in queryList"
        :key="i"
        :type="obj.type"
        :label="obj.label"
        :prop="obj.prop"
        :option="obj.option"
        :option_dict="obj.option_dict"
        :option_http="obj.option_http"
        :placeholder="obj.message"
        :val="obj.val"
        :lab="obj.lab"
        v-model="queryData[obj.prop]"
        class="formItem"
        size="small"
      ></sysFormItem>
      <el-form-item>
        <el-button type="primary" @click="queryBtn">查询</el-button>
        <el-button type="primary" @click="resetBtn">重置</el-button>
        <el-button type="primary" @click="addBtn">添加</el-button>
      </el-form-item>
    </el-form>

    <!-- 渲染列表 -->
    <el-table class="table" :height="400" v-loading="tableLoading" :data="tableData" border>
      <sys-table-column
        v-for="(obj, i) in tableList"
        :key="i"
        :prop="obj.prop"
        align="center"
        :label="obj.label"
        :width="obj.width"
        :type="obj.type"
        :option="obj.option"
      ></sys-table-column>
      <el-table-column label="操作" width="100" fixed="right" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="editBtn(scope.row)">编辑</el-button>
          <el-button size="mini" type="text" @click="removeBtn(scope.row)" style="color:red">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <div class="block">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        @prev-click="previousPage"
        @next-click="nextPage"
        :current-page.sync="currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageNumber"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      ></el-pagination>
    </div>
    <!-- 添加 编辑 弹出框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose"
    >
      <popup :popupData="popupData" ref="popup" @close="handleClose" @getList="queryBtn"></popup>
    </el-dialog>
  </div>
</template>

<#if customTable[table.name]??>
      <#assign customTableData=customTable[table.name]/>
</#if>


<script>
import sysFormItem from "@comp/sysFormItem.vue";
import sysTableColumn from "@comp/sysTableColumn.vue";
import popup from "./popup.vue";
export default {
  components: { sysFormItem, sysTableColumn, popup },
  name: "sysrole",
  data() {
    return {
      queryList: [
         <#list table.fields as field>
            <#if customTableData?? && customTableData[field.annotationColumnName]??>
                 <#assign customField=customTableData[field.annotationColumnName]/>
            <#else>
                 <#assign customField=""/>
            </#if>
              <#if customField?? && customField!="" && customField.queryColumns?? && customField.queryColumns==1>
                { label: "${customField.fieldDesc!}", prop: "${field.propertyName}" ,type:"${customField.outType}" <#if customField.ditcType??>,option: "${customField.ditcType}"</#if>},
              </#if>
        </#list>
      ], //查询表单 表头
      queryData: {}, //查询表单数据
      tableList: [
        <#list table.fields as field>
                  <#if customTableData?? && customTableData[field.annotationColumnName]??>
                       <#assign customField=customTableData[field.annotationColumnName]/>
                  <#else>
                       <#assign customField=""/>
                  </#if>
                  <#if customField?? && customField!="" && customField.listColumns?? && customField.listColumns==1>
                      { label: "${customField.fieldDesc!}", prop: "${field.propertyName}" ,type:"${customField.outType}" <#if customField.ditcType??>,option: "${customField.ditcType}"</#if>},
                  </#if>
               </#list>
      ], //列表 表头
      tableData: [], //列表数据
      tableLoading: false, //列表加载状态
      total: 0, //分页 数据总条数
      page: 1, //分页 页码
      currentPage: 1, //当前页
      pageNumber: 10, //分页 每页条数
      dialogTitle: "添加", //弹出框标题
      dialogVisible: false, //弹出框 显示状态
      popupData: {}
    };
  },
  methods: {
    // 分页的四个方法
    handleSizeChange(val) {
      this.pageNumber = val;
      this.getList();
    },
    // 上一页
    previousPage() {
      if (this.page > 0) {
        this.page--;
      } else {
        return;
      }
    },
    // 下一页
    nextPage() {
      if (this.page++ < this.total) {
        this.page++;
      } else {
        return;
      }
    },
    handleCurrentChange(val) {
      this.page = val;
      this.getList();
    },
    //   查询按钮
    queryBtn() {
      this.page = 1;
      this.currentPage = 1;
      this.getList();
    },
    //   重置按钮
    resetBtn() {
      this.queryData = {};
      this.page = 1;
      this.currentPage = 1;
      this.pageNumber = 10;
    },
    // 编辑按钮
    editBtn(data) {
      this.dialogTitle = "编辑";
      let params = {
        id: data.<#list table.fields as field><#if field.keyFlag>${field.propertyName}</#if></#list>
      };
      this.$http.sysrole.editLook(params).then(res => {
        this.popupData = { ...res.result };
        this.dialogVisible = true;
        this.$nextTick(() => {
          this.$refs.popup.init();
        });
      });
    },
    // 删除按钮
    removeBtn(data) {
      this.$confirm("此操作将永久删除该数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.remove(data);
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    // 删除
    remove(data) {
      let params = {
        id: data.<#list table.fields as field><#if field.keyFlag>${field.propertyName}</#if></#list>
      };
      this.$http.sysrole.remove(params).then(res => {
        if (res.success) {
          this.queryBtn();
        }
      });
    },
    // 添加按钮
    addBtn() {
      this.dialogTitle = "添加";
      this.dialogVisible = true;
      this.$nextTick(() => {
                this.$refs.popup.init();
             });
    },
    // 关闭弹出框
    handleClose() {
      this.popupData = {};
      this.dialogVisible = false;
    },
    // 获取列表数据
    getList() {
      this.tableLoading = true;
      let params = {
        ...this.queryData,
        pageNo: this.page,
        pageSize: this.pageNumber
      };
      this.$http.sysrole
        .getList(params)
        .then(res => {
          if (res.success) {
            this.tableLoading = false;
            this.total = res.result.total;
            this.tableData = res.result.records;
          } else {
            this.tableLoading = false;
          }
        })
        .catch(error => {
          this.tableLoading = false;
          console.log(error);
        });
    }
  },
  mounted() {
    this.getList();
  }
};
</script>

<style lang="scss" scoped>
.v-list {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 20px;
  box-sizing: border-box;
  .form {
    display: flex;
    flex-wrap: wrap;
    .formItem {
      width: 240px;
    }
  }

  .table {
    flex: 1;
  }
}
</style>