
![](/screenshots/pop_search.gif)

在project的gradle中导入

	maven { url 'https://jitpack.io' }

然后在app的gradle中导入

	implementation 'com.github.jayjunLiao:SearchPopupWindow:1.0.2'

#### 使用：

	SearchLayout searchLayout = new SearchLayout(this);
	searchLayout.pop(pupupWindow依赖的view,历史搜索数据源，热门搜索数据源);

例如全屏弹出

	String[] tags = {"披萨", "火锅", "海底捞", "芝士蛋糕", "大闸蟹", "黄焖鸡米饭", "沙县小吃"};
    searchLayout.pop(getWindow().getDecorView(), Arrays.asList(tags),Arrays.asList(tags));

可以设置标签字体大小、间距，popupWindow大小等，默认宽高占满屏幕(locationView使用getWindow().getDecorView()的情况下)