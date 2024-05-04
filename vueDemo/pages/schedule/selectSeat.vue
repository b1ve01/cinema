<template>
	<view v-if="this.requestFlag==1">
	<view class="selectSeat" :style="'height:'+(this.houseRow*110+60+130+400)+'rpx;'">
		<movable-area class="house_area" :style="'height:'+(this.houseRow*110+60+130)+'rpx;width:100vw;'">
			<movable-view class="seat_area" :style="'height:'+(this.houseRow*110+60+130)+'rpx;width:100vw;'"
				direction="all" :inertia="true" :scale="true" :scale-min="0.95" :scale-max="2">
				<view class="screen">
					{{this.scheduleData.houseName}}
				</view>
				<view style="width: 100vw;height:30rpx;" class="screen_mid">银幕中央</view>
				<view class="seats" v-for="(item,index) in this.seatData" :key="index">
					<view class="seats_for" v-for="(item,indexColumn) in this.seatData[index]" :key="indexColumn"
						@click="selectSeat(index,indexColumn)">
						<image v-if="this.seatData[index][indexColumn].seatState==0 && ((index*this.houseColumn)+(indexColumn+1))<=this.scheduleData.houseSeats" class="unselectSeat"
							src="/static/unselected.png" mode="aspectFit"></image>
						<image v-if="this.seatData[index][indexColumn].seatState==1 && ((index*this.houseColumn)+(indexColumn+1))<=this.scheduleData.houseSeats" class="unselectSeat"
							src="/static/selected.png" mode="aspectFit"></image>
						<image v-if="this.seatData[index][indexColumn].seatState==2 && ((index*this.houseColumn)+(indexColumn+1))<=this.scheduleData.houseSeats" class="unselectSeat"
							src="/static/bought.png" mode="aspectFit"></image>
					</view>
				</view>
			</movable-view>
		</movable-area>
	</view>
	<view class="buy_info" :style="'top:70vh;'">
		<view class="description_seat">
			<view class="description_seat_unselected">
				<image :style="'width:60rpx;height:60rpx'" src="/static/unselected.png" mode="aspectFit"></image><span
					:style="'color: #ffffff;'">可选</span>
			</view>
			<view class="description_seat_unselected">
				<image :style="'width:60rpx;height:60rpx'" src="/static/bought.png" mode="aspectFit"></image><span
					:style="'color: #ffffff;'">已售</span>
			</view>
			<view class="description_seat_unselected">
				<image :style="'width:60rpx;height:60rpx'" src="/static/selected.png" mode="aspectFit"></image><span
					:style="'color: #ffffff;'">选中</span>
			</view>
		</view>
		<view class="show_select" v-if="this.selectCnt>0">
			<button class="show_select_button" v-for="(item,index) in this.selectList" :key="index"
				@click="popSeatList(this.selectList[index])">{{this.selectList[index]}}</button>
		</view>
		<view class="buy_button">
			<button v-if="this.selectCnt>0" class="true_button" hover-class="is_hover"
				@click="scheduleBymovie()">￥{{this.selectPrice}} 确认选座</button>
			<button v-if="this.selectCnt==0" class="false_button">请先选座</button>
		</view>
	</view>
	</view>
	<view>
		<!-- 提示窗示例 -->
		<uni-popup ref="alertDialog" type="dialog">
			<uni-popup-dialog :type="error" cancelText="关闭" confirmText="确定" content="请先登录" @confirm="dialogConfirm"
				@close="dialogClose"></uni-popup-dialog>
		</uni-popup>
	</view>
	<view>
		<!-- 提示窗示例 -->
		<uni-popup ref="buyDialog" type="dialog">
			<uni-popup-dialog :type="error" cancelText="关闭" confirmText="确定" content="请确认购票" @confirm="buyConfirm"
				@close="dialogClose"></uni-popup-dialog>
		</uni-popup>
	</view>
	<view>
		<!-- 提示窗示例 -->
		<uni-popup ref="payDialog" type="dialog">
			<uni-popup-dialog :type="error" cancelText="关闭" confirmText="确定" content="座位已售请重新选座" @confirm="payConfirm"
				@close="dialogClose"></uni-popup-dialog>
		</uni-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				requestFlag:0,
				scheduleData: {},
				seatData: [],
				selectCnt: 0,
				selectPrice: 0,
				selectedData: [],
				ordersData: [],

				houseRow: '',
				houseColumn: '',
				selectList: [],
			}
		},

		onLoad: function(option) {
			this.scheduleId = uni.getStorageSync("scheduleId");
			this.houseId = uni.getStorageSync("houseId");
			this.cinemaId = uni.getStorageSync("cinemaId");
			this.movieId = uni.getStorageSync("movieId");

			let schedule = {
				"scheduleId": this.scheduleId,
				"cinemaId": this.cinemaId,
				"movieId": this.movieId,
				"houseId": this.houseId,
			};

			uni.request({
				url: '/api/schedule/infoById',
				method: 'GET',
				dataType: 'json',
				data: schedule,
				success: (res) => {
					this.scheduleData = res.data.data;

					uni.request({
						url: '/api/cinema/infoById',
						method: 'GET',
						dataType: 'json',
						data: schedule,

						success: (res) => {
							this.scheduleData.cinemaName = res.data.data.cinemaName;

							uni.request({
								url: '/api/movie/infoMovieById',
								method: 'GET',
								dataType: 'json',
								data: schedule,
								success: (res) => {
									this.scheduleData.movieNameCn = res.data.data
										.movie.movieNameCn;

									uni.request({
										url: '/api/house/infoById',
										method: 'GET',
										dataType: 'json',
										data: schedule,
										success: (res) => {
											this.scheduleData
												.houseName = res.data
												.data.houseName;
											this.scheduleData
												.houseSeats = res.data
												.data.houseSeats;

											this.houseRow = Math.ceil(
												Math.sqrt(this
													.scheduleData
													.houseSeats));
											this.houseColumn =
												Math.ceil(Math.sqrt(
													this
													.scheduleData
													.houseSeats));
											let sum = this.houseRow +
												this.houseColumn;

											this.houseRow = Math.ceil((
													(sum) / 25) *
												9)
											this.houseColumn = Math
												.ceil(((sum) / 25) *
													16)

											for (let i = 1; i < (this
													.houseRow + 1
												); i++) {
												let row = [{
													seatState: "",
													seatX: "",
													seatY: ""
												}];
												for (let j = 1; j < (
														this
														.houseColumn +
														1); j++) {
													let temp_row = {
														seatState: "",
														seatX: "",
														seatY: ""
													};
													temp_row
														.seatState = 0;
													temp_row.seatX = j;
													temp_row.seatY = i;
													row.push(temp_row);
												}
												this.seatData.push(
													row);
											}

											for (let i = 0; i < this
												.seatData.length; i++
											) {
												for (let j = 0; j <
													this.seatData[i]
													.length; j++) {
													if (this.seatData[
															i][j]
														.seatState ==
														'' && this
														.seatData[i][j]
														.seatX == '' &&
														this.seatData[
															i][j]
														.seatY == '') {
														this.seatData[
																i]
															.splice(j,
																1);
													}
												}
											}

											uni.request({
												url: '/api/orders/infoByScheduleId',
												method: 'GET',
												dataType: 'json',
												data: schedule,
												success: (res) => {

													this.ordersData =res.data.data;
													console.log('this.ordersData',this.ordersData);
													for (let i =0; i <this.ordersData.length; i++) {

														let boughtCnt =this.ordersData[i].ordersSeat.split(" ").length;
														for (let j =0; j <boughtCnt; j++) {
															let temp_seat =this.ordersData[i].ordersSeat.split(" ")[j];
															let temp_x =temp_seat.split("/")[1] -1;
															let temp_y =temp_seat.split("/")[0] -1;
															this.seatData[temp_y][temp_x].seatState =2;
														}
													}
													this.requestFlag=1;
												},
											})

											console.log(
												'this.houseRow',
												this.houseRow);
											console.log(
												'this.houseColumn',
												this.houseColumn);
											console.log('info', this
												.scheduleData);
											console.log('seatData',
												this.seatData);
										}
									})
								}
							})
						}
					})

				}
			})

		},

		onBackPress: function(option) {
			console.log('退出场次页面');
			uni.removeStorageSync("scheduleId");
			uni.removeStorageSync("houseId");
		},

		methods: {
			selectSeat(x, y) {
				console.log(x + 1, '排', y + 1, '座');
				console.log('数量',(x)*this.houseColumn + (y+1));
				console.log('座位状态', this.seatData[x][y].seatState);
				if (this.seatData[x][y].seatState == 2) {
					return;
				} else {
					if (this.seatData[x][y].seatState == 0 && this.selectCnt < 4) {
						this.seatData[x][y].seatState = 1;
						this.selectCnt++;
						let tmep_info = (x + 1) + '排' + (y + 1) + '座';
						this.selectList.push(tmep_info);
						console.log('this.selectList', this.selectList);
						let temp_price = parseFloat(this.selectPrice) + parseFloat(this.scheduleData.schedulePrice);
						this.selectPrice = temp_price.toFixed(1);
					} else if (this.seatData[x][y].seatState == 1) {
						this.seatData[x][y].seatState = 0;
						this.selectCnt--;
						let tmep_info = (x + 1) + '排' + (y + 1) + '座';
						//array = array.filter(item => item !== valueToRemove);
						this.selectList = this.selectList.filter(item => item !== tmep_info);
						console.log('this.selectList', this.selectList);
						let temp_price = parseFloat(this.selectPrice) - parseFloat(this.scheduleData.schedulePrice);
						this.selectPrice = temp_price.toFixed(1);
					} else {
						uni.showToast({
							title: '最多选座四人',
							icon: 'error',
							mask: 'true',
						})
					}
				}
				console.log('已选座位数量', this.selectCnt);
			},

			popSeatList(res) {
				let temp_x = res[0];
				let x = temp_x - 1;
				let temp_y = res[2];
				let y = temp_y - 1;
				this.selectCnt--;
				this.seatData[x][y].seatState = 0;
				this.selectList = this.selectList.filter(item => item !== res);
				let temp_price = parseFloat(this.selectPrice) - parseFloat(this.scheduleData.schedulePrice);
				this.selectPrice = temp_price.toFixed(1);
			},
			
			scheduleBymovie(){
				this.token = uni.getStorageSync("token");
				if(this.token){
					this.$refs.buyDialog.open();
					console.log('yes');
					
				}else{
					this.$refs.alertDialog.open();
					console.log('no');
				}
				console.log('seatList',this.selectList);
			},
			
			buyConfirm(){
				console.log(this.selectList);
				let selectListStr="";
				for(let i=0;i<this.seatData.length;i++){
					for(let j=0;j<this.seatData[i].length;j++){
						if(this.seatData[i][j].seatState==1){
							selectListStr=selectListStr+this.seatData[i][j].seatY+'/'+this.seatData[i][j].seatX+' ';
						}
					}
				}
				selectListStr=selectListStr.slice(0, -1);
				console.log('selectListStr',selectListStr);
				let schedule = {
					"scheduleId": this.scheduleId,
					"ordersSeat": selectListStr
				};
				console.log('schedule',schedule);
				uni.request({
					url: '/api/orders/infoSeatFlag',
					method: 'GET',
					dataType: 'json',
					data: schedule,
					header: {
						'Authorization': this.token
					},
					success:(res)=>{
						console.log('res',res.data.data);
						if(res.data.data==false){
							this.userId = uni.getStorageSync("userId");
							this.token = uni.getStorageSync("token");
							let price_sum=parseFloat(0);
							for(let i=0;i<this.selectCnt;i++){
								price_sum=parseFloat(price_sum) + parseFloat(this.scheduleData.schedulePrice);
							}
							console.log('选座',selectListStr);
							let pay = {
								"userId": this.userId,
								"scheduleId": this.scheduleId,
								"ordersSeat": selectListStr,
								"ordersState": 1,
								"ordersPrice": price_sum,
							};
							uni.request({
								url: '/api/orders/create',
								method: 'POST',
								dataType: 'json',
								data: pay,
								header: {
									'Authorization': this.token
								},
								success:(res)=>{
									console.log('订单',res.data);
									uni.showToast({
										title: '购票成功',
										icon: 'true',
										mask: 'true',
									})
									uni.removeStorageSync("scheduleId");
									uni.removeStorageSync("houseId");
									uni.navigateBack({
										delta: 1
									});
								}
							})
							
							
						}else{
							this.$refs.payDialog.open();
						}
					}
				})
			},
			
			payConfirm(){
				uni.removeStorageSync("scheduleId");
				uni.removeStorageSync("houseId");
				uni.navigateBack({
					delta: 1
				});
			},
			
			dialogConfirm() {
				uni.clearStorageSync();
				uni.reLaunch({
					url: '/pages/user/login',
				})
			},
			
			dialogClose() {
			},
		
		}
	}
</script>

<style lang="scss" scoped>
	text {
		color: #00aaff;
	}

	.selectSeat {
		width: 100%;
	}

	.screen {
		background-color: #999999;
		width: 380rpx;
		height: 60rpx;
		transform: perspective(34rpx) rotateX(-10deg);
		margin: 0 auto;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 35rpx;
		color: #ffffff
	}

	.screen_mid {
		margin: 0 auto;
		margin-top: 50rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 30rpx;
		margin-bottom: 50rpx;
		color: #ffffff
	}

	.house_area {
		//background-color: #ffffff;
	}

	.seat_area {
		//background-color: #55ffff;
	}

	.seats {
		padding-left: 5%;
		width: 90%;
		height: 110rpx;
		display: flex;
		justify-content: center;
	}

	.seats_for {
		width: 100rpx;
		height: 100rpx;
		display: flex;
		justify-content: space-around;
	}

	.unselectSeat {
		width: 80%;
		height: 100%;
	}

	.buy_info {
		position: fixed;
		height: 30%;
		width: 100%;
		border-top-left-radius: 5%;
		border-top-right-radius: 5%;
		display: flex;
		flex-direction: column;
		background-color: #000000;
		justify-content: space-around;
		border-top: 10rpx solid #f9da49;
	}

	.description_seat {
		height: auto;
		//background-color: #ffffff;
		display: flex;
		justify-content: space-around;
		align-items: center;
		margin: 0 80rpx;
	}

	.description_seat_unselected {
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.buy_button {
		bottom: 0;
	}

	.show_select {
		display: flex;
		flex-direction: row;
		justify-content: center;
		width: 90%;
		padding-left: 5%;
	}

	.true_button {
		bottom: 5%;
		width: 90%;
		color: #010101;
		border-color: #010101;
		background-color: #f9da49;
		bottom: 0;
	}

	.show_select_button {
		color: #f9da49;
		background-color: #3f3f3f;
		size: 30rpx;
		font-size: 30rpx;
	}

	.false_button {
		width: 90%;
		left: 5%;
		color: #999999;
		border-color: #999999;
		background-color: #d8bc3f;
		position: fixed;
		bottom: 5%;
	}

	.is_hover {
		color: #010101;
		border-color: #010101;
		background-color: #d8bc3f;
	}
</style>