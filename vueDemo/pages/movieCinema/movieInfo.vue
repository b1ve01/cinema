<template>
	<view class="">

	</view>
</template>

<script>
	export default {
		data() {
			return {
				movieData: {},
			}
		},
		
		onLoad: function(option) {
		
			let movie = {
				"movieId": option.movieId
			};
		
			uni.request({
				url: '/api/movie/infoMovieById',
				method: 'GET',
				dataType: 'json',
				data: movie,
		
				success: (res) => {
					console.log('调用成功',res.data.data);
					this.movieData = res.data.data;
				},
			});
		
		},

		methods: {}
	}
</script>

<style lang="scss" scoped>

</style>
