<template>
	<view class="info">
		<text>{{userId}}</text>
	</view>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		
		onLoad: function(option) {
			this.userId = uni.getStorageSync("userId");
			console.log('id在这',this.userId);
		},

		methods: {}
	}
</script>

<style lang="scss" scoped>
	text{
		color:#ffffff;
	}
</style>