<template>
	<view class="movieUpdate">
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				movieData:[],
				movieType:[],
			}
		},
		
		onLoad: function(option){
			console.log(option);
			uni.request({
				url: '/api/movie/infoMovieById',
				method: 'GET',
				dataType: 'json',
				data: option,
				success:(res)=>{
					this.movieData=res.data.data.movie
					this.movieType=res.data.data.type
					console.log('movieINFO',this.movieData)
					console.log('movieType',this.movieType)
				}
			})
			
		},

		methods: {}
	}
</script>

<style lang="scss" scoped>

	.movieUpdate{
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
	
</style>
