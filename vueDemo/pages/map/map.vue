<template>
	<view v-if="this.locationFlag==1">
		<view class="page-body">
			<view class="page-section page-section-gap">
				<map class="cinema_map" style="width: 100%;" :latitude="this.cinemaLocation.latitude" :longitude="this.cinemaLocation.longitude"
					:markers="covers">
				</map>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				locationFlag:0,
				cinemaLocation: {
					longitude: '',
					latitude: '',
				},
				covers: [{
					latitude: '',
					longitude: '',
					iconPath: '/static/Location.png'
				},
				{
					latitude:'',
					longitude:'',
					iconPath: '/static/LocationPerson.png'
				}]
			}
		},
		
		onLoad: function(option){
			this.cinemaX = Number(uni.getStorageSync("cinemaX"));
			this.cinemaY = Number(uni.getStorageSync("cinemaY"));
			this.cinemaLocation.longitude=this.cinemaX;
			this.cinemaLocation.latitude=this.cinemaY;
			this.covers[0].longitude=this.cinemaX;
			this.covers[0].latitude=this.cinemaY;
			
			console.log('cinemaLocation',this.cinemaLocation);
			uni.getLocation({
				success: (res) => {
					console.log("当前位置的经度：", res.longitude);
					console.log("当前位置的纬度：", res.latitude);
					this.covers[1].longitude=res.longitude;
					this.covers[1].latitude=res.latitude;
					console.log('标记',this.covers)
					this.locationFlag=1
				}
			});
		},
		
		onBackPress: function(option){
			uni.removeStorageSync("cinemaX");
			uni.removeStorageSync("cinemaY");
		},

		methods: {}
	}
</script>

<style lang="scss" scoped>
	text {
		color: #fff;
	}

	.cinema_map {
		width:100%;
		height: 100vh;
	}
</style>
