package com.example.administrator.bicycle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.List;

public class SouSuoActivity extends AppCompatActivity implements PoiSearch.OnPoiSearchListener, Inputtips.InputtipsListener {
    PoiSearch poiSearch;
    PoiSearch.Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sou_suo);

//        query = new PoiSearch.Query(keyWord, "", "");
        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，
        //POI搜索类型共分为以下20种：汽车服务|汽车销售|
        //汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
        //住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
        //金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(10);//设置查询页码


        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();

        //协助输入
        //第二个参数传入null或者“”代表在全国进行检索，否则按照传入的city进行检索
//        InputtipsQuery inputquery = new InputtipsQuery(newText, null);
//        inputquery.setCityLimit(true);//限制在当前城市
//        Inputtips inputTips = new Inputtips(SouSuoActivity.this, inputquery);
//        inputTips.setInputtipsListener(this);
//        inputTips.requestInputtipsAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /*
     * 协助输入　
     */
    @Override
    public void onGetInputtips(List<Tip> list, int i) {



    }
}
