<template>
	<view class="login">
		<uni-section title="表单校验" type="line">
			<view class="example">
				<!-- 基础表单校验 -->
				<uni-forms ref="valiForm" :rules="rules" :modelValue="valiFormData" label-position="top">
					<uni-forms-item label="邮箱" required name="userEmail">
						<uni-easyinput v-model="valiFormData.userEmail" placeholder="请输入邮箱" />
					</uni-forms-item>
					<uni-forms-item label="密码" required name="userPassword">
						<uni-easyinput v-model="valiFormData.userPassword" placeholder="请输入密码" />
					</uni-forms-item>
					<uni-forms-item label="验证码" required name="code">
						<uni-easyinput v-model="valiFormData.code" placeholder="请输入验证码" />
					</uni-forms-item>
				</uni-forms>
				<button type="primary" @click="submit('valiForm')">提交</button>
			</view>
		</uni-section>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				// 校验表单数据
				valiFormData: {
					userEmail: '',
					userPassword: '',
				},
				// 校验规则
				rules: {
					userEmail: {
						rules: [{
							required: true,
							errorMessage: '邮箱不能为空'
						}]
					},
					userPassword: {
						rules: [{
							required: true,
							errorMessage: '密码不能为空'
						}]
					},
					code: {
						rules: [{
							required: true,
							errorMessage: '验证码不能为空'
						}]
					}
				}
			}
		},

		methods: {

			//submit函数
			submit(ref) {
				this.$refs[ref].validate().then(res => {
					let user = {
						"userEmail": this.valiFormData.userEmail,
						"userPassword": this.valiFormData.userPassword,
						"code": this.valiFormData.code
					};
					console.log('user', user);

					uni.request({
						url: '/api/user/register',
						method: 'POST',
						dataType: 'json',
						data: user,
						success(res) {
							console.log('调用成功',res);
						},
						fail: (res) => { //如果访问接口失败就会进入fail
							console.log(res.errMsg)
							console.log("调用接口失败")
						}
					})
					console.log('success', res);
					uni.showToast({
						title: `校验通过`
					})
				}).catch(err => {
					console.log('err', err);
				})
			},
		}
	}
</script>


<style lang="scss" scoped>

</style>