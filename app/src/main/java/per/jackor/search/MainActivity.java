package per.jackor.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;

import per.jackor.lib_search.adapter.SearchLayout;

public class MainActivity extends AppCompatActivity implements SearchLayout.OnSearchClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void search_click(View view) {
        SearchLayout searchLayout = new SearchLayout(this);
        String[] tags = {"披萨", "火锅", "海底捞", "芝士蛋糕", "大闸蟹", "黄焖鸡米饭", "沙县小吃"};
        searchLayout.pop(getWindow().getDecorView(), Arrays.asList(tags),Arrays.asList(tags));
        searchLayout.setOnSearchClickListener(this);
    }

    @Override
    public void onSearchClick(String searchContent) {
        Toast.makeText(this, searchContent, Toast.LENGTH_SHORT).show();
    }

}
