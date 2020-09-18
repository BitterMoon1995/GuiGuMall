package com.lewo.zmail.search.function;

import com.lewo.zmall.model.PmsBaseAttrInfo;
import com.lewo.zmall.model.PmsBaseAttrValue;
import com.lewo.zmall.model.PmsSearchCrumb;
import com.lewo.zmall.model.PmsSearchParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
//分离的方法类，避免controller代码臃肿
public class SearchFunction {
    //拼接get请求的URL中的请求参数部分
    public String genUrlParams(PmsSearchParam params,String ...delValueId){
        String keyword = params.getKeyword();
        String catalog3Id = params.getCatalog3Id();
        String[] valueIdList = params.getValueId();
        StringBuilder urlParam= new StringBuilder();
        //把参数中非空的拼起来就行
        if (StringUtils.isNotBlank(keyword)){
            urlParam.append("&keyword=").append(keyword);
        }
        if (StringUtils.isNotBlank(catalog3Id)){
            urlParam.append("&catalog3Id=").append(catalog3Id);
        }
        if (valueIdList!=null){
            //主逻辑，拼接所有的valueId参数
            if (delValueId.length==0) {//代表没有传delValueId这个参数
                for (String valueId : valueIdList) {
                    urlParam.append("&valueId=").append(valueId);
                }
            }
            //生成面包屑UrlParams的逻辑
            else {
                for (String valueId:valueIdList) {
                /*面包屑的UrlParams的生成，就是在当前页面的UrlParams的基础上，
                排除掉this valueId*/
                    if (!valueId.equals(delValueId[0]))
                        urlParam.append("&valueId=").append(valueId);
                }
            }
        }
        //处理开头的多余'&'
        if (urlParam.charAt(0)=='&'){
            urlParam.delete(0, 1);
        }
        return urlParam.toString();
    }

    public List<PmsSearchCrumb> genCrumbs(String[] valueIds,PmsSearchParam params){
        ArrayList<PmsSearchCrumb> crumbs = new ArrayList<>();
        for (String valueId : valueIds) {
            PmsSearchCrumb crumb = new PmsSearchCrumb();
            crumb.setValueId(valueId);
            String urlParams = genUrlParams(params, valueId);
            crumb.setUrlParam(urlParams);
            crumb.setValueName(valueId);
            crumbs.add(crumb);
        }
        return crumbs;
    }

    //揽抄方法，一个方法干两个活（大逆不道啊！！！）
    public List<PmsBaseAttrInfo> processSelectedAttr(List<PmsBaseAttrInfo> aggAttrs,
        String[] toDelValueIds,PmsSearchParam param,List<PmsSearchCrumb> crumbs) {
        /*遍历用户已经点击了的平台属性值ID集合，这些属性值要经过两种处理：
        1.删除该值所处的平台属性
        2.生成面包屑。为什么非要在这里？因为只有这里薅得到属性名
        要尽努力避免数据库IO，或者说一切IO，所以牺牲代码整洁性
        */
        for (String delValueId : toDelValueIds) {
            /*重新获取迭代器。怎么可能还用上次循环的迭代器呢？那个cursor都指到头了啊都已经遍历完了，
            .hasNext()直接判false啊！其实是很低级的错误*/
            Iterator<PmsBaseAttrInfo> iterator = aggAttrs.iterator();

            PmsSearchCrumb crumb = new PmsSearchCrumb();
            crumb.setValueId(delValueId);
            String urlParams = genUrlParams(param, delValueId);
            crumb.setUrlParam(urlParams);

            /*  遍历所有属性集
            循环的打破方式：一个selectedId的外层循环，一旦满足条件生成了面包屑、删除了所处的属性集，
            就不用再遍历其他属性集了，内层循环也就是遍历属性集的其他属性值就更不需要了。所以这里必须隔层break*/
            nigger: while (iterator.hasNext()) {
                PmsBaseAttrInfo attr = iterator.next();
                List<PmsBaseAttrValue> valueList = attr.getAttrValueList();
                //遍历当前属性集的属性值
                for (PmsBaseAttrValue value : valueList) {
                    //获取当前属性值的ID
                    String valueId = value.getId();
                    //如果当前属性值ID等于待删除属性值ID集合的任意一个
                    if (valueId.equals(delValueId)) {
                        //为面包屑的属性名设置为该属性的属性名
                        crumb.setValueName(value.getValueName());
                        //并删除当前属性
                        iterator.remove();
                        break nigger;
                    }
                }
            }
            crumbs.add(crumb);
        }
        return aggAttrs;
    }
    void nigger(int ...arr){
        System.out.println(arr[0]);
        System.out.println(arr.length);
    }

    public static void main(String[] args) {
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        numbers.add(12);
        numbers.add(8);
        numbers.add(2);
        numbers.add(23);
        // 删除小于 10 的元素
        numbers.removeIf(i -> i < 10);
        System.out.println(numbers);
    }
}
