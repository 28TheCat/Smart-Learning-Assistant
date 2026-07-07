import axios from 'axios'
import { ElMessage } from 'element-plus'
import { getToken, removeToken } from './auth'

//创建axios实例对象
const request = axios.create({
  baseURL: '/api',
  timeout: 600000
})

//axios的请求 request 拦截器
request.interceptors.request.use(
  (config) => {
    const token = getToken()
    if (token && config.url !== '/login') {
      config.headers = config.headers || {}
      config.headers.token = token
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

//axios的响应 response 拦截器
request.interceptors.response.use(
  (response) => { //成功回调
    return response.data
  },
  (error) => { //失败回调
    if (error?.response?.status === 401) {
      removeToken()
      if (window.location.pathname !== '/login') {
        ElMessage.error('登录已过期，请重新登录')
        window.location.replace('/login')
      }
    }
    return Promise.reject(error)
  }
)

export default request
