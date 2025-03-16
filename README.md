### Asset项目

- 使用Jetpack和Compose重写存利多项目
- 项目开始：2025-03-10
- 项目结束：2025-03-15
- 工时：40+h

#### 项目架构

这是一个典型的MVVM架构，数据层通过Repository与ViewModel交互，ViewModel与UI层通过Compose进行数据绑定和状态管理。项目结构清晰，遵循了Android开发的最佳实践。

- 数据层 ：

  - data/local ：包含数据库相关实现，使用Room进行数据持久化
  - repository ：数据仓库，负责数据获取和业务逻辑处理
- UI层 ：

  - ui/screen ：页面组件，负责界面展示
  - ui/components ：可复用UI组件
  - ui/theme ：主题和样式定义
- navigation：导航相关
- 业务逻辑层 ：

  - viewmodel ：ViewModel负责处理业务逻辑和状态管理
  - util ：工具类，包含通用功能
- 配置层 ：

  - gradle ：Gradle相关配置
  - res ：资源文件管理

#### 技术栈

- Room
- Repository
- ViewModel
- Navigation
- Compose
  - 组件
    - Text
    - Button
    - FloatingActionButton
    - ModalBottomSheet
  - 布局
- 

#### 项目难点

- 添加记录时，不能在主线程中进行
  - 需要在`ViewModel`中使用`withContext(Dispatchers.IO)`进行添加，
- 添加记录后，不能与`LazyColumn`同步，这是由于添加的`AppendSavings`是由`Navigation`导航后的页面，它使用的`ViewModel`与`LazyColumn`中的不是一个实例
  - 需要使用`CompositionLocalProvider`在`Compose`的UI树中提供一个共享的 `MainViewModel` 实例，让所有子组件都可以访问它
- 在`compose`中切换应用的深色和浅色主题时，需要在`ViewModel`中建立一个变量，然后用这个变量在`Theme`中的`compose`主题函数中更改应用的主题
- 在使用`SnackBar`时，最好定义一个`CompositionLocal`来共享`SnackbarHostState`，在使用`SnackBar`的`compose`函数中获取它，并使用`scope`来让它显示，显示 的位置也很重要，注意不要让其他的组件因重组重绘而遮挡

