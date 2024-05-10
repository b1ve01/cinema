<template>
	<view class="houseCreate">
		<view class="houseINFO">

			<uni-forms-item>
			<text>放映厅名称：</text><uni-easyinput v-model="houseData.houseName" placeholder="请输入放映厅名称" />
			</uni-forms-item>
			
			<uni-forms-item>
			<text>行数：</text><uni-easyinput v-model="houseData.houseX" placeholder="请输入行数" />
			</uni-forms-item>
			
			<uni-forms-item>
			<text>列数：</text><uni-easyinput v-model="houseData.houseY" placeholder="请输入列数" />
			</uni-forms-item>
			
			<button v-if="this.houseData.houseName!=''
			&& this.houseData.houseX!=''
			&& this.houseData.houseY!=''
			" class="create_house" size="mini" @click="house_create()">新增放映厅信息</button>
			
			<button v-if="this.houseData.houseName==''
			|| this.houseData.houseX==''
			|| this.houseData.houseY==''
			" class="create_house_false" size="mini" >新增放映厅信息</button>

		</view>
		
		<view>
			<uni-popup ref="houseCreateDialog" type="dialog">
				<uni-popup-dialog :type="error" cancelText="关闭" confirmText="确定" content="是否新增该放映厅信息" @confirm="houseCreateConfirm"
					@close="dialogClose"></uni-popup-dialog>
			</uni-popup>
		</view>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				houseData: {
					houseName: '',
					houseX: '',
					houseY: '',
				},
			}
		},
		
		onLoad: function(option){
			this.token = uni.getStorageSync("token");
		},

		methods: {
			house_create(){
				this.$refs.houseCreateDialog.open();
			},
			houseCreateConfirm(){
				let house = {
					"houseName": this.houseData.houseName,
					"houseX":this.houseData.houseX,
					"houseY":this.houseData.houseY,
				};
				
				uni.request({
					url: '/api/house/create',
					method: 'POST',
					dataType: 'json',
					data: house,
					header: {
						'Authorization': this.token
					},
					success:(res)=>{
						uni.showToast({
							title: '新增成功',
							icon: 'true',
							mask: 'true',
						})
						uni.reLaunch({
							url: '/pages/web/login'
						})
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
	
	.houseCreate{
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
	
	.houseINFO{
		height: 80%;
		padding-top: 5%;
		padding-left: 40%;
		width: 20%;
	}
	
	.create_house{
		width: 100%;
		background-color: #f9da49;
		color: #000000;
	}
	
	.create_house_false{
		width: 100%;
		background-color: #999999;
		color: #ffffff;
	}
	
	
</style>
