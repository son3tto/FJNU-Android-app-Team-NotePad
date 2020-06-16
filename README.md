# 移动软件开发期末作业

##### 福建师范大学数学与信息学院2017级软件工程闽台班7组

**小组成员**

[sonettofighting](https://github.com/sonettofighting/)

[yoooogaaaa](https://github.com/yoooogaaaa/)

[lyl10](https://github.com/lyl10/)

[eric-ruhu](https://github.com/eric-ruhu)

### 写给小组

| 功能认领                                     | **抄送** |
| -------------------------------------------- | -------- |
| 修改背景色及字体颜色                         |          |
| 添加标签文本分类                             |          |
| 插入图片                                     |          |
| 添加提醒时间                                 |          |
| 添加代办                                     |          |
| 一个长按选项，长按后出现删除和短信发送的提示 |          |
|                                              |          |

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


（1）到时候如果大家做安卓，怕不小心把错的程序推上去了，
可以先git checkout -b [分支名字]，在自己本机上新建分支，之后可以跑了，
再使用git checkout master，切换到本机的master分支，
然后使用git merge [分支名]，把本机的新分支和本机的master分支合并再推送。

（2）补充，如果想试试可以试试，不用也没关系！！：
如果直接在新分支上进行
git commit ..
git push origin [新分支]
就是从自己的分支上推送到github上面，之后在github上面会生成一个pull request(合并请求)，
打开github的话，在仓库这里甚至可以查看每个分支的内容。
![git-version-control-1](https://github.com/sonettofighting/FJNU-Android-app-Team-NotePad/tree/master/git-version-control-1.jpg)
选择pull requests这里这以查看代码修改了哪些地方啥的, 
更重要的是，在这里同意合并的话，就会合并到master分支上！
![git-version-control-2](https://github.com/sonettofighting/FJNU-Android-app-Team-NotePad/tree/master/git-version-control-2.jpg)


如果程序出错可以用rollback进行回滚，不过这我又不懂了。Android studio可以直接
![git-version-control-3](https://github.com/sonettofighting/FJNU-Android-app-Team-NotePad/tree/master/git-version-control-3.jpg)
在这里版本控制，也可以用命令行。感觉好难，有错再说吧。。

整了个图
![git-version-control-4](https://github.com/sonettofighting/FJNU-Android-app-Team-NotePad/tree/master/git-version-control-4.jpg)