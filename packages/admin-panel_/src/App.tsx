import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';

import Login from './pages/login';
import CommonView from './pages/commonview';
import Error404 from './components/error404';

import './App.css';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* 默认首页跳转到登录 */}
        <Route path="/" element={<Navigate to="/login" />} />

        {/* 登录页 */}
        <Route path="/login" element={<Login />} />

        {/* 用户管理和日志管理页 */}
        <Route path="/user-management/*" element={<CommonView />} />

        {/* 404 页面 */}
        <Route path="*" element={<Error404 />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
