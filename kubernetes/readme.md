# k8s按照记录
参考的：https://www.cnblogs.com/liwinallucky/articles/17219248.html

# Containerd or Docker
遇到的第一个问题就是新版k8s换成了Containerd，对这套命令不熟悉，包括ctr和crictl，似乎是一个原始命令，另一个是对原始命令做了封装后的插件命令，能做到类似docker的操作。

# Cannot pull by k8s and docker
第二个问题就是无法从k8s网站和hub.docker.com(docker.io)获取镜像，导致最基础的pod构建不了，需要找代理，可以baidu几个然后试试，类似docker修改daemon.json文件，Containerd需要修改config.tomi文件，添加两段配置
```
[plugins."io.containerd.grpc.v1.cri"]
  sandbox_image = "registry.aliyuncs.com/google_containers/pause:3.8" // 如果获取不了，可以用crictl手动pull下来，然后用ctr tag命令从新命名解决
[plugins."io.containerd.grpc.v1.cri".registry.mirrors]
  [plugins."io.containerd.grpc.v1.cri".registry.mirrors."docker.io"]
    endpoint = ["{docker-source-url}"]
  [plugins."io.containerd.grpc.v1.cri".registry.mirrors."docker.io/library"]
    endpoint = ["{docker-source-url/library}"]
```
