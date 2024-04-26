<template>
	<view v-if="!isLogin" class="isLogin_false">
		//登录页面
		<view v-if="!isRegister" class="login">
			<view class="login_text">
				<view class="info_login_register">
					<text class="login_text_true" v-if="!isRegister" @click="isRegister=true">登录</text>
				</view>

				<view class="info_icon">
					<uni-icons type="contact-filled" size=150rpx color=#999999></uni-icons>
				</view>
			</view>

			<view class="login_example">
				<!-- 基础表单校验 -->
				<uni-forms ref="loginForm" :rules="rules_login" :modelValue="loginData">
					<uni-forms-item name="cinemaPhone">
						<uni-easyinput prefixIcon="phone-filled" v-model="loginData.cinemaPhone" placeholder="请输入电话号码" />
					</uni-forms-item>
					<uni-forms-item name="cinemaPassword">
						<uni-easyinput prefixIcon="locked-filled" type="password" v-model="loginData.cinemaPassword"
							placeholder="请输入密码" />
					</uni-forms-item>
				</uni-forms>
				<button class="false_button" v-if="!loginData.cinemaPhone||!loginData.cinemaPassword" @click="err_submit()"
					hover-class="is_errhover">登录</button>
				<button class="true_button" v-if="loginData.cinemaPhone&&loginData.cinemaPassword"
					@click="submit_login('loginForm')" hover-class="is_hover">登录</button>
			</view>
		</view>
	</view>


	<!--影院账号-->
	<view v-if="isLogin  && this.cinemaId!=-1" class="isCinema_true">
		<view class="cinema_tabbar">
			<view class="cinema_admin">
				<uni-list>
				<uni-list-item :show-extra-icon="true" :extra-icon="adminIcon" title="影院管理系统" />
				<view class="border_line"></view>
				</uni-list>
			</view>
			
			<view class="tabbar_info">
				<uni-list>
				<view class="border_line"></view>
				<uni-list-item v-if="this.webState!=0" :show-extra-icon="true"  :extra-icon="cinemaIcon" title="影院信息" clickable
					@click="to_cinema()"/>
				<uni-list-item v-if="this.webState==0" class="chose_cinema" :show-extra-icon="true"  :extra-icon="chose_cinemaIcon" title="影院信息" clickable
					@click="to_cinema()"/>
				<view class="border_line"></view>
				
				<uni-list-item v-if="this.webState!=2" :show-extra-icon="true" :extra-icon="houseIcon" title="放映厅信息管理" clickable
					@click="to_house()"/>
				<uni-list-item v-if="this.webState==2" class="chose_house" :show-extra-icon="true" :extra-icon="chose_houseIcon" title="放映厅信息管理" clickable
					@click="to_house()"/>
				<view class="border_line"></view>
				
				<uni-list-item v-if="this.webState!=3" :show-extra-icon="true"  :extra-icon="scheduleIcon" title="场次信息管理" clickable
					@click="to_schedule()"/>
				<uni-list-item v-if="this.webState==3" class="chose_schedule" :show-extra-icon="true"  :extra-icon="chose_scheduleIcon" title="场次信息管理" clickable
					@click="to_schedule()"/>	
				<view class="border_line"></view>
				
				</uni-list>
			</view>	
			
		</view>
		<view class="isLogin_true" v-if="this.webState==0">
			<view class="isLogin_1">
				<uni-list>
				<uni-list-item :show-extra-icon="true" :extra-icon="infoIcon" :title="this.cinemaData.cinemaName+'\n'+this.cinemaData.cinemaAddress+'\n电话：'+this.cinemaData.cinemaPhone"
				/>
				</uni-list>
			</view>
			<view class="isLogin_2">
				<uni-list >
				<view class="border_line"></view>
				<uni-list-item :show-extra-icon="true"  :extra-icon="orderIcon" title="影院订单" 
				clickable @click="to_orders()"/>
				
				<view class="border_line"></view>
				
				<uni-list-item :show-extra-icon="true"  :extra-icon="setIcon" title="设定" />
				<view class="border_line"></view>
				</uni-list>
			</view>
			<view class="isLogin_3">
				<uni-list>
				<uni-list-item :show-extra-icon="true" :extra-icon="logoutIcon" title="登出" clickable
					@click="to_logout()" />
					<view class="border_line"></view>
					</uni-list>
			</view>
		</view>
	</view>
	
	<!--admin账号-->
	<view v-if="isLogin  && this.cinemaId == -1" class="isCinema_true">
		<view class="cinema_tabbar">
			<view class="cinema_admin">
				<uni-list>
				<uni-list-item :show-extra-icon="true" :extra-icon="adminIcon" title="后台管理系统" />
				<view class="border_line"></view>
				</uni-list>
			</view>
			
			<view class="tabbar_info">
				<uni-list>
				<view class="border_line"></view>
				<uni-list-item v-if="this.webState!=0" :show-extra-icon="true"  :extra-icon="movieIcon" title="电影信息管理" clickable
					@click="to_movie_admin()"/>
				<uni-list-item v-if="this.webState==0" class="chose_movie" :show-extra-icon="true"  :extra-icon="chose_movieIcon" title="电影信息管理" clickable
					@click="to_movie_admin()"/>	
				<view class="border_line"></view>
				
				<uni-list-item v-if="this.webState!=2" :show-extra-icon="true" :extra-icon="houseIcon" title="影院信息管理" clickable
					@click="to_house_admin()"/>
				<uni-list-item v-if="this.webState==2" class="chose_house" :show-extra-icon="true" :extra-icon="chose_houseIcon" title="影院信息管理" clickable
					@click="to_house_admin()"/>
				<view class="border_line"></view>
				
				<uni-list-item :show-extra-icon="true" :extra-icon="logoutIcon" title="登出" clickable
					@click="to_logout()" />
					<view class="border_line"></view>
				</uni-list>
			</view>	
		</view>
		
		<view class="movie_info_admin" v-if="this.webState==0 && this.movieRequestFlag==1">
			<uni-table class="movie_info_admin_table" ref="table" :loading="loading" border stripe  emptyText="暂无更多数据"
				@selection-change="selectionChange">
				<uni-tr>
					<uni-th width="50" align="center">序号</uni-th>
					<uni-th width="150" align="center">电影中文名</uni-th>
					<uni-th width="150" align="center">电影外文名</uni-th>
					<uni-th width="150" align="center">时长（min）</uni-th>
					<uni-th align="center">上映日期</uni-th>
					<uni-th width="200" align="center">设置</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in movieDataAdminListPages[pageCurrent-1]" :key="index">
					<uni-td align="center">{{ item.movieId }}</uni-td>
					<uni-td align="center">{{ item.movieNameCn }}</uni-td>
					<uni-td align="center">
						{{ item.movieNameEn }}
					</uni-td>
					<uni-td align="center">{{ item.movieDuration }}</uni-td>
					<uni-td align="center">{{ item.movieReleaseDate }}</uni-td>
					<uni-td>
						<view class="uni-group">
							<button class="movie_info_admin_button1" size="mini" type="primary" @click="movie_update(movieDataAdminListPages[pageCurrent-1][index].movieId)">查看</button>
							<button class="movie_info_admin_button2" size="mini" type="warn" @click="movie_delete(movieDataAdminListPages[pageCurrent-1][index].movieId)" >删除</button>
						</view>
					</uni-td>
				</uni-tr>
			</uni-table>
				<view class="movie_info_admin_box"><uni-pagination class="movie_pages" show-icon :page-size="pageSize" :current="pageCurrent"
					:total="total" @change="change" />
					<button class="create_movie" size="mini" @click="movie_create()">新增电影</button>
			</view>
		</view>
		
		<view class="cinema_info_admin" v-if="this.webState==2 && this.cinemaRequestFlag==1">
			<uni-table class="cinema_info_admin_table" ref="table_cinema" :loading="loadingCinema" border stripe  emptyText="暂无更多数据"
				@selection-change="selectionChangeCinema">
				<uni-tr>
					<uni-th width="50" align="center">序号</uni-th>
					<uni-th width="180" align="center">影院名称</uni-th>
					<uni-th width="50" align="center">电话</uni-th>
					<uni-th width="300" align="center">影院地址</uni-th>
					<uni-th width="150" align="center">设置</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in cinemaDataAdminListPages[pageCurrentCinema-1]" :key="index">
					<uni-td align="center">{{ item.cinemaId }}</uni-td>
					<uni-td align="center">{{ item.cinemaName }}</uni-td>
					<uni-td align="center">
						{{ item.cinemaPhone }}
					</uni-td>
					<uni-td align="center">{{ item.cinemaAddress }}</uni-td>
					<uni-td>
						<view class="uni-group">
							<button class="cinema_info_admin_button1" size="mini" type="primary" @click="cinema_update(cinemaDataAdminListPages[pageCurrentCinema-1][index].cinemaId)">查看</button>
							<button class="cinema_info_admin_button2" size="mini" type="warn" @click="cinema_delete(cinemaDataAdminListPages[pageCurrentCinema-1][index].cinemaId)" >删除</button>
						</view>
					</uni-td>
				</uni-tr>
			</uni-table>
				<view class="cinema_info_admin_box"><uni-pagination class="cinema_pages" show-icon :page-size="pageSizeCinema" :current="pageCurrentCinema"
					:total="cinemaTotal" @change="changeCinema" />
					<button class="create_cinema" size="mini" @click="cinema_create()">新增影院</button>
			</view>
		</view>
		
		
		
	</view>
	
	
	
	<view>
		<!-- 提示窗示例 -->
		<uni-popup ref="alertDialog" type="dialog">
			<uni-popup-dialog :type="error" cancelText="关闭" confirmText="确定" content="是否确定登出" @confirm="dialogConfirm"
				@close="dialogClose"></uni-popup-dialog>
		</uni-popup>
	</view>
	
	<view>
		<!-- 删除电影示例 -->
		<uni-popup ref="movieDeleteDialog" type="dialog">
			<uni-popup-dialog :type="error" cancelText="关闭" confirmText="确定" content="是否确定删除该电影信息" @confirm="movieDeleteConfirm"
				@close="dialogClose"></uni-popup-dialog>
		</uni-popup>
	</view>
	
	<view>
		<!-- 删除电影示例 -->
		<uni-popup ref="cinemaDeleteDialog" type="dialog">
			<uni-popup-dialog :type="error" cancelText="关闭" confirmText="确定" content="是否确定删除该电影院信息" @confirm="cinemaDeleteConfirm"
				@close="dialogClose"></uni-popup-dialog>
		</uni-popup>
	</view>
	
	

</template>

<script>
	export default {
		data() {
			return {
				isRegister: false,
				isLogin: false,
				movieSystemMovieId:0,
				movieRequestFlag:0,
				cinemaSystemCinemaId:0,
				cinemaRequestFlag:0,

				// 校验表单数据
				loginData: {
					cinemaPhone: '',
					cinemaPassword: '',
				},
				cinemaData: {
					cinemaId: '',
					cinemaName: '',
					cinemaPassword: '',
					cinemaPhone: '',
					cinemaAddress: '',
					cinemaX: '',
					cinemaY:'',
				},
				// 校验规则
				rules_login: {
					cinemaPhone: {
						rules: [{
							required: true
						}]
					},
					cinemaPassword: {
						rules: [{
							required: true
						}]
					}
				},
				orderIcon: {
					color: '#f9da49',
					size: '60rpx',
					type: 'cart'
				},
				infoIcon: {
					color: '#f9da49',
					size: '200rpx',
					type: 'shop'
				},
				setIcon: {
					color: '#f9da49',
					size: '60rpx',
					type: 'gear'
				},
				logoutIcon: {
					color: '#f9da49',
					size: '60rpx',
					type: 'arrow-left'
				},
				adminIcon: {
					color: '#f9da49',
					size: '60rpx',
					type: 'bars'
				},
				cinemaIcon: {
					color: '#f9da49',
					size: '60rpx',
					type: 'info'
				},
				chose_cinemaIcon: {
					color: '#000000',
					size: '60rpx',
					type: 'info'
				},
				movieIcon: {
					color: '#f9da49',
					size: '60rpx',
					type: 'images'
				},
				chose_movieIcon: {
					color: '#000000',
					size: '60rpx',
					type: 'images'
				},
				houseIcon: {
					color: '#f9da49',
					size: '60rpx',
					type: 'home'
				},
				chose_houseIcon: {
					color: '#000000',
					size: '60rpx',
					type: 'home'
				},
				scheduleIcon: {
					color: '#f9da49',
					size: '60rpx',
					type: 'plus'
				},
				chose_scheduleIcon: {
					color: '#000000',
					size: '60rpx',
					type: 'plus'
				},
				
				webState:0,
				movieDataAdminList:[],
				movieDataAdminListPages:[],
				
				cinemaDataAdminList:[],
				cinemaDataAdminListPages:[],
				
				pageSize: 13,
				// 当前页
				pageCurrent: 1,
				loading: false,
				total:0,
				
				pageSizeCinema: 13,
				// 当前页
				pageCurrentCinema: 1,
				loadingCinema: false,
				cinemaTotal:0,
				
			}
		},

		onLoad: function(option) {
			this.cinemaId = uni.getStorageSync("cinemaId");
			this.token = uni.getStorageSync("token");
			this.isLogin = uni.getStorageSync("isLogin");

			let cinema = {
				"cinemaId": this.cinemaId
			};

			uni.request({
				url: '/api/cinema/info',
				method: 'GET',
				dataType: 'json',
				data: cinema,
				header: {
					'Authorization': this.token
				},
				success: (res) => {
					this.cinemaData.cinemaId = res.data.data.cinemaId;
					this.cinemaData.cinemaName = res.data.data.cinemaName;
					this.cinemaData.cinemaPassword = res.data.data.cinemaPassword;
					this.cinemaData.cinemaPhone = res.data.data.cinemaPhone;
					this.cinemaData.cinemaAddress = res.data.data.cinemaAddress;
					this.cinemaData.cinemaX = res.data.data.cinemaX;
					this.cinemaData.cinemaY = res.data.data.cinemaY;
				},
			})
			
			uni.request({
				url: '/api/movie/info',
				method: 'GET',
				dataType: 'json',
				
				success:(res)=>{
					this.movieDataAdminList=res.data.data;
					
					let temp_arr=[];
					for(let i=0;i<this.movieDataAdminList.length;i++){
						temp_arr.push(this.movieDataAdminList[i]);
						if(temp_arr.length == 13){
							this.movieDataAdminListPages.push(temp_arr);
							temp_arr=[];
						}
						if(i == this.movieDataAdminList.length-1){
							this.movieDataAdminListPages.push(temp_arr);
							temp_arr=[];
						}
					}
					console.log('movie',this.movieDataAdminList)
					console.log('this.movieDataAdminListPages',this.movieDataAdminListPages)
					this.total=this.movieDataAdminList.length;
					this.movieRequestFlag=1;
				}
			})
			
			uni.request({
				url: '/api/cinema/infoAllCinema',
				method: 'GET',
				dataType: 'json',
				success:(res)=>{
					this.cinemaDataAdminList=res.data.data;
					let temp_arr=[];
					for(let i=0;i<this.cinemaDataAdminList.length;i++){
						temp_arr.push(this.cinemaDataAdminList[i]);
						if(temp_arr.length == 13){
							this.cinemaDataAdminListPages.push(temp_arr);
							temp_arr=[];
						}
						if(i == this.cinemaDataAdminList.length-1){
							this.cinemaDataAdminListPages.push(temp_arr);
							temp_arr=[];
						}
					}
					console.log('cinema',this.cinemaDataAdminList)
					console.log('this.cinemaDataAdminListPages',this.cinemaDataAdminListPages)
					this.cinemaTotal=this.cinemaDataAdminList.length;
					this.cinemaRequestFlag=1;
				}
			})
			
			
		},

		methods: {
			err_submit() {
				if (this.loginData.cinemaPhone && !this.loginData.cinemaPassword) {
					uni.showToast({
						title: '请输入密码',
						icon: 'error',
						mask: 'true',
					})
				} else {
					uni.showToast({
						title: '请输入电话号码',
						icon: 'error',
						mask: 'true',
					})
				}
			},


			//submit_login函数
			submit_login(ref) {
				this.$refs[ref].validate().then(res => {
					let cinema = {
						"cinemaPhone": this.loginData.cinemaPhone,
						"cinemaPassword": this.loginData.cinemaPassword
					};

					uni.request({
						url: '/api/cinema/login',
						method: 'POST',
						dataType: 'json',
						data: cinema,
						success: (res) => {
							if (res.data.code == 0) {
								console.log('调用成功', res.data.data);

								uni.setStorageSync('cinemaId', res.data.data.cinema.cinemaId);
								uni.setStorageSync('token', res.data.data.token);
								uni.setStorageSync('isLogin', true);

								uni.reLaunch({
									url: '/pages/web/login',
								});
							}
							if (res.data.code == 1) {
								uni.showToast({
									title: res.data.message,
									icon: 'error',
									mask: 'true',
								})
							}
						},
						fail: (res) => { //如果访问接口失败就会进入fail
							console.log(res.errMsg)
							console.log("调用接口失败")
						}
					})
				})
			},

			to_logout() {
				this.$refs.alertDialog.open();
			},
			dialogConfirm() {
				uni.clearStorageSync();
				uni.reLaunch({
					url: '/pages/web/login',
				})
			},
			movieDeleteConfirm(){
				console.log('movieId',this.movieSystemMovieId);
				
				uni.request({
					url: '/api/movie/deleteByMovieId?movieId='+this.movieSystemMovieId,
					method: 'DELETE',
					dataType: 'json',
					header: {
						'Authorization': this.token
					},
					success:(res)=>{
						uni.showToast({
							title: '删除成功',
							icon: 'true',
							mask: 'true',
						})
						uni.reLaunch({
							url: '/pages/web/login'
						})
					}
				})
			},
			cinemaDeleteConfirm(){
				console.log('cinemaId',this.cinemaSystemCinemaId);
				
				uni.request({
					url: '/api/cinema/deleteByCinemaId?cinemaId='+this.cinemaSystemCinemaId,
					method: 'DELETE',
					dataType: 'json',
					header: {
						'Authorization': this.token
					},
					success:(res)=>{
						uni.showToast({
							title: '删除成功',
							icon: 'true',
							mask: 'true',
						})
						uni.reLaunch({
							url: '/pages/web/login'
						})
					}
				})
			},
			
			
			dialogClose() {
			},
			
			to_cinema(){
				this.webState=0;
			},

			to_movie(){
				this.webState=1;
			},
			
			to_movie_admin(){
				this.webState=0;
			},
			
			to_house(){
				this.webState=2;
			},
			
			to_house_admin(){
				this.webState=2;
			},
			
			to_schedule(){
				this.webState=3;
			},
			
			// 多选
			selectionChange(e) {
				console.log(e.detail.index)
				this.selectedIndexs = e.detail.index
			},
			// 分页触发
			change(e) {
				this.$refs.table.clearSelection()
				this.selectedIndexs.length = 0
				this.pageCurrent=e.current
			},
			
			selectionChangeCinema(e) {
				console.log(e.detail.index)
				this.selectedIndexsCinema = e.detail.index
			},
			changeCinema(e) {
				this.$refs.table_cinema.clearSelection()
				this.selectedIndexsCinema.length = 0
				this.pageCurrentCinema=e.current
			},
			
			
			
			
			
			movie_update(movieId){
				uni.navigateTo({
					url: '/pages/web/movie/movieUpdate?movieId='+movieId,
					animationType: 'pop-in',
					animationDuration: 200
				});
			},
			cinema_update(cinemaId){
				uni.navigateTo({
					url: '/pages/web/cinema/cinemaUpdate?cinemaId='+cinemaId,
					animationType: 'pop-in',
					animationDuration: 200
				});
			},
			
			movie_delete(movieId){
				this.$refs.movieDeleteDialog.open();
				this.movieSystemMovieId=movieId;
				console.log('movieSystemMovieId',this.movieSystemMovieId);
			},
			
			cinema_delete(cinemaId){
				this.$refs.cinemaDeleteDialog.open();
				this.cinemaSystemCinemaId=cinemaId;
				console.log('cinemaSystemCinemaId',this.cinemaSystemCinemaId);
			},
			
			movie_create(){
				uni.navigateTo({
					url: '/pages/web/movie/movieCreate',
					animationType: 'pop-in',
					animationDuration: 200
				});
			},
			cinema_create(){
				uni.navigateTo({
					url: '/pages/web/cinema/cinemaCreate',
					animationType: 'pop-in',
					animationDuration: 200
				});
			}
			

		}
	}
</script>

<style lang="scss" scoped>
	.isLogin_false {
		display: flex;
		flex-direction: column;
		align-items: space-around;
		justify-content: center;
		height: 100%;
	}

	.login {
		padding: 0 10%;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		height: 100%;
	}

	text {
		color: #ffffff;
	}

	.code {
		color: #999;
	}

	.true_button {
		color: #010101;
		border-color: #010101;
		background-color: #f9da49;
	}

	.false_button {
		color: #010101;
		border-color: #010101;
		background-color: #999999;
	}

	.login_text_true {
		font-size: 100rpx;
	}


	.login_text_false {
		font-size: 50rpx;
		color: #999999;
	}

	.register_text_true {
		font-size: 100rpx;
	}

	.login_text {
		display: flex;
		flex-direction: row;
		width: 30%;
	}
	
	.login_example{
		width: 30%;
	}

	.info_login_register {
		flex: 1;
	}

	::v-deep.uni-forms-item {
		.is-required {
			color: #010101;
		}
	}

	.is_hover {
		color: #010101;
		border-color: #010101;
		background-color: #d8bc3f;
	}

	.is_errhover {
		color: #010101;
		border-color: #010101;
		background-color: #6a6a6a;
	}
	
	.isCinema_true{
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
	
	.border_line{
		width: 100%;
		border:1px solid #f9da49;
	}

	.isLogin_true {
		display: flex;
		flex-direction: column;
		height: 100%;
		width: 80%;
	}
	
	.movie_info_admin{
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		height: 100%;
		width: 80%;
	}
	
	.cinema_info_admin{
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		height: 100%;
		width: 80%;
	}
	
	.movie_info_admin_table{
		width: 100%;
	}
	
	.cinema_info_admin_table{
		width: 100%;
	}
	
	.movie_info_admin_button1{
		background-color: #f9da49;
		border:none;
		color: #000000;
	}
	
	.cinema_info_admin_button1{
		background-color: #f9da49;
		border:none;
		color: #000000;
	}
	
	.movie_info_admin_button2{
		background-color: #6a6a6a;
		border:none;
		color: #ffffff;
	}
	
	.cinema_info_admin_button2{
		background-color: #6a6a6a;
		border:none;
		color: #ffffff;
	}
	
	.movie_info_admin_box{
		width: 100%;
		display: flex;
		flex-direction: row;
	}
	
	.cinema_info_admin_box{
		width: 100%;
		display: flex;
		flex-direction: row;
	}
	
	.movie_pages{
		padding-left: 20%;
		flex:1
	}
	
	.cinema_pages{
		padding-left: 20%;
		flex:1
	}
	
	.create_movie{
		width:200px;
		border-radius: 0%;
		background-color: #f9da49;
		border:none;
		color: #000000;
	}
	
	.create_cinema{
		width:185px;
		border-radius: 0%;
		background-color: #f9da49;
		border:none;
		color: #000000;
	}
		
	.isLogin_1{
	}
	.isLogin_2{
	}
	.isLogin_3{
		justify-self: flex-end;
	}
	
	.cinema_tabbar{
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		width: 20%;
		border-right:3px solid #f9da49;
		background-color: #000000;
		gap:0%;
	}
	
	.cinema_admin{
		align-self: center;
		::v-deep.uni-list-item__content-title {
			font-size: 50rpx;
			color: #ffffff;
			overflow: hidden;
		}
	}
	
	.tabbar_info{
		
	}

	.chose_cinema{
		::v-deep.uni-list-item__container{
			background-color: #f9da49;
		}
		::v-deep.uni-list-item__content-title {
			color: #000000;
			overflow: hidden;
		}
	}
	
	.chose_movie{
		::v-deep.uni-list-item__container{
			background-color: #f9da49;
		}
		::v-deep.uni-list-item__content-title {
			color: #000000;
			overflow: hidden;
		}
	}
	
	.chose_house{
		::v-deep.uni-list-item__container{
			background-color: #f9da49;
		}
		::v-deep.uni-list-item__content-title {
			color: #000000;
			overflow: hidden;
		}
	}
	
	.chose_schedule{
		::v-deep.uni-list-item__container{
			background-color: #f9da49;
		}
		::v-deep.uni-list-item__content-title {
			color: #000000;
			overflow: hidden;
		}
	}
	
	.uni-group {
		display: flex;
		align-items: center;
	}
	
</style>