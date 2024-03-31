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
				</uni-forms>
				<button type="primary" @click="submit('valiForm')">登录</button>
				<button class="button popup-success" @click="messageToggle('success')"><text
						class="button-text success-text">成功</text></button>
			</view>
		</uni-section>
		<view>
			<!-- 提示信息弹窗 -->
			<uni-popup ref="message" type="message">
				<uni-popup-message :type="msgType" :message="messageText" :duration="2000"></uni-popup-message>
			</uni-popup>
		</view>
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
						}, {
							format: 'email',
							errorMessage: '请正确填写邮箱'
						}]
					},
					userPassword: {
						rules: [{
							required: true,
							errorMessage: '密码不能为空'
						}]
					}
				},
				type: 'center',
				msgType: 'success',
				messageText: ''
			}
		},

		methods: {

			messageToggle(type) {
				this.msgType = type
				this.messageText = `登录成功`
				this.$refs.message.open()
			},

			//submit函数
			submit(ref) {
				this.$refs[ref].validate().then(res => {

					console.log(this.messageToggle(this.msgType));

					let user = {
						"userEmail": this.valiFormData.userEmail,
						"userPassword": this.valiFormData.userPassword
					};

					uni.request({
						url: '/api/user/login',
						method: 'POST',
						dataType: 'json',
						data: user,
						success: (res) => {
							this.messageToggle(this.msgType);
							console.log('调用成功', res.data.data);

							uni.setStorageSync('userId',res.data.data.userId);


							uni.navigateTo({
								url: '/pages/user/info',
								animationType: 'pop-in',
								animationDuration: 300
							});
						},
						fail: (res) => { //如果访问接口失败就会进入fail
							console.log(res.errMsg)
							console.log("调用接口失败")
						}
					})

					console.log('success', res);
					/* 					uni.showToast({
											title: `校验通过`
										}) */
				}).catch(err => {
					console.log('err', err);
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	
</style>