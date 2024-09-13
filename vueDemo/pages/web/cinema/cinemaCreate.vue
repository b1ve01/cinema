<template>
	<view class="cinemaCreate">
		<view class="cinemaINFO">
			
		<uni-forms-item>	
		<text>影院名称：</text><uni-easyinput v-model="cinemaData.cinemaName" placeholder="请输入影院名称" />
		</uni-forms-item>
		
		<uni-forms-item>
		<text>影院电话：</text><uni-easyinput v-model="cinemaData.cinemaPhone" placeholder="请输入影院电话" />
		</uni-forms-item>
		
		<uni-forms-item>
		<text>密码：</text><uni-easyinput v-model="cinemaData.cinemaPassword" placeholder="请输入密码" />
		</uni-forms-item>
		
		<uni-forms-item>
		<text>影院地址：</text><uni-easyinput v-model="cinemaData.cinemaAddress" placeholder="请输入影院地址" />
		</uni-forms-item>
		
		<uni-forms-item>
		<text>影院位置经度：</text><uni-easyinput v-model="cinemaData.cinemaX" placeholder="请输入影院位置经度" />
		</uni-forms-item>
		
		<uni-forms-item>
		<text>影院位置纬度：</text><uni-easyinput v-model="cinemaData.cinemaY" placeholder="请输入影院位置纬度" />
		</uni-forms-item>
		
		<button v-if="this.cinemaData.cinemaName!=''
		&& this.cinemaData.cinemaPhone!=''
		&& this.cinemaData.cinemaPassword!=''
		&& this.cinemaData.cinemaAddress!=''
		&& this.cinemaData.cinemaX!=''
		&& this.cinemaData.cinemaY!=''
		" class="create_cinema" size="mini" @click="cinema_create()">新增影院</button>
		
		<button v-if="this.cinemaData.cinemaName==''
		|| this.cinemaData.cinemaPhone==''
		|| this.cinemaData.cinemaPassword==''
		|| this.cinemaData.cinemaAddress==''
		|| this.cinemaData.cinemaX==''
		|| this.cinemaData.cinemaY==''
		" class="create_cinema_false" size="mini">新增影院</button>
		
		
		</view>
		
		<view>
			<uni-popup ref="cinemaCreateDialog" type="dialog">
				<uni-popup-dialog :type="error" cancelText="关闭" confirmText="确定" content="是否新增该影院信息" @confirm="cinemaCreateConfirm"
					@close="dialogClose"></uni-popup-dialog>
			</uni-popup>
		</view>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				cinemaData: {
					cinemaName: '',
					cinemaPhone: '',
					cinemaPassword: '',
					cinemaAddress: '',
					cinemaX: '',
					cinemaY: '',
				},
			}
		},

		methods: {
			cinema_create(){
				this.$refs.cinemaCreateDialog.open();
				console.log('cinemaData',this.cinemaData);
			},
			cinemaCreateConfirm(){
				let cinema = {
					"cinemaName": this.cinemaData.cinemaName,
					"cinemaPhone":this.cinemaData.cinemaPhone,
					"cinemaPassword":this.cinemaData.cinemaPassword,
					"cinemaAddress":this.cinemaData.cinemaAddress,
					"cinemaX":this.cinemaData.cinemaX,
					"cinemaY":this.cinemaData.cinemaY,
				};
				
				uni.request({
					url: '/api/cinema/register',
					method: 'POST',
					dataType: 'json',
					data: cinema,
					success:(res)=>{
						if (res.data.code == 0){
							uni.showToast({
								title: '新增成功',
								icon: 'true',
								mask: 'true',
							})
							uni.reLaunch({
								url: '/pages/web/login'
							})
						}
						if (res.data.code == 1) {
							uni.showToast({
								title: res.data.message,
								icon: 'error',
								mask: 'true',
							})
						}
					}
				})
				
			},
		}
	}
</script>

<style lang="scss" scoped>
	text {
		color: #fff;
	}
	
	.cinemaCreate{
		display: flex;
		flex-direction: row;
		align-items: space-around;
		position: absolute;
		top: 5%;
		left: 10%;
		height: 90%;
		width: 80%;
		border: 3px solid #f9da49;
		border-top-left-radius: 20rpx;
		border-top-right-radius: 20rpx;
		border-bottom-left-radius: 20rpx;
		border-bottom-right-radius: 20rpx;
		overflow: hidden;
	}
	
	.cinemaINFO{
		height: 80%;
		padding-top: 5%;
		padding-left: 40%;
		width: 20%;
	}
	
	.create_cinema{
		width: 100%;
		background-color: #f9da49;
		color: #000000;
	}
	
	.create_cinema_false{
		width: 100%;
		background-color: #999999;
		color: #ffffff;
	}


</style>
