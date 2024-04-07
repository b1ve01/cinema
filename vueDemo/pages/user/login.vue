<template>
	<view v-if="!isLogin" class="isLogin_false">
		//登录页面
		<view v-if="!isRegister" class="login">
			<view class="login_text">
				<view class="info_login_register">
					<text class="login_text_true" v-if="!isRegister" @click="isRegister=true">登录 | </text>
					<text class="login_text_false" v-if="isRegister" @click="isRegister=false">登录</text>

					<text class="register_text_false" v-if="!isRegister" @click="isRegister=true">注册</text>
					<text class="register_text_true" v-if="isRegister" @click="isRegister=false"> | 注册</text>
				</view>

				<view class="info_icon">
					<uni-icons type="contact-filled" size=150rpx color=#999999></uni-icons>
				</view>
			</view>

			<view class="login_example">
				<!-- 基础表单校验 -->
				<uni-forms ref="loginForm" :rules="rules_login" :modelValue="loginData">
					<uni-forms-item name="userEmail">
						<uni-easyinput prefixIcon="email-filled" v-model="loginData.userEmail" placeholder="请输入邮箱" />
					</uni-forms-item>
					<uni-forms-item name="userPassword">
						<uni-easyinput prefixIcon="locked-filled" type="password" v-model="loginData.userPassword"
							placeholder="请输入密码" />
					</uni-forms-item>
				</uni-forms>
				<button class="false_button" v-if="!loginData.userEmail||!loginData.userPassword" @click="err_submit()"
					hover-class="is_errhover">登录</button>
				<button class="true_button" v-if="loginData.userEmail&&loginData.userPassword"
					@click="submit_login('loginForm')" hover-class="is_hover">登录</button>
			</view>
		</view>

		//注册页面
		<view v-if="isRegister" class="register">
			<view class="register_text">
				<view class="info_login_register">
					<text class="login_text_true" v-if="!isRegister" @click="isRegister=true">登录 | </text>
					<text class="login_text_false" v-if="isRegister" @click="isRegister=false">登录</text>

					<text class="register_text_false" v-if="!isRegister" @click="isRegister=true">注册</text>
					<text class="register_text_true" v-if="isRegister" @click="isRegister=false"> | 注册</text>
				</view>

				<view class="info_icon">
					<uni-icons type="contact-filled" size=150rpx color=#999999></uni-icons>
				</view>
			</view>

			<view class="register_example">
				<!-- 基础表单校验 -->
				<uni-forms ref="registerForm" :rules="rules_register" :modelValue="registerData">
					<uni-forms-item name="userEmail">
						<uni-easyinput prefixIcon="email-filled" v-model="registerData.userEmail" placeholder="请输入邮箱" />
					</uni-forms-item>
					<uni-forms-item name="userPassword">
						<uni-easyinput prefixIcon="locked-filled" type="password" v-model="registerData.userPassword"
							placeholder="请输入密码" />
					</uni-forms-item>
					<uni-forms-item name="userCode">
						<uni-easyinput prefixIcon="info-filled" v-model="registerData.userCode" placeholder="请输入验证码">
							<template #right>
								<view>
									<text class="code" @click="sendEmail()">
										{{!codeTime?'获取验证码':codeTime+'s'}}
									</text>
								</view>
							</template>
						</uni-easyinput>
					</uni-forms-item>
				</uni-forms>
				<button class="false_button"
					v-if="!registerData.userEmail||!registerData.userPassword||!registerData.userCode"
					@click="err_submit_register()" hover-class="is_errhover">注册</button>
				<button class="true_button"
					v-if="registerData.userEmail&&registerData.userPassword&&registerData.userCode"
					@click="submit_register('registerForm')" hover-class="is_hover">注册</button>
			</view>
		</view>
	</view>

	<view v-if="isLogin" class="isLogin_true">
			<view class="isLogin_1">
				<uni-list-item :show-extra-icon="true" showArrow :extra-icon="infoIcon" :title="this.userData.userName"
					clickable @click="to_info()" />
			</view>
			<view class="isLogin_2">
				<uni-list-item :show-extra-icon="true" showArrow :extra-icon="orderIcon" title="购票记录" />
				<uni-list-item :show-extra-icon="true" showArrow :extra-icon="setIcon" title="设定" />
			</view>
			<view class="isLogin_3">
				<uni-list-item :show-extra-icon="true" :extra-icon="logoutIcon" title="登出" clickable
					@click="to_logout()" />
			</view>
			

	</view>
	
	<view>
		<!-- 提示窗示例 -->
		<uni-popup ref="alertDialog" type="dialog">
			<uni-popup-dialog :type="error" cancelText="关闭" confirmText="确定" content="是否确定登出" @confirm="dialogConfirm"
				@close="dialogClose"></uni-popup-dialog>
		</uni-popup>
	</view>

</template>

<script>
	export default {
		data() {
			return {
				isRegister: false,
				isLogin: false,

				codeTime: 0,

				sendEmailCount: 0,

				// 校验表单数据
				loginData: {
					userEmail: '',
					userPassword: '',
				},
				registerData: {
					userEmail: '',
					userPassword: '',
					userCode: '',
				},
				userData: {
					userId: '',
					userName: '',
					userEmail: '',
					userPassword: '',
					userPhone: '',
					userUrl: '',
					userProfile: '',
				},
				// 校验规则
				rules_login: {
					userEmail: {
						rules: [{
							required: true
						}, {
							format: 'email',
							errorMessage: " "
						}]
					},
					userPassword: {
						rules: [{
							required: true
						}]
					}
				},
				rules_register: {
					userEmail: {
						rules: [{
							required: true
						}, {
							format: 'email',
							errorMessage: " "
						}]
					},
					userPassword: {
						rules: [{
							required: true
						}]
					},
					userCode: {
						rules: [{
							required: true
						}]
					},
				},
				orderIcon: {
					color: '#f9da49',
					size: '60rpx',
					type: 'cart'
				},
				infoIcon: {
					color: '#f9da49',
					size: '200rpx',
					type: 'contact-filled'
				},
				setIcon: {
					color: '#f9da49',
					size: '60rpx',
					type: 'gear'
				},
				logoutIcon: {
					color: '#f9da49',
					size: '60rpx',
					type: 'arrow-left'
				},
			}
		},

		onLoad: function(option) {
			this.userId = uni.getStorageSync("userId");
			this.token = uni.getStorageSync("token");
			this.isLogin = uni.getStorageSync("isLogin");

			let user = {
				"userId": this.userId
			};
			
			console.log('here',user);

			uni.request({
				url: '/api/user/info',
				method: 'GET',
				dataType: 'json',
				data: user,
				header: {
					'Authorization': this.token
				},
				success: (res) => {
					console.log('调用成功', res.data.data);
					this.userData.userId = res.data.data.userId;
					this.userData.userName = res.data.data.userName;
					this.userData.userEmail = res.data.data.userEmail;
					this.userData.userPassword = res.data.data.userPassword;
					this.userData.userPhone = res.data.data.userPhone;
					this.userData.userUrl = res.data.data.userUrl;
					this.userData.userProfile = res.data.data.userProfile;
				},
			})
		},

		methods: {
			err_submit() {
				if (this.loginData.userEmail && !this.loginData.userPassword) {
					uni.showToast({
						title: '请输入密码',
						icon: 'error',
						mask: 'true',
					})
				} else {
					uni.showToast({
						title: '请输入邮箱',
						icon: 'error',
						mask: 'true',
					})
				}
			},

			err_submit_register() {
				if (this.registerData.userEmail && (!this.registerData.userPassword || !this.registerData.userCode)) {
					if (this.registerData.userPassword && !this.registerData.userCode) {
						uni.showToast({
							title: '请输入验证码',
							icon: 'error',
							mask: 'true',
						})
					} else {
						uni.showToast({
							title: '请输入密码',
							icon: 'error',
							mask: 'true',
						})
					}
				} else {
					uni.showToast({
						title: '请输入邮箱',
						icon: 'error',
						mask: 'true',
					})
				}
			},


			//submit_login函数
			submit_login(ref) {
				this.$refs[ref].validate().then(res => {
					let user = {
						"userEmail": this.loginData.userEmail,
						"userPassword": this.loginData.userPassword
					};

					uni.request({
						url: '/api/user/login',
						method: 'POST',
						dataType: 'json',
						data: user,
						success: (res) => {
							if (res.data.code == 0) {
								console.log('调用成功', res.data.data);

								uni.setStorageSync('userId', res.data.data.user.userId);
								uni.setStorageSync('token', res.data.data.token);
								uni.setStorageSync('isLogin', true);

								uni.reLaunch({
									url: '/pages/user/login',
								});
							}
							if (res.data.code == 1) {
								uni.showToast({
									title: res.data.message,
									icon: 'error',
									mask: 'true',
								})
							}
						},
						fail: (res) => { //如果访问接口失败就会进入fail
							console.log(res.errMsg)
							console.log("调用接口失败")
						}
					})
				}).catch(err => {
					uni.showToast({
						title: '请正确填写邮箱',
						icon: 'error',
						mask: 'true',
					})
					console.log('err', err);
				})
			},

			submit_register(ref) {
				this.$refs[ref].validate().then(res => {

					let user_register = {
						"userEmail": this.registerData.userEmail,
						"userPassword": this.registerData.userPassword,
						"userCode": this.registerData.userCode,
					};

					uni.request({
						url: '/api/user/register?code=' + this.registerData.userCode,
						method: 'POST',
						dataType: 'json',
						data: user_register,
						success: (res) => {

							console.log(user_register);
							console.log(res.data);
							if (res.data.code == 0) {
								uni.request({
									url: '/api/user/login',
									method: 'POST',
									dataType: 'json',
									data: user_register,
									success: (res) => {
										if (res.data.code == 0) {
											console.log('调用成功', res.data.data);

											uni.setStorageSync('userId', res.data.data.user
												.userId);
											uni.setStorageSync('token', res.data.data
												.token);
											uni.setStorageSync('isLogin', true);

											uni.reLaunch({
												url: '/pages/user/login',
											});
										}
									}
								})
							}
							if (res.data.code == 1) {
								console.log(res.data);
								uni.showToast({
									title: res.data.message,
									icon: 'error',
									mask: 'true',
								})
							}
						}
					})
				}).catch(err => {
					uni.showToast({
						title: '请正确填写邮箱',
						icon: 'error',
						mask: 'true',
					})
					console.log('err', err);
				})
			},

			sendEmail() {

				this.sendEmailCount++;

				//邮箱验证
				let email_reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;

				if (email_reg.test(this.registerData.userEmail)) {
					let user = {
						"userEmail": this.registerData.userEmail,
						"userPassword": this.registerData.userPassword
					};

					uni.request({
						url: '/api/user/sendEmail',
						method: 'POST',
						dataType: 'json',
						data: user,
						success: (res) => {
							if (res.data.code == 0) {

								this.codeTime = 120
								let timer = setInterval(() => {
									this.codeTime--;
									if (this.codeTime < 1) {
										clearInterval(timer);
										this.codeTime = 0
									}
								}, 1000)


								uni.showToast({
									title: '请前往邮箱查看验证码',
									icon: 'success',
									mask: true,
								})
							}

							if (res.data.code == 1) {
								if (res.data.data != null) {
									this.codeTime = 120 - res.data.data.time_gap;
									if (this.codeTime > 0) {
										uni.showToast({
											title: res.data.message,
											icon: 'error',
											mask: 'true',
										});
										if (this.sendEmailCount == 1) {
											let timer = setInterval(() => {
												this.codeTime--;
												if (this.codeTime < 1) {
													clearInterval(timer);
													this.codeTime = 0
												}
											}, 1000)
										}
										return;
									}
								} else {
									uni.showToast({
										title: res.data.message,
										icon: 'error',
										mask: 'true',
									});
								}
							}
						}
					})
				}
				if (!email_reg.test(this.registerData.userEmail) && this.registerData.userEmail) {
					uni.showToast({
						title: '请正确填写邮箱',
						icon: 'error',
						mask: 'true',
					})
				}
				if (!this.registerData.userEmail) {
					uni.showToast({
						title: '请输入邮箱',
						icon: 'error',
						mask: 'true',
					})
				}
			},
			to_logout() {
				this.$refs.alertDialog.open();
			},
			dialogConfirm() {
				uni.clearStorageSync();
				uni.reLaunch({
					url: '/pages/user/login',
				})
			},
			dialogClose() {
			},
			to_info() {
				uni.navigateTo({
					url: '/pages/user/info',
				});
			},
		}
	}
</script>

<style lang="scss" scoped>
	.isLogin_false {
		display: flex;
		flex-direction: column;
		align-items: space-around;
		justify-content: center;
		height: 100%;
	}

	.login {
		padding: 0 10%;
		display: flex;
		flex-direction: column;
		align-items: space-around;
		justify-content: center;
		height: 100%;
	}

	.register {
		padding: 0 10%;
		display: flex;
		flex-direction: column;
		align-items: space-around;
		justify-content: center;
		height: 100%;
	}

	text {
		color: #ffffff;
	}

	.code {
		color: #999;
	}

	.true_button {
		color: #010101;
		border-color: #010101;
		background-color: #f9da49;
	}

	.false_button {
		color: #010101;
		border-color: #010101;
		background-color: #999999;
	}

	.login_text_true {
		font-size: 100rpx;
	}

	.register_text_false {
		font-size: 50rpx;
		color: #999999;
	}

	.login_text_false {
		font-size: 50rpx;
		color: #999999;
	}

	.register_text_true {
		font-size: 100rpx;
	}

	.login_text {
		display: flex;
		flex-direction: row;
	}

	.register_text {
		display: flex;
		flex-direction: row;
	}

	.info_login_register {
		flex: 1;
	}

	::v-deep.uni-forms-item {
		.is-required {
			color: #010101;
		}
	}

	.is_hover {
		color: #010101;
		border-color: #010101;
		background-color: #d8bc3f;
	}

	.is_errhover {
		color: #010101;
		border-color: #010101;
		background-color: #6a6a6a;
	}

	.isLogin_true {
		display: flex;
		flex-direction: column;
		align-items: space-around;
		justify-content: space-around;
		height: 100%;
	}
	
	.isLogin_1{
		flex:1;
	}
	.isLogin_2{
		flex:1;
	}
	.isLogin_3{
		flex:1;
	}
	
</style>