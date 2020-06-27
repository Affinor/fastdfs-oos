一、fastdfs环境
1、准备1台sentos7虚拟机192.168.80.129
2、安装编译环境
yum install git gcc gcc-c++ make automake vim wget libevent -y
3、安装libfastcommon 基础库
mkdir /home/db/fastdfs 
cd /home/db/fastdfs 
git clone https://github.com/happyfish100/libfastcommon.git --depth 1 
cd libfastcommon/ 
./make.sh && ./make.sh install
4、安装FastDFS
cd /home/db/fastdfs 
wget https://github.com/happyfish100/fastdfs/archive/V5.11.tar.gz 
tar -zxvf V5.11.tar.gz 
cd fastdfs-5.11 
./make.sh && ./make.sh install 
5、配置文件准备 
cp /etc/fdfs/tracker.conf.sample /etc/fdfs/tracker.conf 
cp /etc/fdfs/storage.conf.sample /etc/fdfs/storage.conf 
cp /etc/fdfs/client.conf.sample /etc/fdfs/client.conf 
cp /home/db/fastdfs/fastdfs-5.11/conf/http.conf /etc/fdfs 
cp /home/db/fastdfs/fastdfs-5.11/conf/mime.types /etc/fdfs
6、修改配置文件
vim /etc/fdfs/tracker.conf 
#需要修改的内容如下 
port=22122 
base_path=/home/db/fastdfs
vim /etc/fdfs/storage.conf 
#需要修改的内容如下 
port=23000 
base_path=/home/db/fastdfs 
# 数据和日志文件存储根目录 
store_path0=/home/db/fastdfs 
# 第一个存储目录 
tracker_server=192.168.211.136:22122 
# http访问文件的端口(默认8888,看情况修改,和nginx中保持一致) 
http.server_port=8888
7、启动
/usr/bin/fdfs_trackerd /etc/fdfs/tracker.conf restart 
/usr/bin/fdfs_storaged /etc/fdfs/storage.conf restart
8、测试上传
vim /etc/fdfs/client.conf 
#需要修改的内容如下 
base_path=/home/fastdfs 
#tracker服务器IP和端口 
tracker_server=192.168.211.136:22122 
#保存后测试,返回ID表示成功 如：group1/M00/00/00/xxx.png 
/usr/bin/fdfs_upload_file /etc/fdfs/client.conf /home/db/fastdfs/1.png 
group1/M00/00/00/wKhQgV70oLKAQu-pAAZ39D4ZtXI654.png

二、OSS环境
1、登陆阿里云
2、开头OSS对象存储服务
3、在页面创建Bucket
4、获取accessKeyId和accessKeySecret
5、记录上海域名endpoint=http://oss-cn-shanghai.aliyuncs.com
6、开始编码并测试效果