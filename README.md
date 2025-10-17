🧠 智能学习辅助系统

📖 项目简介

智能学习辅助系统是一款面向学校与培训机构的综合教学管理平台。  

系统包含 教师端 与 学生端 两个主要模块，采用 前后端分离架构（Spring Boot + Vue），支持员工管理、学生成绩管理、智能分析等功能。  

旨在通过信息化手段提升教学效率，实现师生数据一体化与智能化管理。

---

🏗️ 系统架构

  层级  	技术栈                                 
  前端  	Vue 3、Element Plus、Axios、ECharts    
  后端  	Spring Boot、MyBatis、MySQL、PageHelper
  部署  	Nginx、Tomcat（可选）、HikariCP 数据源       
  接口文档	Swagger3 / Knife4j                  

---

✨ 核心功能

👩‍🏫 教师端

- 员工信息管理（增删改查）
- 教师部门分配与角色管理
- 学生成绩数据导入与统计
- 学生成绩趋势分析（ECharts 可视化）

🎓 学生端

- 登录与个人信息查看
- 成绩查询与课程统计
- 成绩变化趋势图表展示
- 智能学习建议与目标分析（可扩展）

---

📁 项目结构（后端）

    demo/
    ├── src/main/java/com/wyt/demo
    │   ├── controller/      # 控制层
    │   ├── service/         # 业务逻辑层接口
    │   ├── service/impl/    # 业务逻辑实现层
    │   ├── mapper/          # MyBatis Mapper接口
    │   ├── entity/          # 实体类（Emp、Student、Score等）
    │   ├── config/          # 项目配置类（Swagger、Cors等）
    │   └── DemoApplication.java
    ├── src/main/resources/
    │   ├── mapper/          # Mapper XML 文件
    │   ├── application.yml  # Spring Boot 配置文件
    │   └── static/          # 静态资源（如图片）

---

⚙️ 环境配置

数据库

    CREATE DATABASE tlias CHARACTER SET utf8mb4;

修改 application.yml 数据库连接配置：

    spring:
      datasource:
        url: jdbc:mysql://localhost:3306/tlias?useSSL=false&serverTimezone=UTC
        username: root
        password: root

---

🚀 启动步骤

后端启动

    cd demo
    mvn spring-boot:run

前端启动

    cd frontend
    npm install
    npm run serve

---

📊 功能预览

- 员工信息管理  
- 部门信息管理  
- 学生成绩录入与查询  
- 成绩可视化图表  
- 智能学习推荐（待开发）  
- AI 辅助教学分析（规划中）

---

🧩 接口示例

获取所有员工信息

    GET /emp/list

返回示例：

    [
      {
        "id": 36,
        "username": "linghuchong",
        "name": "令狐冲",
        "deptName": "教研部",
        "salary": 6800
      }
    ]

---

💡 未来计划

- 支持学生端移动端适配（Vue3 + Vant）
- 成绩预测与智能学习建议（基于机器学习模型）
- 在线作业与自动批改功能

---

🧑‍💻 作者信息

开发者： 28TheCat  

项目类型： 学生管理 / 教学辅助系统  

后端框架： Spring Boot + MyBatis  

前端框架： Vue 3 + Element Plus  

数据库： MySQL  
