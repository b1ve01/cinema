<template>
	<!-- 	<view>
		<text>userId:{{userId}}</text>
	</view>
	<view>
		<text>userName:{{userData.userName}}</text>
	</view>
	<view>
		<text>userEmail:{{userData.userEmail}}</text>
	</view>
	<view>
		<text>userPassword:{{userData.userPassword}}</text>
	</view>
	<view>
		<text>userPhone:{{userData.userPhone}}</text>
	</view>
	<view>
		<text>userUrl:{{userData.userUrl}}</text>
	</view>
	<view>
		<text>userProfile:{{userData.userProfile}}</text>
	</view> -->
	<view class="info">


		<view class="image_box">
			<uni-file-picker v-if=this.userData.userUrl limit="1" :del-icon="false" disable-preview
				:imageStyles="imageStyles" file-mediatype="image" v-model="img" @select="update_url"></uni-file-picker>

			<uni-file-picker v-if=!this.userData.userUrl limit="1" :del-icon="false" disable-preview
				:imageStyles="imageStyles" file-mediatype="image" v-model="img_default"
				@select="update_url"></uni-file-picker>
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

			}
		},

		onLoad: function(option) {

			this.userId = uni.getStorageSync("userId");
			console.log('id在这', this.userId);
			this.token = uni.getStorageSync("token");
			console.log('token在这', this.token);

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
						if(responseData.code== 0){
							uni.request({
								url: '/api/user/updateUrl',
								method: 'PATCH',
								dataType: 'json',
								data: user,
								header: {
									'Authorization': this.token
								},
								success:(res)=>{
									console.log(res.data);
									this.userData.userUrl = res.data.data.userUrl;
									this.img.url =res.data.data.userUrl;
									uni.reLaunch({
										url: '/pages/user/login',
										success:()=>{
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
			}

		}
	}
</script>

<style lang="scss" scoped>
	text {
		color: #ffffff;
	}

	.info {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: space-around;
		height: 100%;
	}

	.image_box {}
</style>