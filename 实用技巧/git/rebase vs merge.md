---
title: Git合并分支使用Mgerge还是Rebase？
categories: Git
date: 2018-05-16 19:51:15
---

作为一个有追求的开发者，我一定会选择更好的版本管理工具（Git）, 使用中我们难免会在 Merge 和 Rebase 中选择其一用于合并分支。

<!-- more -->

Rebase 和 merge 都是被设计用于集成你所做的改变从一个分支到另一个分支，只是通过不同的方式。虽然目的相同，但不同的方式有不同的优缺点。

## 区别
例如：我们有下面的几个commit，merge会将一些commit的组合作为一个结果，而rebase会将所有commit添加到目标分支的最近一次提交之后。
![merge vs rebase](images/merge%20vs%20rebase.png)

通过上图我们可以看到，merge 会存在合并的历史记录，而rebase没有了历史记录且成一条直线。

### Merge

- 简单易理解
- 源分支和目标分支相互分离
- 保留功能分支的提交历史和分支图形
- 分支一旦较多显示比较混乱

### Rebase

- 简化复杂的记录且线性可读
- 没有合并的记录
- 多个commit冲突时必须一个个提交去修改
- 不能和 merge/pull request 一起使用

## 什么时候使用rebase？什么时候使用merge ？

- 独立开发

如果你不是团队合作开发，那么你可以优先选择使用rebase来保持你整洁的提交历史。

- 准备code review

你需要在合并的时候有人来给你review，此时你需要提交一个 pull request,此时别人认为通过会执行merge来合并，这将保存你此次的请求合并的记录，已备将来追溯。

- 合并多个目标分支或其他人正在使用当前分支

这是应该使用merge,因为你执行rebase时,当前分支原先的commit会被删除，形成新的commit连接在目标分支最新commit之后。

## 我的倾向

我更倾向于团队成员不多于4人的时候提交merge request,这个我可以review别人的代码并确定是否通过请求，也可以看到每个人开发合并的历史记录。
当人数较多且不符合上面第三点时（合并多个目标分支或其他人正在使用当前分支），我希望使用rebase合并前要邀请别人进行review,当通过时使用rebase进行合并。

不知道你是如何的呢？

## 参考资料

https://dzone.com/articles/git-merge-vs-rebase

https://medium.freecodecamp.org/an-introduction-to-git-merge-and-rebase-what-they-are-and-how-to-use-them-131b863785f

https://www.git-tower.com/learn/git/ebook/cn/command-line/advanced-topics/rebase