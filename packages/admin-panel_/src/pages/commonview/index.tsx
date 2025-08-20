import React, { useEffect } from 'react';
import { Layout, Menu } from 'antd';
import { UserOutlined, TeamOutlined } from '@ant-design/icons';
import { Routes, Route, useNavigate, useLocation } from 'react-router-dom';
import UserManagement from '../user-management';
import LogManagement from '../log-management';
import WelcomePage from '../welcome';
import './index.less';

const { Sider, Content } = Layout;

const CommonView: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const selectedKey = location.pathname; // 根据 URL 设置选中菜单

  const checkLoggedIn = () => {
    const isLoggedIn = localStorage.getItem('isLoggedIn');

    if (isLoggedIn?.startsWith('true')) {
      console.log('logged in', isLoggedIn);
    } else {
      console.log('not logged in');
      navigate('/login');
    }
  }

  useEffect(() => {
    checkLoggedIn();
  })

  return (
    <Layout className="dashboard-layout">
      <Sider width='15%' className="site-sider">
        <Menu
          mode="inline"
          selectedKeys={[selectedKey]}
          style={{ height: '100%' }}
          onClick={({ key }) => navigate(key)}
        >
          <Menu.Item key="/user-management/users" icon={<UserOutlined />}>
            人员管理
          </Menu.Item>
          <Menu.Item key="/user-management/logs" icon={<TeamOutlined />}>
            日志管理
          </Menu.Item>
        </Menu>
      </Sider>

      <Layout>
        <Content className="site-content">
          <Routes>
            <Route path="users" element={<UserManagement />} />
            <Route path="logs" element={<LogManagement />} />
            <Route path="*" element={<WelcomePage />} />
          </Routes>
        </Content>
      </Layout>
    </Layout>
  );
};

export default CommonView;
