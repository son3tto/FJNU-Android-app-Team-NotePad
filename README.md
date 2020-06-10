# 移动软件开发期末作业

**抄送**

福建师范大学数学与信息学院2017级软件工程闽台班7组

**小组成员**

[sonettofighting](https://github.com/sonettofighting/)

[yoooogaaaa](https://github.com/yoooogaaaa/)

[lyl10](https://github.com/lyl10/)

[eric-ruhu](https://github.com/eric-ruhu)

### 写给小组

**第一次关联仓库：：：**
PS:因为可以使用ssh/http两种方式，考虑到科学上网/ssh密钥的问题，我不确定两种方式是否可以成功！
所以没有设置成脚本！！！！！

```shell
git init
git remote add origin https://github.com/sonettofighting/FJNU-Android-app-Team-NotePad.git
git pull origin master
```

**：之后每次更新代码：：：：：：**

双击update.sh！！！！确认更新了就可以继续了！！！！！！！

update.sh写的就是：

> git pull origin master
> git add -A
> git commit -am "$(date "+%Y-%m-%d %H:%M:%S")"
> git push origin master