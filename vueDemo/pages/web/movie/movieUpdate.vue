<template>
	<view class="movieUpdate" v-if="this.requestFlag==1">
		<view class="movieImage">
			<image :src="this.movieData.movieUrl" class="movieImage_url"></image>
		</view>
			
		<view class="movieDATA">
			<text class="movieNameCn">{{this.movieData.movieNameCn}}</text>
			<text class="movieNameEn">外文名：{{this.movieData.movieNameEn}}</text>
			<text class="movieDirector">导演：{{this.movieData.movieDirector}}</text>
			<scroll-view :scroll-top="scrollTop" scroll-y="true" class="scroll-Y" @scrolltoupper="upper"
								@scrolltolower="lower" @scroll="scroll" :show-scrollbar="false">
			<text class="description">简介：</text>
			<text class="movieDescription">{{this.movieData.movieDescription}}</text>
			</scroll-view>
			
			<text class="movieReleaseDate">上映时间：{{this.movieData.movieReleaseDate.split("/")[0]+' 年 '+this.movieData.movieReleaseDate.split("/")[1]+' 月 '+this.movieData.movieReleaseDate.split("/")[2]+' 日'}}</text>
			<text class="movieType">类型：{{this.typeData}}</text>
			<text class="movieDuration">时长：{{this.movieData.movieDuration}}  min</text>
			<text class="movieCountry">片源地：{{this.movieData.movieCountry}}</text>
			<text class="movieState" v-if="this.movieData.movieState==0">电影状态：待映</text>
			<text class="movieState" v-if="this.movieData.movieState==1">电影状态：正在上映</text>
		</view>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				movieData:[],
				movieType:[],
				typeData:'',
				requestFlag:0,
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
					for(let i=0;i<this.movieType.length;i++){
						this.typeData=this.typeData+this.movieType[i].genreName+' ';
					}
					console.log('this.typeData',this.typeData)
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
	
	.movieImage{
		display: flex;
		justify-content: center;
		align-items: center;
		width:35%;
		border-right: 3px solid #f9da49;
	}
	
	.movieImage_url{
		width: 100%;
		height:100%;
	}
	
	.movieDATA{
		display: flex;
		flex-direction:column;
		justify-content: space-between;
		width:60%;
		padding-left: 5%;
		padding-top: 2%;
		height:90%;
	}
	
	.movieNameCn{
		color: #f9da49;
		font-size: 100rpx;
	}
	
	.movieNameEn{
		font-size: 50rpx;
	}
	
	.movieDirector{
		font-size: 50rpx;
	}
	
	.description{
		font-size: 50rpx;
	}
	
	.movieDescription{
		font-size: 30rpx;
	}
	
	.scroll-Y{
		height:300rpx;
	}
	
	::v-deep.uni-scroll-view::-webkit-scrollbar{
		display: none;
	}
	
	.movieReleaseDate{
		font-size: 50rpx;
	}
	
	.movieType{
		font-size: 50rpx;
	}
	
	.movieDuration{
		font-size: 50rpx;
	}
	
	.movieCountry{
		font-size: 50rpx;
	}
	
	.movieState{
		font-size: 50rpx;
	}
	
	
	
</style>
