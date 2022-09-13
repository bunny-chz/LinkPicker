/**                     
    * Project:  LinkPicker
    * Comments: QS_Tile快捷操作下拉栏界面功能实现类
    * JDK version used: <JDK1.8>
    * Author： Bunny     Github: https://github.com/bunny-chz/
    * Create Date：2022-06-26
    * Version: 1.0
    */

package com.bunny.LinkPicker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class QS_Tile_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qs_tile_activity);
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
        if(!url.equals("")) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("提取结果");
        builder.setMessage(url);
        builder.setPositiveButton("确定", (dialogInterface, i) -> {
            this.finish();
        });
        builder.setNegativeButton("复制到剪切板", (dialogInterface, i) -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(MainActivity.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("url", url);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "已复制到剪切板", Toast.LENGTH_SHORT).show();
            this.finish();
        });
        builder.setNeutralButton("浏览器访问", (dialogInterface, i) -> {
            try {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(uri);
                startActivity(intent);
                this.finish();
            } catch (Exception e) {
                Toast.makeText(this, "错误：网络链接格式有误或者没安装浏览器", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        });
        builder.create().show();
        }
        if(url.equals("")) {
            Toast.makeText(this, "无网络链接可提取", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }

}
