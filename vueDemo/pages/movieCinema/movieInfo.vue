<template>
	<view class="movieInfo">

		<uni-list-item v-if="movieData" :title="movieData.movie.movieNameCn" showArrow ellipsis="1" :note=" movieData.movie.movieNameEn + 
		'\n导演：' + movieData.movie.movieDirector +
		'\n上映日期：' + movieData.movie.movieReleaseDate + 
		'\n时长：' + movieData.movie.movieDuration + ' min' " :thumb=movieData.movie.movieUrl thumb-size="lg" clickable
			@click="to_movie_detail()">
		</uni-list-item>

		<uni-card title="简介">
			<text>{{this.movieData.movie.movieDescription}}</text>
		</uni-card>


		<button class="true_button_bottom">bottom</button>
		<button class="true_button" hover-class="is_hover" @click="scheduleBymovie()">电影场次</button>


	</view>



</template>

<script>
	export default {

		data() {
			return {
				movieData: {
					movie: {
						movieCountry: "",
						movieDescription: "",
						movieDirector: "",
						movieDuration: "",
						movieId: "",
						movieNameCn: "",
						movieNameEn: "",
						movieReleaseDate: "",
						movieState: "",
						movieUrl: ""
					},
					type: [{
						genreId: "",
						genreName: "",
						movieId: ""
					}]
				},
			}
		},

		onLoad: function(option) {
			this.movieNameCn = uni.getStorageSync("movieNameCn");
			this.movieNameEn = uni.getStorageSync("movieNameEn");
			this.movieDirector = uni.getStorageSync("movieDirector");
			this.movieReleaseDate = uni.getStorageSync("movieReleaseDate");
			this.movieDuration = uni.getStorageSync("movieDuration");
			this.movieUrl = uni.getStorageSync("movieUrl");
			this.movieDescription = uni.getStorageSync("movieDescription");
			this.movieCountry = uni.getStorageSync("movieCountry");

			this.movieData.movie.movieNameCn = this.movieNameCn;
			this.movieData.movie.movieNameEn = this.movieNameEn;
			this.movieData.movie.movieDirector = this.movieDirector;
			this.movieData.movie.movieReleaseDate = this.movieReleaseDate;
			this.movieData.movie.movieDuration = this.movieDuration;
			this.movieData.movie.movieUrl = this.movieUrl;
			this.movieData.movie.movieDescription = this.movieDescription;
			this.movieData.movie.movieCountry = this.movieCountry;

			let movie = {
				"movieId": option.movieId
			};

			uni.request({
				url: '/api/movie/infoMovieById',
				method: 'GET',
				dataType: 'json',
				data: movie,

				success: (res) => {
					this.movieData = res.data.data;
					console.log('movieData', res.data.data);
				}
			});
		},

		onBackPress: function(option) {
			console.log('退出');
			uni.removeStorageSync("movieId");
			uni.removeStorageSync("movieNameCn");
			uni.removeStorageSync("movieNameEn");
			uni.removeStorageSync("movieDirector");
			uni.removeStorageSync("movieReleaseDate");
			uni.removeStorageSync("movieDuration");
			uni.removeStorageSync("movieUrl");
			uni.removeStorageSync("movieDescription");
			uni.removeStorageSync("movieCountry");
			uni.removeStorageSync("movieType");

			const res = uni.getStorageInfoSync();
			console.log(res.keys);
			console.log(res.currentSize);
			console.log(res.limitSize);
			console.log('type', uni.getStorageSync("movieType"));
		},

		methods: {
			to_movie_detail() {
				uni.navigateTo({
					url: '/pages/movieCinema/detailInfo',
					animationType: 'pop-in',
					animationDuration: 200
				});
			},

			scheduleBymovie() {
				uni.navigateTo({
					url: '/pages/schedule/byMovie',
					animationType: 'pop-in',
					animationDuration: 200
				});
			}
		},

	}
</script>

<style lang="scss" scoped>
	text {
		color: #999999;
	}

	.movieInfo {
		display: flex;
		flex-direction: column;
		gap: 80rpx;
	}

	::v-deep.uni-list--lg {
		height: 300rpx;
		width: 200rpx;
		border-radius: 20rpx;
	}

	::v-deep.uni-list-item__content-title {
		font-size: 50rpx;
		color: #f9da49;
		overflow: hidden;
	}

	::v-deep.uni-list-item__content-note {
		margin-top: 6rpx;
		color: #999999;
		font-size: 25rpx;
		overflow: hidden;
	}

	::v-deep.uni-list--border-top {
		left: 5%;
		width: 90%;
		background-color: #f9da49;
	}

	.uni-body {
		color: #999999;
	}

	.uni-card {
		background-color: #000000;
		bottom: 100rpx;
	}

	.true_button {
		width: 90%;
		left: 5%;
		color: #010101;
		border-color: #010101;
		background-color: #f9da49;
		position: fixed;
		bottom: 2%;
	}

	.is_hover {
		color: #010101;
		border-color: #010101;
		background-color: #d8bc3f;
	}

	.true_button_bottom {
		width: 100%;
		color: #010101;
		border-color: #010101;
		background-color: #000000;
		position: fixed;
		bottom: 0;
	}
</style>