<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="名称" prop="name">
      <el-input v-model="dataForm.name" placeholder="名称"></el-input>
    </el-form-item>
    <el-form-item label="父分类id" prop="parentId">
      <el-input v-model="dataForm.parentId" placeholder="父分类id"></el-input>
    </el-form-item>
    <el-form-item label="层级" prop="catLevel">
      <el-input v-model="dataForm.catLevel" placeholder="层级"></el-input>
    </el-form-item>
    <el-form-item label="0不显示，1显示]" prop="isShow">
      <el-input v-model="dataForm.isShow" placeholder="0不显示，1显示]"></el-input>
    </el-form-item>
    <el-form-item label="排序" prop="sort">
      <el-input v-model="dataForm.sort" placeholder="排序"></el-input>
    </el-form-item>
    <el-form-item label="图标" prop="icon">
      <el-input v-model="dataForm.icon" placeholder="图标"></el-input>
    </el-form-item>
    <el-form-item label="统计单位" prop="proUnit">
      <el-input v-model="dataForm.proUnit" placeholder="统计单位"></el-input>
    </el-form-item>
    <el-form-item label="商品数量" prop="proCount">
      <el-input v-model="dataForm.proCount" placeholder="商品数量"></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          id: 0,
          name: '',
          parentId: '',
          catLevel: '',
          isShow: '',
          sort: '',
          icon: '',
          proUnit: '',
          proCount: ''
        },
        dataRule: {
          name: [
            { required: true, message: '名称不能为空', trigger: 'blur' }
          ],
          parentId: [
            { required: true, message: '父分类id不能为空', trigger: 'blur' }
          ],
          catLevel: [
            { required: true, message: '层级不能为空', trigger: 'blur' }
          ],
          isShow: [
            { required: true, message: '0不显示，1显示]不能为空', trigger: 'blur' }
          ],
          sort: [
            { required: true, message: '排序不能为空', trigger: 'blur' }
          ],
          icon: [
            { required: true, message: '图标不能为空', trigger: 'blur' }
          ],
          proUnit: [
            { required: true, message: '统计单位不能为空', trigger: 'blur' }
          ],
          proCount: [
            { required: true, message: '商品数量不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/commodity/category/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.name = data.category.name
                this.dataForm.parentId = data.category.parentId
                this.dataForm.catLevel = data.category.catLevel
                this.dataForm.isShow = data.category.isShow
                this.dataForm.sort = data.category.sort
                this.dataForm.icon = data.category.icon
                this.dataForm.proUnit = data.category.proUnit
                this.dataForm.proCount = data.category.proCount
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/commodity/category/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'name': this.dataForm.name,
                'parentId': this.dataForm.parentId,
                'catLevel': this.dataForm.catLevel,
                'isShow': this.dataForm.isShow,
                'sort': this.dataForm.sort,
                'icon': this.dataForm.icon,
                'proUnit': this.dataForm.proUnit,
                'proCount': this.dataForm.proCount
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
