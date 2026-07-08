# 智能学习辅助系统

## 项目简介

智能学习辅助系统是一款面向学校与培训机构的综合教学管理平台。

系统包含教师端与学生端两个主要模块，采用前后端分离架构（Spring Boot + Vue），支持员工管理、学生成绩管理、智能分析等功能。

旨在通过信息化手段提升教学效率，实现师生数据一体化与智能化管理。

---

## 系统架构

| 层级 | 技术栈 |
| --- | --- |
| 前端 | Vue 3、Vite、Element Plus、Axios、ECharts |
| 后端 | Spring Boot、MyBatis、MySQL、PageHelper |
| 部署 | Nginx、Tomcat（可选）、HikariCP 数据源 |
| 接口文档 | Swagger3 / Knife4j |

---

## 核心功能

### 教师端

- 员工信息管理（增删改查）
- 教师部门分配与角色管理
- 学生成绩数据导入与统计
- 学生成绩趋势分析（ECharts 可视化）

### 学生端

- 登录与个人信息查看
- 成绩查询与课程统计
- 成绩变化趋势图表展示
- 智能学习建议与目标分析（可扩展）

---

## 项目结构

```text
Smart-Learning-Assistant/
├── backend/                         # Spring Boot 后端主工程
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/wyt/
│       │   ├── controller/          # 控制层
│       │   ├── service/             # 业务逻辑层接口
│       │   ├── service/impl/        # 业务逻辑实现层
│       │   ├── mapper/              # MyBatis Mapper 接口
│       │   ├── pojo/                # 实体与查询对象
│       │   ├── WebConfig/           # Web 配置
│       │   └── DemoApplication.java
│       └── resources/
│           ├── com/wyt/mapper/      # Mapper XML 文件
│           ├── application.yml      # Spring Boot 配置文件
│           └── static/              # 静态资源
├── frontend/
│   ├── Smart-Learning-Assistant/    # Vue 3 + Vite 前端主工程
│   └── vue-project/                 # 历史/实验前端目录，非主工程
├── database/                        # 数据库相关文件
└── nginx-1.22.0-web/                # Nginx 部署相关文件
```

> 当前协作默认以前端 `frontend/Smart-Learning-Assistant` 为主工程。`frontend/vue-project` 保留为历史/实验目录，建议后续确认无用后归档或移除，避免维护时混淆。

---

## 环境配置

### 数据库

```sql
CREATE DATABASE tlias CHARACTER SET utf8mb4;
```

后端通过环境变量读取数据库和 OSS 配置。启动前至少需要配置：

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/tlias?useSSL=false&serverTimezone=UTC"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="root"
$env:JWT_SIGN_KEY="replace-with-a-long-random-secret"
```

如果需要使用阿里云 OSS 上传功能，还需要配置：

```powershell
$env:ALIYUN_OSS_ENDPOINT="your-endpoint"
$env:ALIYUN_OSS_BUCKET_NAME="your-bucket"
$env:ALIYUN_OSS_REGION="your-region"
```

JWT 默认有效期为 12 小时，可通过 `JWT_EXPIRE_MILLIS` 调整。轮换 JWT 密钥时，将新密钥写入 `JWT_SIGN_KEY`，并把仍需兼容的旧密钥以逗号分隔写入 `JWT_PREVIOUS_SIGN_KEYS`。

---

## 启动步骤

### 后端启动

```powershell
cd backend
mvn spring-boot:run
```

也可以使用仓库内的 Maven Wrapper：

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

### 前端启动

```powershell
cd frontend/Smart-Learning-Assistant
npm install
npm run dev
```

前端开发服务器由 Vite 启动，默认访问地址通常为 `http://localhost:5173/`。

---

## 功能预览

- 员工信息管理
- 部门信息管理
- 学生成绩录入与查询
- 成绩可视化图表
- 智能学习推荐（待开发）
- AI 辅助教学分析（规划中）

---

## 接口示例

获取所有员工信息：

```http
GET /emp/list
```

返回示例：

```json
[
  {
    "id": 36,
    "username": "linghuchong",
    "name": "令狐冲",
    "deptName": "教研部",
    "salary": 6800
  }
]
```

---

## 未来计划

- 支持学生端移动端适配（Vue3 + Vant）
- 成绩预测与智能学习建议（基于机器学习模型）
- 在线作业与自动批改功能

---

## 作者信息

开发者：28TheCat

项目类型：学生管理 / 教学辅助系统

后端框架：Spring Boot + MyBatis

前端框架：Vue 3 + Element Plus

数据库：MySQL
