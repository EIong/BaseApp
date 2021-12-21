# BaseApp
> 一个封装了加载状态、屏幕适配和日志查看的Android MVC&MVP开发框架。👉[项目地址](https://github.com/EIong/BaseApp)👈

### 集成
* 在项目根目录下的 `build.gradle` 文件中加入

``` groovy
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```
* 在项目app模块下的 `build.gradle` 文件中加入

``` groovy
android {
	viewBinding {
		enabled = true
	}
}

dependencies {
	implementation 'com.github.EIong:BaseApp:2.0.0'
}
```
### 使用
* 在应用 `Application` 中设置框架
  * 屏幕适配用副单位pt进行开发，dp和sp不会进行屏幕适配，可参考[Android 屏幕适配终结者](https://juejin.cn/post/6844903742135861261)
  * 自定义加载UI，加载状态适配器可参考[BaseLoadingAdapter](https://github.com/EIong/BaseApp/blob/master/baseapp/src/main/java/com/eiong/baseapp/adapter/BaseLoadingAdapter.kt)

``` kotlin
LogUtils.getConfig()
	// 设置日志保存天数
	.setSaveDays(3)
	// 设置输出日志文件
	.isLog2FileSwitch = true

BaseManager
	// 设置是否开启屏幕适配（默认关闭）
	.setOpenAdaptScreen(true)
	// 设置屏幕适配方向（默认宽度）
	.setAdaptScreenDirection(BaseManager.Direction.WIDTH)
	// 设置屏幕适配参数（默认1080）
	.setAdaptScreenValue(1080)
	// 设置加载状态适配器（默认BaseLoadingAdapter）
	.setLoadingAdapter(BaseLoadingAdapter())
	// 设置日志加密（密码长度为16）
	.enableLogEncryption("1111111111111111")
```
* MVC Activity继承 `BaseActivity`，传入相应ViewBinding

``` kotlin
class MvcActivity : BaseActivity<ActivityMvcBinding>()
```
* MVC Fragment继承 `BaseFragment`，传入相应ViewBinding

``` kotlin
class MvcFragment : BaseFragment<FragmentMvcBinding>()
```
* MVP 编写 `Contract` 契约类

``` kotlin
interface MvpContract {
	interface MvpPresenter : BaseContract.BasePresenter
	interface MvpView : BaseContract.BaseView
}
```
* MVP Presenter继承 `BasePresenter`，传入相应View，实现契约类中Presenter接口

``` kotlin
class MvpPresenter : BasePresenter<MvpContract.MvpView>(), MvpContract.MvpPresenter
```
* MVP Activity继承 `BaseActivity`，传入相应ViewBinding和Presenter，实现契约类中View接口

``` kotlin
class MvpActivity : BaseActivity<ActivityMvpBinding, MvpPresenter>(), MvpContract.MvpView
```
* MVP Fragment继承 `BaseFragment`，传入相应ViewBinding和Presenter，实现契约类中View接口

``` kotlin
class MvpFragment : BaseFragment<FragmentMvpBinding, MvpPresenter>(), MvpContract.MvpView
```
> Activity和Fragment中通过 `vb` 拿到ViewBinding对象，`p` 拿到Presenter对象，Presenter中通过 `v` 拿到View对象。

* 重写此方法实现初始化

``` kotlin
override fun initialize(savedInstanceState: Bundle?)
```
* 请求权限
  * permissionsTip：请求权限提示
  * cancel：取消按钮文本
  * confirm：确定按钮文本
  * onCancel：点击取消回调
  * onGrantedAll：获取所有权限回调

``` kotlin
requestPermissions(
	permissionsTip,
	cancel,
	confirm,
	onCancel,
	onGrantedAll,
	permission1,
	permission2,
	...
)
```
* 重写此方法实现请求权限弹窗自定义UI
  * message：消息内容
  * cancel：取消按钮文本
  * confirm：确定按钮文本
  * onCancel：点击取消回调
  * onConfirm：点击确定回调

``` kotlin
override fun showBaseDialog(
	activity: Activity,
	message: String,
	cancel: String,
	confirm: String,
	onCancel: () -> Unit,
	onConfirm: () -> Unit
)
```
* 显示加载中

``` kotlin
showLoading()
```
* 显示加载成功

``` kotlin
showSuccess()
```
* 显示加载失败
  * retry：重试任务

``` kotlin
showFailed(retry)
```
* 显示无数据
  * retry：重试任务

``` kotlin
showEmpty(retry)
```
* 指定View上显示加载中
  * view：指定View

``` kotlin
showLoadingOverView(view)
```
* 指定View上显示加载成功
  * view：指定View

``` kotlin
showSuccessOverView(view)
```
* 指定View上显示加载失败
  * view：指定View
  * retry：重试任务

``` kotlin
showFailedOverView(view, retry)
```
* 指定View上显示无数据
  * view：指定View
  * retry：重试任务

``` kotlin
showEmptyOverView(view, retry)
```
* 添加点击事件

``` kotlin
addClick(view1, view2, ...)
```
* 重写此方法实现点击事件

``` kotlin
override fun onClick(v: View)
```
* 显示View

``` kotlin
showViews(view1, view2, ...)
```
* 隐藏View（INVISIBLE）

``` kotlin
hideViews(view1, view2, ...)
```
* 隐藏View（GONE）

``` kotlin
goneViews(view1, view2, ...)
```
* 查看日志
  * password：密码，长度为16

``` kotlin
BaseManager.goToLog(activity, password)
```
### 说明
> 框架中引入了[**Gloading**](https://github.com/luckybilly/Gloading)（超轻量级，深度解耦Android App中全局加载中、加载失败及无数据视图），主要用于加载状态显示，引入了[**AndroidUtilCode**](https://github.com/Blankj/AndroidUtilCode)（一个强大易用的安卓工具类库，它合理地封装了安卓开发中常用的函数，具有完善的 Demo 和单元测试，利用其封装好的 APIs 可以大大提高开发效率），主要用于屏幕适配和权限请求等，如需使用以上框架，无需重新引入，**Respect~**。