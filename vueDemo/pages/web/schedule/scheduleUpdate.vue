<template>
	<view class="scheduleUpdate" v-if="this.requestFlag==1">
		<view class="selectSeat" :style="'height:100%;'">
			<movable-area class="house_area" :style="'height:'+(this.houseRow*130+60+130+200)+'rpx;width:100%;'">
				<movable-view class="seat_area" :style="'height:'+(this.houseRow*110+60+130)+'rpx;width:100%;'"
					direction="all" :inertia="true" :scale="true" :scale-min="0.95" :scale-max="2">
					<view class="screen">
						{{this.scheduleData.houseName}}
					</view>
					<view style="width: 100%;height:30rpx;" class="screen_mid">银幕中央</view>
					<view class="seats" v-for="(item,index) in this.seatData" :key="index">
						<view class="seats_for" v-for="(item,indexColumn) in this.seatData[index]" :key="indexColumn">
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
		
		<view class="scheduleINFO">
			<image :src="this.scheduleData.movieUrl" class="movieImage_url"></image>
			<text class="movieNameCn">{{this.scheduleData.movieNameCn}}</text>
			<text class="text_size">时间：{{this.scheduleData.scheduleTime}}</text>
			<text class="text_size">价格：{{this.scheduleData.schedulePrice}} ¥</text>
			<text class="text_size">描述：{{this.scheduleData.scheduleDescription}}</text>
			<text class="text_size">放映厅总座位量：{{this.scheduleData.houseSeats}}</text>
		
		</view>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				requestFlag:0,
				scheduleData:[],
				seatData: [],
				ordersData: [],
				houseRow: '',
				houseColumn: '',
			}
		},
		
		onLoad: function(option){
			this.token = uni.getStorageSync("token");
			uni.request({
				url: '/api/schedule/infoById',
				method: 'GET',
				dataType: 'json',
				data: option,
				header: {
					'Authorization': this.token
				},
				success:(res)=>{
					this.scheduleData=res.data.data;
					
					let schedule = {
						"scheduleId": this.scheduleData.scheduleId,
						"movieId": this.scheduleData.movieId,
						"houseId": this.scheduleData.houseId,
					};
					
					uni.request({
						url: '/api/movie/infoMovieById',
						method: 'GET',
						dataType: 'json',
						data: schedule,
						header: {
							'Authorization': this.token
						},
						success:(res)=>{
							this.scheduleData.movieNameCn=res.data.data.movie.movieNameCn
							this.scheduleData.movieUrl=res.data.data.movie.movieUrl
							uni.request({
								url: '/api/house/infoById',
								method: 'GET',
								dataType: 'json',
								data: schedule,
								header: {
									'Authorization': this.token
								},
								success:(res)=>{
									this.scheduleData.houseName=res.data.data.houseName
									this.scheduleData.houseSeats=res.data.data.houseSeats
									console.log('场次信息',this.scheduleData)
									this.houseRow = parseInt(res.data
												.data.houseX);
									this.houseColumn =parseInt(res.data
												.data.houseY);
									
									
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
										}
									})
									this.requestFlag=1;
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
		},

		methods: {}
	}
</script>

<style lang="scss" scoped>
	
	text{
		color: #fff;
	}
	
	.scheduleUpdate{
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
	
	/* .movieImage{
		display: flex;
		justify-content: center;
		align-items: center;
		width:35%;
		border-right: 3px solid #f9da49;
	}
	
	.movieImage_url{
		width: 100%;
		height:100%;
	} */
	
	.selectSeat{
		display: flex;
		justify-content: center;
		align-items: center;
		width:75%;
		border-right: 3px solid #f9da49;
	}
	
	.screen{
		background-color: #999999;
		width: 380rpx;
		height: 60rpx;
		transform: perspective(34rpx) rotateX(-10deg);
		margin: 0 auto;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 35rpx;
		color: #ffffff;
	}
	
	.screen_mid{
		margin: 0 auto;
		margin-top: 50rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 30rpx;
		margin-bottom: 50rpx;
		color: #ffffff
	}
	
	.seats{
		padding-left: 5%;
		width: 90%;
		height: 110rpx;
		display: flex;
		justify-content: center;
	}
	
	.seats_for{
		width: 100rpx;
		height: 100rpx;
		display: flex;
		justify-content: space-around;
	}
	
	
	.unselectSeat {
		width: 80%;
		height: 100%;
	}
	
	.scheduleINFO{
		display: flex;
		flex-direction:column;
		justify-content: space-between;
		width:30%;
		padding-left: 5%;
		height:90%;
		padding: 2%;
	}
	
	.movieImage_url{
		align-self: center;
		border-radius: 2%;
		width: 400rpx;
		height:600rpx;
	}
	
	.movieNameCn{
		align-self: center;
		color: #f9da49;
		font-size: 100rpx;
	}
	
	.text_size{
		font-size: 50rpx;
	}
	
</style>
