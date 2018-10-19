package net.suntrans.powerpeace.ui.activity

import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.webkit.*
import android.widget.AdapterView
import net.suntrans.looney.utils.UiUtils
import net.suntrans.powerpeace.App
import net.suntrans.powerpeace.R
import net.suntrans.powerpeace.api.RetrofitHelper
import net.suntrans.powerpeace.bean.MessageCenter
import net.suntrans.powerpeace.bean.ResultBody
import net.suntrans.powerpeace.databinding.ActivityMsgAddBinding
import net.suntrans.powerpeace.rx.BaseSubscriber
import net.suntrans.powerpeace.utils.StatusBarCompat
import rx.Subscriber

/**
 * Created by Looney on 2017/9/19.
 */

class MsgAddActivity : BasedActivity(), SwipeRefreshLayout.OnRefreshListener {

    private var binding: ActivityMsgAddBinding? = null

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_msg_add)
        StatusBarCompat.compat(binding!!.headerView)

        binding!!.toolbar.title = "推送消息"
        setSupportActionBar(binding!!.toolbar)

        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        binding!!.delay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                delay = position + 1
            }

        }

        binding!!.msg.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                    }

                    override fun afterTextChanged(s: Editable) {
                        println(s.length)
                        val a = s.length
                        binding!!.count.text = "$a/30"
                    }
                })
        binding!!.commit.setOnClickListener {
            message=binding!!.msg.text.toString()
            if (TextUtils.isEmpty(message)) {
                UiUtils.showToast("请输入信息")
                return@setOnClickListener
            }
            commitNotice(message, delay.toString())
        }
//        binding!!.msg

//        setUpWebview(binding!!.webview)
//        val username=App.sharedPreferences.getString("username","admin")
//        val user_id=App.sharedPreferences.getString("studentID","1")
//        val token=App.sharedPreferences.getString("token","1")
//        binding!!.webview.setWebViewClient(object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//                view.loadUrl(url)
//                return true
//            }
//
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
//                binding!!.webview.loadUrl("javascript:initEnergy('$token','$user_id','$username')")
//            }
//        })
//        binding!!.webview.setWebChromeClient(object : WebChromeClient() {
//            override fun onProgressChanged(view: WebView, newProgress: Int) {
//                //get the newProgress and refresh progress bar
//
//            }
//        })
//        binding!!.webview.addJavascriptInterface(AndroidtoJs(), "nativeAlert")
//
//        val url = "file:///android_asset/msg/msg.html"
//        binding!!.webview.loadUrl(url)
    }


    private fun setUpWebview(webview: WebView) {
        val settings = webview.settings

        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.setGeolocationEnabled(true)
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.builtInZoomControls = true
        settings.displayZoomControls = false

        webview.setInitialScale(0)
        webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
//        webview.setWebViewClient(WebViewClient())
        webview.isVerticalScrollBarEnabled = false

        val localWebSettings = webview.settings
        localWebSettings.javaScriptEnabled = true
        localWebSettings.javaScriptCanOpenWindowsAutomatically = true
        localWebSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL

        webview.isHorizontalScrollBarEnabled = false//水平不显示
        webview.isVerticalScrollBarEnabled = true


    }

    // 继承自Object类
    inner class AndroidtoJs : Any() {

        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        fun complete(code: String, msg: String) {
            handler.removeCallbacksAndMessages(null)
            if (code == "200") {

            } else {

            }
        }
    }

    override fun onRefresh() {
    }

    override fun onDestroy() {
        super.onDestroy()
//        binding!!.webview.removeAllViews()
//        binding!!.webview.destroy()
        handler.removeCallbacksAndMessages(null)
    }

    private var message = ""
    private var delay: Int = 1
    fun commitNotice(msg: String, delay: String) {
        addSubscription(RetrofitHelper.getApi().addNotice(message, delay), object : BaseSubscriber<ResultBody<*>>(this) {
            override fun onCompleted() {

            }
            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onNext(info: ResultBody<*>) {
                if (info.isOk) {
                    AlertDialog.Builder(this@MsgAddActivity)
                            .setMessage(info.message)
                            .setCancelable(false)
                            .setPositiveButton("确定") { dialog, which ->
                                dialog.dismiss()
                                finish()
                            }
                            .create().show()
                }
            }
        })
    }
}
