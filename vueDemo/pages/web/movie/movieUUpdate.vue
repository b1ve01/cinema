<template>
	<view class="movieCreate">
		<view class="movieURL">
			<uni-file-picker class="movieURLCREATE" v-model="img" limit="1" :del-icon="false" disable-preview
				:imageStyles="imageStyles" file-mediatype="image" @select="update_url">
			</uni-file-picker>
		</view>

		<view class="movieINPUT">
			<uni-forms ref="createForm" :rules="rules_create" :modelValue="movieData">

				<uni-forms-item name="movieReleaseDate">
					<text>上映日期：</text><uni-datetime-picker type="date" :clear-icon="false"
						v-model="movieData.movieReleaseDate" @maskClick="maskClick" />
				</uni-forms-item>

				<uni-forms-item name="movieNameCn">
					<text>电影中文名：</text><uni-easyinput v-model="movieData.movieNameCn" placeholder="请输入电影中文名" />
				</uni-forms-item>

				<uni-forms-item name="movieNameEn">
					<text>电影外文名：</text><uni-easyinput v-model="movieData.movieNameEn" placeholder="请输入外文名" />
				</uni-forms-item>

				<uni-forms-item name="movieDirector">
					<text>导演名：</text><uni-easyinput v-model="movieData.movieDirector" placeholder="请输入导演名">
					</uni-easyinput>
				</uni-forms-item>

				<uni-forms-item name="movieDescription">
					<text>简介：</text><uni-easyinput type="textarea" v-model="movieData.movieDescription"
						placeholder="请输入简介信息" />
				</uni-forms-item>

				<uni-forms-item name="movieDuration">
					<text>电影时长（min）：</text><uni-easyinput v-model="movieData.movieDuration"
						placeholder="请输入电影时长（min）" />
				</uni-forms-item>

				<uni-forms-item name="movieCountry">
					<text>片源地：</text><uni-easyinput v-model="movieData.movieCountry" placeholder="请输入片源地" />
				</uni-forms-item>


			</uni-forms>
		</view>

		<view class="movieInputRight">
			
			<uni-forms-item name="movieState" class="movieState">
			<text>电影状态：</text>
			<uni-data-checkbox selectedColor="#f9da49" mode="button" selectedTextColor="#f9da49" v-model="State"
				:localdata="state" @change="changeState"></uni-data-checkbox>
			</uni-forms-item>
			
			<uni-forms-item name="movieType" class="movieType">
				<text>电影类型：</text>
				<uni-data-checkbox selectedColor="#f9da49" mode="button" selectedTextColor="#f9da49" multiple v-model="movieType" 
				:localdata="type"></uni-data-checkbox>
			</uni-forms-item>
			
			<button v-if="this.movieData.movieNameCn!=''
			&& this.movieData.movieNameEn!=''
			&& this.movieData.movieDirector!=''
			&& this.movieData.movieDescription!=''
			&& this.movieData.movieReleaseDate!=''
			&& this.movieData.movieDuration!=''
			&& this.movieData.movieCountry!=''
			&& this.movieData.movieUrl!=''
			&& this.movieType!=''
			" class="create_movie" size="mini" @click="movie_create()">修改电影</button>
			
			
			
			<button v-if="this.movieData.movieNameCn==''
			|| this.movieData.movieNameEn==''
			|| this.movieData.movieDirector==''
			|| this.movieData.movieDescription==''
			|| this.movieData.movieReleaseDate==''
			|| this.movieData.movieDuration==''
			|| this.movieData.movieCountry==''
			|| this.movieData.movieUrl==''
			|| this.movieType==''
			" class="create_movie_false" size="mini" >修改电影</button>

		</view>

	<view>
		<uni-popup ref="movieCreateDialog" type="dialog">
			<uni-popup-dialog :type="error" cancelText="关闭" confirmText="确定" content="是否修改该电影信息" @confirm="movieCreateConfirm"
				@close="dialogClose"></uni-popup-dialog>
		</uni-popup>
	</view>


	</view>
</template>

<script>
	export default {
		data() {
			return {
				img: {
					url: ''
				},
				movieData:[],
				movieType:[],
				State:-1,
				typeData:'',
				requestFlag:0,
				imageStyles: {
					width: 420,
					height: 600,
					border: {
						color: '#000000;',
						style: 'solid'
					},
				},
				rules_create: {
					movieNameCn: {
						rules: [{
							required: true
						}]
					},
					movieNameEn: {
						rules: [{
							required: true
						}]
					},
					movieDirector: {
						rules: [{
							required: true
						}]
					},
					movieDescription: {
						rules: [{
							required: true
						}]
					},
					movieReleaseDate: {
						rules: [{
							required: true
						}]
					},
					movieDuration: {
						rules: [{
							required: true
						}]
					},
					movieCountry: {
						rules: [{
							required: true
						}]
					},
					movieUrl: {
						rules: [{
							required: true
						}]
					},
					movieState: {
						rules: [{
							required: true
						}]
					},
				},
				state: [{
					text: '待映',
					value: 0
				}, {
					text: '正在上映',
					value: 1
				}],
				type: [{
					text: '剧情',
					value: 1
				}, {
					text: '动作',
					value: 2
				}, {
					text: '冒险',
					value: 3
				}, {
					text: '喜剧',
					value: 4
				}, {
					text: '爱情',
					value: 5
				}, {
					text: '奇幻',
					value: 6
				}, {
					text: '科幻',
					value: 7
				}, {
					text: '惊悚',
					value: 8
				}, {
					text: '悬疑',
					value: 9
				}, {
					text: '战争',
					value: 10
				}, {
					text: '历史',
					value: 11
				}, {
					text: '传记',
					value: 12
				}, {
					text: '音乐',
					value: 13
				}, {
					text: '动画',
					value: 14
				}, {
					text: '运动',
					value: 15
				}, {
					text: '犯罪',
					value: 16
				}],
			}
		},
		
		onLoad: function(option){
			this.token = uni.getStorageSync("token");
			console.log(option);
			uni.request({
				url: '/api/movie/infoMovieById',
				method: 'GET',
				dataType: 'json',
				data: option,
				success:(res)=>{
					this.movieData=res.data.data.movie
					console.log('movieINFO',this.movieData)
					this.requestFlag=1;
					this.img.url=this.movieData.movieUrl
					this.State=parseInt(this.movieData.movieState)
				}
			})
			
		},

		methods: {
			changeState(e) {
				console.log('e:', e);
				this.movieData.movieState=this.State
			},
			movie_create(){
				console.log('movieData',this.movieData.movieReleaseDate);
				this.$refs.movieCreateDialog.open();
			},
			movieCreateConfirm(){
				let movie = {
					"movieId": this.movieData.movieId,
					"movieNameCn": this.movieData.movieNameCn,
					"movieNameEn":this.movieData.movieNameEn,
					"movieDirector":this.movieData.movieDirector,
					"movieDescription":this.movieData.movieDescription,
					"movieDuration":this.movieData.movieDuration,
					"movieCountry":this.movieData.movieCountry,
					"movieUrl":this.movieData.movieUrl,
					"movieState":this.movieData.movieState
				};
				uni.request({
					url: '/api/movie/update?movieReleaseDate='+this.movieData.movieReleaseDate+'&movieType='+this.movieType,
					method: 'PUT',
					dataType: 'json',
					data: movie,
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
	
	.movieCreate {
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
	
	.movieURL {
		width: 35%;
		border-right: 3px solid #f9da49;
	}
	
	.movieURLCREATE {
		height: 100%;
		width: 100%;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	
	.movieINPUT {
		height: 100%;
		width: 20%;
		padding-left: 15%;
		padding-top: 2%;
		//overflow-y: scroll;
	}
	
	.movieInputRight {
		display: flex;
		flex-direction: column;
		align-items: center;
		height: 100%;
		width: 30%;
	}
	
	.movieState{
		padding-top: 6%;
	}
	
	.movieType{
		justify-self: center;
		align-self: center;
		padding-left: 10%;
		padding-top: 20%;
	}
	
	.create_movie{
		top:250rpx;
		width: 60%;
		background-color: #f9da49;
		color: #000000;
	}
	
	.create_movie_false{
		top:250rpx;
		width: 60%;
		background-color: #999999;
		color: #ffffff;
	}
</style>
