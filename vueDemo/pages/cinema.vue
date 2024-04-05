<template>

		<uni-section title="自定义图片大小" type="line">
			<view class="example-body custom-image-box">
				<text class="text">选择头像</text>
				<uni-file-picker limit="1" :del-icon="false" disable-preview :imageStyles="imageStyles"
					file-mediatype="image">选择</uni-file-picker>
			</view>
		</uni-section>

</template>


<script>
	export default {
		data() {
			return {
				imageStyles: {
					width: 100,
					height: 100,
					border: {
						radius: '1000%'
					}
				},
			}
		}
	}
</script>


<style lang="scss">
	.example-body {
		padding: 10px;
		padding-top: 0;
	}

	.custom-image-box {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
	}

	.text {
		font-size: 14px;
		color: #333;
	}
</style>


