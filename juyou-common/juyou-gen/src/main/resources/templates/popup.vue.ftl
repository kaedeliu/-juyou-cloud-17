<template>
  <div class="v-popup">
    <el-form :model="formData" :rules="rules" ref="formData" label-width="100px" class="formData">
      <sysFormItem
        v-for="(obj, i) in formList"
        :key="i"
        :type="obj.type"
        :label="obj.label"
        :prop="obj.prop"
        :option="obj.option"
        :option_dict="obj.option_dict"
        :option_http="obj.option_http"
        :placeholder="obj.placeholder"
        :val="obj.val"
        :lab="obj.lab"
        v-model="formData[obj.prop]"
        class="formItem"
        size="small"
      ></sysFormItem>
    </el-form>
    <div class="btn_box">
      <el-button class="btn" type="primary" @click="submitBtn">确定</el-button>
      <el-button class="btn" @click="close">取消</el-button>
    </div>
  </div>
</template>

<#if customTable[table.name]??>
      <#assign customTableData=customTable[table.name]/>
</#if>

<script>
import sysFormItem from "@comp/sysFormItem.vue";
export default {
  components: { sysFormItem },
  props: {
    popupData: {
      type: Object,
      default: () => {
        return {};
      }
    }
  },
  name: "",
  data() {
    return {
      formData: {},
      formList: [
           <#list table.fields as field>
             <#if customTableData?? && customTableData[field.annotationColumnName]??>
                  <#assign customField=customTableData[field.annotationColumnName]/>
             <#else>
                  <#assign customField=""/>
             </#if>
             <#if customField?? && customField!="" && ((customField.addColumns?? && customField.addColumns==1) || ( customField.editColumns?? && customField.editColumns==1))>
                   { label: "${customField.fieldDesc!}", prop: "${field.propertyName}" ,type:"${customField.outType}" <#if customField.ditcType??>,option: "${customField.ditcType}"</#if>},
             </#if>
          </#list>
      ],
      rules: {
      <#list table.fields as field>
                <#if customTableData?? && customTableData[field.annotationColumnName]??>
                     <#assign customField=customTableData[field.annotationColumnName]/>
                <#else>
                     <#assign customField=""/>
                </#if>
                <#if customField?? && customField!="" && ((customField.addColumns?? && customField.addColumns==1) || ( customField.editColumns?? && customField.editColumns==1))
                  && ((customField.notNullColumns?? && customField.notNullColumns==1) || (customField.regExp??))>
                      ${field.propertyName}:[
                          <#if customField.notNullColumns?? && customField.notNullColumns==1>
                              { required: true, message: "请输入${customField.fieldDesc!}", trigger: "blur" }
                          </#if>
                          <#if customField.regExp??>
                              <#if customField.notNullColumns?? && customField.notNullColumns==1>,</#if>
                              {
                                  validator: (rule, value, callback) => {
                                    validator(rule, value, callback, '${customField.regExp}', '${customField.regMsg!'${customField.fieldDesc!}输入错误'}');
                                  },
                                  trigger: "blur"
                              }
                          </#if>
                      ],
                </#if>
             </#list>
      }
    };
  },
  methods: {
    init() {
      this.formData = {};
      this.formData = { ...this.popupData };
      console.log(this.formData);
      this.$refs.formData.clearValidate();
      
    },
    submitBtn() {
      this.$refs.formData.validate(valid => {
        if (valid) {
          if (this.formData.<#list table.fields as field><#if field.keyFlag>${field.propertyName}</#if></#list>) {
            this.$http.sysrole.edit({ ...this.formData }).then(res => {
              if (res.success) {
                this.formData = {};
                this.$emit("close");
                this.$emit("getList");
              }
            });
          } else {
            this.$http.sysrole.add({ ...this.formData }).then(res => {
              if (res.success) {
                this.formData = {};
                this.$emit("close");
                this.$emit("getList");
              }
            });
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    close() {
      this.formData = {};
      this.$emit("close");
    }
  },
  mounted() {}
};
</script>

<style lang="scss" scoped>
.v-popup {
  .formData {
    padding: 20px;
  }
}
</style>