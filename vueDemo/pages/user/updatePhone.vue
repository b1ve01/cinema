<template>
	<view class="updatePhone">
		<uni-forms ref="userForm" :modelValue="userFormData">
			<uni-forms-item name="userPhone">
				<uni-easyinput class="inputBorder" v-model="userFormData.userPhone" :placeholder=this.userData.userPhone />
			</uni-forms-item>
		</uni-forms>
		<uni-list-item class="update_true" v-if="userFormData.userPhone" rightText="修改" clickable @click="update()" />
		<uni-list-item class="update_false" v-if="!userFormData.userPhone" :disabled="true" rightText="修改" />
	</view>

	<view>
		<!-- 提示窗示例 -->
		<uni-popup ref="alertDialog" type="dialog">
			<uni-popup-dialog :type="error" cancelText="关闭" confirmText="确定" content="是否确定修改电话" @confirm="dialogConfirm"
				@close="dialogClose"></uni-popup-dialog>
		</uni-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				userData: {
					userId: '',
					userName: '',
					userEmail: '',
					userPassword: '',
					userPhone: '',
					userUrl: '',
					userProfile: '',
				},
				userFormData: {
					userPhone: '',
				}
			}
		},

		onLoad: function(option) {

			this.userId = uni.getStorageSync("userId");
			this.token = uni.getStorageSync("token");

			let user = {
				"userId": this.userId
			};
			
			let baseURL = '/api';
			// #ifdef APP-PLUS
			baseURL = "http://cinema.nat100.top"
			// #endif

			uni.request({
				url: baseURL+'/user/info',
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
				fail: (res) => { //如果访问接口失败就会进入fail
					console.log(res.errMsg)
					console.log("调用接口失败")
				}
			})

		},

		methods: {
			update() {
				this.$refs.alertDialog.open();
			},
			dialogConfirm() {
				let update_name = {
					"userName": this.userData.userName,
					"userPhone": this.userFormData.userPhone,
					"userProfile": this.userData.userProfile,
				};
				
				let baseURL = '/api';
				// #ifdef APP-PLUS
				baseURL = "http://cinema.nat100.top"
				// #endif
				
				uni.request({
					url: baseURL+'/user/update',
					method: 'PUT',
					dataType: 'json',
					data: update_name,
					header: {
						'Authorization': this.token
					},
					success:(res)=>{
						uni.reLaunch({
							url: '/pages/user/login',
							success: () => {
								uni.navigateTo({
									url: '/pages/user/info',
								})
							}
						})
					}
				})
			},
			dialogClose() {},
		}
	}
</script>

<style lang="scss" scoped>
	.update_true {
		::v-deep.uni-list-item__extra-text {
			color: #f9da49;
			font-size: 20px;
		}
	}
	
	.update_false {
		::v-deep.uni-list-item__extra-text {
			color: #ffffff;
			font-size: 20px;
		}
	}
	
	.inputBorder{
		::v-deep.is-input-border{
			border-radius: 0px;
		}
	}
	
</style>