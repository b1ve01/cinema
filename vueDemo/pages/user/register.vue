<template>
	<view class="login">
		<uni-section title="表单校验" type="line">
			<view class="example">
				<!-- 基础表单校验 -->
				<uni-forms ref="valiForm" :rules="rules" :modelValue="valiFormData" label-position="top">
					<uni-forms-item label="邮箱" required name="userEmail">
						<uni-easyinput v-model="valiFormData.userEmail" placeholder="请输入邮箱" />
					</uni-forms-item>
					<uni-forms-item label="密码" required name="userPassword">
						<uni-easyinput v-model="valiFormData.userPassword" placeholder="请输入密码" />
					</uni-forms-item>
					<uni-forms-item label="验证码" required name="code">
						<uni-easyinput v-model="valiFormData.code" placeholder="请输入验证码" />
					</uni-forms-item>
				</uni-forms>
				<button type="primary" @click="register">提交</button>
			</view>
		</uni-section>
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue'
	import {
		onReady
	} from '@dcloudio/uni-app'

	const title = ref('Hello')

	onReady(() => {
		console.log('hello')
	})

	//定义数据模型
	const valiFormData = ref({
		userEmail: '',
		userPassword: '',
		code: ''
	})

	//定义表单校验规则
	const rules = {
		userEmail: {
			rules: [{
				required: true,
				errorMessage: '邮箱不能为空'
			}]
		},
		userPassword: {
			rules: [{
				required: true,
				errorMessage: '密码不能为空'
			}]
		}
	}

	// 创建表单引用
	const valiForm = ref(null)

	// 提交函数
	const submit = (ref) => {
		valiForm.value.validate().then(res => {

			console.log('success', res);
			uni.showToast({
				title: `校验通过`
			});
		}).catch(err => {
			console.log('err', err);
		});
	}



	//调用后台接口,完成注册
	import {
		userRegisterService
	} from '@/api/user.js'
	const register = async () => {
		try {
			//valiFormData是一个响应式对象,如果要获取值,需要.value
			let result = await userRegisterService(valiFormData.value).then(response => {
					console.log('请求成功:', response);
				})
				.catch(error => {
					console.error('请求失败:', error);
				});;
			if (result.code === 0) {
				//成功了
				alert(result.msg ? result.msg : '注册成功');
			} else {
				//失败了
				alert('注册失败')
			}
		} catch (error) {
			console.error('注册接口调用失败:', error);
			alert('注册接口调用失败，请稍后重试');
		}
	}
</script>


<style lang="scss" scoped>

</style>