<template>
	<view class="detailInfo">
		<uni-list>
			<uni-list-item :title="'片名：'+movie.movieNameCn">
			</uni-list-item>
		</uni-list>

		<uni-list>
			<uni-list-item :title="'外文名：'+movie.movieNameEn">
			</uni-list-item>
		</uni-list>

		<uni-list>
			<uni-list-item :title="'导演：'+movie.movieDirector">
			</uni-list-item>
		</uni-list>

		<uni-list>
			<uni-list-item :title="'类型：'+movie.movieType">
			</uni-list-item>
		</uni-list>

		<uni-list>
			<uni-list-item :title="'片源地：'+movie.movieCountry">
			</uni-list-item>
		</uni-list>

		<uni-list>
			<uni-list-item :title="'时长：'+movie.movieDuration+' min'">
			</uni-list-item>
		</uni-list>

		<uni-list>
			<uni-list-item :title="'上映时间：'+movie.movieReleaseDate">
			</uni-list-item>
		</uni-list>


	</view>
</template>

<script>
	export default {
		data() {
			return {
				movie: {
					movieCountry: "",
					movieDirector: "",
					movieDuration: "",
					movieNameCn: "",
					movieNameEn: "",
					movieReleaseDate: "",
					movieType: ""
				},
			}
		},

		onLoad: function(option) {
			this.movieNameCn = uni.getStorageSync("movieNameCn");
			this.movieNameEn = uni.getStorageSync("movieNameEn");
			this.movieReleaseDate = uni.getStorageSync("movieReleaseDate");
			this.movieType = uni.getStorageSync("movieType");
			this.movieCountry = uni.getStorageSync("movieCountry");
			this.movieDirector = uni.getStorageSync("movieDirector");
			this.movieDuration = uni.getStorageSync("movieDuration");

			this.movie.movieNameCn = this.movieNameCn;
			this.movie.movieNameEn = this.movieNameEn;
			this.movie.movieReleaseDate = this.movieReleaseDate;
			this.movie.movieType = this.movieType;
			this.movie.movieCountry = this.movieCountry;
			this.movie.movieDirector = this.movieDirector;
			this.movie.movieDuration = this.movieDuration;

			console.log(this.movie);
		},

		methods: {}
	}
</script>

<style lang="scss" scoped>
	.detailInfo{
	}
	
	::v-deep.uni-list--border-bottom {
		left: 5%;
		width: 90%;
		background-color: #f9da49;
	}
	
	::v-deep.uni-list-item__content-title {
		padding-left: 2%;
		font-size: 35rpx;
		color: #ffffff;
		overflow: hidden;
	}

</style>