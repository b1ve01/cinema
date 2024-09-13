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
				
				<uni-list-item :show-extra-icon="true" :extra-icon="logoutIcon" title="登出" clickable
					@click="to_logout()" />
					<view class="border_line"></view>
				
				</uni-list>
			</view>	
		</view>
		
		<view class="isLogin_true" v-if="this.webState==0 && this.cinemaInfoFlag==1">
			<view class="isLogin_1">
				<uni-list>
				<uni-list-item :show-extra-icon="true" :extra-icon="infoIcon" :title="this.cinemaData.cinemaName+'\n'+this.cinemaData.cinemaAddress+'\n电话：'+this.cinemaData.cinemaPhone"
				/>
				</uni-list>
			</view>
			<view class="isLogin_2">
				<uni-list >
				<view class="border_line"></view>
				<map class="cinema_map" style="width: 100%;" :latitude="this.cinemaData.cinemaY" :longitude="this.cinemaData.cinemaX"
					:markers="covers">
				</map>
				</uni-list>
			</view>
			
		</view>
		
		<view class="house_info_cinema" v-if="this.webState==2 && this.houseRequestFlag==1">
			<uni-table class="house_info_admin_table" ref="table_house" :loading="loadingHouse" border stripe  emptyText="暂无更多数据"
				@selection-change="selectionChangeHouse">
				<uni-tr>
					<uni-th width="100" align="center">序号</uni-th>
					<uni-th width="200" align="center">放映厅名称</uni-th>
					<uni-th width="100" align="center">行数</uni-th>
					<uni-th width="100" align="center">列数</uni-th>
					<uni-th width="50" align="center">设置</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in houseDataListPages[pageCurrentHouse-1]" :key="index">
					<uni-td align="center">{{ (pageCurrentHouse-1)*13+(index+1) }}</uni-td>
					<uni-td align="center">{{ item.houseName }}</uni-td>
					<uni-td align="center">
						{{ item.houseX }}
					</uni-td>
					<uni-td align="center">
						{{ item.houseY }}
					</uni-td>
					<uni-td>
						<view class="uni-group">
							<button class="house_info_admin_button1" size="mini" type="primary" @click="house_update(houseDataListPages[pageCurrentHouse-1][index].houseId)">修改</button>
							<button class="house_info_admin_button2" size="mini" type="warn" @click="house_delete(houseDataListPages[pageCurrentHouse-1][index].houseId)" >删除</button>
						</view>
					</uni-td>
				</uni-tr>
			</uni-table>
				<view class="house_info_admin_box"><uni-pagination class="house_pages" show-icon :page-size="pageSizeHouse" :current="pageCurrentHouse"
					:total="houseTotal" @change="changeHouse" />
					<button class="create_house" size="mini" @click="house_create()">新增放映厅</button>
			</view>
		</view>	
		
		<view class="schedule_info_cinema" v-if="this.webState==3 && this.scheduleRequestFlag==1">
			<uni-table class="schedule_info_admin_table" ref="table_schedule" :loading="loadingSchedule" border stripe  emptyText="暂无更多数据"
				@selection-change="selectionChangeSchedule">
				<uni-tr>
					<uni-th width="10" align="center">序号</uni-th>
					<uni-th width="100" align="center">电影</uni-th>
					<uni-th width="100" align="center">放映厅</uni-th>
					<uni-th width="200" align="center">时间</uni-th>
					<uni-th width="100" align="center">设置</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in scheduleDataListPages[pageCurrentSchedule-1]" :key="index">
					<uni-td align="center">{{ (pageCurrentSchedule-1)*13+(index+1) }}</uni-td>
					<uni-td align="center">{{ item.movieNameCn }}</uni-td>
					<uni-td align="center">
						{{ item.houseName }}
					</uni-td>
					<uni-td align="center">
						{{ item.scheduleTime }}
					</uni-td>
					<uni-td>
						<view class="uni-group">
							<button class="schedule_info_admin_button1" size="mini" type="primary" @click="schedule_update(scheduleDataListPages[pageCurrentSchedule-1][index].scheduleId)">查看</button>
							<button class="schedule_info_admin_button1" size="mini" type="primary" @click="scheduleUpdate(scheduleDataListPages[pageCurrentSchedule-1][index].scheduleId)">修改</button>
							<button class="schedule_info_admin_button2" size="mini" type="warn" @click="schedule_delete(scheduleDataListPages[pageCurrentSchedule-1][index].scheduleId)" >删除</button>
						</view>
					</uni-td>
				</uni-tr>
			</uni-table>
				<view class="schedule_info_admin_box"><uni-pagination class="schedule_pages" show-icon :page-size="pageSizeSchedule" :current="pageCurrentSchedule"
					:total="scheduleTotal" @change="changeSchedule" />
					<button class="create_schedule" size="mini" @click="schedule_create()">新增场次</button>
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
					<uni-th width="250" align="center">设置</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in movieDataAdminListPages[pageCurrent-1]" :key="index">
					<uni-td align="center">{{ (pageCurrent-1)*13+(index+1) }}</uni-td>
					<uni-td align="center">{{ item.movieNameCn }}</uni-td>
					<uni-td align="center">
						{{ item.movieNameEn }}
					</uni-td>
					<uni-td align="center">{{ item.movieDuration }}</uni-td>
					<uni-td align="center">{{ item.movieReleaseDate }}</uni-td>
					<uni-td>
						<view class="uni-group">
							<button class="movie_info_admin_button1" size="mini" type="primary" @click="movie_update(movieDataAdminListPages[pageCurrent-1][index].movieId)">查看</button>
							<button class="movie_info_admin_button1" size="mini" type="primary" @click="movieUpdate(movieDataAdminListPages[pageCurrent-1][index].movieId)">修改</button>
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
					<uni-th width="200" align="center">影院名称</uni-th>
					<uni-th width="50" align="center">电话</uni-th>
					<uni-th width="350" align="center">影院地址</uni-th>
					<uni-th width="200" align="center">设置</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in cinemaDataAdminListPages[pageCurrentCinema-1]" :key="index">
					<uni-td align="center">{{ (pageCurrentCinema-1)*13+(index+1) }}</uni-td>
					<uni-td align="center">{{ item.cinemaName }}</uni-td>
					<uni-td align="center">
						{{ item.cinemaPhone }}
					</uni-td>
					<uni-td align="center">{{ item.cinemaAddress }}</uni-td>
					<uni-td>
						<view class="uni-group">
							<button class="cinema_info_admin_button1" size="mini" type="primary" @click="cinema_update(cinemaDataAdminListPages[pageCurrentCinema-1][index].cinemaId)">查看</button>
							<button class="cinema_info_admin_button1" size="mini" type="primary" @click="cinemaUpdate(cinemaDataAdminListPages[pageCurrentCinema-1][index].cinemaId)">修改</button>
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
		<uni-popup ref="movieDeleteDialog" type="dialog">
			<uni-popup-dialog :type="error" cancelText="关闭" confirmText="确定" content="是否确定删除该电影信息" @confirm="movieDeleteConfirm"
				@close="dialogClose"></uni-popup-dialog>
		</uni-popup>
	</view>
	
	<view>
		<uni-popup ref="cinemaDeleteDialog" type="dialog">
			<uni-popup-dialog :type="error" cancelText="关闭" confirmText="确定" content="是否确定删除该电影院信息" @confirm="cinemaDeleteConfirm"
				@close="dialogClose"></uni-popup-dialog>
		</uni-popup>
	</view>
	
	<view>
		<uni-popup ref="houseDeleteDialog" type="dialog">
			<uni-popup-dialog :type="error" cancelText="关闭" confirmText="确定" content="是否确定删除该放映厅信息" @confirm="houseDeleteConfirm"
				@close="dialogClose"></uni-popup-dialog>
		</uni-popup>
	</view>
	
	<view>
		<uni-popup ref="scheduleDeleteDialog" type="dialog">
			<uni-popup-dialog :type="error" cancelText="关闭" confirmText="确定" content="是否确定删除该场次信息" @confirm="scheduleDeleteConfirm"
				@close="dialogClose"></uni-popup-dialog>
		</uni-popup>
	</view>
	

</template>

<script>
	export default {
		data() {
			return {
				cinemaInfoFlag:0,
				
				isRegister: false,
				isLogin: false,
				
				movieSystemMovieId:0,
				movieRequestFlag:0,
				
				cinemaSystemCinemaId:0,
				cinemaRequestFlag:0,
				
				houseSystemHouseId:0,
				houseRequestFlag:0,
				
				scheduleSystemScheduleId:0,
				scheduleRequestFlag:0,

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
				
				houseDataList:[],
				houseDataListPages:[],
				
				scheduleDataList:[],
				scheduleDataListPages:[],
				
				pageSize: 13,
				pageCurrent: 1,
				loading: false,
				total:0,
				
				pageSizeCinema: 13,
				pageCurrentCinema: 1,
				loadingCinema: false,
				cinemaTotal:0,
				
				pageSizeHouse:13,
				pageCurrentHouse:1,
				loadingHouse:false,
				houseTotal:0,
				
				pageSizeSchedule:13,
				pageCurrentSchedule:1,
				loadingSchedule:false,
				scheduleTotal:0,
				
				covers: [{
					latitude: '',
					longitude: '',
					iconPath: '/static/Location.png'
				}]
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
					this.covers[0].longitude=this.cinemaData.cinemaX;
					this.covers[0].latitude=this.cinemaData.cinemaY;
					this.cinemaInfoFlag=1;
				},
			})
			
			uni.request({
				url: '/api/house/infoByCinemaId',
				method: 'GET',
				dataType: 'json',
				data: cinema,
				header: {
					'Authorization': this.token
				},
				success:(res)=>{
					this.houseDataList=res.data.data;
					
					let temp_arr=[];
					for(let i=0;i<this.houseDataList.length;i++){
						temp_arr.push(this.houseDataList[i]);
						if(temp_arr.length == 13){
							this.houseDataListPages.push(temp_arr);
							temp_arr=[];
						}
						if(i == this.houseDataList.length-1){
							this.houseDataListPages.push(temp_arr);
							temp_arr=[];
						}
					}
					console.log('house',this.houseDataList)
					console.log('this.houseDataListPages',this.houseDataListPages)
					this.houseTotal=this.houseDataList.length;
					this.houseRequestFlag=1;
				}
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
			
			let currentDate = new Date();
			console.log('currentDate', currentDate);
			uni.request({
				url: '/api/schedule/infoByCinema',
				method: 'GET',
				dataType: 'json',
				data: cinema,
				header: {
					'Authorization': this.token
				},
				success:(res)=>{
					for(let i=0;i<res.data.data.length;i++){
						if (currentDate < new Date(res.data.data[i].scheduleTime)) {
							this.scheduleDataList.push(res.data.data[i]);
						}
					}
					
					let requests = [];
					for(let i=0;i<this.scheduleDataList.length;i++){
						let house = {
							"houseId": this.scheduleDataList[i].houseId
						};
						
						let request = new Promise((resolve,reject) => {
							uni.request({
								url: '/api/house/infoById',
								method: 'GET',
								dataType: 'json',
								data: house,
								success:(res)=>{
									this.scheduleDataList[i].houseName=res.data.data.houseName
									this.scheduleDataList[i].houseSeats=res.data.data.houseSeats
									resolve();
								}
							})
						})
						requests.push(request);
					}
					
					for(let i=0;i<this.scheduleDataList.length;i++){
						let movie = {
							"movieId": this.scheduleDataList[i].movieId
						};
						
						let request = new Promise((resolve,reject) => {
							uni.request({
								url: '/api/movie/infoMovieById',
								method: 'GET',
								dataType: 'json',
								data: movie,
								success:(res)=>{
									this.scheduleDataList[i].movieNameCn=res.data.data.movie.movieNameCn
									resolve();
								}
							})
						})
						requests.push(request);
					}
					
					Promise.all(requests).then(() => {
						let temp_arr=[];
						for(let i=0;i<this.scheduleDataList.length;i++){
							temp_arr.push(this.scheduleDataList[i]);
							if(temp_arr.length == 13){
								this.scheduleDataListPages.push(temp_arr);
								temp_arr=[];
							}
							if(i == this.scheduleDataList.length-1){
								this.scheduleDataListPages.push(temp_arr);
								temp_arr=[];
							}
						}
						console.log('schedule',this.scheduleDataList)
						console.log('this.scheduleDataListPages',this.scheduleDataListPages)
						this.scheduleTotal=this.scheduleDataList.length;
						this.scheduleRequestFlag=1;
					})
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
			
			houseDeleteConfirm(){
				console.log('houseId',this.houseSystemHouseId);
				
				uni.request({
					url: '/api/house/deleteByHouseId?houseId='+this.houseSystemHouseId,
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
			
			scheduleDeleteConfirm(){
				console.log('scheduleId',this.scheduleSystemScheduleId);
				
				uni.request({
					url: '/api/schedule/deleteByScheduleId?scheduleId='+this.scheduleSystemScheduleId,
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
			
			selectionChangeHouse(e) {
				console.log(e.detail.index)
				this.selectedIndexsHouse = e.detail.index
			},
			changeHouse(e) {
				this.$refs.table_house.clearSelection()
				this.selectedIndexsHouse.length = 0
				this.pageCurrentHouse=e.current
			},
			
			selectionChangeSchedule(e) {
				console.log(e.detail.index)
				this.selectedIndexsSchedule = e.detail.index
			},
			changeSchedule(e) {
				this.$refs.table_schedule.clearSelection()
				this.selectedIndexsSchedule.length = 0
				this.pageCurrentSchedule=e.current
			},
			
			
			
			
			
			movie_update(movieId){
				uni.navigateTo({
					url: '/pages/web/movie/movieUpdate?movieId='+movieId,
					animationType: 'pop-in',
					animationDuration: 200
				});
			},
			movieUpdate(movieId){
				uni.navigateTo({
					url: '/pages/web/movie/movieUUpdate?movieId='+movieId,
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
			cinemaUpdate(cinemaId){
				uni.navigateTo({
					url: '/pages/web/cinema/cinemaUUpdate?cinemaId='+cinemaId,
					animationType: 'pop-in',
					animationDuration: 200
				});
			},
			house_update(houseId){
				uni.navigateTo({
					url: '/pages/web/house/houseUpdate?houseId='+houseId,
					animationType: 'pop-in',
					animationDuration: 200
				});
			},
			schedule_update(scheduleId){
				uni.navigateTo({
					url: '/pages/web/schedule/scheduleUpdate?scheduleId='+scheduleId,
					animationType: 'pop-in',
					animationDuration: 200
				});
			},
			scheduleUpdate(scheduleId){
				uni.navigateTo({
					url: '/pages/web/schedule/scheduleUUpdate?scheduleId='+scheduleId,
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
			
			house_delete(houseId){
				this.$refs.houseDeleteDialog.open();
				this.houseSystemHouseId=houseId;
				console.log('houseSystemHouseId',this.houseSystemHouseId);
			},
			schedule_delete(scheduleId){
				this.$refs.scheduleDeleteDialog.open();
				this.scheduleSystemScheduleId=scheduleId;
				console.log('scheduleSystemScheduleId',this.scheduleSystemScheduleId);
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
			},
			house_create(){
				uni.navigateTo({
					url: '/pages/web/house/houseCreate',
					animationType: 'pop-in',
					animationDuration: 200
				});
			},
			schedule_create(){
				uni.navigateTo({
					url: '/pages/web/schedule/scheduleCreate',
					animationType: 'pop-in',
					animationDuration: 200
				});
			},
			

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
	
	.house_info_cinema{
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		height: 100%;
		width: 80%;
	}
	
	.schedule_info_cinema{
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
	
	.house_info_admin_table{
		width: 100%;
	}
	
	.schedule_info_admin_table{
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
	
	.house_info_admin_button1{
		background-color: #f9da49;
		border:none;
		color: #000000;
	}
	
	.schedule_info_admin_button1{
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
	
	.house_info_admin_button2{
		background-color: #6a6a6a;
		border:none;
		color: #ffffff;
	}
	
	.schedule_info_admin_button2{
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
	
	.house_info_admin_box{
		width: 100%;
		display: flex;
		flex-direction: row;
	}
	
	.schedule_info_admin_box{
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
	
	.house_pages{
		padding-left: 20%;
		flex:1
	}
	
	.schedule_pages{
		padding-left: 20%;
		flex:1
	}
	
	.create_movie{
		width:250px;
		border-radius: 0%;
		background-color: #f9da49;
		border:none;
		color: #000000;
	}
	
	.create_cinema{
		width:215px;
		border-radius: 0%;
		background-color: #f9da49;
		border:none;
		color: #000000;
	}
	
	.create_house{
		width:187px;
		border-radius: 0%;
		background-color: #f9da49;
		border:none;
		color: #000000;
	}
	
	.create_schedule{
		width:250px;
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
	
	.cinema_map{
		height: 100vh;
	}
	
</style>