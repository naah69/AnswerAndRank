# AnswerAndRank
模仿冲顶大会规则制作的H5版答题类游戏
有详细要问得可以加我的QQ或者微信
278567949


## 架构
Springboot+redis+mybatis+clickhouse

主要使用websocket进行构建，用于双工通信


## 配置
SpringBoot配置文件 src/main/resources/application*.yml

    dev为开发环境，ngrok为内网穿透环境，prod为工作环境

mybatis配置文件 src/main/resources/mybatis/

    数据库只用于存储题，结构相对简单，大家可以改为mysql或其他数据库

## 使用流程
    1.进入后台页面-问题设置
    2.上传题目excel，并确定保存
    3.进入后台页面-时间设置
    4.设置开始时间
    5.用户填写信息登录
    6.进入后台页面-题目选择
    7.控制流程发（下一题OR提交答案）
    8.打印数据导出excel


## 常见问题
（一）地址问题

    构建项目时，配置文件中数据库、redis以及js中的后台地址需要填写正确

（二）时间问题

    服务器时间需要精准,开启后台控制页面的机器也需要进行时间校准

（三）页面地址

    http://ip/ 是前端答题页，需填写姓名公司和手机号，手机号作为唯一标识

    http://ip/RankingList.html 是排行榜页，可以实时查看题目和排行榜

    http://ip/admin  是后台控制页面，首次登陆需填写密码


