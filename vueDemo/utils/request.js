//定制请求的实例

//导入axios  npm install axios
import axios from 'axios';


const baseURL = '/api';
const instance = axios.create({ baseURL })


/* import {useRouter} from 'vue-router'
const router = useRouter(); */


//添加响应拦截器
instance.interceptors.response.use(
    result => {

        //操作失败
        //alert(result.data.msg?result.data.msg:'服务异常')
        //ElMessage.error(result.data.msg?result.data.msg:'服务异常')
        //异步操作的状态转换为失败
        return result.data;
        
    },
    err => {
		alert('服务异常');
        return Promise.reject(err);//异步的状态转化成失败的状态
    }
)

export default instance;