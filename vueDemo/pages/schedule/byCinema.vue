<template>
	<view class="byCinema" v-if="this.items.length!=0">
		<view class="which_date">
			<scroll-view v-if="this.items.length==1" scroll-x="true" show-scrollbar="false">
				<uni-segmented-control class="date1" :current="current" :values="items" style-type="text"
					active-color="#f9da49" @clickItem="onClickItem" />
			</scroll-view>
		
			<scroll-view v-if="this.items.length==2" scroll-x="true" show-scrollbar="false">
				<uni-segmented-control class="date2" :current="current" :values="items" style-type="text"
					active-color="#f9da49" @clickItem="onClickItem" />
			</scroll-view>
		
			<scroll-view v-if="this.items.length==3" scroll-x="true" show-scrollbar="false">
				<uni-segmented-control class="date3" :current="current" :values="items" style-type="text"
					active-color="#f9da49" @clickItem="onClickItem" />
			</scroll-view>
		
			<scroll-view v-if="this.items.length==4" scroll-x="true" show-scrollbar="false">
				<uni-segmented-control class="date4" :current="current" :values="items" style-type="text"
					active-color="#f9da49" @clickItem="onClickItem" />
			</scroll-view>
		
			<scroll-view v-if="this.items.length==5" scroll-x="true" show-scrollbar="false">
				<uni-segmented-control class="date5" :current="current" :values="items" style-type="text"
					active-color="#f9da49" @clickItem="onClickItem" />
			</scroll-view>
		
			<scroll-view v-if="this.items.length==6" scroll-x="true" show-scrollbar="false">
				<uni-segmented-control class="date6" :current="current" :values="items" style-type="text"
					active-color="#f9da49" @clickItem="onClickItem" />
			</scroll-view>
		
			<scroll-view v-if="this.items.length==7" scroll-x="true" show-scrollbar="false">
				<uni-segmented-control class="date7" :current="current" :values="items" style-type="text"
					active-color="#f9da49" @clickItem="onClickItem" />
			</scroll-view>
		</view>
		
		<view class="which_movie" v-if="this.movieListFlag==1" v-for="(item,index) in this.dateItems" :key="index">
			<view class="movieList" v-show="dateCurrent === this.dateItems[index]" v-for="(item,listIndex) in this.scheduleDataByDate[index]" :key="listIndex" >
				<uni-list>
					<uni-list-item show-arrow="true" :title="this.scheduleDataByDate[index][listIndex].movieNameCn" ellipsis="1" :note=" this.scheduleDataByDate[index][listIndex].movieNameEn +
					'\n导演：' + this.scheduleDataByDate[index][listIndex].movieDirector +
					'\n上映日期：' + this.scheduleDataByDate[index][listIndex].movieReleaseDate + 
					'\n时长：' + this.scheduleDataByDate[index][listIndex].movieDuration + ' min' " :thumb=this.scheduleDataByDate[index][listIndex].movieUrl
							thumb-size="lg" clickable @click="to_buy(index,listIndex)">						
					</uni-list-item>
				</uni-list>
			</view>
		</view>
	</view>
	
	<view class="byMovieNotSchedule" v-if="this.items.length==0 && this.movieListFlag==1">
		<text>暂无场次 :(</text>
	</view>
	
</template>

<script>
	export default {
		data() {
			return {
				scheduleFlag: 0,
				movieListFlag:0,
				
				scheduleData:[],
				dateItems:[],
				dateCurrent: "",
				items: [],
				current: 0,
				scheduleDataByDate: [],
			}
		},
		
		onShow: function(option){
			let currentDate = new Date();
			currentDate.setMinutes(currentDate.getMinutes() + 15);
			// 星期数组
			let weekDays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
			
			this.cinemaId = uni.getStorageSync("cinemaId");
			console.log('this.cinemaId',this.cinemaId)
			
			
			let schedule = {
				"cinemaId": this.cinemaId
			};
			
			let baseURL = '/api';
			// #ifdef APP-PLUS
			baseURL = "http://cinema.nat100.top"
			// #endif
			
			uni.request({
				url: baseURL+'/schedule/infoByCinema',
				method: 'GET',
				dataType: 'json',
				data: schedule,
				success:(res)=>{
					
					for(let i=0;i<res.data.data.length;i++){
						if (currentDate < new Date(res.data.data[i].scheduleTime)){
							this.scheduleData.push(res.data.data[i]);
						}
					}
					
					for (let i = 0; i < this.scheduleData.length; i++) {
						this.scheduleData[i].scheduleTime = new Date(this.scheduleData[i].scheduleTime);
						this.scheduleData[i].scheduleDate = new Date(this.scheduleData[i].scheduleTime)
							.getFullYear() + '/' + (new Date(this.scheduleData[i].scheduleTime)
								.getMonth() + 1) +
							'/' + new Date(this.scheduleData[i].scheduleTime).getDate();
					}
					
					for (let i = 0; i < this.scheduleData.length; i++) {
						if (!this.dateItems.includes(this.scheduleData[i].scheduleDate) && this.dateItems
							.length < 8) {
							this.dateItems.push(this.scheduleData[i].scheduleDate);
						}
					}
					
					this.dateCurrent=this.dateItems[0];
					
					let currentDateString = currentDate.getFullYear() + '/' + (currentDate.getMonth() +
							1) +
						'/' + currentDate.getDate();
					console.log('currentDateString', currentDateString);
					
					for (let i = 0; i < this.dateItems.length; i++) {
						if (this.dateItems[i] == currentDateString) {
							this.items.push('今天 ' + this.dateItems[i].split("/")[1] +
								'月' + this.dateItems[i].split("/")[2] + '日')
						} else {
							this.items.push(weekDays[this.getWeekDay(this.dateItems[i].split("/")[0], this
									.dateItems[i].split("/")[1], this.dateItems[i].split("/")[2])] +
								' ' + this.dateItems[i].split("/")[1] +
								'月' + this.dateItems[i].split("/")[2] + '日')
						}
					}
					
					for (let i = 0; i < this.dateItems.length; i++) {
						let row = [{
							cinemaId: "",
							houseId: "",
							movieId: "",
							scheduleDescription: "",
							scheduleId: "",
							schedulePrice: "",
							scheduleRemain: "",
							scheduleState: "",
							scheduleTime: "",
							scheduleDate: "",
							movieNameCn: "",
							movieNameEn: "",
							movieUrl: "",
							movieDirector: "",
							movieRealeasDate: "",
							movieDuration: ""
						}];
						let addCinemaId={};
						for (let j = 0; j < this.scheduleData.length; j++) {
							const temp_movieId=this.scheduleData[j].movieId;
							if (this.dateItems[i] == this.scheduleData[j].scheduleDate) {
								if(!addCinemaId[temp_movieId]){
									row.push(this.scheduleData[j]);
									addCinemaId[temp_movieId]=true;
								}
							}
						}
						this.scheduleDataByDate.push(row);
					}
					
					for (let i = 0; i < this.scheduleDataByDate.length; i++) {
						for (let j = 0; j < this.scheduleDataByDate[i].length; j++) {
							this.scheduleDataByDate[i][j].movieNameCn = '';
							this.scheduleDataByDate[i][j].movieNameEn = '';
							this.scheduleDataByDate[i][j].movieUrl = '';
							this.scheduleDataByDate[i][j].movieDirector = '';
							this.scheduleDataByDate[i][j].movieRealeasDate = '';
							this.scheduleDataByDate[i][j].movieDuration = '';
						}
					}
					
					for (let i = 0; i < this.scheduleDataByDate.length; i++) {
						for (let j = 0; j < this.scheduleDataByDate[i].length; j++) {
							if (this.scheduleDataByDate[i][j].scheduleId == '') {
								this.scheduleDataByDate[i].splice(j, 1);
							}
						}
					}
					
					let requests = [];
					for(let i = 0; i < this.scheduleDataByDate.length; i++){
						for(let j = 0; j < this.scheduleDataByDate[i].length; j++){
							let schedule_movie = {
								"movieId": this.scheduleDataByDate[i][j].movieId
							};
							
							let request = new Promise((resolve, reject) => {
								
								let baseURL = '/api';
								// #ifdef APP-PLUS
								baseURL = "http://cinema.nat100.top"
								// #endif
								
								uni.request({
									url: baseURL+'/movie/infoMovieById',
									method: 'GET',
									dataType: 'json',
									data: schedule_movie,
									success:(res)=>{
										this.scheduleDataByDate[i][j].movieType = '';
										this.scheduleDataByDate[i][j].movieNameCn=res.data.data.movie.movieNameCn
										this.scheduleDataByDate[i][j].movieNameEn=res.data.data.movie.movieNameEn
										this.scheduleDataByDate[i][j].movieUrl=res.data.data.movie.movieUrl
										this.scheduleDataByDate[i][j].movieDirector=res.data.data.movie.movieDirector
										this.scheduleDataByDate[i][j].movieRealeasDate=res.data.data.movie.movieRealeasDate
										this.scheduleDataByDate[i][j].movieDuration=res.data.data.movie.movieDuration
										
										for (let k = 0; k < res.data.data.type.length; k++) {
											this.scheduleDataByDate[i][j].movieType = this.scheduleDataByDate[i][j].movieType + ' ' + res.data.data.type[k].genreName;
										}
										
										resolve();
									}
								})
							})
							requests.push(request);
						}
					}
					Promise.all(requests).then(()=>{
						this.movieListFlag=1;
						console.log('this.scheduleData',this.scheduleData)
						console.log('this.dateItems',this.dateItems)
						console.log('this.items',this.items)
						console.log('this.scheduleDataByDate',this.scheduleDataByDate)
					})
				}
			})
		},
		
		onHide: function(option){
			this.scheduleFlag=0;
			this.movieListFlag=0,
			this.scheduleData=[];
			this.dateItems=[];
			this.items=[];
			this.current=0;
			this.dateCurrent="";
			this.scheduleDataByDate=[];
			console.log('页面隐藏');
		},
		
		onBackPress: function(option){
			uni.removeStorageSync("cinemaId");
		},

		methods: {
			getWeekDay(year, month, day) {
				const date = new Date(year, month-1, day);
				const weekday = date.getDay();
				return weekday;
			},
			
			onClickItem(e) {
				if (this.current != e.currentIndex) {
					this.current = e.currentIndex;
					this.dateCurrent = this.dateItems[this.current];
					console.log('dateCurrent', this.dateCurrent);
				}
			},
			
			to_buy(index,listIndex){
				uni.setStorageSync('movieId', this.scheduleDataByDate[index][listIndex].movieId);
				uni.setStorageSync('movieDuration', this.scheduleDataByDate[index][listIndex].movieDuration);
				uni.setStorageSync('movieType', this.scheduleDataByDate[index][listIndex].movieType);
				uni.navigateTo({
					url: '/pages/schedule/byCinemaAndMovie',
					animationType: 'pop-in',
					animationDuration: 200
				});
			}
		}
	}
</script>

<style lang="scss" scoped>
	text {
		color: #999999;
	}
	
	.date1 {
		width: 100%;
		height: 100rpx;
	}
	
	.date2 {
		width: 100%;
		height: 100rpx;
	}
	
	.date3 {
		width: 100%;
		height: 100rpx;
	}
	
	.date4 {
		margin-left: 10rpx;
		margin-right: 10rpx;
		margin-bottom: 40rpx;
		width: 130%;
		height: 100rpx;
	}
	
	.date5 {
		margin-left: 10rpx;
		margin-right: 10rpx;
		margin-bottom: 40rpx;
		width: 160%;
		height: 100rpx;
	}
	
	.date6 {
		margin-left: 10rpx;
		margin-right: 10rpx;
		margin-bottom: 40rpx;
		width: 190%;
		height: 100rpx;
	}
	
	.date7 {
		margin-left: 10rpx;
		margin-right: 10rpx;
		margin-bottom: 40rpx;
		width: 220%;
		height: 100rpx;
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
	
	::v-deep.uni-list--border-bottom {
		left: 4%;
		width: 92%;
		background-color: #f9da49;
	}
	
	::v-deep.uni-icon-wrapper{
		padding-right: 5%;
	}
	
	.byMovieNotSchedule {
		display: flex;
		justify-content: center;
		align-items: center;
		height: 100%;
		font-size: 200%;
	}
	
	
</style>
