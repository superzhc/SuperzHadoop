# Git 版本控制命令

## `git init`

初始化一个Git仓库

## `git add`

将修改文件添加到暂存区

**示例**

```bash
# 提交被修改的和新建的文件，但不包括被删除的文件，注意：有个.符号
git add .

# update tracked files    更新所有改变的文件，即提交所有变化的文件
git add -u
git add --update

# add changes from all tracked and untracked files   提交已被修改和已被删除文件，但是不包括新的文件
git add -A
git add --all
```

注：支持 Ant 风格添加修改

## `git commit`

格式：`git commit -m <comment>`

将暂存区的所有内容提交到当前分支，后面添加上有意义的注释信息。

## `git status` 查看仓库状态

`git status` 命令可以查看仓库当前的状态。通过 `git status` 命令会输出用户对文件的操作情况。

## `git diff` 

格式：`git diff file`

在 file 被修改了还未提交的时候查看修改的部分，显示的格式是 Unix 通用的 diff 格式。

注：diff 比对的是工作区和版本库最新版本的区别

## `git log` 查看历史纪录

查看 git 的 commit 信息，每次提交的信息包括注释在内，从最新提交到最久提交。

参数：
- `--pretty=oneline`：将 commit 信息简化成一行显示
- `--graph`:查看图形化日志

## `git reset`

`git reset` 命令既可以回退版本，也可以把暂存区的修改回退到工作区。

- **回退版本**

要进行版本回退，首先 Git 必须知道当前版本是哪个版本，*在 Git 中，用 HEAD 表示当前版本，也就是最新的提交 `commit ID`，上一个版本就是 `HEAD^`，上上一个版本就是`HEAD^^`，当然往上 100 个版本写 100 个 `^` 比较容易数不过来，所以写成 `HEAD~100`*。

用法：

```bash
git reset --hard HEAD^   #退回到相对于当前版本的上一个版本 HEAD 表示当前版本
git reset --hard HEAD^^  #退回到相对于当前版本的上上一个版本  HEAD 代表当前版本
git reset --hard HEAD~100 #退回到相对于当前版本的上 100 个版本去  HEAD 表示当前版本
git reset --hard 3628164 #退回到指定的版本   这里不需要全部写commit id  Git 会去自动适配,Git的版本回退速度非常快，因为Git在内部有个指向当前版本的HEAD指针，当你回退版本的时候，Git仅仅是把HEAD从指向commit ID
```

回退版本后，若需要还原回来，通过找到还原版本的 commit ID，就可以还原回来

Git 回退版本的过程：

Git 在内部有个指向当前版本的 HEAD 指针，当回退版本的时候，Git 仅仅是把 HEAD 从指向 `append GPL` 改为指向 `add distributed`:

![git回退](./images/Git_reset1.jpg)

变更指向：

![git回退](./images/Git_reset2.jpg)

然后把工作区的文件更新下。所以 HEAD 指向哪个版本号，当前工作空间就处于那个版本。

也可以对单个文件进行版本回退的操作，如下所示：

```bash
git reset --hard HEAD^ test.txt
```

- **暂存区的修改回退到工作区**

格式：

```bash
git reset HEAD <file>
```

把暂存区的修改撤销掉（unstage），重新放回工作区。即，撤销先前 `git add` 的操作

## `git reflog` 记录每次提交命令

当需要从回退版本回到最新版本，可以使用 `git reflog` 来找到旧版本之前的提交日志的 commit Id。

### `git checkout`

- **撤销修改**

格式：

```bash
# 单个文件的修改
git checkout -- <file>
# 整个文件夹的修改
git checkout .
git checkout src/main/resources
```

把 file 在工作区的所有修改都撤销，这里有两种情况：
- 一种是 file 自修改后还没有被放到暂存区，现在，撤销修改就回到和版本库一模一样的状态；
- 一种是 file 已经添加到暂存区后，又做了修改，现在，撤销修改就回到添加到暂存区后的状态。

注：`git checkout -- <file> `命令中的 `--` 很重要，没有 `--`，就变成了“切换到另一个分支”的命令

- **切换分支**

格式：`git checkout <name>`

参数：
- `-b` :表示创建并切换，相当于以下两个命令
    ```bash
    git checkout -b dev
    # ||
    #\||/
    # \/
    git branch dev
    git checkout master
    ```

### `git rm` 删除文件

格式：`git rm <file>`

从 Git 版本库中删除文件(同时从文件系统中删除文件)

### `git clone`

将一个远程的仓库克隆到本地

**指定保存目录**

```sh
git clone <仓库地址> <目录地址名称，如：./aaa>
```

### `git remote`

[远程仓库](./远程仓库.md)

### `git pull`

`git pull` 命令用于从另一个存储库或本地分支获取并集成(整合)。`git pull` 命令的作用是：取回远程主机某个分支的更新，再与本地的指定分支合并。

`git pull` 其实就是 `git fetch` 和 `git merge FETCH_HEAD` 的简写。

```bash
git pull [options] [<repository> [<refspec>…]]
# ==
git pull <远程主机名> <远程分支名>:<本地分支名>
```

**示例**

```bash
# 更新操作
git pull
git pull origin

# 将远程主机 origin 的 master 分支拉取过来，与本地的 brantest 分支合并
git pull origin master:brantest

# 如果远程分支是与当前分支合并，则冒号后面的部分可以省略
git pull origin master
```

### `git push`

把本地库的内容推送到远程

```bash
git push -u origin master #将本地的master分支推送到远程的master分支中
git push -u origin dev #本地切换到dev分支然后将本地的dev分支推送到远程
```

### `git branch`

- **查看分支**

格式：`git branch`

`git branch` 命令会列出所有分支，当前分支前面会标一个 `*` 号。

- **创建分支**

格式：`git branch <name>`

- **删除分支**

格式：`git branch -d <name>`

- **丢弃一个没有被合并过的分支**

格式：`git branch -D <name>`

强制删除一个没有被合并过的分支

### `git merge`

格式：`git merge <name>`

合并某分支到当前分支

### `git stash`