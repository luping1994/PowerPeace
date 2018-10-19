package net.suntrans.powerpeace.ui.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.AdapterView
import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseViewHolder
import net.suntrans.looney.utils.UiUtils
import net.suntrans.looney.widgets.CompatDatePickerDialog
import net.suntrans.powerpeace.R
import net.suntrans.powerpeace.api.RetrofitHelper
import net.suntrans.powerpeace.bean.YichangEntity
import net.suntrans.powerpeace.databinding.ActivityYichangBinding
import net.suntrans.powerpeace.rx.BaseSubscriber
import net.suntrans.powerpeace.utils.StatusBarCompat
import net.suntrans.stateview.StateView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Looney on 2017/8/17.
 */

class YichangActivity : BasedActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        checkedId = v!!.id
        pickerDialog = CompatDatePickerDialog(this, mDateSetListener, mYear, mMonth - 1, mDay)
        val datePicker = pickerDialog!!.getDatePicker()
        datePicker.maxDate = System.currentTimeMillis()

        pickerDialog!!.show()
    }

    private var datas: MutableList<YichangEntity.DataBean>? = null
    private var adapter: MyAdapter? = null
    private var stateView: StateView? = null
    private var recyclerView: RecyclerView? = null

    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0

    private var currentPage = 1
    private val fristLoad = 0
    private val loadMore = 2

    private var binding: ActivityYichangBinding? = null

    private var pickerDialog: CompatDatePickerDialog? = null
    private var checkedId: Int = 0

    private var vType = "01"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_yichang)

        StatusBarCompat.compat(binding!!.statusbar)

        stateView = StateView.inject(findViewById(R.id.content))
        stateView!!.setOnRetryClickListener { getdata(fristLoad) }
        initToolBar()
        init()
    }

    private fun init() {


        datas = ArrayList<YichangEntity.DataBean>() as MutableList<YichangEntity.DataBean>?
        recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        adapter = MyAdapter(R.layout.item_yicahng, datas)
//        val itemDragAndSwipeCallback = ItemDragAndSwipeCallback(adapter)
//        val touchHelper = ItemTouchHelper(itemDragAndSwipeCallback)
//        touchHelper.attachToRecyclerView(recyclerView)
//        adapter!!.enableSwipeItem()
//        adapter!!.setOnItemSwipeListener(object : OnItemSwipeListener {
//            override fun onItemSwipeStart(viewHolder: RecyclerView.ViewHolder, pos: Int) {
//
//            }
//
//            override fun clearView(viewHolder: RecyclerView.ViewHolder, pos: Int) {
//
//            }
//
//            override fun onItemSwiped(viewHolder: RecyclerView.ViewHolder, pos: Int) {
//                delete(datas!![pos].log_id)
//            }
//
//            override fun onItemSwipeMoving(canvas: Canvas, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, isCurrentlyActive: Boolean) {
//
//            }
//        })
        adapter!!.setOnLoadMoreListener({
            getdata(loadMore)
        }, recyclerView)
        recyclerView!!.adapter = adapter

//        adapter!!.bindToRecyclerView(recyclerView)
        adapter!!.setEmptyView(R.layout.no_yichang)

        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH) + 1
        mDay = c.get(Calendar.DAY_OF_MONTH)


        binding!!.endTime.text = mYear.toString() + "-" + pad(mMonth) + "-" + pad(mDay)


//        c.add(Calendar.DAY_OF_MONTH, -1)
//        mYear = c.get(Calendar.YEAR)
//        mMonth = c.get(Calendar.MONTH) + 1
//        mDay = c.get(Calendar.DAY_OF_MONTH)


        binding!!.startTime.text = mYear.toString() + "-" + pad(mMonth) + "-" + pad(mDay)


        binding!!.startTime.setOnClickListener(this)
        binding!!.endTime.setOnClickListener(this)

        binding!!.query.setOnClickListener {
            getdata(fristLoad)
        }
        getdata(fristLoad)

//        val array = resources.getStringArray(R.array.yichangType)
//        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, array)
//        binding!!.type.adapter = adapter
        binding!!.type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    vType = "01"
                } else if (position == 1) {
                    vType = "02"
                } else if (position==2){
                    vType = "03"
                }else{
                    vType = "04"
                }
                currentPage = 1
                datas!!.clear()
                adapter!!.notifyDataSetChanged()
                getdata(loadMore)
            }

        }
    }

    private fun delete(id: Int) {

    }

    private fun initToolBar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.title = "异常提示"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    internal inner class MyAdapter(@LayoutRes layoutResId: Int, data: List<YichangEntity.DataBean>?) : BaseItemDraggableAdapter<YichangEntity.DataBean, BaseViewHolder>(layoutResId, data) {

        override fun convert(helper: BaseViewHolder, item: YichangEntity.DataBean) {
            helper.setText(R.id.msg, "" + item.room_sn + ",异常信息:" + item.message)
                    .setText(R.id.time, item.updated_at)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun getdata(loadtype: Int) {
        if (loadtype == fristLoad) {
            recyclerView!!.visibility = View.INVISIBLE
            stateView!!.showLoading()
        }
        val map = HashMap<String, String>()
        map["page"] = currentPage.toString() + ""
        map["time"] = binding!!.startTime.text.toString() + " 00:00:00"
        map["end_time"] = binding!!.endTime.text.toString() + " 23:59:59"
        map["code"] = vType
        addSubscription(RetrofitHelper.getYichangApi().getYichang(map), object : BaseSubscriber<YichangEntity>(this) {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                super.onError(e)
                if (loadtype == loadMore) {
                    adapter!!.loadMoreFail()
                } else {
                    recyclerView!!.visibility = View.INVISIBLE
                    stateView!!.showRetry()
                }
            }

            override fun onNext(o: YichangEntity) {
                if (o.code == 200) {
                    currentPage++
                    val lists = o.list
                    println(lists.size)
                    if (lists == null || lists.size == 0) {
                        if (loadtype == fristLoad) {
                            stateView!!.showEmpty()
                            recyclerView!!.visibility = View.INVISIBLE
                        } else {
                            adapter!!.loadMoreEnd()
                        }

                    } else {
                        if (loadtype == loadMore) {
                            adapter!!.loadMoreComplete()
                        }

                        stateView!!.showContent()
                        recyclerView!!.visibility = View.VISIBLE
                        datas!!.addAll(lists)
                        adapter!!.notifyDataSetChanged()
                    }

                } else {
                    UiUtils.showToast("获取数据失败")
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
    }


    private val mDateSetListener = CompatDatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        mYear = year
        mMonth = monthOfYear + 1
        mDay = dayOfMonth

        if (checkedId == R.id.startTime) {
            binding!!.startTime.text = StringBuilder()
                    .append(mYear).append("-")
                    .append(pad(mMonth)).append("-")
                    .append(pad(mDay))
        } else if (checkedId == R.id.endTime) {
            binding!!.endTime.text = StringBuilder()
                    .append(mYear).append("-")
                    .append(pad(mMonth)).append("-")
                    .append(pad(mDay))
        }


        val startTime = binding!!.startTime.text.toString()
        val endTime = binding!!.endTime.text.toString()
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var startTimeLong: Long = 0
        var endTimeLong: Long = 0
        try {
            startTimeLong = sdf.parse(startTime).time
            endTimeLong = sdf.parse(endTime).time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        if (startTimeLong > endTimeLong) {
            UiUtils.showToast("结束时间必须大于起始时间")
            datas!!.clear()
            adapter!!.notifyDataSetChanged()
            return@OnDateSetListener
        }
        datas!!.clear()
        adapter!!.notifyDataSetChanged()
        currentPage = 1
        getdata(fristLoad)
    }

    private fun pad(c: Int): String {
        return if (c >= 10)
            c.toString()
        else
            "0" + c.toString()
    }
}
