<template>
	<view class="info">


		<view class="image_box">
			<uni-file-picker v-if="this.userData.userUrl" limit="1" :del-icon="false" disable-preview
				:imageStyles="imageStyles" file-mediatype="image" v-model="img" @select="update_url"></uni-file-picker>

			<uni-file-picker v-if="!this.userData.userUrl" limit="1" :del-icon="false" disable-preview
				:imageStyles="imageStyles" file-mediatype="image" v-model="img_default"
				@select="update_url"></uni-file-picker>
		</view>

		<view class="name">
			<text v-if="this.userData.userName" class="name">
				{{this.userData.userName}}
			</text>
		</view>


		<view class="info_box">
			<uni-list-item :show-extra-icon="true" showArrow :extra-icon="nameIcon" title="名字" clickable
				@click="to_updateName()" />
			<uni-list-item :show-extra-icon="true" showArrow :extra-icon="phoneIcon" title="电话" clickable
				@click="to_updatePhone()" />
			<uni-list-item :show-extra-icon="true" showArrow :extra-icon="profileIcon" title="简介" clickable
				@click="to_updateProfile()" />
		</view>

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
				imageStyles: {
					width: 100,
					height: 100,
					border: {
						color: '#010101',
						style: 'solid',
						radius: '50%'
					},
				},
				img: {
					url: ''
				},
				img_default: {
					url: 'https://cinema-bucket.oss-cn-beijing.aliyuncs.com/default.png'
				},
				nameIcon: {
					color: '#f9da49',
					size: '60rpx',
					type: 'person'
				},
				phoneIcon: {
					color: '#f9da49',
					size: '60rpx',
					type: 'phone'
				},
				profileIcon: {
					color: '#f9da49',
					size: '60rpx',
					type: 'compose'
				},
			}
		},

		onLoad: function(option) {

			this.userId = uni.getStorageSync("userId");
			this.token = uni.getStorageSync("token");
			this.userName = uni.getStorageSync("userName");
			this.userUrl = uni.getStorageSync("userUrl");
			this.userData.userName=this.userName;
			this.userData.userUrl=this.userUrl;

			let user = {
				"userId": this.userId
			};

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
					this.img.url = res.data.data.userUrl;
				},
				fail: (res) => { //如果访问接口失败就会进入fail
					console.log(res.errMsg)
					console.log("调用接口失败")
				}
			})

		},
		
		onBackPress:function(option){
			uni.removeStorageSync("userName");
			uni.removeStorageSync("userUrl");
		},

		methods: {
			update_url(res) {
				console.log('上传成功', res);
				uni.uploadFile({
					url: '/api/upload',
					method: 'POST',
					filePath: res.tempFilePaths[0],
					name: 'file',
					dataType: 'json',
					header: {
						'Authorization': this.token,
					},
					success: (res) => {
						let responseData = JSON.parse(res.data);
						let user = {
							"userUrl": responseData.data
						};
						if (responseData.code == 0) {
							uni.request({
								url: '/api/user/updateUrl',
								method: 'PATCH',
								dataType: 'json',
								data: user,
								header: {
									'Authorization': this.token
								},
								success: (res) => {
									console.log(res.data);
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
						}
					}
				})
			},
			to_updateName() {
				uni.navigateTo({
					url: '/pages/user/updateName',
				});
			},
			to_updatePhone() {
				uni.navigateTo({
					url: '/pages/user/updatePhone',
				});
			},
			to_updateProfile() {
				uni.navigateTo({
					url: '/pages/user/updateProfile',
				});
			},
		}
	}
</script>

<style lang="scss" scoped>

	.info {
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		height: 80%;
	}

	.image_box {
		align-self: center;
	}

	.name {
		color: #f9da49;
		align-self: center;
		font-size: 50rpx;
	}
</style>