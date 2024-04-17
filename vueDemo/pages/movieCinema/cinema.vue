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

		<view v-if="isCinema" class="cinema_info" v-for="(item,index) in this.cinemaAfter" :key="index">
			<uni-list>
				<uni-list-item v-if="this.cinemaAfter[index].cinemaDistance<=0.1"
					:title="this.cinemaAfter[index].cinemaName" ellipsis="2"
					:note=" this.cinemaAfter[index].cinemaAddress" clickable
					@click="to_cinema_info(this.cinemaAfter[index].cinemaId)" right-text="< 0.1 km">
				</uni-list-item>
				<uni-list-item v-if="this.cinemaAfter[index].cinemaDistance>0.1"
					:title="this.cinemaAfter[index].cinemaName" ellipsis="2"
					:note=" this.cinemaAfter[index].cinemaAddress" clickable
					@click="to_cinema_info(this.cinemaAfter[index].cinemaId)"
					:right-text="this.cinemaAfter[index].cinemaDistance+' km'">
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
				cinemaData: [],
				cinemaAfter: [{
					cinemaAddress: "",
					cinemaId: "",
					cinemaName: "",
					cinemaPassword: "",
					cinemaPhone: "",
					cinemaX: "",
					cinemaY: "",
					cinemaDistance: ""
				}],
				cinemaLocation: {
					longitude: '',
					latitude: '',
				},

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

			uni.getLocation({
				success: (res) => {
					console.log("当前位置的经度：", res.longitude);
					console.log("当前位置的纬度：", res.latitude);
					this.cinemaLocation.longitude = res.longitude;
					this.cinemaLocation.latitude = res.latitude;
					console.log('标记', this.cinemaLocation);

					uni.request({
						url: '/api/cinema/infoAllCinema',
						method: 'GET',
						dataType: 'json',
						success: (res) => {
							console.log(res.data);
							this.cinemaData = res.data.data;
							console.log('电影院列表', this.cinemaData);
							for (let i = 0; i < this.cinemaData.length; i++) {
								let distance = this.getDistance(this.cinemaLocation, this
									.cinemaData[i].cinemaX, this.cinemaData[i].cinemaY);

								this.cinemaAfter[i] = this.cinemaData[i];
								this.cinemaAfter[i].cinemaDistance = distance;
							}
							// 根据cinemaDistance对cinemaAfter进行排序
							this.cinemaAfter.sort((a, b) => a.cinemaDistance - b
								.cinemaDistance);
							console.log('cinemaAfter sorted', this.cinemaAfter);
						}
					})
				}
			})

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
						this.movieType = '';
						console.log('调用成功', res.data.data.movie);
						uni.setStorageSync('movieId', res.data.data.movie.movieId);
						uni.setStorageSync('movieNameCn', res.data.data.movie.movieNameCn);
						uni.setStorageSync('movieNameEn', res.data.data.movie.movieNameEn);
						uni.setStorageSync('movieDirector', res.data.data.movie.movieDirector);
						uni.setStorageSync('movieReleaseDate', res.data.data.movie.movieReleaseDate);
						uni.setStorageSync('movieDuration', res.data.data.movie.movieDuration);
						uni.setStorageSync('movieUrl', res.data.data.movie.movieUrl);
						uni.setStorageSync('movieDescription', res.data.data.movie.movieDescription);
						uni.setStorageSync('movieCountry', res.data.data.movie.movieCountry);

						for (let i = 0; i < res.data.data.type.length; i++) {
							this.movieType = this.movieType + ' ' + res.data.data.type[i].genreName;
						}

						console.log('总类型', this.movieType);
						uni.setStorageSync('movieType', this.movieType);

						uni.navigateTo({
							url: '/pages/movieCinema/movieInfo?movieId=' + ref,
							animationType: 'pop-in',
							animationDuration: 200
						});
					}
				});
			},

			onClickItem(e) {
				if (this.current != e.currentIndex) {
					this.current = e.currentIndex;
				}
			},

			getDistance(cinemaLocation, c_x, c_y) {
				const earthRadius = 6378137; // 地球半径，单位：米
				const radLat1 = cinemaLocation.latitude * Math.PI / 180;
				const radLat2 = c_y * Math.PI / 180;
				const a = radLat1 - radLat2;
				const b = cinemaLocation.longitude * Math.PI / 180 - c_x * Math.PI / 180;
				const s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) *
					Math.pow(Math.sin(b / 2), 2)));
				return (s * earthRadius / 1000).toFixed(1);
			},
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

	.cinema_info {
		color: #ffffff;
		display: flex;
		flex-direction: column;
		justify-content: flex-start;

		::v-deep.uni-list-item__content-title {
			font-size: 40rpx;
			color: #f9da49;
			overflow: hidden;
		}

		;

		::v-deep.uni-list--border-top {
			background-color: #000000;
		}

		;

		::v-deep.uni-list--border-bottom {
			width: 96%;
			left: 2%;
			background-color: #f9da49;
		}

		;

		::v-deep.uni-list-item__content-note {
			margin-top: 20rpx;
			color: #999999;
			font-size: 30rpx;
			overflow: hidden;
			width: 90%;
		}
	}

	::v-deep.uni-list--lg {
		height: 225rpx;
		width: 150rpx;
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
		left: 4%;
		width: 92%;
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
		font-size: 80rpx;
	}

	.cinema_text_false {
		font-size: 40rpx;
		color: #999999;
	}

	.movie_text_false {
		font-size: 40rpx;
		color: #999999;
	}

	.cinema_text_true {
		color: #f9da49;
		font-size: 80rpx;
	}

	.segmented-control {
		width: 50%;
		height: 100rpx;
	}
</style>