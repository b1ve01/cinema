<template>
	<view class="cinemaUpdate" v-if="this.requestFlag==1">
		<view class="cinemaMap">
			<map class="cinema_map" style="width: 100%;" :latitude="this.cinemaData.cinemaY" :longitude="this.cinemaData.cinemaX"
				:markers="covers">
			</map>
		</view>
		
		<view class="cinemaINFO">
			<text class="cinemaName">{{this.cinemaData.cinemaName}}</text>
			<text class="cinemaAddress">地址：{{this.cinemaData.cinemaAddress}}</text>
			<text class="cinemaPhone">电话：{{this.cinemaData.cinemaPhone}}</text>
			<text class="cinemaX">经度：{{this.cinemaData.cinemaX}}</text>
			<text class="cinemaY">纬度：{{this.cinemaData.cinemaY}}</text>
		</view>


	</view>
</template>

<script>
	export default {
		data() {
			return {
				requestFlag:0,
				cinemaData:[],
				covers: [{
					latitude: '',
					longitude: '',
					iconPath: '/static/Location.png'
				}]
			}
		},
		
		onLoad: function(option){
			uni.request({
				url: '/api/cinema/infoById',
				method: 'GET',
				dataType: 'json',
				data: option,
				success:(res)=>{
					this.cinemaData=res.data.data
					this.covers[0].longitude=this.cinemaData.cinemaX;
					this.covers[0].latitude=this.cinemaData.cinemaY;
					console.log('cinema',this.cinemaData);
					this.requestFlag=1;
				}
			})
		},

		methods: {}
	}
</script>

<style lang="scss" scoped>
	
	text{
		color: #fff;
	}
	
	.cinemaUpdate{
		display: flex;
		flex-direction: row;
		align-items: space-around;
		position: absolute;
		top:5%;
		left:10%;
		height: 90%;
		width: 80%;
		border:3px solid #f9da49;
		border-top-left-radius: 20rpx;
		border-top-right-radius: 20rpx;
		border-bottom-left-radius: 20rpx;
		border-bottom-right-radius: 20rpx;
		overflow: hidden;
	}
	
	.cinemaMap{
		display: flex;
		justify-content: center;
		align-items: center;
		width:35%;
		border-right: 3px solid #f9da49;
	}
	
	.cinema_map{
		height:100vh;
	}
	
	.cinemaINFO{
		display: flex;
		flex-direction:column;
		justify-content: space-between;
		width:60%;
		padding-left: 5%;
		padding-top: 2%;
		height:90%;
	}
	
	.cinemaName{
		color: #f9da49;
		font-size: 100rpx;
	}
	
	.cinemaAddress{
		font-size: 50rpx;
	}
	
	.cinemaPhone{
		font-size: 50rpx;
	}
	
	.cinemaX{
		font-size: 50rpx;
	}
	
	.cinemaY{
		font-size: 50rpx;
	}
	
	
	
</style>
