-- 根据skuID和spuID查询spu销售属性和属性值
-- 并且标记当前sku对应的销售属性值为checked=1	

-- 1.先玩明白三个表的关系
SELECT * FROM pms_product_sale_attr sa
WHERE sa.product_id = 80;

SELECT * FROM pms_product_sale_attr_value sav
WHERE sav.product_id = 80;

-- 2.先不考虑ischecked试试
SELECT * FROM pms_product_sale_attr sa
LEFT JOIN pms_product_sale_attr_value sav
ON sa.product_id = sav.product_id
AND sa.sale_attr_id = sav.sale_attr_id
WHERE sa.product_id = 80;

SELECT * FROM pms_sku_sale_attr_value ssav
WHERE ssav.sku_id = 110;

-- 3.运用ON的性质，即左联时左边一定会完整展示，右边不满足ON条件的会为空
SELECT sa.id as sa_id,sav.id as sav_id,sa.*,sav.*,IF(ssav.sku_id,1,0) isChecked
FROM pms_product_sale_attr sa
LEFT JOIN pms_product_sale_attr_value sav
ON sa.product_id = sav.product_id
AND sa.sale_attr_id = sav.sale_attr_id
LEFT JOIN pms_sku_sale_attr_value ssav
ON sav.id = ssav.sale_attr_value_id
AND ssav.sku_id = 110
WHERE sa.product_id = 80;

/* 4.还没完，为了降低持久层IO，需要用到mybatis的一对多复杂映射(resultMap-collection)
	愿荣光归小黑鸟	*/	



-- 页面点击切换销售属性，展示不同的SKU	
-- 传统方案之先根据切换后的sku销售属性ID组，查询skuId并返回
-- 再用这个skuId请求对应的sku数据，会有两次网络IO和两次数据库IO	
SELECT
	sku_id 
FROM
	pms_sku_sale_attr_value ssav 
WHERE
	ssav.sale_attr_value_id IN ( 261, 263 ) 
	LIMIT 1;