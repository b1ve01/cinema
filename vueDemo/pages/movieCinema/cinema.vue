<template>
	<view class="cinema_main">

		<view class="movie_or_cinema">
			<text class="movie_text_true" v-if="!isCinema" @click="isCinema=true">电影 | </text>
			<text class="movie_text_false" v-if="isCinema" @click="isCinema=false">电影</text>

			<text class="cinema_text_false" v-if="!isCinema" @click="isCinema=true">影院</text>
			<text class="cinema_text_true" v-if="isCinema" @click="isCinema=false"> | 影院</text>
		</view>

		<view class="release_or_not" v-if="!isCinema">
			<view class="uni-padding-wrap uni-common-mt">
				<uni-segmented-control :current="current" :values="items" style-type="text" active-color="#f9da49"
					@clickItem="onClickItem" />
			</view>
		</view>

		<view v-if="!isCinema" v-show="current === 0" class="movie_info" v-for="(item,index) in this.movieData.data"
			:key="index">
			<uni-list>
				<uni-list-item :title="this.movieData.data[index].movieNameCn" ellipsis="1" :note=" this.movieData.data[index].movieNameEn + 
			'\n导演：' + this.movieData.data[index].movieDirector +
			'\n上映日期：' + this.movieData.data[index].movieReleaseDate + 
			'\n时长：' + this.movieData.data[index].movieDuration + ' min' " :thumb=this.movieData.data[index].movieUrl
					thumb-size="lg" clickable @click="to_movie_info(this.movieData.data[index].movieId)">
					<template v-slot:footer>
						<button class="buy_button" hover-class="is_buy_hover" size="mini">购票</button>
					</template>

				</uni-list-item>
			</uni-list>
		</view>

		<view v-if="!isCinema" v-show="current === 1" class="movie_info" v-for="(item,index) in this.movieData_not.data"
			:key="index">
			<uni-list>
				<uni-list-item :title="this.movieData_not.data[index].movieNameCn" ellipsis="1" :note=" this.movieData_not.data[index].movieNameEn + 
			'\n导演：' + this.movieData_not.data[index].movieDirector +
			'\n上映日期：' + this.movieData_not.data[index].movieReleaseDate + 
			'\n时长：' + this.movieData_not.data[index].movieDuration + ' min' " :thumb=this.movieData_not.data[index].movieUrl
					thumb-size="lg" clickable @click="to_movie_info(this.movieData_not.data[index].movieId)">
					<template v-slot:footer>
						<button class="buy_button" hover-class="is_buy_hover" size="mini">购票</button>
					</template>

				</uni-list-item>
			</uni-list>
		</view>





	</view>
</template>


<script>
	export default {
		data() {
			return {
				isCinema: false,
				movieData: [],
				movieData_not: [],
				items: ['正在上映', '待映'],
				movieType: '',
				current: 0,
			}
		},

		onShow: function(option) {

			uni.request({
				url: '/api/movie/infoMovieByState?movieState=' + 1,
				method: 'GET',
				dataType: 'json',

				success: (res) => {
					this.movieData = res.data;
					this.movieData.data.reverse();
					console.log('电影页面', this.movieData.data);
				}
			});

			uni.request({
				url: '/api/movie/infoMovieByState?movieState=' + 0,
				method: 'GET',
				dataType: 'json',

				success: (res) => {
					this.movieData_not = res.data;
				}
			});

		},

		methods: {
			to_movie_info(ref) {

				let movie = {
					"movieId": ref
				};

				uni.request({
					url: '/api/movie/infoMovieById',
					method: 'GET',
					dataType: 'json',
					data: movie,

					success: (res) => {
						this.movieType='';
						console.log('调用成功', res.data.data.movie);
						uni.setStorageSync('movieNameCn', res.data.data.movie.movieNameCn);
						uni.setStorageSync('movieNameEn', res.data.data.movie.movieNameEn);
						uni.setStorageSync('movieDirector', res.data.data.movie.movieDirector);
						uni.setStorageSync('movieReleaseDate', res.data.data.movie.movieReleaseDate);
						uni.setStorageSync('movieDuration', res.data.data.movie.movieDuration);
						uni.setStorageSync('movieUrl', res.data.data.movie.movieUrl);
						uni.setStorageSync('movieDescription', res.data.data.movie.movieDescription);
						uni.setStorageSync('movieCountry', res.data.data.movie.movieCountry);

						for (let i = 0; i < res.data.data.type.length;i++) {
							this.movieType=this.movieType+' '+res.data.data.type[i].genreName;
						}

						console.log('总类型', this.movieType);
						uni.setStorageSync('movieType', this.movieType);

						uni.navigateTo({
							url: '/pages/movieCinema/movieInfo?movieId=' + ref,
						});
					}
				});
			},

			onClickItem(e) {
				if (this.current != e.currentIndex) {
					this.current = e.currentIndex;
				}
			}
		}

	}
</script>


<style lang="scss" scoped>
	.cinema_main {
		padding-bottom: var(--window-bottom);
	}

	.movie_info {
		color: #ffffff;
		display: flex;
		flex-direction: column;
		justify-content: flex-start;
	}

	::v-deep.uni-list--lg {
		height: 200rpx;
		width: 133rpx;
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

	.buy_button {
		align-self: center;
		height: auto;
		width: auto;
		border-radius: auto;
		color: #010101;
		border-color: #010101;
		background-color: #f9da49;
	}

	.is_buy_hover {
		align-self: center;
		height: auto;
		width: auto;
		border-radius: auto;
		color: #010101;
		border-color: #010101;
		background-color: #d8bc3f;
	}

	.movie_or_cinema {
		padding-left: 2%;
	}

	.movie_text_true {
		color: #f9da49;
		font-size: 100rpx;
	}

	.cinema_text_false {
		font-size: 50rpx;
		color: #999999;
	}

	.movie_text_false {
		font-size: 50rpx;
		color: #999999;
	}

	.cinema_text_true {
		color: #f9da49;
		font-size: 100rpx;
	}

	.segmented-control {
		width: 50%;
		height: 100rpx;
	}
</style>