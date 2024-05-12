<template>
	<view class="showOrder" v-if="this.requestFlag==1 && this.ordersData!='' " >
	<view  v-for="(item,index) in this.ordersData" :key="index">
		<uni-card :title="this.ordersData[index].cinemaName" 
		:sub-title="this.ordersData[index].movieNameCn+'\n\n'+this.ordersData[index].scheduleTime+'\n\n'+this.ordersData[index].houseName+'\n'+this.ordersData[index].seatString" :thumbnail="this.ordersData[index].movieUrl">
			<text class="uni-body">总价：{{this.ordersData[index].ordersPrice}} 元</text>
		</uni-card>
	</view>
	</view>
	
	<view class="noOrders" v-if="this.notFlag==1 && this.ordersData=='' ">
		<text>尚无购票记录</text>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				ordersData: [],
				requestFlag:0,
				notFlag:0,
			}
		},

		onLoad: function(option) {
			this.userId = uni.getStorageSync("userId");
			this.token = uni.getStorageSync("token");

			let orders = {
				"userId": this.userId,
			};
			
			let baseURL = '/api';
			// #ifdef APP-PLUS
			baseURL = "http://cinema.nat100.top"
			// #endif

			uni.request({
				url: baseURL+'/orders/infoByUserId',
				method: 'GET',
				dataType: 'json',
				data: orders,
				header: {
					'Authorization': this.token
				},

				success: (res) => {
					this.ordersData = res.data.data;
					console.log('orders', this.ordersData.length);
					
					
					for(let i=0;i<this.ordersData.length;i++){
						let schedule = {
							"scheduleId": this.ordersData[i].scheduleId,
						};
						
						let baseURL = '/api';
						// #ifdef APP-PLUS
						baseURL = "http://cinema.nat100.top"
						// #endif
						
						uni.request({
							url: baseURL+'/schedule/infoById',
							method: 'GET',
							dataType: 'json',
							data: schedule,
							success:(res)=>{
								this.ordersData[i].cinemaId=res.data.data.cinemaId;
								this.ordersData[i].movieId=res.data.data.movieId;
								this.ordersData[i].houseId=res.data.data.houseId;
								this.ordersData[i].scheduleDescription=res.data.data.scheduleDescription;
								this.ordersData[i].scheduleTime=res.data.data.scheduleTime;
								
								let idObject = {
									"cinemaId": this.ordersData[i].cinemaId,
									"movieId": this.ordersData[i].movieId,
									"houseId": this.ordersData[i].houseId,
								};
								
								let baseURL = '/api';
								// #ifdef APP-PLUS
								baseURL = "http://cinema.nat100.top"
								// #endif
								
								uni.request({
									url: baseURL+'/movie/infoMovieById',
									method: 'GET',
									dataType: 'json',
									data: idObject,
									success:(res)=>{
										this.ordersData[i].movieNameCn=res.data.data.movie.movieNameCn;
										this.ordersData[i].movieUrl=res.data.data.movie.movieUrl;
										
										let baseURL = '/api';
										// #ifdef APP-PLUS
										baseURL = "http://cinema.nat100.top"
										// #endif
										
										uni.request({
											url: baseURL+'/cinema/infoById',
											method: 'GET',
											dataType: 'json',
											data: idObject,
											success:(res)=>{
												this.ordersData[i].cinemaName=res.data.data.cinemaName
												this.ordersData[i].cinemaPhone=res.data.data.cinemaPhone
												
												let baseURL = '/api';
												// #ifdef APP-PLUS
												baseURL = "http://cinema.nat100.top"
												// #endif
												
												uni.request({
													url: baseURL+'/house/infoById',
													method: 'GET',
													dataType: 'json',
													data: idObject,
													success:(res)=>{
														this.ordersData[i].houseName=res.data.data.houseName
														this.ordersData[i].seatString="";
														for(let j=0;j<this.ordersData[i].ordersSeat.split(" ").length;j++){
															this.ordersData[i].seatString=this.ordersData[i].seatString+this.ordersData[i].ordersSeat.split(" ")[j].split("/")[0]+'排'+this.ordersData[i].ordersSeat.split(" ")[j].split("/")[1]+'座  ';
														}
														if(i==this.ordersData.length-1){
															this.requestFlag=1;
														}
														console.log('yes')
													}
												})
											}
										})
									}
									
								})
								
								
							}	
						})
					}
			console.log('card',this.ordersData);
			this.notFlag=1;
				}
			})
		},

		methods: {
			
			
		}
	}
</script>

<style lang="scss" scoped>
	text {
		color: #999999;
	}
	
	.showOrder{
		padding-bottom: 2%;
		padding-left: 2%;
		padding-right: 2%;
	}
	
	.uni-card{
		background-color: #000000;
		border-color:  #f9da49;
	}
	
	.noOrders{
		display: flex;
		justify-content: center;
		align-items: center;
		height: 100%;
		font-size: 200%;
	}

</style>