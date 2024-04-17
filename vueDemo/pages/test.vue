<template>
	<view>
		<view class="page-body">
			<view class="page-section page-section-gap">
				<map class="cinema_map" style="width: 100%;" :latitude="latitude" :longitude="longitude"
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
				title: 'map',
				latitude: 29.606335,
				longitude: 106.293298,
				covers: [{
					latitude: 29.606335,
					longitude: 106.293298,
					iconPath: '../../../static/Location.png'
				},
				{
					latitude:'',
					longitude:'',
					iconPath: '../../../static/LocationPerson.png'
				}]
			}
		},

		onLoad: function(option) {
			
			uni.getLocation({
				success: (res) => {
					console.log("当前位置的经度：", res.longitude);
					console.log("当前位置的纬度：", res.latitude);
					this.covers[1].longitude=res.longitude;
					this.covers[1].latitude=res.latitude;
					console.log('标记',this.covers)
				}
			});
			
			let dateStr = new Date().toLocaleDateString();
			console.log('time',dateStr);
		},

		methods: {}
	}
</script>

<style lang="scss" scoped>
	text {
		color: #fff;
	}

	.cinema_map {
		height: 100vh;
	}
</style>