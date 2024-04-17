<template>
	<view class="byMovie" v-if="this.items.length!=0">

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

		<view class="which_cinema" v-if="this.cinemaListFlag==1" v-for="(item,index) in this.dateItems" :key="index">
			<view class="cinemaList" v-show="dateCurrent === this.dateItems[index]" v-for="(item,listIndex) in this.scheduleDataByDate[index]" :key="listIndex" >
				<uni-list>
					<uni-list-item v-if="this.scheduleDataByDate[index][listIndex].cinemaDistance<=0.1"
						:title="this.scheduleDataByDate[index][listIndex].cinemaName" ellipsis="2"
						:note=" this.scheduleDataByDate[index][listIndex].cinemaAddress" clickable
						@click="to_buy(this.scheduleDataByDate[index][listIndex].cinemaId)" right-text="< 0.1 km">
					</uni-list-item>
					<uni-list-item v-if="this.scheduleDataByDate[index][listIndex].cinemaDistance>0.1"
						:title="this.scheduleDataByDate[index][listIndex].cinemaName" ellipsis="2"
						:note=" this.scheduleDataByDate[index][listIndex].cinemaAddress" clickable
						@click="to_buy(this.scheduleDataByDate[index][listIndex].cinemaId)"
						:right-text="this.scheduleDataByDate[index][listIndex].cinemaDistance+' km'">
					</uni-list-item>
				</uni-list>
			</view>
		</view>

	</view>

	<view class="byMovieNotSchedule" v-if="this.items.length==0 && this.scheduleFlag==1">
		<text>暂无场次 :(</text>
	</view>



</template>

<script>
	export default {
		data() {
			return {
				scheduleFlag: 0,
				cinemaListFlag:0,

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
					cinemaName: "",
					cinemaAddresss: "",
					cinemaX: "",
					cinemaY: ""
				}],
				dateItems: [],
				items: [],
				current: 0,
				dateCurrent: "",
				cinemaLocation: {
					longitude: '',
					latitude: '',
				},
				scheduleDataByDate: [],
			}
		},

		onShow: function(option) {

			let currentDate = new Date();
			// 星期数组
			let weekDays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];

			// 获取星期
			let weekDay = currentDate.getDay();

			// 打印星期
			//console.log(`当前星期：${weekDays[weekDay]}`);

			this.movieId = uni.getStorageSync("movieId");

			let schedule = {
				"movieId": this.movieId
			};

			uni.request({
				url: '/api/schedule/infoByMovie',
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
							cinemaName: "",
							cinemaAddresss: "",
							cinemaX: "",
							cinemaY: "",
							cinemaDistance: ""
						}];
						let addCinemaId={};
						for (let j = 0; j < this.scheduleData.length; j++) {
							const temp_cinemaId=this.scheduleData[j].cinemaId;
							if (this.dateItems[i] == this.scheduleData[j].scheduleDate) {
								if(!addCinemaId[temp_cinemaId]){
									row.push(this.scheduleData[j]);
									addCinemaId[temp_cinemaId]=true;
								}
							}
						}
						this.scheduleDataByDate.push(row);
					}

					for (let i = 0; i < this.scheduleDataByDate.length; i++) {
						for (let j = 0; j < this.scheduleDataByDate[i].length; j++) {
							this.scheduleDataByDate[i][j].cinemaName = '';
							this.scheduleDataByDate[i][j].cinemaAddress = '';
							this.scheduleDataByDate[i][j].cinemaX = '';
							this.scheduleDataByDate[i][j].cinemaY = '';
							this.scheduleDataByDate[i][j].cinemaDistance = '';
						}
					}

					for (let i = 0; i < this.scheduleDataByDate.length; i++) {
						for (let j = 0; j < this.scheduleDataByDate[i].length; j++) {
							if (this.scheduleDataByDate[i][j].scheduleId == '') {
								this.scheduleDataByDate[i].splice(j, 1);
							}
						}
					}

					uni.getLocation({
						success: (res) => {
							console.log("当前位置的经度：", res.longitude);
							console.log("当前位置的纬度：", res.latitude);
							this.cinemaLocation.longitude = res.longitude;
							this.cinemaLocation.latitude = res.latitude;
							console.log('标记', this.cinemaLocation);

							let requests = [];

							for (let i = 0; i < this.scheduleDataByDate.length; i++) {
								for (let j = 0; j < this.scheduleDataByDate[i].length; j++) {
									let schedule_cinema = {
										"cinemaId": this.scheduleDataByDate[i][j].cinemaId
									};
									let request = new Promise((resolve, reject) => {
										uni.request({
											url: '/api/cinema/infoById',
											method: 'GET',
											dataType: 'json',
											data: schedule_cinema,
											success: (res) => {
												this.scheduleDataByDate[i][
														j
													]
													.cinemaName =
													res
													.data
													.data.cinemaName;
												this.scheduleDataByDate[i][
														j
													]
													.cinemaAddress =
													res
													.data.data
													.cinemaAddress;
												this.scheduleDataByDate[i][
														j
													].cinemaX =
													res
													.data
													.data.cinemaX;
												this.scheduleDataByDate[i][
														j
													].cinemaY =
													res
													.data
													.data.cinemaY;
												resolve();
											}
										})
									});
									requests.push(request);
								}
							}
							console.log('请求',requests);
							Promise.all(requests).then(()=>{
								console.log('等待成功');
								for(let i = 0; i < this.scheduleDataByDate.length; i++){
									for (let j = 0; j < this.scheduleDataByDate[i].length; j++){
										let distance = this.getDistance(this.cinemaLocation, this
											.scheduleDataByDate[i][j].cinemaX, this
											.scheduleDataByDate[i][j].cinemaY);
										this.scheduleDataByDate[i][j].cinemaDistance = distance;
									}
								}
								for(let i = 0; i < this.scheduleDataByDate.length; i++){
									for (let j = 0; j < this.scheduleDataByDate[i].length; j++){
										this.scheduleDataByDate[i].sort((a, b) => a.cinemaDistance - b
											.cinemaDistance);
									}
								}
								this.cinemaListFlag=1;
							})
						}
					})
					console.log('Items', this.items);
					console.log('dateItems', this.dateItems);
					console.log('scheduleData', this.scheduleData);
					console.log('scheduleDataByDate', this.scheduleDataByDate);
					this.scheduleFlag = 1;
				}
			});
		},
		
		onHide: function(option){
			this.scheduleFlag=0;
			this.cinemaListFlag=0,
			this.scheduleData=[];
			this.dateItems=[];
			this.items=[];
			this.current=0;
			this.dateCurrent="";
			this.cinemaLocation={};
			this.scheduleDataByDate=[];
			console.log('页面隐藏');
		},

		methods: {

			onClickItem(e) {
				if (this.current != e.currentIndex) {
					this.current = e.currentIndex;
					this.dateCurrent = this.dateItems[this.current];
					console.log('dateCurrent', this.dateCurrent);
				}
			},

			getWeekDay(year, month, day) {
				const date = new Date(year, month-1, day);
				const weekday = date.getDay();
				return weekday;
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
			
			to_buy(ref){
				uni.setStorageSync('cinemaId', ref);
				uni.navigateTo({
					url: '/pages/schedule/byMovieAndCinema',
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

	.byMovieNotSchedule {
		display: flex;
		justify-content: center;
		align-items: center;
		height: 100%;
		font-size: 200%;
	}
	
	.cinemaList{
		color: #ffffff;
		display: flex;
		flex-direction: column;
		justify-content: flex-start;
		
		::v-deep.uni-list-item__content-title {
			font-size: 40rpx;
			color: #f9da49;
			overflow: hidden;
		}
		
		::v-deep.uni-list--border-bottom {
			width: 96%;
			left: 2%;
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
	
</style>