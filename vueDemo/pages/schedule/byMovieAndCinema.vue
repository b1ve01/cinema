<template>
	<view class="byMovieAndCinema" v-if="this.allRequest==1">

		<view class="cinemaDetail">
			<uni-list>
				<uni-list-item :title="this.scheduleInfo.cinemaName" ellipsis="2"
					:note=" this.scheduleInfo.cinemaAddress" clickable
					@click="to_map(this.scheduleInfo,this.cinemaLocation)">
					<template v-slot:footer>
						<uni-icons class="mapIcon" type="location-filled" size="30" color="#f9da49"></uni-icons>
					</template>
				</uni-list-item>
			</uni-list>>
		</view>

		<view class="movieDetail">
			<image class="movieImg" :src="this.scheduleInfo.movieUrl"></image>
			<text class="titleGap">" "</text>
			<text class="movieTitle">{{this.scheduleInfo.movieNameCn}}</text>
			<text class="movieTitle0">{{this.scheduleInfo.movieNameEn}}</text>
			<text class="movieNote">{{this.scheduleInfo.movieDuration}} 分钟 | {{this.scheduleInfo.movieType}}</text>
			<text class="titleGap">" "</text>
			<view class="movieLine"></view>
		</view>

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

		<view class="which_cinema" v-for="(item,index) in this.dateItems" :key="index">
			<view class="cinemaList" v-show="dateCurrent === this.dateItems[index]"
				v-for="(item,listIndex) in this.scheduleDataByDate[index]" :key="listIndex">
				<uni-list>
					<uni-list-item
						:title="this.scheduleDataByDate[index][listIndex].scheduleDescription" 
						ellipsis="2"
						:note=" this.scheduleDataByDate[index][listIndex].scheduleFinal.getHours()+' : '+this.scheduleDataByDate[index][listIndex].scheduleFinal.getMinutes()"
						clickable @click="to_buy(this.scheduleDataByDate[index][listIndex].cinemaId)"
						>
						<template v-slot:footer>
							<button class="buy_button" hover-class="is_buy_hover" size="mini">购票</button>
						</template>
						<template v-slot:header>
							<view class="scheduleWhichTime">
								<uni-dateformat class="scheduleTime"
									:date="this.scheduleDataByDate[index][listIndex].scheduleTime"
									format="hh:mm"></uni-dateformat>
								<view class="finalTime">
									<uni-dateformat class="scheduleFinalTime"
										:date="this.scheduleDataByDate[index][listIndex].scheduleFinal"
										format="hh:mm"></uni-dateformat>
									<text class="finalTimeText"> 结束</text>
								</view>
							</view>
						</template>
					</uni-list-item>
				</uni-list>
			</view>
		</view>



	</view>
</template>

<script>
	export default {
		data() {
			return {
				allRequest: 0,
				scheduleData: [{
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
					scheduleFinal: "",
				}],
				scheduleInfo: {
					movieNameCn: "",
					movieNameEn: "",
					movieUrl: "",
					movieDuration: "",
					movieType: "",
					cinemaName: "",
					cinemaAddress: "",
					cinemaX: "",
					cinemaY: ""
				},
				dateItems: [],
				items: [],
				dateCurrent: "",
				current: 0,
				cinemaLocation: {
					longitude: '',
					latitude: '',
				},
				scheduleDataByDate: [],
			}
		},

		onLoad: function(option) {
			this.movieId = uni.getStorageSync("movieId");
			this.movieDuration = uni.getStorageSync("movieDuration");
			this.movieType = uni.getStorageSync("movieType");
			this.scheduleInfo.movieDuration = this.movieDuration;
			this.scheduleInfo.movieType = this.movieType;

			this.cinemaId = uni.getStorageSync("cinemaId");
			console.log('movieId', this.movieId);
			console.log('cinemaId', this.cinemaId);

			let currentDate = new Date();
			// 星期数组
			let weekDays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];

			// 获取星期
			let weekDay = currentDate.getDay();

			let schedule = {
				"movieId": this.movieId,
				"cinemaId": this.cinemaId
			};

			uni.request({
				url: '/api/schedule/infoByMovieAndCinema',
				method: 'GET',
				dataType: 'json',
				data: schedule,
				success: (res) => {
					for (let i = 0; i < res.data.data.length; i++) {
						if (currentDate < new Date(res.data.data[i].scheduleTime)) {
							this.scheduleData.push(res.data.data[i]);
						}
					}

					for (let i = 0; i < this.scheduleData.length; i++) {
						if (this.scheduleData[i].scheduleId == '') {
							this.scheduleData.splice(i, 1);
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

					this.dateCurrent = this.dateItems[0];

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
							scheduleDate: "",
							scheduleDescription: "",
							scheduleId: "",
							schedulePrice: "",
							scheduleRemain: "",
							scheduleState: "",
							scheduleTime: "",
							scheduleFinal: "",
						}];
						for (let j = 0; j < this.scheduleData.length; j++) {
							if (this.dateItems[i] == this.scheduleData[j].scheduleDate) {
								row.push(this.scheduleData[j]);
							}
						}
						this.scheduleDataByDate.push(row);
					}

					for (let i = 0; i < this.scheduleDataByDate.length; i++) {
						for (let j = 0; j < this.scheduleDataByDate[i].length; j++) {
							this.scheduleDataByDate[i][j].scheduleFinal = new Date(this.scheduleDataByDate[
								i][j].scheduleTime);
							let temp_minutes = this.scheduleDataByDate[i][j].scheduleFinal.getMinutes() +
								parseInt(this.scheduleInfo.movieDuration)
							this.scheduleDataByDate[i][j].scheduleFinal.setMinutes(temp_minutes);
						}
					}

					for (let i = 0; i < this.scheduleDataByDate.length; i++) {
						for (let j = 0; j < this.scheduleDataByDate[i].length; j++) {
							if (this.scheduleDataByDate[i][j].scheduleId == '') {
								this.scheduleDataByDate[i].splice(j, 1);
							}
						}
					}

					uni.request({
						url: '/api/movie/infoMovieById',
						method: 'GET',
						dataType: 'json',
						data: schedule,
						success: (res) => {
							this.scheduleInfo.movieNameCn = res.data.data.movie.movieNameCn;
							this.scheduleInfo.movieNameEn = res.data.data.movie.movieNameEn;
							this.scheduleInfo.movieUrl = res.data.data.movie.movieUrl;

							uni.request({
								url: '/api/cinema/infoById',
								method: 'GET',
								dataType: 'json',
								data: schedule,
								success: (res) => {
									this.scheduleInfo.cinemaName = res.data.data
										.cinemaName;
									this.scheduleInfo.cinemaAddress = res.data.data
										.cinemaAddress;
									this.scheduleInfo.cinemaX = res.data.data
										.cinemaX;
									this.scheduleInfo.cinemaY = res.data.data
										.cinemaY;
									console.log('场次信息', this.scheduleData);
									console.log('周几有场次', this.dateItems);
									console.log('选项卡日期', this.items);
									console.log('根据日期分类场次', this
										.scheduleDataByDate);
									console.log('电影和电影院信息', this.scheduleInfo);
									this.allRequest = 1;
								}
							})
						}
					})
				}
			})
		},

		onBackPress: function(option) {
			console.log('退出场次页面');
			uni.removeStorageSync("cinemaId");
		},

		methods: {
			getWeekDay(year, month, day) {
				const date = new Date(year, month - 1, day);
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
		}
	}
</script>

<style lang="scss" scoped>
	.byMovieAndCinema {
		display: flex;
		flex-direction: column;
	}

	.cinemaDetail {
		::v-deep.uni-list-item__content-title {
			font-size: 40rpx;
			color: #f9da49;
			overflow: hidden;
		}

		::v-deep.uni-list--border-bottom {
			width: 100%;
			background-color: #f9da49;
		}

		::v-deep.uni-list-item__content-note {
			margin-top: 20rpx;
			color: #999999;
			font-size: 30rpx;
			overflow: hidden;
			width: 90%;
		}
	}

	.mapIcon {
		align-self: center;
		height: auto;
		width: auto;
	}

	.movieDetail {
		display: flex;
		flex-direction: column;
		align-self: center;
		width: 100%;

		.movieImg {
			align-self: center;
			height: 300rpx;
			width: 200rpx;
			border-radius: 20rpx;
		}

		.movieTitle {
			align-self: center;
			font-size: 50rpx;
			color: #f9da49;
		}

		.movieTitle0 {
			align-self: center;
			font-size: 25rpx;
			color: #999999;
		}

		.movieNote {
			align-self: center;
			font-size: 25rpx;
			color: #999999;
		}

		.titleGap {
			align-self: center;
			font-size: 10rpx;
		}

		.movieLine {
			border-bottom: 1px solid #f9da49;
			align-self: center;
			width: 100%;
		}
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

	.cinemaList {
		color: #ffffff;
		display: flex;
		flex-direction: column;
		justify-content: flex-start;



		::v-deep.uni-list-item__content-title {
			padding-left: 5%;
			font-size: 40rpx;
			color: #f9da49;
			overflow: hidden;
		}

		::v-deep.uni-list--border-top {
			width: 90%;
			left: 5%;
			background-color: #f9da49;
		}

		::v-deep.uni-list-item__content-note {
			margin-top: 5rpx;
			padding-left: 5%;
			color: #999999;
			font-size: 25rpx;
			overflow: hidden;
			width: 90%;
		}

		.scheduleWhichTime {
			display: flex;
			flex-direction: column;
			width: 30%;
		}

		.scheduleTime {
			align-self: center;
			padding-left: 5%;
			font-size: 40rpx;
			color: #f9da49;
			overflow: hidden;
		}
		
		.finalTime{
			align-self: center;
		}

		.scheduleFinalTime {
			align-self: center;
			margin-top: 5rpx;
			color: #999999;
			font-size: 25rpx;
			overflow: hidden;
			width: 90%;
		}
		
		.finalTimeText{
			font-size: 25rpx;
			color: #999999;
		}
		
		.buy_button {
			right:2%;
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
	}
</style>