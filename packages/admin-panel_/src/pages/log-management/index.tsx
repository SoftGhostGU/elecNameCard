import React, { useEffect, useState } from 'react';
import { Layout, Table, Input, Select } from 'antd';
import { SearchOutlined } from '@ant-design/icons';
import request from '../../api';
import './index.less';
import Toast from '../../components/Toast';
import { format } from 'date-fns';

const { Content, Header } = Layout;

interface LoginLog {
  id: number;
  loginIp: string;
  loginTime: string;
  status: number; // 1成功 0失败
  userAgent: string;
  attemptUsername: string;
}

function formatLoginTime(loginTime: string): string {
  const date = new Date(loginTime);
  return format(date, 'yyyy-MM-dd HH:mm:ss'); // 自定义格式
}

const LogManagement: React.FC = () => {
  const [logs, setLogs] = useState<LoginLog[]>([]);
  const [searchText, setSearchText] = useState('');
  const [pageSize, setPageSize] = useState(5);
  const [toast, setToast] = useState<{ message: string; type: 'success' | 'error' | 'info' } | null>(null);

  // 获取日志
  const fetchLogs = async () => {
    // 模拟一些数据
    // const logList: LoginLog[] = [
    //   {
    //     id: 1,
    //     loginIp: '192.168.1.1',
    //     loginTime: '2021-10-10 10:10:10',
    //     status: 1,
    //     userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',
    //     attemptUsername: 'admin',
    //   },
    //   {
    //     id: 2,
    //     loginIp: '192.168.1.2',
    //     loginTime: '2021-10-10 10:10:10',
    //     status: 0,
    //     userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',
    //     attemptUsername: 'admin',
    //   },
    //   {
    //     id: 3,
    //     loginIp: '192.168.1.3',
    //     loginTime: '2021-10-10 10:10:10',
    //     status: 1,
    //     userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',
    //     attemptUsername: 'admin',
    //   },
    //   {
    //     id: 4,
    //     loginIp: '192.168.1.4',
    //     loginTime: '2021-10-10 10:10:10',
    //     status: 0,
    //     userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',
    //     attemptUsername: 'admin',
    //   },
    //   {
    //     id: 5,
    //     loginIp: '192.168.1.5',
    //     loginTime: '2021-10-10 10:10:10',
    //     status: 1,
    //     userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',
    //     attemptUsername: 'admin',
    //   },
    //   {
    //     id: 6,
    //     loginIp: '192.168.1.6',
    //     loginTime: '2021-10-10 10:10:10',
    //     status: 0,
    //     userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',
    //     attemptUsername: 'admin',
    //   },
    //   {
    //     id: 7,
    //     loginIp: '192.168.1.7',
    //     loginTime: '2021-10-10 10:10:10',
    //     status: 1,
    //     userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',
    //     attemptUsername: 'admin',
    //   },
    //   {
    //     id: 8,
    //     loginIp: '192.168.1.8',
    //     loginTime: '2021-10-10 10:10:10',
    //     status: 0,
    //     userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',
    //     attemptUsername: 'admin',
    //   },
    //   {
    //     id: 9,
    //     loginIp: '192.168.1.9',
    //     loginTime: '2021-10-10 10:10:10',
    //     status: 1,
    //     userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',
    //     attemptUsername: 'admin',
    //   },
    //   {
    //     id: 10,
    //     loginIp: '192.168.1.10',
    //     loginTime: '2021-10-10 10:10:10',
    //     status: 0,
    //     userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',
    //     attemptUsername: 'admin',
    //   },
    // ];
    // setLogs(logList);
    // setToast({ message: '获取日志成功', type: 'success' });
    try {
      const res = await request.getLogs({});
      const logList = res.data.data.map((item: any) => ({
        id: item.id,
        loginIp: item.loginIp,
        loginTime: formatLoginTime(item.loginTime),
        status: item.status,
        userAgent: item.userAgent,
        attemptUsername: item.attemptUsername,
      }));
      setLogs(logList);
      setToast({ message: '获取日志成功', type: 'success' });
    } catch (err) {
      console.log('获取日志失败', err);
      setToast({ message: '获取日志失败', type: 'error' });
    }
  };

  useEffect(() => {
    fetchLogs();
  }, []);

  // 搜索过滤
  const filteredLogs = logs.filter(
    (log) =>
      log.loginIp.includes(searchText) ||
      log.attemptUsername.includes(searchText) ||
      log.userAgent.includes(searchText)
  );

  const handleChangePageSize = (value: string) => {
    setPageSize(Number(value));
  };

  const columns = [
    { title: '登录IP', dataIndex: 'loginIp', key: 'loginIp' },
    { title: '登录时间', dataIndex: 'loginTime', key: 'loginTime' },
    { 
      title: '状态', 
      dataIndex: 'status', 
      key: 'status',
      render: (status: number) => (status === 1 ? '✅ 成功' : '❌ 失败')
    },
    { title: '浏览器/设备', dataIndex: 'userAgent', key: 'userAgent' },
    { title: '尝试用户名', dataIndex: 'attemptUsername', key: 'attemptUsername' },
  ];

  return (
    <Layout className="user-management-layout">
      <Layout>
        <Header className="site-header">
          <div className="search-bar">
            <Input
              placeholder="搜索IP/用户名/设备信息"
              prefix={<SearchOutlined />}
              value={searchText}
              onChange={(e) => setSearchText(e.target.value)}
              style={{ width: 300, marginRight: 16 }}
            />
          </div>
        </Header>

        <Content className="site-content">
          <Select
            defaultValue={`每页显示${pageSize.toString()}条`}
            style={{ width: 120, marginBottom: 16 }}
            onChange={handleChangePageSize}
          >
            <Select.Option value={5}>每页显示5条</Select.Option>
            <Select.Option value={10}>每页显示10条</Select.Option>
            <Select.Option value={20}>每页显示20条</Select.Option>
            <Select.Option value={50}>每页显示50条</Select.Option>
          </Select>

          <Table
            rowKey="id"
            columns={columns}
            dataSource={filteredLogs.sort((a, b) => b.id - a.id)}
            pagination={{ pageSize }}
          />
        </Content>
      </Layout>

      {toast && (
        <Toast
          message={toast.message}
          type={toast.type}
          onClose={() => setToast(null)}
        />
      )}
    </Layout>
  );
};

export default LogManagement;
