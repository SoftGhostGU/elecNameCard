import { axios } from './methods';
import { service } from './service';
import { notification, message } from 'antd'


// 统一导出供调用  请勿重复命名

const request = {
    getLogin: (params) => { return axios('post', service.login, params) }, //登录
    getAllUsers: (params) => { return axios('get', service.allUsers, params) }, //获取用户列表
    getUser: (params) => { return axios('get', service.users, params) }, //获取用户信息
    addUser: (params) => { return axios('post', service.users, params) }, //新增用户
    editUser: (params) => { return axios('put', service.users, params) }, //编辑用户
    deleteUser: (params) => { return axios('delete', service.users, params) }, //删除用户
    getLogs: (params) => { return axios('get', service.allLogs, params) }, //获取日志列表
};

export default request;
