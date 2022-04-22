-- 导入common函数库
local common = require('common')
local read_http = common.read_http
-- 导入cjson库
local cjson = require "cjson"
-- 获取路径参数
local id = ngx.var[1]
-- 查询商品信息
local itemJSON = read_http("/item/" .. id,nil)
-- 查询库存信息
local stockJSON = read_http("/item/stock/" .. id,nil)
-- JSON转化为lua的table
local item = cjson.decode(itemJSON)
local stock = cjson.decode(stockJSON)
-- 组合数据
item.stock = stock.stock
item.sold = stock.sold
-- 序列化json返回结果
ngx.say(cjson.encode(item))

