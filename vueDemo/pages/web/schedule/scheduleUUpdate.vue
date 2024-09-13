<template>
	<view class="scheduleCreate" v-if="this.flag1==1&&this.flag2==1&&this.flag3==1&&this.flag4==1">
		<scroll-view class="scroll_MOVIE" scroll-y="true" show-scrollbar="false">
		<view class="movieList" >
			<view class="release_or_not">
					<uni-segmented-control :current="current" :values="items" style-type="text" active-color="#f9da49"
						@clickItem="onClickItem" />
			</view>
			
			<view class="movieINFO" v-show="current === 0" v-for="(item,index) in this.movieData" :key="index">
			<uni-list>
				<uni-list-item :title="this.movieData[index].movieNameCn" ellipsis="1" :note=" this.movieData[index].movieNameEn + 
			'\n导演：' + this.movieData[index].movieDirector +
			'\n上映日期：' + this.movieData[index].movieReleaseDate + 
			'\n时长：' + this.movieData[index].movieDuration + ' min' " :thumb=this.movieData[index].movieUrl
					thumb-size="lg" clickable @click="to_movie_info(this.movieData[index].movieId)">
					<template v-slot:footer>
						<uni-icons v-if="this.scheduleData.movieId==this.movieData[index].movieId" class="chose_movie" type="checkbox-filled" size="30" style="color:#f9da49" ></uni-icons>
						<uni-icons v-if="this.scheduleData.movieId!=this.movieData[index].movieId" class="chose_movie" type="checkbox-filled" size="30" ></uni-icons>
					</template>
				</uni-list-item>
			</uni-list>
			</view>
			
			<view class="movieINFO" v-show="current === 1" v-for="(item,index) in this.movieData_not" :key="index">
			<uni-list>
				<uni-list-item :title="this.movieData_not[index].movieNameCn" ellipsis="1" :note=" this.movieData_not[index].movieNameEn + 
			'\n导演：' + this.movieData_not[index].movieDirector +
			'\n上映日期：' + this.movieData_not[index].movieReleaseDate + 
			'\n时长：' + this.movieData_not[index].movieDuration + ' min' " :thumb=this.movieData_not[index].movieUrl
					thumb-size="lg" clickable @click="to_movie_info(this.movieData_not[index].movieId)">
					<template v-slot:footer>
						<uni-icons v-if="this.scheduleData.movieId==this.movieData_not[index].movieId" class="chose_movie" type="checkbox-filled" size="30" style="color:#f9da49" ></uni-icons>
						<uni-icons v-if="this.scheduleData.movieId!=this.movieData_not[index].movieId" class="chose_movie" type="checkbox-filled" size="30" ></uni-icons>
					</template>
				</uni-list-item>
			</uni-list>
			</view>
			
		</view>
		</scroll-view>
		
		<view class="scheduleINFO">
			<view class="box">
			<uni-forms-item name="scheduleTime">
				<text>场次时间：</text><uni-datetime-picker type="datetime" :clear-icon="false"
					v-model="scheduleData.scheduleTime" @maskClick="maskClick" />
			</uni-forms-item>
			
			<uni-forms-item><text>放映厅：</text>
			<uni-data-select
			        v-model="scheduleData.houseId"
			        :localdata="range"
			        @change="change"
			      ></uni-data-select>
			</uni-forms-item>  
				  
			<uni-forms-item>
			<text>场次价格：</text><uni-easyinput v-model="scheduleData.schedulePrice" placeholder="请输入场次价格" />
			</uni-forms-item>
				  
			<uni-forms-item>
			<text>场次描述：</text><uni-easyinput v-model="scheduleData.scheduleDescription" placeholder="请输入场次描述" />
			</uni-forms-item>
			
			<button v-if="this.scheduleData.movieId!=''
			&& this.scheduleData.scheduleTime!=''
			&& this.scheduleData.houseId!=''
			&& this.scheduleData.schedulePrice!=''
			&& this.scheduleData.scheduleDescription!=''
			" class="create_schedule" size="mini" @click="schedule_create()">修改场次信息</button>
			
			<button v-if="this.scheduleData.movieId==''
			|| this.scheduleData.scheduleTime==''
			|| this.scheduleData.houseId==''
			|| this.scheduleData.schedulePrice==''
			|| this.scheduleData.scheduleDescription==''
			" class="create_schedule_false" size="mini">修改场次信息</button>
			
			
			</view>
		</view>
		
		<view>
			<uni-popup ref="scheduleCreateDialog" type="dialog">
				<uni-popup-dialog :type="error" cancelText="关闭" confirmText="确定" content="是否修改该场次信息" @confirm="scheduleCreateConfirm"
					@close="dialogClose"></uni-popup-dialog>
			</uni-popup>
		</view>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				movieData: [],
				movieData_not: [],
				houseData:[],
				current: 0,
				items: ['正在上映', '待映'],
				range: [],
				value:0,
				flag1:0,
				flag2:0,
				flag3:0,
				flag4:0,
				
				scheduleData:[],
			}
		},
		
		onLoad: function(option){
			this.token = uni.getStorageSync("token");
			this.cinemaId = uni.getStorageSync("cinemaId");
			
			
			uni.request({
				url: '/api/movie/infoMovieByState?movieState=' + 1,
				method: 'GET',
				dataType: 'json',
			
				success: (res) => {
					this.movieData = res.data.data;
					this.movieData.reverse();
					console.log('正在上映', this.movieData);
					this.flag1=1;
				}
			});
			
			uni.request({
				url: '/api/movie/infoMovieByState?movieState=' + 0,
				method: 'GET',
				dataType: 'json',
			
				success: (res) => {
					this.movieData_not = res.data.data;
					console.log('待映', this.movieData_not);
					this.flag2=1;
				}
			});
			
			let cinema = {
				"cinemaId": this.cinemaId,
			};
			
			uni.request({
				url: '/api/house/infoByCinemaId',
				method: 'GET',
				dataType: 'json',
				data: cinema,
				header: {
					'Authorization': this.token
				},
				success:(res)=>{
					this.houseData=res.data.data
					for(let i=0;i<this.houseData.length;i++){
						let temp={};
						temp.value=this.houseData[i].houseId;
						temp.text=(i+1)+"  ："+this.houseData[i].houseName;
						this.range.push(temp);
					}
					console.log('放映厅',this.houseData)
					console.log('选项',this.range)
					this.flag3=1;
				}
			})
			
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
					console.log('scheduleData',this.scheduleData)
					this.flag4=1;
				}
			})
		},

		methods: {
			onClickItem(e) {
				if (this.current != e.currentIndex) {
					this.current = e.currentIndex;
				}
			},
			to_movie_info(movieID){
				console.log('所选电影',movieID);
				this.scheduleData.movieId=movieID;
			},
			change(e) {
				this.scheduleData.houseId=e;
			    console.log("e:", e);
			},
			schedule_create(){
				this.$refs.scheduleCreateDialog.open();
			},
			scheduleCreateConfirm(){
				let schedule = {
					"scheduleId": this.scheduleData.scheduleId,
					"movieId": this.scheduleData.movieId,
					"houseId": this.scheduleData.houseId,
					"schedulePrice": this.scheduleData.schedulePrice,
					"scheduleRemain": 1,
					"scheduleState": 1,
					"scheduleDescription": this.scheduleData.scheduleDescription,
				};
				console.log('schedule',schedule)
				
				uni.request({
					url: '/api/schedule/update?scheduleTime='+this.scheduleData.scheduleTime,
					method: 'PUT',
					dataType: 'json',
					data: schedule,
					header: {
						'Authorization': this.token
					},
					success:(res)=>{
						uni.showToast({
							title: '修改成功',
							icon: 'true',
							mask: 'true',
						})
						uni.reLaunch({
							url: '/pages/web/login'
						})
					}
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	
	text {
		color: #fff;
	}

	.scheduleCreate{
		display: flex;
		flex-direction: row;
		align-items: space-around;
		position: absolute;
		top: 5%;
		left: 10%;
		height: 90%;
		width: 80%;
		border: 3px solid #f9da49;
		border-top-left-radius: 20rpx;
		border-top-right-radius: 20rpx;
		border-bottom-left-radius: 20rpx;
		border-bottom-right-radius: 20rpx;
		overflow: hidden;
	}
	
	.movieList{
		align-items: center;
		width:100%;
		//border-right: 3px solid #f9da49;
		display: flex;
		flex-direction: column;
		justify-content: flex-start;
		
	}
	
	.movieINFO{
		width:100%;
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
	
	::v-deep.uni-scroll-view::-webkit-scrollbar {
		display: none;
	}
	
	.release_or_not{
		width: 100%;
		padding-top: 2%;
		height:100rpx;
	}
	
	.scheduleINFO{
		width:65%;
		height:100%;
		border-left: 3px solid #f9da49;
	}
	
	.box{
		//display: flex;
		//flex-direction:column;
		//justify-items: center;
		//align-items: center;
		height: 90%;
		padding-top: 5%;
		padding-left: 30%;
		width: 40%;
	}
	
	.scroll_MOVIE{
		width:35%;
	}
	
	.chose_movie{
		align-self: center;
		padding-right: 10%;
		color: #f9da49;
	}
	
	.create_schedule{
		width: 100%;
		background-color: #f9da49;
		color: #000000;
	}
	
	.create_schedule_false{
		width: 100%;
		background-color: #999999;
		color: #ffffff;
	}
	
	::v-deep.uni-select__selector-scroll{
			overflow: hidden;
			height: 200px;
	}

</style>
