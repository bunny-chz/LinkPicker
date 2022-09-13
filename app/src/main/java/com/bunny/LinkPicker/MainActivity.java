/**                     
    * Project:  LinkPicker
    * Comments: 主界面类
    * JDK version used: <JDK1.8>
    * Author： Bunny     Github: https://github.com/bunny-chz/
    * Create Date：2022-06-26
    * Version: 1.0
    */

package com.bunny.LinkPicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText ShareLinkText;
    Button parse_ShareLinkText,clear_text;
    TextView parse_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView() {
        ShareLinkText = findViewById(R.id.ShareLinkText);
        parse_ShareLinkText = findViewById(R.id.parse_ShareLinkText);
        clear_text = findViewById(R.id.clear_text);
        parse_result = findViewById(R.id.parse_result);
        parse_ShareLinkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(ShareLinkText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "输入为空，请您输入字符！", Toast.LENGTH_SHORT).show();
                    return;
                }
                parse_ShareLinkText();
            }
        });
        clear_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareLinkText.setText("");
            }
        });
    }

    public void parse_ShareLinkText() {
        final String url = Util.getUrl(ShareLinkText.getText().toString());
        if(!url.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(true);
            builder.setTitle("提取结果");
            builder.setMessage(url);
            builder.setPositiveButton("确定", null);
            builder.setNegativeButton("复制到剪切板", (dialogInterface, i) -> {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(MainActivity.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("url", url);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "已复制到剪切板", Toast.LENGTH_SHORT).show();
            });
            builder.setNeutralButton("浏览器访问", (dialogInterface, i) -> {
                try {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(uri);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "错误：网络链接格式有误或者没安装浏览器", Toast.LENGTH_SHORT).show();
                }
            });
            builder.create().show();
        }
        if(url.equals("")) {
            Toast.makeText(this, "无网络链接可提取", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            return;
        }
        // 在 Android Q（10）中，应用在前台的时候才可以获取到剪切板内容。
        String shareText = Util.getClipboardText(this);
        final String url = Util.getUrl(shareText);
        parse_result.setText(url);
        parse_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(MainActivity.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("url", url);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "已复制到剪切板", Toast.LENGTH_SHORT).show();
            }
        });
        parse_result.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                try {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(uri);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "错误：网络链接格式有误或者没安装浏览器", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    /**
     再按一次退出主界面操作
     **/
    long exitTime = 0;
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
            return;
        }
        finish();
    }
}