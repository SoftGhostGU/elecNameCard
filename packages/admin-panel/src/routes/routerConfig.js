import request from '@/api'
import { lazy } from "react";
// import routes from "@/assets/router.json";
const getMenu = () => {
    // // 模拟登录
    // return new Promise((resolve,rejec)=>{
    //     let appInfo = JSON.parse(localStorage.getItem(window.envConfig['ROOT_APP_INFO']))
    //     appInfo.menuList = routes.result.menu
    //     localStorage.setItem(window.envConfig['ROOT_APP_INFO'], JSON.stringify(appInfo))
    //     resolve(routes.result.menu)
    //     return routes.result.menu
    // })

    
    // 真实登录 返回数据参考router.json
    return request.getMenuBar({ _t: "1682558421" }).then((res) => {
        if (res.data.code = 200) {
            let list = res.data.data
            let appInfo = JSON.parse(localStorage.getItem(window.envConfig['ROOT_APP_INFO']))
            // appInfo.menuList = res.data.data 
            appInfo.menuList = transformMenu(res.data.data)
            localStorage.setItem(window.envConfig['ROOT_APP_INFO'], JSON.stringify(appInfo))
            return list
        }
    })
}



/**
 * 递归转换菜单数据 前端数据结构见"@/assets/router.json"
 * 后端数据结构
 * @param {Array} menuList 后端返回的菜单列表
 * @returns {Array} 转换后的菜单列表
 */
function transformMenu(menuList) {
    if (!menuList || menuList.length === 0) return [];

    return menuList.map(menu => {
        // 1. 处理当前菜单项
        const transformedMenu = {
            id: menu.id,
            name: menu.url.replace(/\//g, '-').substring(1), // 如 "/dashboard/analysis" -> "dashboard-analysis"
            path: menu.url,
            component: menu.component,
            redirect: menu.redirect,
            route: "1", // 固定值
            meta: {
                title: menu.name,
                icon: menu.icon,
                keepAlive: menu.keepAlive,
                componentName: menu.componentName || menu.component.split('/').pop(), // 提取组件名
                internalOrExternal: false // 固定值
            }
        };

        // 2. 递归处理子菜单
        if (menu.children && menu.children.length > 0) {
            transformedMenu.children = transformMenu(menu.children);
        } else {
            transformedMenu.children = null; // 确保 children 存在
        }

        return transformedMenu;
    });
}

export default getMenu