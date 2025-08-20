import React, { useEffect, useState } from 'react';
import { Layout, Menu, Table, Button, Modal, Form, Input, Space, Popconfirm, Select, message } from 'antd';
import {
  UserOutlined,
  TeamOutlined,
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  SearchOutlined
} from '@ant-design/icons';
import request from '../../api'
import './index.less';
import Toast from '../../components/Toast'

const { Sider, Content, Header } = Layout;

interface User {
  userId: number;
  username: string;
  company: string;
  position: string;
  phoneNumber: string;
  email: string;
}

const UserManagement: React.FC = () => {

  const [users, setUsers] = useState<User[]>([]);

  const [searchText, setSearchText] = useState('');
  const [modalVisible, setModalVisible] = useState(false);
  const [editingUser, setEditingUser] = useState<User | null>(null);
  const [form] = Form.useForm();
  const [pageSize, setPageSize] = useState(5);
  const [toast, setToast] = useState<{ message: string; type: 'success' | 'error' | 'info' } | null>(null);

  // 获取用户列表
  const fetchUsers = async () => {
    try {
      const res = await request.getAllUsers({});
      const allUserInfo = res.data.data;
      const userList = allUserInfo.map((item: any) => {
        const newUser: User = {
          userId: item.userId,
          username: item.username,
          company: item.company,
          position: item.position,
          phoneNumber: item.phoneNumber,
          email: item.email,
        }
        return newUser;
      })
      setUsers(userList);
      setToast({ message: '获取用户列表成功', type: 'success' });
    } catch (err) {
      console.log('获取用户列表失败');
      setToast({ message: '获取用户列表失败', type: 'error' });
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  // 分页
  const handleChange = (value: string) => {
    console.log(`selected ${value}`);
    setPageSize(Number(value));
  };

  const handleChangeCompany = (value: string) => {
    console.log(`selected ${value}`);
  };

  // 搜索
  const filteredUsers = users.filter(
    (u) =>
      u.username.includes(searchText) ||
      u.company.includes(searchText) ||
      u.position.includes(searchText) ||
      u.phoneNumber.includes(searchText) ||
      u.email.includes(searchText)
  );

  // 打开新增/编辑弹窗
  const openModal = (user?: User) => {
    if (user) {
      setEditingUser(user);
      form.setFieldsValue(user);
    } else {
      setEditingUser(null);
      form.resetFields();
    }
    setModalVisible(true);
  };

  // 保存
  const handleSave = () => {
    form.validateFields().then((values) => {
      if (editingUser) {
        const updatedUser = { ...editingUser, ...values };
        console.log('更新用户信息', updatedUser);

        // 更新到前端表格
        setUsers((prev) =>
          prev.map((u) => (u.userId === editingUser.userId ? updatedUser : u))
        );

        // 调用接口时带上 userId
        const updateRequest = {
          userId: editingUser.userId,
          ...values
        };

        request.editUser(updateRequest).then((res: any) => {
          if (res.data.code === 200) {
            setToast({ message: '更新用户信息成功', type: 'success' });
          } else {
            setToast({ message: '更新用户信息失败', type: 'error' });
          }
        }).catch(() => {
          setToast({ message: '更新请求出错', type: 'error' });
        });
      } else {
        const newUser: User = {
          userId: Date.now(),
          ...values,
        };
        setUsers((prev) => [...prev, newUser]);
        setToast({ message: '新增用户信息成功', type: 'success' });
      }
      setModalVisible(false);
    });
  };


  // 删除
  const handleDelete = async (id: number) => {
    setUsers((prev) => prev.filter((u) => u.userId !== id));
    console.log("待删除的用户id: ", id);
    const deleteRequest = {
      userId: id
    }
    request.deleteUser(deleteRequest).then((res: any) => {
      console.log("删除返回结果：", res);
      if (res.data.code === 201) {
        console.log('用户删除成功');
        setToast({ message: '用户删除成功', type: 'success' });
      } else {
        console.log('用户删除失败');
        setToast({ message: '用户删除失败', type: 'error' });
      }
    }).catch((err: any) => {
      setToast({ message: '删除请求出错', type: 'error' });
      console.log("删除失败", err);
    });
  };

  // 表格列
  const columns = [
    { title: '姓名', dataIndex: 'username', key: 'username' },
    { title: '公司', dataIndex: 'company', key: 'company' },
    { title: '职位', dataIndex: 'position', key: 'position' },
    { title: '电话', dataIndex: 'phoneNumber', key: 'phoneNumber' },
    { title: '邮箱', dataIndex: 'email', key: 'email' },
    {
      title: '操作',
      key: 'action',
      render: (_: any, record: User) => (
        <Space>
          <Button icon={<EditOutlined />} onClick={() => openModal(record)}>
            编辑
          </Button>
          <Popconfirm
            title="确定删除此用户吗？"
            onConfirm={() => handleDelete(record.userId)}
          >
            <Button danger icon={<DeleteOutlined />}>
              删除
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <Layout className="user-management-layout">
      {/* 主内容 */}
      <Layout>
        <Header className="site-header">
          <div className="search-bar">
            <Input
              placeholder="搜索姓名/公司/职位/电话/邮箱"
              prefix={<SearchOutlined />}
              value={searchText}
              onChange={(e) => setSearchText(e.target.value)}
              style={{ width: 300, marginRight: 16 }}
            />
            {/* <Button type="primary" icon={<PlusOutlined />} onClick={() => openModal()}>
              新增用户
            </Button> */}
          </div>
        </Header>

        <Content className="site-content">
          <Select
            defaultValue="每页显示5条"
            style={{ width: 120, marginBottom: 16 }}
            onChange={handleChange}
            options={[
              { value: 5, label: '每页显示5条' },
              { value: 10, label: '每页显示10条' },
              { value: 20, label: '每页显示20条' },
              { value: 50, label: '每页显示50条' },
            ]}
          />
          <Table
            rowKey="userId"
            columns={columns}
            dataSource={filteredUsers}
            pagination={{ pageSize }}
          />
        </Content>
      </Layout>

      {/* 弹窗 */}
      <Modal
        title={editingUser ? '编辑用户' : '新增用户'}
        open={modalVisible}
        onCancel={() => setModalVisible(false)}
        onOk={handleSave}
        okText="保存"
        cancelText="取消"
      >
        <Form form={form} layout="vertical">
          <Form.Item
            name="username"
            label="姓名"
            rules={[{ required: true, message: '请输入姓名' }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="company"
            label="公司"
            rules={[{ required: true, message: '请输入公司' }]}
          >
            <Select
              value={form.getFieldValue('company')}
              onChange={(val) => form.setFieldValue('company', val)}
              options={[
                { value: '公司A', label: '公司A' },
                { value: '公司B', label: '公司B' },
                { value: '公司C', label: '公司C' },
              ]}
            />
          </Form.Item>
          <Form.Item
            name="position"
            label="职位"
            rules={[{ required: true, message: '请输入职位' }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="phoneNumber"
            label="电话"
            rules={[{ required: true, message: '请输入电话' }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="email"
            label="邮箱"
            rules={[{ required: true, type: 'email', message: '请输入有效邮箱' }]}
          >
            <Input />
          </Form.Item>
        </Form>
      </Modal>
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

export default UserManagement;
